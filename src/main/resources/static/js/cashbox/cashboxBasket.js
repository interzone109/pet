//метод добавляет товар в корзину
function addProductToBusket( productId){
	var storeId = $("#cuurent_store_id").text();
	var prod = state.get("storeId_"+storeId).filter(product => product.id === productId);
	
	if($("#busket_id_"+prod[0].id).length == 0){
		$("#busketProductList").prepend(createBusketElem(prod[0]) );
	}else{
		addProductToexistBusketElem(prod[0]);
	}
	recountTotalPrice();
}
$("#hiddenFoter").hide() ;

//создаем карточку с товаром для конзины
function createBusketElem(product){
	var quantity = $("#input_product_id_"+product.id).val()<=0 ? 1 :$("#input_product_id_"+product.id).val();
	var description = quantity + " шт X " + displayProductPrice(product.propertiesProduct);
	 var productElem = document.createElement('li');
	 productElem.id="busket_id_"+product.id;
	 productElem.innerHTML =   "<div class=\"conteiner border-info card bg-info text-center\">"
		+"<div class=\"card-title \">"
		+product.name
		+"<button id=\"remove\" type=\"button\" class=\"close \" onclick=\"removeFromBusket("+product.id+")\">"
		+"<span aria-hidden=\"true\">&times;</span> </button> " 
		+ "</div> <div id=\"busket_description_"+product.id +"\">"
		+description
		+"</div> </div> <br>"
		 
	 return productElem;

}



// метод суммирует количество продуктов если такой уже был в корзине
function addProductToexistBusketElem(product){
	
	var quantity = $("#input_product_id_"+product.id).val()<=0 ? 0 :$("#input_product_id_"+product.id).val();
	var description = $("#busket_description_"+product.id).text().split(" X ");
	quantity = parseInt(quantity) + parseInt(description[0]);
	console.log(quantity+" шт X "+description[1]);
	$("#busket_description_"+product.id).text(quantity+" шт X "+description[1]);
}

// метод пересчитывает итоговую сумму покупки
function recountTotalPrice(){
	var id ;
	var totalPrice = 0;
	var size = $("#busketProductList").children().length;
	 size > 3 
		? $("#hiddenFoter").show()
		:$("#hiddenFoter").hide() ;
	
	
	for(var i = 0; i < size ;i++){
		id = $("#busketProductList").children()[i].id.split("busket_id_")[1];
		var description = $("#busket_description_"+id).text().split(" шт X ");
		totalPrice += description[0]*description[1].split(" ")[0]*100;
	}
	$("#topPrice").text(displayProductPrice(totalPrice));
	$("#bottomPrice").text(displayProductPrice(totalPrice));
}



//удоляем продукт из корзины
function removeFromBusket(productId){
	$("#busket_id_"+productId).remove();
	recountTotalPrice();
}




function conductSale(){
	var storeId = $("#cuurent_store_id").text();
	var size = $("#busketProductList").children().length;
	var idsQuantity = "";
	for(var i = 0; i < size ;i++){
		id = $("#busketProductList").children()[i].id.split("busket_id_")[1];
		var description = $("#busket_description_"+id).text().split(" шт X ");
		idsQuantity += "\""+id+"\":"+description[0]+",";
	}
	
	var invoiceData = "{"+idsQuantity.slice(0, -1) + "}";
	var today = new Date();
	var dd = String(today.getDate()).padStart(2, '0');
	var mm = String(today.getMonth() + 1).padStart(2, '0');
	var yyyy = today.getFullYear();

	 var data  = 
				"{\"dateStart\":"+ "\""+dd+"."+mm+"."+yyyy+"\","
		        +"\"cashBoxStartDay\":"+ 0+","
		        +"\"invoiceData\":"+ invoiceData+","
		        +"\"currentSell\":"+ $("#topPrice").text().split(" ")[0]*100+","
		        +"\"sellQuantity\":"+ 1+","
		        +"\"storeId\":"+ Number.parseInt(storeId)+"}";
			   
	 
	 request('PUT', connectUrl+'/user/stores/invoice/'+storeId, updateCashboxInfo,data);
	$("#busketProductList").empty();
	$("#hiddenFoter").hide()
	$("#topPrice").text(displayProductPrice(0));
	$("#bottomPrice").text(displayProductPrice(0));
}











function comfirmSell(){
	console.log("sell");
	
}