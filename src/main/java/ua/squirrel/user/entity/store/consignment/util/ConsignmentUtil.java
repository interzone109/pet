package ua.squirrel.user.entity.store.consignment.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.service.store.consignment.ConsignmentServiceImpl;
import ua.squirrel.user.service.store.consignment.status.ConsignmentStatusServiceImpl;

@Component
public class ConsignmentUtil {

	@Autowired
	private ConsignmentServiceImpl consignmentServiceImpl;
	@Autowired
	private ConsignmentStatusServiceImpl consignmentStatusServiceImpl;

	/**
	 * метод служит добавления в партии новых продуктов 
	 * 
	 */
	
	public Consignment updateConsignment(Store store , List<Product> storeProducts) {
		
		Consignment consignment ;
		//получаем сегодняшнюю дату
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);  
		currentDate.set(Calendar.MINUTE, 0);  
		currentDate.set(Calendar.SECOND, 0);  
		currentDate.set(Calendar.MILLISECOND, 0); 
		
		
		//получаем приходную партию по сегодняшней дате
		Optional<Consignment> consignmentOptional = consignmentServiceImpl.findOneByDateAndStorageAndConsignmentStatus(currentDate
				, store.getStorage()
				, consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());
		
		//если партия отсутствует создаем новую 
		//и заполняем ее
		if(!consignmentOptional.isPresent()) {
		//добавляем все id продуктов которые задействованны на данной ТТ 
		// по умолчанию количество и общая цена устонавливается в ноль
		StringBuilder strBuilder = new StringBuilder();
		storeProducts.forEach(product->{
			strBuilder.append(product.getId()+":"+0+"quant"+0+"totalPrice");
		});
		
		
		//создаем обьект партии
		//в данном случае это будет первая партия при создании ТТ
		consignment = new Consignment();
		consignment.setStartConsignment(strBuilder.toString());//устанавливаем id продукта -его количество - общую сумму
		consignment.setApproved(false);//помечаем как неподтвержденную партию
		consignment.setConsignmentEmpty(true);//помечаем партию пустой (без количества и цены)
		consignment.setConsignmentStatus(consignmentStatusServiceImpl.findOneByName("ARRIVAL").get());// устанавливаем статус как приход
		consignment.setDate(currentDate);//устонавливаем текущую дату для партии
		consignment.setStorage(store.getStorage());//устанавливаем склад которому принадлежит партия
		consignment.setMeta("new consignment");// служебная информация
		
		
		}
		//если партия за сегодняшнюю дату есть
		//добавляем в нее новые записи
		else {
			//колекция для храннения уже существующих продуктов в партии
			Set<Long>consignmentIds = new HashSet<>();
			//приходная партия за сегодняшнюю дату
			consignment = consignmentOptional.get();
			//парсим все продукты и сохраняем их id в колекцию
			for(String id:consignment.getStartConsignment().split(":[0-9]quant[0-9]totalPrice")){
				consignmentIds.add(Long.parseLong(id));
			}
			
			//коллекция для хранения id новых продуктов
			Set<Long> newProductsId = new HashSet<>();
			//сохраняем все новые ид продуктов
			storeProducts.forEach(prod->{
				newProductsId.add(prod.getId());
			});
			
			//далее необходимо удалить дубликаты id из новых продуктов 
			//это делаеться для того что бы не затирать записи по цене и кол 
			//уже хранимых данных  на данный момент 
			newProductsId.removeAll(consignmentIds);
			
			//создаем билдер для записей данных
			StringBuilder strBuilder = new StringBuilder();
			
			//записи которые проверены на дубликат записываються в strBuilder
			newProductsId.forEach(id->{
				strBuilder.append(id+":"+0+"quant"+0+"totalPrice");
			});
			//новые записи остаються в начале а старые в конце
			strBuilder.append(consignment.getStartConsignment());
			// устанавливаем новые записи на текущую партию
			consignment.setStartConsignment(strBuilder.toString());
		}
		return consignment;
	}
}
