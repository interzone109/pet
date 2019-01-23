package ua.squirrel.z.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.squirrel.user.partner.entity.Partner;
import ua.squirrel.user.product.entity.CompositeProduct;
import ua.squirrel.user.product.entity.Product;
import ua.squirrel.user.product.helper.service.GroupProductServiceImpl;
import ua.squirrel.user.product.helper.service.MeasureProductServiceImpl;
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
	private  GroupProductServiceImpl groupProductServiceImpl;
	@Autowired
	private  MeasureProductServiceImpl measureProductServiceImpl;
	@Autowired
	ProductServiceImpl productServiceImpl ;
	
	public Set<CompositeProduct> getCofeeProduct(User owner ){
		Set<CompositeProduct> listCompositeProduct = new HashSet<>();
		//List<CompositeProduct> listCompositeProduct = new ArrayList<>();
		
		
		CompositeProduct compositeProduct = new CompositeProduct();
		compositeProduct.setName("Американо");
		compositeProduct.setUser(owner);
		
		//List<Product> products = new ArrayList<>();
		// List<Integer> consumption = new ArrayList<>();
		Map<Product, Integer> productsConsumption = new HashMap<>();
		
		 owner.getPartners().stream().forEach(partner->{
			 partner.getProducts().stream().forEach(prod->{
				 if(prod.getName().equals("Кофе Черная тара")) { productsConsumption.put(prod, 10); }
				 else if ( prod.getName().equals("Вода")) {productsConsumption.put(prod, 30);}
				 else if( prod.getName().equals("Сахар стик")) {productsConsumption.put(prod, 2);}
				 else if ( prod.getName().equals("Стаканчик 0.33")) {productsConsumption.put(prod, 1);}
				 else  if ( prod.getName().equals("Пластиковое мешало")) {productsConsumption.put(prod, 1); }
			 });
		 });
		
		// compositeProduct.setProducts(products);
		// compositeProduct.setConsumption(consumption);
		 compositeProduct.setProductsConsumption(productsConsumption);
		listCompositeProduct.add(compositeProduct);
		
		return listCompositeProduct ;
	}
	
	
	
	
	
	
	public  List<Partner> getPartner() {
		List<Partner> partner = new ArrayList<>();
		
	
			Partner p = new Partner();
			p.setCompany("Кофеиновый делец");
			p.setPartnerMail("cofee@mail.com");
			p.setProducts(getCofee(p));
			p.setPhonNumber("21-313-213");
			partner.add(p);
			
			p = new Partner();
			p.setCompany("Картошечный флибустьер");
			p.setPartnerMail("potato@mail.com");
			p.setProducts(getPotato(p));
			p.setPhonNumber("21-313-288");
			partner.add(p);
			
			p = new Partner();
			p.setCompany("Армейские Расходники Компани");
			p.setPartnerMail("consumbles@mail.com");
			p.setProducts(getConsumbles(p));
			p.setPhonNumber("27-385-288");
			partner.add(p);
		
	
		return partner;
	}

	private  List<Product> getCofee(Partner partner) {
		List<Product> product = new ArrayList<>();
		
		Product p = new Product();
		p.setName("Вода");
		p.setDescription("Вода питьевая");
		p.setPrice(8350);
		p.setPartner(partner);									
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		product.add(p);
		
		p = new Product();
		p.setName("Кофе Черная тара");
		p.setDescription("кофе зерновой ");
		p.setPrice(28300);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		product.add(p);
		
		p = new Product();
		p.setName("Сахар стик");
		p.setDescription("сахар в стиках женный");
		p.setPrice(25);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		p = new Product();
		p.setName("Молоко");
		p.setDescription("молоко в цетропаках");
		p.setPrice(1935);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		product.add(p);

		p = new Product();
		p.setName("Чай 1000 вопросов");
		p.setDescription("Чай отвечай");
		p.setPrice(2635);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		
		return product;
	}
	
	private  List<Product> getPotato(Partner partner) {
		List<Product> product = new ArrayList<>();
		
		
		Product p = new Product();
		p.setName("Картошка");
		p.setDescription("катошка фри мороженая");
		p.setPrice(550);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPLETE_COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		product.add(p);
		
		p = new Product();
		p.setName("Масло Стожор");
		p.setDescription("Масло подсолнечное стожор банка 0.9л ");
		p.setPrice(2980);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("LITER"));
		product.add(p);
		
		p = new Product();
		p.setName("Соль столовая");
		p.setDescription("Сель соленая");
		p.setPrice(830);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("KILOGRAM"));
		product.add(p);
		
		
		return product ;
		}
	
	private  List<Product> getConsumbles(Partner partner) {
		List<Product> product = new ArrayList<>();
		
		
		
		Product p = new Product();
		p.setName("Стаканчик 0.33");
		p.setDescription("Стаканчик 0.33");
		p.setPrice(55);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		p = new Product();
		p.setName("Стаканчик 0.45");
		p.setDescription("Стаканчик 0.45");
		p.setPrice(65);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		p = new Product();
		p.setName("Стаканчик 0.66");
		p.setDescription("Стаканчик 0.66");
		p.setPrice(85);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		p = new Product();
		p.setName("Крышка для стаканов");
		p.setDescription("Крышка для стаканов");
		p.setPrice(25);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		p = new Product();
		p.setName("Пластиковое мешало");
		p.setDescription("Мешало для кофе чай");
		p.setPrice(15);
		p.setPartner(partner);
		p.setGroupProduct(groupProductServiceImpl.findOneByName("COMPOSITE"));
		p.setMeasureProduct(measureProductServiceImpl.findOneByMeasure("UNIT"));
		product.add(p);
		
		return product ;
	}
}
