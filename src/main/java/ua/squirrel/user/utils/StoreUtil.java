package ua.squirrel.user.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ua.squirrel.user.entity.product.Product;
import ua.squirrel.user.entity.product.ProductModel;
import ua.squirrel.user.entity.product.composite.CompositeProduct;
import ua.squirrel.user.entity.product.composite.CompositeProductModel;
import ua.squirrel.user.entity.store.Store;
import ua.squirrel.user.entity.store.compositeproduct.node.StoreCompositeProductNode;
import ua.squirrel.user.entity.store.consignment.Consignment;
import ua.squirrel.user.entity.store.consignment.node.ConsignmentNode;
import ua.squirrel.user.entity.store.ingridient.node.StoreIngridientNode;

@Component
public class StoreUtil extends SmallOneUtil {
	
	public List<ProductModel> getConsigmentProductPrice(Consignment consignment){
		List<ProductModel> productModelList = new ArrayList<>();
		consignment.getConsignmentNode().forEach(productNode->{
			Product product = productNode.getProduct();
			productModelList.add(ProductModel.builder()
					.id(product.getId())
					.name(product.getName())
					.measureProduct(product.getMeasureProduct().getMeasure())
					.propertiesProduct(Integer.toString(productNode.getQuantity()))//quantity
					.description(Integer.toString(productNode.getUnitPrice()))//price
					.group(product.getGroup())
					.build()
					);
			
		});
		return productModelList;
	}
	
	public List<CompositeProductModel> getProductPriceModel(List<StoreCompositeProductNode> storeCompositeProductNode) {
		List<CompositeProductModel> compositeProducts = new ArrayList<>();
		storeCompositeProductNode.forEach(compositeNode -> {
			CompositeProduct compositeProd = compositeNode.getCompositeProduct();
			compositeProducts.add(CompositeProductModel.builder().id(compositeProd.getId())
					.name(compositeProd.getName()).group(compositeProd.getGroup())
					.propertiesProduct(Integer.toString(compositeNode.getPrice()))
					.measureProduct(compositeProd.getMeasureProduct().getMeasure()).build());
		});
		return compositeProducts;
	}
	
	
	public Consignment fillConsigment( Consignment consignment, List<Product> currentProduct) {
		List<ConsignmentNode> consignmentsNode = consignment.getConsignmentNode();
		currentProduct.forEach(product->{
			 
			ConsignmentNode consignmentNode = new ConsignmentNode();
			consignmentNode.setConsignment(consignment);
			consignmentNode.setProduct(product);
			consignmentNode.setQuantity(0);
			consignmentNode.setUnitPrice(0);
			consignmentsNode.add(consignmentNode);
		});
		return consignment;
	}
	
	
	public Consignment uniqueConsigment( Consignment consignment, List<Product> currentProduct) {
		List<Product> existProduct = new ArrayList<>();
		consignment.getConsignmentNode().forEach(consignmentNode->
			existProduct.add(consignmentNode.getProduct()) );
		return fillConsigment(consignment, 
				currentProduct.stream()
				.filter(product->!existProduct.contains(product))
				.collect(Collectors.toList()));
	}
	
	public void updateStoreLeftovers(Store store,  Map<Long, String> consignmentData, String sing ) {
		List<StoreIngridientNode> ingridientNode = store.getStoreIngridientNode();
		
			ingridientNode.forEach(node->{
				if(consignmentData.containsKey(node.getProduct().getId())){
					String[] data = consignmentData.get(node.getProduct().getId()).split(":|quantity|price");
					if (sing.equals("+")) {
						int leftover = node.getLeftOvers() + Integer.parseInt(data[1]);
						node.setLeftOvers(leftover);
					}else {
						int leftover = node.getLeftOvers() - Integer.parseInt(data[1]);
						node.setLeftOvers(leftover);
					}
				}
			});
		
	}




}
