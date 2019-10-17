package ua.squirrel.user.controller.store.consignment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.ConsignmentModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentSearchModel;
import ua.squirrel.user.entity.store.consignment.ConsignmentStatus;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode;
import ua.squirrel.user.entity.store.ingridient.node.StoreIngridientNode;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;
import ua.squirrel.user.service.store.ingridient.node.StoreIngridientNodeServiceImpl;
import ua.squirrel.user.utils.ConsignmentUtil;
import ua.squirrel.user.utils.StoreUtil;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.user.UserServiceImpl;

@RestController
@RequestMapping("/user/stores/consignment")
@Slf4j
public class ConsignmentController {

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	@Autowired
	private StoreIngridientNodeServiceImpl storeIngridientNodeServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private ConsignmentUtil consignmentUtil;
	@Autowired
	private StoreUtil storeUtil;

	@GetMapping("{storeId}/{consignmentId}")
	public List<ProductModel> getСonsignmentData(Authentication authentication, @PathVariable("storeId") Long storeId,
			@PathVariable("consignmentId") Long consignmentId) throws NotFoundException {
		log.info("LOGGER: get Consignment data");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		Store store = getCurrentStore(user, storeId);
		Consignment consignment = consignmentServiceImpl.findOneByIdAndStore(consignmentId, store)
				.orElseThrow(() -> new NotFoundException("Status not found"));

		return storeUtil.getConsigmentProductPrice(consignment);
	}

	/**
	 * Контроллер обновляет даные о количестве и  цене ингридиентов в накладной,
	 * так же добавляет новые ингридиенты.
	 * 1. Получаем данные из баззы (магазин, накладная, список ингридиентов)
	 * 2. Добавляем новые ингридиенты
	 * 3. Обновляем у всех ингридиентов их количество
	 */
	@PutMapping("{storeId}/{consignmentId}")
	public Map<Long, String> putСonsignmentData(Authentication authentication,
			@RequestBody Map<Long, String> consignmentData, @PathVariable("storeId") Long storeId,
			@PathVariable("consignmentId") Long consignmentId) throws NotFoundException {
		log.info("LOGGER: update Consignment data");
		/**
		 *		1 пункт
		 * */
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();//тестовый пользователь
		Store store = getCurrentStore(user, storeId);//находим магазин к которому был направлен запрос
		Consignment consignment = consignmentServiceImpl.findOneByIdAndStore(consignmentId, store)
				.orElseThrow(() -> new NotFoundException("Status not found"));//находим накладную  в  которую будем вносить обновления
		//если накладная не проведена то вносим изменения
		if (!consignment.isApproved()) {//обновляем старые ингридиенты
		//получаем список ингридиентов (в данном списке страные и новые ингридиенты
		List<Product> ingridientList = productServiceImpl.findAllByUserAndIdIn(user, consignmentData.keySet());
		/**
		 *		2 пункт
		 * */
		//добавляем новые ингридиенты если они есть
		storeUtil.uniqueConsigment(consignment, ingridientList);
		/**
		 *		3 пункт
		 * */
		//получаем все узлы из накладной
		List<ConsignmentNode> consignmentNodeList = consignment.getConsignmentNode(); 
			//вносим изменения в количество ингридиентов для каждого узла
			consignmentNodeList.forEach(consignmentNode->{
				if(ingridientList.contains(consignmentNode.getProduct())) {
					long ingridientId = consignmentNode.getProduct().getId(); 
					String[] data = consignmentData.get(ingridientId).split(":|quantity|price");
					consignmentNode.setQuantity(Integer.parseInt(data[1]));
					consignmentNode.setCurrentQuantity(Integer.parseInt(data[1]));
					consignmentNode.setUnitPrice(Integer.parseInt(data[2]));
				}
			});
			consignmentServiceImpl.save(consignment);

			return consignmentData;
		}

		return new HashMap<>();
	}

	/**
	 * Метод обновляет даные о количестве и цене в накладной и закрывает накладную
	 * для редактирования
	 */
	
