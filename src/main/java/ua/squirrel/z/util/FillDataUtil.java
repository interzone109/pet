package ua.squirrel.z.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.partner.service.PartnerServiceImpl;
import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.user.product.entity.Product;
import ua.squirrel.user.product.helper.service.MeasureProductServiceImpl;
import ua.squirrel.user.product.helper.service.PropertiesProductServiceImpl;
import ua.squirrel.user.product.service.ProductServiceImpl;
import ua.squirrel.web.user.entity.User;

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
	
	public List<CompositeProduct> getProduct(User owner ){
		List<CompositeProduct> listCompositeProduct = new ArrayList<>();
		
		CompositeProduct americano = new CompositeProduct();
		americano.setName("Американо");
		americano.setUser(owner);


		CompositeProduct tea = new CompositeProduct();
		tea.setName("чай");
		tea.setUser(owner);
		
		CompositeProduct fries = new CompositeProduct();
		fries.setName("картошка фри 250");
		fries.setUser(owner);
		
		CompositeProduct friesBig = new CompositeProduct();
		friesBig.setName("картошка фри 500");
		friesBig.setUser(owner);
	
		
		
		
		Map<Long, Integer> productAmericano = new HashMap<>();
		Map<Long, Integer> productTea = new HashMap<>();
		Map<Long, Integer> productFries = new HashMap<>();
		Map<Long, Integer> productFriesBig = new HashMap<>();
		
		
		partnerServiceImpl.findAllByUser(owner).stream().forEach(partner->{
			 partner.getProducts().stream().forEach(prod->{
				 if(prod.getName().equals("Кофе Черная тара")) { productAmericano.put(prod.getId(), 10); }
				 else if ( prod.getName().equals("Вода")) {productAmericano.put(prod.getId(), 30);}
				 else if( prod.getName().equals("Сахар стик")) {productAmericano.put(prod.getId(), 2);}
				 else if ( prod.getName().equals("Стаканчик 0.33")) {productAmericano.put(prod.getId(), 1);}
				 else  if ( prod.getName().equals("Пластиковое мешало")) {productAmericano.put(prod.getId(), 1); }
				 
				  if ( prod.getName().equals("Вода")) {productTea.put(prod.getId(), 50);}
				  else if ( prod.getName().equals("Чай 1000 вопросов")) {productTea.put(prod.getId(), 5);}
				  else if ( prod.getName().equals("Стаканчик 0.66")) {productTea.put(prod.getId(), 1);}
				  else if ( prod.getName().equals("Пластиковое мешало")) {productTea.put(prod.getId(), 1);}
				 
				 
				  if ( prod.getName().equals("Картошка")) {productFries.put(prod.getId(), 250);}
				  else  if ( prod.getName().equals("Масло Стожор")) {productFries.put(prod.getId(), 50);}
				  else  if ( prod.getName().equals("Соль столовая")) {productFries.put(prod.getId(), 5);}
				  
				  if ( prod.getName().equals("Картошка")) {productFriesBig.put(prod.getId(), 500);}
				  else  if ( prod.getName().equals("Масло Стожор")) {productFriesBig.put(prod.getId(), 100);}
				  else  if ( prod.getName().equals("Соль столовая")) {productFriesBig.put(prod.getId(), 10);}
			 });
		 });
		
		
		americano.setProductsConsumption(productAmericano);
		tea.setProductsConsumption(productTea);
		fries.setProductsConsumption(productFries);
		friesBig.setProductsConsumption(productFriesBig);
		
		
		listCompositeProduct.add(americano);
		listCompositeProduct.add(tea);
		listCompositeProduct.add(fries);
		listCompositeProduct.add(friesBig);
		
		return listCompositeProduct ;
	}
	
	
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
		p.setName("Вода");
		p.setDescription("Вода питьевая");
		p.setGroup("воды");
		p.setPartner(partner);									
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Кофе Черная тара");
		p.setDescription("кофе зерновой ");
		p.setGroup("чаи");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Сахар стик");
		p.setDescription("сахар в стиках женный");
		p.setGroup("");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Молоко");
		p.setDescription("молоко в цетропаках");
		p.setGroup("молочка");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);

		p = new Product();
		p.setName("Чай 1000 вопросов");
		p.setDescription("Чай отвечай");
		p.setGroup("чаи");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		productServiceImpl.saveAll(product);
		return product;
	}
	
	private  List<Product> getPotato(Partner partner,User owner) {
		List<Product> product = new ArrayList<>();
		
		
		Product p = new Product();
		p.setName("Картошка");
		p.setDescription("катошка фри мороженая");
		p.setGroup("овощи");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPLETE_COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Масло Стожор");
		p.setDescription("Масло подсолнечное стожор банка 0.9л ");
		p.setGroup("");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Соль столовая");
		p.setDescription("Сель соленая");
		p.setGroup("");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
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
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Стаканчик 0.45");
		p.setDescription("Стаканчик 0.45");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Стаканчик 0.66");
		p.setDescription("Стаканчик 0.66");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Крышка для стаканов");
		p.setDescription("Крышка для стаканов");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		p = new Product();
		p.setName("Пластиковое мешало");
		p.setDescription("Мешало для кофе чай");
		p.setGroup("тара");
		p.setPartner(partner);
		p.setPropertiesProduct(propertiesProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		p.setUser(owner);
		product.add(p);
		
		productServiceImpl.saveAll(product);
		
		return product ;
	}
}
