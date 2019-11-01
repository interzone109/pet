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
$("#hiddenFoter").hide() ;//
$("#restCountInput").hide() ;//

$("#cashTitleBtn").on("click", function (){
	$("#restCountInput").show() ;
	$("#cashTitle").hide() ;
}) ;
$("#restCountInputBtn").on("click", function (){
	$("#cashTitle").show() ;
	$("#restCountInput").hide() ;
}) ;

$(document).ready(function() { $('#calcTooltipinput').tooltip(); });

$('[data-toggle="tooltip"]').tooltip({
    trigger : 'hover'
}) 


//создаем карточку с товаром для коpзины
function createBusketElem(product){
	var quantity = $("#input_product_id_"+product.id).val()<=0 ? 1 :$("#input_product_id_"+product.id).val();
	var measur = displayProductMeasure(product.measureProduct,1);
	var description = quantity + " "+measur+" X " + displayProductPrice(product.totalSumm);
	console.log("create card "+ description);
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
	var measur = displayProductMeasure(product.measureProduct,1);
	var quantity = $("#input_product_id_"+product.id).val()<=0 ? 0 :$("#input_product_id_"+product.id).val();
	var description = $("#busket_description_"+product.id).text().split(" "+measur+" X ");
	quantity = parseInt(quantity) + parseInt(description[0]);
	$("#busket_description_"+product.id).text(quantity+" "+measur+" X "+description[1]);
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
		var measur = $("#prod_measur_"+id ).text();
		var description = $("#busket_description_"+id).text().split(" "+measur+" X ");
		totalPrice += description[0]*description[1].split(" ")[0]*100;
		
	}
	totalPrice =Number(totalPrice.toFixed(2));
	$("#topPrice").text(displayProductPrice(totalPrice));
	$("#bottomPrice").text(displayProductPrice(totalPrice));
}



//удоляем продукт из корзины
function removeFromBusket(productId){
	$("#busket_id_"+productId).remove();
	recountTotalPrice();
}



//метод проводит продажу
function conductSale(){
	
	var storeId = $("#cuurent_store_id").text();
	var size = $("#busketProductList").children().length;
	var idsQuantity = "";
	var totalSellQuantity = 0;
	for(var i = 0; i < size ;i++){
		id = $("#busketProductList").children()[i].id.split("busket_id_")[1];
		var measur = $("#prod_measur_"+id ).text();
		var description = $("#busket_description_"+id).text().split(" "+measur+" X ");
		totalSellQuantity += parseInt(description[0]);
		idsQuantity += "\""+id+"\":"+description[0]+",";
		$("#input_product_id_"+id).val("1");
		
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
		        +"\"cashBox\":"+ $("#topPrice").text().split(" ")[0]*100+","
		        +"\"orderQuantity\":"+ 1+","
		        +"\"sellQuantity\":"+ totalSellQuantity+","
		        +"\"storeId\":"+ Number.parseInt(storeId)+"}";
			   
	 if(totalSellQuantity !==0 ){
	 request('PUT', connectUrl+'/user/stores/invoice/'+storeId, updateCashboxInfo,data);
	 }
	$("#busketProductList").empty();
	$("#hiddenFoter").hide()
	$("#topPrice").text(displayProductPrice(0));
	$("#bottomPrice").text(displayProductPrice(0));
}
//метод считает сачу за товар
$("#calcTooltipinput").on("input", function(){
	var inputSum =parseFloat( $(this).val());
	var totalPrice = parseFloat($("#topPrice").text());
	 $("#restText").text(parseFloat(inputSum-totalPrice));
	
});

$( "#calcTooltipinput" )
.focusout(function() {
	$(this).val(0);
});





