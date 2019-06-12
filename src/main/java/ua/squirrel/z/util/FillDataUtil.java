package ua.squirrel.z.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.user.service.partner.PartnerServiceImpl;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.product.properties.MeasureProductServiceImpl;
import ua.squirrel.user.service.product.properties.PropertiesProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.invoice.InvoiceServiceImpl;
import ua.squirrel.user.service.store.spending.SpendServiceImpl;
import ua.squirrel.web.entity.user.User;

@Component
public class FillDataUtil {

	/**
	 * 
	 * 
	 * тестовое заполнение полей пользователя
	 * данными о партнерах и их продуктах
	 * 
	 * */
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private  PropertiesProductServiceImpl propertiesProductServiceImpl;
	@Autowired
	private  MeasureProductServiceImpl measureProductServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl ;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private SpendServiceImpl spendServiceImpl;
	
	
	
	public void setSpend(User user) {
		Spend spend = new Spend();
		spend.setName("аренда за магазин 1");
		spend.setDescription("аренда");
		spend.setCost(10000);
		spend.setInterval(15);
		spend.setIsRegular(true);
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(1l, user).get());
		Calendar date = new GregorianCalendar(2019, 2, 2);
		
		spend.setDate(date);
		spendServiceImpl.save(spend);
		
		spend = new Spend();
		spend.setName("аренда за оборудования");
		spend.setDescription("печь, каса, столы");
		spend.setCost(20000);
		spend.setInterval(15);
		spend.setIsRegular(true);
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(1l, user).get());
		date = new GregorianCalendar(2019, 3, 20);
		spend.setDate(date);
		spendServiceImpl.save(spend);
		
		spend = new Spend();
		spend.setName("ремонт");
		spend.setDescription("кухня и проч");
		spend.setCost(100000);
		spend.setInterval(15);
		spend.setIsRegular(false);
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(1l, user).get());
		 date = new GregorianCalendar(2019, 5,5);
		spend.setDate(date);
		spendServiceImpl.save(spend);
	}
	
	
	
	/**
	 * метод создает продукт из ингрииентов
	 * */
	public List<CompositeProduct> getProduct(User owner ){
		List<CompositeProduct> listCompositeProduct = new ArrayList<>();
		
		CompositeProduct americano = new CompositeProduct();
		americano.setName("Американо");
		americano.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		americano.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		americano.setGroup("горячие напитки");
		americano.setUser(owner);


		CompositeProduct tea = new CompositeProduct();
		tea.setName("чай");
		tea.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		tea.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		tea.setGroup("горячие напитки");
		tea.setUser(owner);
		
		CompositeProduct fries = new CompositeProduct();
		fries.setName("картошка фри 250");
		fries.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		fries.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		fries.setGroup("горячие блюда");
		fries.setUser(owner);
		
		CompositeProduct friesBig = new CompositeProduct();
		friesBig.setName("картошка фри 500");
		friesBig.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		friesBig.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		friesBig.setGroup("горячие блюда");
		friesBig.setUser(owner);
	
		
		
		StringBuilder productAmericano = new StringBuilder();
		StringBuilder productTea = new StringBuilder();
		StringBuilder productFries = new StringBuilder();
		StringBuilder productFriesBig = new StringBuilder();
		
		partnerServiceImpl.findAllByUser(owner).stream().forEach(partner->{
			 partner.getProducts().stream().forEach(prod->{

				  
				  if(prod.getName().equals("Кофе Черная тара")) { productAmericano.append(prod.getId()+":"+ 10+"rate"); }
					 else if ( prod.getName().equals("Вода")) {productAmericano.append(prod.getId()+":"+ 30+"rate");}
					 else if( prod.getName().equals("Сахар стик")) {productAmericano.append(prod.getId()+":"+ 2+"rate");}
					 else if ( prod.getName().equals("Стаканчик 0.33")) {productAmericano.append(prod.getId()+":"+ 1+"rate");}
					 else  if ( prod.getName().equals("Пластиковое мешало")) {productAmericano.append(prod.getId()+":"+ 1+"rate"); }
					 
					  if ( prod.getName().equals("Вода")) {productTea.append(prod.getId()+":"+ 50+"rate");}
					  else if ( prod.getName().equals("Чай 1000 вопросов")) {productTea.append(prod.getId()+":"+ 5+"rate");}
					  else if ( prod.getName().equals("Стаканчик 0.66")) {productTea.append(prod.getId()+":"+ 1+"rate");}
					  else if ( prod.getName().equals("Пластиковое мешало")) {productTea.append(prod.getId()+":"+ 1+"rate");}
					 
					 
					  if ( prod.getName().equals("Картошка")) {productFries.append(prod.getId()+":"+ 22250+"rate");}
					  else  if ( prod.getName().equals("Масло Стожор")) {productFries.append(prod.getId()+":"+ 50+"rate");}
					  else  if ( prod.getName().equals("Соль столовая")) {productFries.append(prod.getId()+":"+ 5+"rate");}
					  
					  if ( prod.getName().equals("Картошка")) {productFriesBig.append(prod.getId()+":"+ 500+"rate");}
					  else  if ( prod.getName().equals("Масло Стожор")) {productFriesBig.append(prod.getId()+":"+ 100+"rate");}
					  else  if ( prod.getName().equals("Соль столовая")) {productFriesBig.append(prod.getId()+":"+ 10+"rate");}
				  
			 });
		 });
		
		americano.setProductExpend(productAmericano.toString());
		tea.setProductExpend(productTea.toString());
		fries.setProductExpend(productFries.toString());
		friesBig.setProductExpend(productFriesBig.toString());
		
		
		listCompositeProduct.add(americano);
		listCompositeProduct.add(tea);
		listCompositeProduct.add(fries);
		listCompositeProduct.add(friesBig);
		
		 return compositeProductServiceImpl.saveAll(listCompositeProduct);
	}
	
	/**
	 * метод создает поставщиков
	 * */
	public  List<Partner> getPartner(User owner) {
		List<Partner> partner = new ArrayList<>();
		
	
			Partner p = new Partner();
			p.setCompany("Кофеиновый делец");
			p.setPartnerMail("cofee@mail.com");
			p.setUser(owner);
			p.setProducts(getCofee(p , owner));
			p.setPhonNumber("21-313-213");
			partner.add(p);
			
			p = new Partner();
			p.setCompany("Картошечный флибустьер");
			p.setPartnerMail("potato@mail.com");
			p.setUser(owner);
			p.setProducts(getPotato(p,owner));
			p.setPhonNumber("21-313-288");
			partner.add(p);
			
			p = new Partner();
			p.setCompany("Армейские Расходники Компани");
			p.setPartnerMail("consumbles@mail.com");
			p.setUser(owner);
			p.setProducts(getConsumbles(p,owner));
			p.setPhonNumber("27-385-288");
			partner.add(p);
		
	
			partnerServiceImpl.saveAll(partner);
			
		return partner;
	}

	private  List<Product> getCofee(Partner partner,User owner) {
		List<Product> product = new ArrayList<>();
		
		Product p = new Product();
		p.setName("Вода 20л");
		p.setDescription("Вода питьевая");
		p.setGroup("воды");
		p.setPartner(partner);									
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Сок 0,33 яблоко");
		p.setDescription("Сок стекло яблоко");
		p.setGroup("сок");
		p.setPartner(partner);									
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Сок 0,33 груша");
		p.setDescription("Сок стекло груша");
		p.setGroup("сок");
		p.setPartner(partner);									
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Сок 0,33 кефир");
		p.setDescription("Сок стекло кефир");
		p.setGroup("сок");
		p.setPartner(partner);									
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		
		p = new Product();
		p.setName("Кофе Черная тара");
		p.setDescription("кофе зерновой ");
		p.setGroup("чаи");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Сахар стик");
		p.setDescription("сахар в стиках женный");
		p.setGroup("");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("CONSUMABLES"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Молоко");
		p.setDescription("молоко в цетропаках");
		p.setGroup("молочка");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);

		p = new Product();
		p.setName("Чай 1000 вопросов");
		p.setDescription("Чай отвечай");
		p.setGroup("чаи");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		productServiceImpl.saveAll(product);
		return product;
	}
	/**
	 * метод заполняет продукт у поставщика
	 * */
	private  List<Product> getPotato(Partner partner,User owner) {
		List<Product> product = new ArrayList<>();
		
		
		Product p = new Product();
		p.setName("Картошка");
		p.setDescription("катошка фри мороженая");
		p.setGroup("овощи");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Масло Стожор");
		p.setDescription("Масло подсолнечное стожор банка 0.9л ");
		p.setGroup("");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Соль столовая");
		p.setDescription("Сель соленая");
		p.setGroup("");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		p.setUser(owner);
		product.add(p);
		
		productServiceImpl.saveAll(product);
		return product ;
		}
	
	private  List<Product> getConsumbles(Partner partner,User owner) {
		List<Product> product = new ArrayList<>();
		
		
		
		Product p = new Product();
		p.setName("Стаканчик 0.33");
		p.setDescription("Стаканчик 0.33");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("CONSUMABLES"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Стаканчик 0.45");
		p.setDescription("Стаканчик 0.45");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("CONSUMABLES"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Стаканчик 0.66");
		p.setDescription("Стаканчик 0.66");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("CONSUMABLES"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Крышка для стаканов");
		p.setDescription("Крышка для стаканов");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("CONSUMABLES"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Пластиковое мешало");
		p.setDescription("Мешало для кофе чай");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("CONSUMABLES"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		productServiceImpl.saveAll(product);
		
		return product ;
	}

	
	/**
	 * метод создает тестовый магазин
	 * */
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	
	public void getStore(User owner) {
		
		StringBuilder idsPrice = new StringBuilder();
		int price = 2999 ;
		compositeProductServiceImpl.findAllByUser(owner).stream().forEach(obj->{
			idsPrice.append(obj.getId()+":"+ (price+obj.getId())+"price");
		});
		
		Store newStore = new Store();
			newStore.setAddress("авто тест");
			newStore.setPhone("545-254-54");
			
			newStore.setMail("empty@mail.com");
			newStore.setUser(owner);
			

			storeServiceImpl.save(newStore);
		
		 newStore = new Store();
		newStore.setAddress("Тираспольский грук 15.Б");
		newStore.setPhone("545-254-54");
		newStore.setProductPrice(idsPrice.toString());
		newStore.setMail("test@mail.com");
		newStore.setUser(owner);
		

		storeServiceImpl.save(newStore);
		
		 newStore = new Store();
		newStore.setAddress("Test");
		newStore.setPhone("545-254-54");
		newStore.setProductPrice(idsPrice.toString());
		newStore.setMail("test@mail.com");
		newStore.setUser(owner);
		

		storeServiceImpl.save(newStore);
		
		
		
	}

	@Autowired
	private InvoiceServiceImpl invoiceServiceImpl;

	public void setInvoice(User user1) {
		List<Invoice> invoices = new ArrayList<>();
		int day = LocalDate.now().lengthOfMonth();
		
		 storeServiceImpl.findAllByUser(user1).forEach(store->{
			 Invoice newInvoice = new Invoice();
				newInvoice.setDate(LocalDate.of(2019, 6, 1));
				newInvoice.setInvoiceData("1:2sale2:3sale3:7sale");
				newInvoice.setStore(store);
			invoices.add(newInvoice);
			
			newInvoice = new Invoice();
			newInvoice.setDate(LocalDate.of(2019, 6, day-1));
			newInvoice.setInvoiceData("4:2sale2:3sale3:7sale");
			newInvoice.setStore(store);
		invoices.add(newInvoice);
		
		newInvoice = new Invoice();
		newInvoice.setDate(LocalDate.of(2019, 6, day-2));
		newInvoice.setInvoiceData("4:2sale2:3sale3:7sale");
		newInvoice.setStore(store);
	invoices.add(newInvoice);
	
	newInvoice = new Invoice();
	newInvoice.setDate(LocalDate.of(2019, 6, day-3));
	newInvoice.setInvoiceData("4:2sale2:3sale3:7sale");
	newInvoice.setStore(store);
	invoices.add(newInvoice);

	newInvoice = new Invoice();
	newInvoice.setDate(LocalDate.of(2019, 6, day-4));
	newInvoice.setInvoiceData("4:2sale2:3sale3:7sale");
	newInvoice.setStore(store);
	invoices.add(newInvoice);
		 });
		 invoiceServiceImpl.saveAll(invoices);
	}

}
