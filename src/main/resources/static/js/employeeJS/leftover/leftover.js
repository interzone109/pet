var connectUrl = "http://localhost:8080";

//метод возращает остатки для выбраного магазина
function searchStoreLeftovers(){
	$('#leftoverTableBodyId').empty();//чистим таблицу от старых данных

	request("GET",connectUrl + "/employee/stores/leftover", createLeftoverRow );

	$('#leftoverTableId').collapse("show");
	$('#invoiceTableId').collapse("hide");
	
}

function createLeftoverRow(data){
	console.log(data);
	if(data.length > 0){
		data.forEach(product => {
		$('#leftoverTableBodyId').append(displayProductRow(product));
		});
	}
}
//метод формирует строку для таблицы остатков
function displayProductRow(product){
	var productRow = document.createElement('tr');
	  productRow.id = "product_row_id_"+product.id;
	  
	  productRow.innerHTML = "<td id=\"product_group_id_"+product.id+"\">"+product.group+"</td>"
		 + "<td id=\"product_name_id_"+product.id+"\">"+product.name+"</td>"
		 + "<td id=\"product_leftover_id_"+product.id+"\">"+createMeasureProduct(product.description,product.measureProduct)+"</td>"
		 +"<td id=\"product_measure_id_"+product.id+"\">"+displayProductMeasure(product.measureProduct,1)+"</td>"
		 +"<td id=\"product_properties_id_"+product.id+"\">"+displayProductProperties(product.propertiesProduct,1)+"</td>";
		
		  return productRow;
}



















//метод преобразовывает количество ингридиента например 100 = 0.100 кг
function createMeasureProduct(expend, measure){
	var result ;
	if(measure ==="UNIT"){
		result = expend;
	}else {
		if(expend.length < 2){
		result = "0.00" + expend + meas ;
		}else if(expend.length < 3){
		result = "0.0" + expend + meas ;
		}else if(expend.length <4){
		result = "0." + expend + meas ;
		}
		else{
		var strStart = expend.substring(0,expend.length-3);
		var strEnd = expend.substring(expend.length-3 ,expend.length);
		result = strStart+"."+strEnd  ;
		}
	} 
	return result
}


function displayProductMeasure (measure ,convert){
	 
	if( measure ==="LITER" || measure==="литр"){
		return (convert === 1)?"литр": "LITER";
	}else if( measure ==="KILOGRAM" || measure==="кило"){
		return  (convert === 1)?"кило": "KILOGRAM";
	}else if( measure ==="UNIT" || measure==="шт"){
		return  (convert === 1)?"шт": "UNIT";
	}
	
};


function displayProductProperties(properties,convert){
	
	if( properties ==="INGREDIENS" || properties==="ингридиент"){
		return (convert === 1)?"ингридиент": "INGREDIENS";
	}else if( properties ==="CONSUMABLES" || properties==="расходник"){
		return  (convert === 1)?"расходник": "CONSUMABLES";
	}else if( properties ==="PRODUCT_FINAL" || properties==="конечный продукт"){
		return  (convert === 1)?"конечный продукт": "PRODUCT_FINAL";
	}else if( properties ==="PRODUCT_COMPOSITE" || properties==="универсальный"){
		return  (convert === 1)?"универсальный": "PRODUCT_COMPOSITE";
	}

};

/** ****************** search function *************************** */

$(document).ready(function(){
	  $("#searchOnPageTable").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    var tableName =  ($("#leftoverTableId").is(':visible')) ?"leftoverTableBodyId" :"invoiceTableBodyId" ;
	    $("#"+tableName +" tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});

/** ****************** search function *************************** */

//устанавливаем период по умолчанию
function fillDate(){
	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setDate(today.getDate()+1);
	
	var dd = String(today.getDate()).padStart(2, '0');
	var mm = String(today.getMonth() + 1).padStart(2, '0');
	var yyyy = today.getFullYear();

	var period = String(today.getDate()).padStart(2, '0')
	+ '.' + String(today.getMonth() + 1).padStart(2, '0')
	+ '.' + today.getFullYear()+"-"
	+String(tomorrow.getDate()).padStart(2, '0')
	+ '.' + String(tomorrow.getMonth() + 1).padStart(2, '0')
	+ '.' + tomorrow.getFullYear();
	$("#dataRangeValue").val(period);
}
fillDate();

//формируем цену в удобнов формате
function displayProductPrice(price){
	if(price < 9){
		return "0.0"+price;
	}
	else if(price <99){
		return "0."+price;
	}
	else if(price >99){
	return price/100; 
	}
}