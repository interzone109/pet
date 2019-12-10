package ua.squirrel.z.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.employee.Employee;
import ua.squirrel.user.entity.partner.Partner;
import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.node.ProductMap;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;
import ua.squirrel.user.entity.store.spending.Spend;
import ua.squirrel.user.service.employee.EmployeeServiceImpl;
import ua.squirrel.user.service.partner.PartnerServiceImpl;
import ua.squirrel.user.service.product.CompositeProductServiceImpl;
import ua.squirrel.user.service.product.ProductServiceImpl;
import ua.squirrel.user.service.product.node.ProductMapServiceImpl;
import ua.squirrel.user.service.product.properties.MeasureProductServiceImpl;
import ua.squirrel.user.service.product.properties.PropertiesProductServiceImpl;
import ua.squirrel.user.service.store.StoreServiceImpl;
import ua.squirrel.user.service.store.compositeproduct.node.StoreCompositeProductServiceImpl;
import ua.squirrel.user.service.store.invoice.InvoiceServiceImpl;
import ua.squirrel.user.service.store.spending.SpendServiceImpl;
import ua.squirrel.web.entity.account.AccountApp;
import ua.squirrel.web.entity.user.Role;
import ua.squirrel.web.entity.user.User;
import ua.squirrel.web.service.registration.RoleServiceImpl;

@Component
public class FillDataUtil {

	
	/**
	 * 
	 * 
	 * тестовое заполнение полей пользователя данными о партнерах и их продуктах
	 * 
	 */
	@Autowired
	private PartnerServiceImpl partnerServiceImpl;
	@Autowired
	private PropertiesProductServiceImpl propertiesProductServiceImpl;
	@Autowired
	private MeasureProductServiceImpl measureProductServiceImpl;
	@Autowired
	private ProductServiceImpl productServiceImpl;
	@Autowired
	private CompositeProductServiceImpl compositeProductServiceImpl;
	@Autowired
	private SpendServiceImpl spendServiceImpl;

	public void setSpend(User user) {
		Spend spend = new Spend();
		spend.setName("аренда за магазин 1");
		spend.setCost(10000);
		spend.setStep(10000);
		spend.setInterval(-1);
		spend.setIsOpen(true);
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(1l, user).get());
		LocalDate date =LocalDate.of(2019, 10, 11);
		spend.setLasteDate(date);
		spend.setDate(date.minusMonths(1l));
		spendServiceImpl.save(spend);

