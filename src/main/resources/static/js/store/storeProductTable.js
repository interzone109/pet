var connectUrl ='http://localhost:8080';


function showProductPrice(storeId){
	$("#collapseStoreBody").collapse("hide");
	$("#collapseProductStoreBody").collapse("show");
	$("#compositeStoreId").text(storeId);
	// запрос  на  получения списка ккомпозитных товаров
	request('GET', connectUrl+'/user/stores/assortment/'+storeId, fillStoreProductTable);
	
	$("#namePlaceholder").val("Магазин - "+$("#store_address_id_"+storeId).text());// находим название магазина по ид и устанавливаем его
	$("#namePlaceholder").collapse("show");//показываем название магазина
	
}

// метод заполняет таблицу с продуктами
function fillStoreProductTable(dataJSON){
	if( Array.isArray(dataJSON)){
		dataJSON.forEach(productRow => {
			$('#storeProductTable').append(createNewStoreProductRow( productRow));
		});
	}else{
		  $('#storeProductTable').append(createNewStoreProductRow( productRow));
	}
}
// метод создает строку продукт - цена
 function createNewStoreProductRow(product){
	 var productRow = document.createElement('tr');
	 productRow.id = "product_row_id_"+product.id;
		  
	 productRow.innerHTML = "<td id=\"product_group_id_"+product.id+"\">"+product.group+"</td>"
		 	 +"<td id=\"store_name_id_"+product.id+"\">"+product.name+"</td>"
			 + "<td id=\"product_price_id_"+product.id+"\">"+displayProductPrice( product.propertiesProduct)+"</td>"
			 + "<td id=\"product_price_id_"+product.id+"\">"+displayProductMeasure(product.measureProduct, 1)+"</td>"
			 +"<td><span class=\"badge badge-success\"> <i class=\"fas fa-edit\" title=\"изменить цену\" onclick=\"updateStoreProductDataModal("+product.id+")\" ></i></span> " 
			 +"<span class=\"badge badge-info\"><i class=\"fas fa-list-alt\" title=\"вернуться к магазинам\" onclick=\"showStoreTable()\"  ></i></span> </td>"
			
			 
			 document.getElementById("productPriceIds").innerText+=product.id+" ";
			 
			  return productRow;
 }



// делаем запрос на получение списка всех доступных продуктов
function postNewProductStoreForm(){
	 $("#compositeProductGroup").empty();// чистим строки с селектами
	 $('#newModalProductStoreForm').modal("show");
	 
	 request("GET", connectUrl+"/user/composites", displayAvailableCompositeRow);

}


//формируем список всех поступных продуктов и добавляем их к селекту
function displayAvailableCompositeRow(products){
		products.forEach(product => {
			$('#compositeProductGroup').append(addProductItem( product));
		});
		
		var array =$('#productPriceIds').text().split(" ");
		for (var i = 0; i < array.length; i++) {
			$("#option_product_id_"+array[i]).hide();
		}
}



//формируем строки селекта
function addProductItem(product){

	var ingridient = document.createElement('option');
	ingridient.id="option_product_id_"+product.id;

	var name ;
	if( product.name.length < 44){
		name = product.name;
	}else{
		name = product.name.substring(0, 40)+"...";
	}
	ingridient.value = product.id;
	ingridient.innerText = name; 
	
	ingridient.setAttribute('title', name);
	return ingridient;

}

// метод проверяет валидность данных и отправляет данные на сервер
$("#newProductStoreButton").on("focus",postNewProductPrice);
function postNewProductPrice(){
	// получаем выбранный продукт
	var productId = $('#compositeProductGroup option:selected').val();
	if( typeof  productId === 'undefined'){
		$("#updateErroreProductChose").text("выберите один продукт");
		$("#updateErroreProductChose").collapse("show");
		productId = null;
	}else{
		$("#updateErroreProductChose").collapse("hide");
	}
	//получаем цену
	var price = $('#updateCompositeProductPrice').val();
	console.log(price.length);
	var priceNumb = Number.parseFloat(price) ;
	
	if( price.length === 0  || priceNumb <= 0 ){
		$("#updateErrorePrice").text("введите стоимость товара / услуги");
		$("#updateErrorePrice").collapse("show");
		price = null;
	}else{
		$("#updateErrorePrice").collapse("hide");
			priceNumb *=  100;
			
			var storeId = $('#compositeStoreId').text();
			
			if(price !==null &&productId !== null ){
				$("#newModalProductStoreForm").modal("hide");
				var data = "{\""+productId+"\":"+priceNumb+"}";
				request('POST', connectUrl+'/user/stores/assortment/'+$('#compositeStoreId').text(), fillStoreProductTable,data);
			}
	}
}



//мутод открывает окно для обновления цены товара
function updateStoreProductDataModal(id){
	$("#updateProductPriceForm").modal("show");
	$("#sendProductId").text(id);//передаем айдишник товара
	$("#updatePrice").val($("#product_price_id_"+id).text().split(" ")[0]);
}


// медот срабатывает при отправки формы с новой цено на сервер
// валидирует данные и создает json
function updateStoreProductDataSend(){
	var productId = $("#sendProductId").text();
	var input = $("#updatePrice").val();
	var price = Number.parseFloat(input)*100 ;
	
	if(input.length !== 0 && price > 0){
		$("#updatePriceErrore").modal("hide");
		
		var data = "{\""+productId+"\":"+price+"}";
		request('PUT', connectUrl+'/user/stores/assortment/'+$('#compositeStoreId').text(), updateProductPriceRow,data);
		$("#updateProductPriceForm").modal("hide");
	}else{
		
		$("#updatePriceErrore").text("введите новую цену");
		$("#updatePriceErrore").modal("show");
	}
}

//метод обновляет цену у выбраного товара
function updateProductPriceRow(updateProducts){
	updateProducts.forEach(data=>{
		var oldPrice = $("#product_price_id_"+data.id).text().split(" ");
		oldPrice[0]= Number.parseFloat(data.propertiesProduct)/100;
		$("#product_price_id_"+data.id).text(oldPrice[0]+" "+oldPrice[1]);
	});
	
}





// формируем цену в удобнов формате
function displayProductPrice(price){
	if(price < 9){
		return "0.0"+price+" грн";
	}
	else if(price <99){
		return "0."+price+" грн";
	}
	else if(price >99){
	return price/100+" грн"; 
	}
}
// выводим меру измерения
function displayProductMeasure(measure ,convert){
	 
	if( measure ==="LITER" || measure==="л"){
		return (convert === 1)?"л": "LITER";
	}else if( measure ==="KILOGRAM" || measure==="кг"){
		return  (convert === 1)?"кг": "KILOGRAM";
	}else if( measure ==="UNIT" || measure==="шт"){
		return  (convert === 1)?"шт": "UNIT";
	}
}

