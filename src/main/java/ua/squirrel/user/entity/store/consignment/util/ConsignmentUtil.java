package ua.squirrel.user.entity.store.consignment.util;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;

@Component
public class ConsignmentUtil {

	private CompositeProductServiceImpl compositeProductServiceImpl;
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;
	
	@Autowired
	public ConsignmentUtil(CompositeProductServiceImpl compositeProductServiceImpl,
			ConsignmentStatusServiceImpl consignmentStatusServiceImpl ) {
		this.consignmentStatusServiceImpl = consignmentStatusServiceImpl;
		this.compositeProductServiceImpl = compositeProductServiceImpl;
	}

	/**
	 * метод служит для добавления новых расходных товаров в партию
	 * */
	public void updateConsignment(Store store) {
		//получаем список всех партий магазина
		List<Consignment> listConsignment = store.getStorage().getConsignment();
		
		
		//проверяем если список пуст то создаем новую партию 
		//поступления продуктов текущей датой
		if (listConsignment == null) {
			//set в котором будут храниться все продукты на продажу
			Set<Long> idComposite = new HashSet<>();
			//разбиваем ProductPrice который содержит ID композитных продуктов
			//и добавляем их в колекцию idComposite
			for (String strId : store.getStorage().getProductPrice().split(":[0-9]+price|price*")) {
				idComposite.add(Long.parseLong(strId));
			}
			
			//коллекция для хранения списка всех продуктов
			//(ингридиентов для из которых состоит композитный продукт)
			Set<Long> idProduct = new HashSet<>();
			//получаем все композитные обьекты которые есть на данной ТТ
			//разбиваем их на id и добавляем их в коллекцию idProduct
			compositeProductServiceImpl.findAllByUserAndIdIn(store.getUser(), idComposite).forEach(compositeProduct->{
				for(String strId :compositeProduct.getProductExpend().split(":[0-9]+rate|rate*")) {
					idProduct.add(Long.parseLong(strId));
				}
			});
			//добавляем все id продуктов которые задействованны на данной ТТ 
			// по умолчанию количество и общая цена устонавливается в ноль
			StringBuilder strBuilder = new StringBuilder();
			idProduct.forEach(id -> {
				strBuilder.append(id+":"+0+"quant"+0+"totalP");
			});
			
			//создаем обьект партии
			//в данном случае это будет первая партия при создании ТТ
			Consignment consignment = new Consignment();
			consignment.setStartConsignment(strBuilder.toString());//устанавливаем id продукта -его количество - общую сумму
			consignment.setApproved(false);//помечаем как неподтвержденную партию
			consignment.setConsignmentEmpty(true);//помечаем партию пустой (без количества и цены)
			consignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());// устанавливаем статус как приход
			consignment.setDate(new GregorianCalendar());//устонавливаем текущую дату для партии
			consignment.setStorage(store.getStorage());// привязываем данную партию к магазину
			consignment.setMeta("new consignment");// служебная информация
		}
		
	}
}