		spend = new Spend();
		spend.setName("аренда за оборудования");
		spend.setCost(20000);
		spend.setStep(20000);
		spend.setInterval(3);
		spend.setIsOpen(true);
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(1l, user).get());
		date =LocalDate.of(2019, 11, 8);
		spend.setLasteDate(date);
		spend.setDate(date.minusDays(3l));
		spendServiceImpl.save(spend);

		spend = new Spend();
		spend.setName("ремонт");
		spend.setCost(100000);
		spend.setStep(0);
		spend.setInterval(0);
		spend.setIsOpen(false);
		spend.setUser(user);
		spend.setStore(storeServiceImpl.findOneByIdAndUser(1l, user).get());
		date =LocalDate.now();
		spend.setLasteDate(date);
		spend.setDate(date);
		spendServiceImpl.save(spend);
	}

	/**
	 * метод создает продукт из ингрииентов
	 */
	public List<CompositeProduct> getProduct(User owner) {
		List<CompositeProduct> listCompositeProduct = new ArrayList<>();

		CompositeProduct americano = new CompositeProduct();
		americano.setProductMap(new ArrayList<>());
		americano.setName("Американо");
		americano.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		americano.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		americano.setGroup("горячие напитки");
		americano.setUser(owner);

		CompositeProduct tea = new CompositeProduct();
		tea.setProductMap(new ArrayList<>());
		tea.setName("чай");
		tea.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		tea.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		tea.setGroup("горячие напитки");
		tea.setUser(owner);

		CompositeProduct fries = new CompositeProduct();
		fries.setProductMap(new ArrayList<>());
		fries.setName("картошка фри 250");
		fries.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		fries.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		fries.setGroup("горячие блюда");
		fries.setUser(owner);

		CompositeProduct friesBig = new CompositeProduct();
		friesBig.setProductMap(new ArrayList<>());
		friesBig.setName("картошка фри 500");
		friesBig.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("PRODUCT_FINAL"));
		friesBig.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		friesBig.setGroup("горячие блюда");
		friesBig.setUser(owner);
		
		productServiceImpl.findAllByUser(owner).forEach(prod -> {

				if (prod.getName().equals("Кофе Черная тара")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(10);
					productMap.setCompositeProduct(americano);
					americano.getProductMap().add(productMap);
				} else if (prod.getName().equals("Вода")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(30);
					productMap.setCompositeProduct(americano);
					americano.getProductMap().add(productMap);
				} else if (prod.getName().equals("Сахар стик")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(2);
					productMap.setCompositeProduct(americano);
					americano.getProductMap().add(productMap);
				} else if (prod.getName().equals("Стаканчик 0.33")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(1);
					productMap.setCompositeProduct(americano);
					americano.getProductMap().add(productMap);
				} else if (prod.getName().equals("Пластиковое мешало")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(1);
					productMap.setCompositeProduct(americano);
					americano.getProductMap().add(productMap);
				}

				if (prod.getName().equals("Вода")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(500);
					productMap.setCompositeProduct(tea);
					tea.getProductMap().add(productMap);
				} else if (prod.getName().equals("Чай 1000 вопросов")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(5);
					productMap.setCompositeProduct(tea);
					tea.getProductMap().add(productMap);
				} else if (prod.getName().equals("Стаканчик 0.66")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(1);
					productMap.setCompositeProduct(tea);
					tea.getProductMap().add(productMap);
				} else if (prod.getName().equals("Пластиковое мешало")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(1);
					productMap.setCompositeProduct(tea);
					tea.getProductMap().add(productMap);
				}

				if (prod.getName().equals("Картошка")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(2250);
					productMap.setCompositeProduct(fries);
					fries.getProductMap().add(productMap);
				} else if (prod.getName().equals("Масло Стожор")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(50);
					productMap.setCompositeProduct(fries);
					fries.getProductMap().add(productMap);
				} else if (prod.getName().equals("Соль столовая")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(50);
					productMap.setCompositeProduct(fries);
					fries.getProductMap().add(productMap);
				}

				if (prod.getName().equals("Картошка")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(22250);
					productMap.setCompositeProduct(friesBig);
					friesBig.getProductMap().add(productMap);
				} else if (prod.getName().equals("Масло Стожор")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(100);
					productMap.setCompositeProduct(friesBig);
					friesBig.getProductMap().add(productMap);
				} else if (prod.getName().equals("Соль столовая")) {
					ProductMap productMap = new ProductMap();
					productMap.setProduct(prod);
					productMap.setRate(100);
					productMap.setCompositeProduct(friesBig);
					friesBig.getProductMap().add(productMap);
				}

			}); 
		
	listCompositeProduct.add(americano);
	listCompositeProduct.add(tea);
	listCompositeProduct.add(fries);
	listCompositeProduct.add(friesBig);
	return compositeProductServiceImpl.saveAll(listCompositeProduct);
	

	}

	/**
	 * метод создает поставщиков
	 */
	public List<Partner> getPartner(User owner) {
		List<Partner> partner = new ArrayList<>();

		Partner p = new Partner();
		p.setCompany("Кофеиновый делец");
		p.setPartnerMail("cofee@mail.com");
		p.setUser(owner);
		p.setProducts(getCofee(p, owner));
		p.setPhonNumber("21-313-213");
		partner.add(p);

		p = new Partner();
		p.setCompany("Картошечный флибустьер");
		p.setPartnerMail("potato@mail.com");
		p.setUser(owner);
		p.setProducts(getPotato(p, owner));
		p.setPhonNumber("21-313-288");
		partner.add(p);

		p = new Partner();
		p.setCompany("Армейские Расходники Компани");
		p.setPartnerMail("consumbles@mail.com");
		p.setUser(owner);
		p.setProducts(getConsumbles(p, owner));
		p.setPhonNumber("27-385-288");
		partner.add(p);

		partnerServiceImpl.saveAll(partner);

		return partner;
	}

	private List<Product> getCofee(Partner partner, User owner) {
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

		for (int i = 0; i < 100; i++) {
			p = new Product();
			p.setName("test " + i);
			p.setDescription("test " + i);
			p.setGroup("test");
			p.setPartner(partner);
			p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("INGREDIENS"));
			p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
			p.setUser(owner);
			product.add(p);
		}

		productServiceImpl.saveAll(product);
		return product;
	}

	/**
	 * метод заполняет продукт у поставщика
	 */
	private List<Product> getPotato(Partner partner, User owner) {
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
		return product;
	}

	private List<Product> getConsumbles(Partner partner, User owner) {
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

		return product;
	}

	/**
	 * метод создает тестовый магазин
	 */
	@Autowired
	private StoreServiceImpl storeServiceImpl;
	@Autowired
	private StoreCompositeProductServiceImpl storeCompositeProductServiceImpl;
	public void getStore(User owner) {

		List<Store> storeList = new ArrayList<>();
		Store newStore = new Store();
		newStore.setAddress("авто тест");
		newStore.setPhone("545-254-54");
		newStore.setMail("empty@mail.com");
		newStore.setUser(owner);
		
		List<StoreCompositeProductNode> compList = new ArrayList<>();
		CompositeProduct s = compositeProductServiceImpl.findByIdAndUser(1l, owner).get();
		
		StoreCompositeProductNode node= new StoreCompositeProductNode();
		node.setPrice(1100);
		node.setCompositeProduct(s);
		node.setStore(newStore);
		
		compList.add(node);
		newStore.setStoreCompositeProductNode(compList);
		
		storeServiceImpl.save(newStore);
		storeCompositeProductServiceImpl.save(node);
		
		
		newStore = new Store();
		newStore.setAddress("Тираспольский грук 15.Б");
		newStore.setPhone("545-254-54");
		newStore.setMail("test@mail.com");
		newStore.setUser(owner);
		
		compList = new ArrayList<>();
		node= new StoreCompositeProductNode();
		node.setPrice(1100);
		node.setCompositeProduct(s);
		node.setStore(newStore);
		
		compList.add(node);
		newStore.setStoreCompositeProductNode(compList);
		storeServiceImpl.save(newStore);
		storeCompositeProductServiceImpl.save(node);
		
		
		newStore = new Store();
		newStore.setAddress("Test");
		newStore.setPhone("545-254-54");
		newStore.setMail("test@mail.com");
		newStore.setUser(owner);
		
		compList = new ArrayList<>();
		node= new StoreCompositeProductNode();
		node.setPrice(1100);
		node.setCompositeProduct(s);
		node.setStore(newStore);
		
		compList.add(node);
		newStore.setStoreCompositeProductNode(compList);
		
		storeServiceImpl.save(newStore);
		storeCompositeProductServiceImpl.save(node);
	}

	@Autowired
	private RoleServiceImpl roleServiceImpl;
	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;

	public void setEmployee(User user) {
	
		List<Store> stores = storeServiceImpl.findAllByUser(user);
		Role role1 = roleServiceImpl.findOneByName("EMPLOYEE_WITH_ACCESS");
		stores.forEach(store -> {
			
			Set<Role> roles = new HashSet<>();
			roles.add(role1);
			AccountApp accountApp = new AccountApp();
			 accountApp.setLogin("log" + store.getId());
			 accountApp.setPassword(new BCryptPasswordEncoder().encode("ter"));
			 accountApp.setRoles(roles);
			
			Employee employee = new Employee();
			employee.setFirstName("BOBA");
			employee.setLastName("BIBA");
			employee.setSalary(900000);
			employee.setCashBoxType(0);
			employee.setHairingDate(LocalDate.now());
			employee.setUser(user);
			employee.setStore(store);
			employee.setAccountApp(accountApp);
		

			employeeServiceImpl.save(employee);

		

		});

	}
	
	@Autowired
	private InvoiceServiceImpl invoiceServiceImpl;
	
	public void getInvoice(User user) {
		List<Store> st = storeServiceImpl.findAllByUser(user);
		st.forEach(store->{
			CompositeProduct s = compositeProductServiceImpl.findByIdAndUser(1l, user).get();
			List<InvoiceNode> invoiceNodes = new ArrayList<>();
			
			Invoice invoice = new Invoice ();
			invoice.setCashBox(0);
			invoice.setCashBoxStartDay(3330);
			LocalDate date = LocalDate.now();
			invoice.setDate(date.minusDays(1));
			invoice.setOrderQuantity(0);
			invoice.setSellQuantity(0);
			invoice.setStore(store);
			
			InvoiceNode invoiceNode = new InvoiceNode();
			invoiceNode.setPrice(11000);
			invoiceNode.setCompositeProduct(s);
			invoiceNode.setInvoice(invoice);
			invoiceNode.setSaleQuantity(2*(int)store.getId());
			invoiceNode.setTime(LocalDateTime.now());
			invoiceNodes.add(invoiceNode);
			invoice.setInvoiceNode(invoiceNodes);
			
			invoiceServiceImpl.save(invoice);
		});
		
	}

}