	@PutMapping("{storeId}/{consignmentId}/uproved")
	public Map<Long, String> putСonsignmentDataUproved(Authentication authentication,
			@RequestBody Map<Long, String> consignmentData, @PathVariable("storeId") Long storeId,
			@PathVariable("consignmentId") Long consignmentId) throws NotFoundException {
		
		log.info("LOGGER: uproved Consignment data");
		
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		Store store = getCurrentStore(user, storeId);
		Consignment consignment = consignmentServiceImpl.findOneByIdAndStore(consignmentId, store)
				.orElseThrow(() -> new NotFoundException("Status not found"));
		
		if (!consignment.isApproved()) {//обновляем старые ингридиенты
			
			List<ConsignmentNode> consignmentNodeList = consignment.getConsignmentNode(); 
			List<Product> ingridientList = productServiceImpl.findAllByUserAndIdIn(user, consignmentData.keySet());
			//добавляем новые ингридиенты если они есть
			storeUtil.uniqueConsigment(consignment, ingridientList);
			consignmentNodeList.forEach(consignmentNode->{
				if(ingridientList.contains(consignmentNode.getProduct())) {
					long ingridientId = consignmentNode.getProduct().getId(); 
					String[] data = consignmentData.get(ingridientId).split(":|quantity|price");
					consignmentNode.setQuantity(Integer.parseInt(data[1]));
					consignmentNode.setCurrentQuantity(Integer.parseInt(data[1]));
					consignmentNode.setUnitPrice(Integer.parseInt(data[2]));
				}
			});
			consignment.setApproved(true);
			consignmentServiceImpl.save(consignment);
			//
			
			//добавляем новые ингридиенты в остатки на магазин
			Set<StoreIngridientNode> nodeList = new HashSet<>();
			Set<Product> existProductList = new HashSet<>();
			store.getStoreIngridientNode().forEach(node->{
				existProductList.add(node.getProduct());
			});
			System.err.println("existProductList size "+existProductList.size());
			ingridientList.removeAll(existProductList);
			System.err.println("ingridientList size after remove "+ingridientList.size());
			if(ingridientList.size()>0) {
			ingridientList.forEach(product->{
				StoreIngridientNode storeIngridientNode = new StoreIngridientNode();
				storeIngridientNode.setLeftOvers(Integer.parseInt(consignmentData.get(product.getId()).split(":|quantity|price")[1]));
				storeIngridientNode.setStore(store);
				storeIngridientNode.setProduct(product);
				nodeList.add(storeIngridientNode);
			});
			}
			 
			storeIngridientNodeServiceImpl.saveAll(nodeList);/**/
			 
			//
			//обновляем остатки на магазине в соответствии с изменениями в накладной
			switch (consignment.getConsignmentStatus().getName()) {
			case "ARRIVAL":// приход
				storeUtil.updateStoreLeftovers(store, consignmentData, "+");
				storeServiceImpl.save(store);
				break;
			case "CONSAMPTION":// расход
				if (!consignment.getMeta().startsWith("auto:%:")) {
					storeUtil.updateStoreLeftovers(store, consignmentData, "-");
					storeServiceImpl.save(store);
				}
				break;
				//fix
			case "HAULING":// внутренее перемещение
				storeUtil.updateStoreLeftovers(store, consignmentData, "-"); // удаляем из магазина отправителя
																				// ингридиенты
				storeServiceImpl.save(store);
				// добавляем в магазин получателя
				long haulingStoreId = Long.parseLong(consignment.getMeta().split(":store:%:")[0]);
				Store haulingStore = getCurrentStore(user, haulingStoreId);
				storeUtil.updateStoreLeftovers(store, consignmentData, "+");
				storeServiceImpl.save(haulingStore);
				break;
			case "RETURN":// возрат поставщику
			case "WRITE-OFF":// списание
				storeUtil.updateStoreLeftovers(store, consignmentData, "-"); // удаляем из магазина ингридиенты
				storeServiceImpl.save(store);
				break;
			}
			return consignmentData;
		}
		return new HashMap<>();
	}

	/**
	 * Метод сохраняет новую накладную
	 */
	@PostMapping("/create")
	public List<ConsignmentModel> createСonsignment(Authentication authentication,
			@RequestBody ConsignmentModel createConsignment) throws NotFoundException {
		log.info("LOGGER: save new empty Consignment");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		Store store = getCurrentStore(user, createConsignment.getStoreId());

		String[] date = createConsignment.getDate().split("\\.");
		LocalDate calendar = LocalDate.of(Integer.parseInt(date[2].trim()), Integer.parseInt(date[1].trim()),
				Integer.parseInt(date[0].trim()));

		Consignment consignment = new Consignment();
		consignment.setDate(calendar);
		consignment.setConsignmentNode(new ArrayList<>());
		consignment.setApproved(false);
		consignment.setMeta(createConsignment.getMeta());
		consignment.setStore(store);
		consignment.setConsignmentStatus(
				consignmentStatusServiceImpl.findOneByName(createConsignment.getConsignmentStatus()).get());

		consignmentServiceImpl.save(consignment);

		List<Consignment> resultList = new ArrayList<>();
		resultList.add(consignment);
		return consignmentUtil.createConsignmentModelList(resultList);
	}

	/**
	 * Метод отдает результаты поиска согласно данным запроса из обьекта
	 * ConsignmentSearchModel
	 */
	@PostMapping
	public List<ConsignmentModel> getСonsignmentSearch(Authentication authentication,
			@RequestBody ConsignmentSearchModel consignmentSearchModel) throws NotFoundException {
		log.info("LOGGER: get Consignment for search value");
		User user = userServiceImpl.findOneByLogin(authentication.getName()).get();
		Store store = getCurrentStore(user, consignmentSearchModel.getStoreId());

		List<Consignment> resultList = null;
		if (consignmentSearchModel.getMeta() == null || consignmentSearchModel.getMeta().isEmpty()) {
			resultList = consignmentServiceImpl.findByStoreAndConsignmentStatusAndDateBetween(store,
					getConsignmentStatus(consignmentSearchModel.getConsignmentStatus()),
					consignmentUtil.convertDate(consignmentSearchModel.getDateStart()),
					consignmentUtil.convertDate(consignmentSearchModel.getDateFinish()));
		} else {
			resultList = consignmentServiceImpl
					.findByStoreAndConsignmentStatusAndMetaIgnoreCaseContainingAndDateBetween(store,
							getConsignmentStatus(consignmentSearchModel.getConsignmentStatus()),
							consignmentSearchModel.getMeta(),
							consignmentUtil.convertDate(consignmentSearchModel.getDateStart()),
							consignmentUtil.convertDate(consignmentSearchModel.getDateFinish()));
		}

		return consignmentUtil.createConsignmentModelList(resultList);
	}

	
	
	
	
	private Store getCurrentStore(User user, Long id) throws NotFoundException {
		return storeServiceImpl.findOneByIdAndUser(id, user)
				.orElseThrow(() -> new NotFoundException("Store not found"));
	}

	private ConsignmentStatus getConsignmentStatus(String name) throws NotFoundException {
		return consignmentStatusServiceImpl.findOneByName(name)
				.orElseThrow(() -> new NotFoundException("Status not found"));
	}

}