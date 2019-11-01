package ua.squirrel.user.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import ua.squirrel.user.entity.store.invoice.Invoice;
import ua.squirrel.user.entity.store.invoice.InvoiceModel;
import ua.squirrel.user.entity.store.invoice.node.InvoiceNode;

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
					.partner(Integer.toString(productNode.getCurrentQuantity()))
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
					.totalSumm(compositeNode.getPrice())
					.measureProduct(compositeProd.getMeasureProduct().getMeasure()).build());
		});
		return compositeProducts;
	}
	
	/**
	 * Метод создает новые узлы с ингридиентом и количеством
	 * для накладной
	 * */
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
	
	// метод добавляет новые ингридиенты в накладную
	public Consignment uniqueConsigment( Consignment consignment, List<Product> currentProduct) {
		List<Product> existProduct = new ArrayList<>();//колекция хранит список уже имеющихся ингридиентов
		consignment.getConsignmentNode().forEach(consignmentNode-> existProduct.add(consignmentNode.getProduct()) );
		/**
		 * передаем в метод fillConsigment список (currentProduct) который может содержать новые игридиенты
		 * предварительно убераем из списка (currentProduct) все ингридиенты которые уже есть в накладной и находятся в списке (existProduct)
		 * */
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
	public void updateStoreLeftoversForSale(Store store,  Map<Product, Integer> consignmentData, String sing ) {
		List<StoreIngridientNode> ingridientNode = store.getStoreIngridientNode();
		
			ingridientNode.forEach(node->{
				if(consignmentData.containsKey(node.getProduct())){ 
					if (sing.equals("+")) {
						int leftover = node.getLeftOvers()  + consignmentData.get(node.getProduct());
						node.setLeftOvers(leftover);
					}else {
						int leftover = node.getLeftOvers() - consignmentData.get(node.getProduct());
						node.setLeftOvers(leftover);
					}
				}
			});
		
	}
	
	/**
	 * Метод формирует список инвойсов с данными о продажах
	 * */
	public List<InvoiceModel> createSaleProductViev(List<Invoice> invoiceList){
		List<InvoiceModel> invoiceModelList = new ArrayList<>();
		invoiceList.forEach(invoice->{
			InvoiceModel invoiceModel = createInvoiceModel(invoice);
			invoiceModel.setInvoiceNode(getTotalInvoiceData(invoice.getInvoiceNode()));
			invoiceModelList.add(invoiceModel);
		});
		return invoiceModelList;
	}
	
	
	/**
	 * 
	 * @param invoiceNodeList
	 * @return метод возращает список моделей композитного продукта в котором находиться 
	 * количество его продаж и общая сумма (для получения седней цены) в разрезе дня
	 */
	public Collection<CompositeProductModel> getTotalInvoiceData(List<InvoiceNode> invoiceNodeList){
		Map<Long, CompositeProductModel> compositeProductModelMap = new HashMap<>();
		invoiceNodeList.forEach(invoiceNode->{// если продукт с таким ид уже есть в мапе то обновляем данные
			long compProdId = invoiceNode.getCompositeProduct().getId();
			if(compositeProductModelMap.containsKey(compProdId)) {
				CompositeProductModel compositeProductModel = compositeProductModelMap.get(compProdId);
				int sellQuantite = compositeProductModel.getSellQuantite();
				int totalSumm = compositeProductModel.getTotalSumm();
				compositeProductModel.setSellQuantite(sellQuantite + invoiceNode.getSaleQuantity());
				compositeProductModel.setTotalSumm(totalSumm + (invoiceNode.getPrice()*invoiceNode.getSaleQuantity()));
			}else {// если нет то создаем новую модель
			CompositeProductModel compositeProductModel = CompositeProductModel.builder()
					.id(compProdId)
					.name(invoiceNode.getCompositeProduct().getName())
					.totalSumm(invoiceNode.getPrice()*invoiceNode.getSaleQuantity())
					.measureProduct(invoiceNode.getCompositeProduct().getMeasureProduct().getMeasure())
					.sellQuantite(invoiceNode.getSaleQuantity())
					.build();		
			compositeProductModelMap.put(compProdId, compositeProductModel);
			}
		});
		return compositeProductModelMap.values() ;
	}
	
	
	public InvoiceModel createInvoiceModel(Invoice invoice) {
		return InvoiceModel.builder()
				.id(invoice.getId())
				.cashBox(invoice.getCashBox())
				.cashBoxStartDay(invoice.getCashBoxStartDay())
				.dateStart(invoice.getDate().toString())
				.orderQuantity(invoice.getOrderQuantity())
				.sellQuantity(invoice.getSellQuantity())
				.storeId(invoice.getStore().getId())
				.build();
	}
}
