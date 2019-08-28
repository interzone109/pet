var state = new Map();//переменная хранит данные о запросе остатков для разным магазинов
var connectUrl = "http://localhost:8080";
$("#spiners").hide();
// получаем список  магазинов
request("GET",connectUrl + "/user/stores", addStoreSearchRow );

//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 1 ){// если магазинов у поьзователя нет то просм его создать
		$("#storeSelect").append("<option value=\"0\">Создайте магазин</option>");
		$("#searchIngridientBytton").prop("disabled","disabled");// блокируем кнопку поиска
	} else {
		$("#searchIngridientBytton").prop("disabled",false);// разблокируем кнопку поиска
		data.forEach(store=>{
			$("#storeSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");//добавлям селекты с названиями лоступных магазинов
			});
		$("#storeSelect").val(data[0].id);//устанавливаем selected первому магазину
		state.set("stores", data);
		
	}	
}


// метод возращает остатки для выбраного магазина
function searchStoreLeftovers(){
	$('#leftoverTableBodyId').empty();//чистим таблицу от старых данных
	var storeId = $("#storeSelect option:selected").val()
	var storeLeftover = state.get(storeId+":storeLeftover")
	if(storeLeftover !== undefined ){
		createLeftoverRow(storeLeftover)
		console.log("variable data");
	}else{
		console.log("request data");
		$("#spiners").show();
	request("GET",connectUrl + "/user/stores/assortment/"+storeId+"/leftovers", createLeftoverRow );
	
	
	}
	$('#invoiceTableId').collapse("hide");
	
}

function createLeftoverRow(data){
	if(data.length > 0){
		var storeId = $("#storeSelect option:selected").val()
		state.set(storeId+":storeLeftover", data);
		data.forEach(product => {
		$('#leftoverTableBodyId').append(displayProductRow(product));
		});
	}
	$("#spiners").hide();
	$('#leftoverTableId').collapse("show");
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
//метод формирует таблицу с продажами
function postFindInvoiceByValue(){
	cleaneInvoiceTable();

	var date = $("#dataRangeValue").val().split("-");
	var isValid =  false ;
	var between = false ;
	if( date.length >1){
		isValid = (date[0].length > 8 && date[1].length >8 && formValidation($("#dataRangeValue"))) ;
		between = true ;
	}else{
		isValid =formValidation($("#dataRangeValue"));
	}
	if(isValid){
		
		start = date[0];
		var storeId = $("#storeSelect option:selected").val()
		// если валидация пройдена формируем джейсон
	var data = JSON.stringify({
		 "storeId": storeId,
        "dateStart": date[0],
        "dateEnd": date[1],
        "between" : between
        
	   });
		$("#spiners").show();
		request('POST', connectUrl+'/user/stores/invoice/find',showInvoiceData ,data); 
		$('#leftoverTableId').collapse("hide");
		
	}
}

//метод очищает таблицу от старых данных
function cleaneInvoiceTable(){
	$('#invoiceTableBodyId').empty();
	$('#invoiceDataRow').empty();
	$('#invoiceTableFooterId').empty();
}

function showInvoiceData(data){

	//колонка с названием продукта
	$('#invoiceDataRow').append("<th colspan =\"1\" class=\"text-center\">Название</th>");
	data.forEach(invoice => {
		//колонка с датой инвойса
		$('#invoiceDataRow').append("<th colspan =\"2\" class=\"text-center\" id=\"col_id_"+invoice.id+"\">"
				+invoice.dateStart+"</th>");
		invoice.invoiceNode.forEach(product =>{
			
			 if( !$('#product_name_'+product.id).lenght){//если название отсутсвует в таблице то добавляем новое
					$('#invoiceTableBodyId').append( "<tr id=\"row_id_"+product.id 
							+"\"><th id =\"product_name_" +product.id+"\">"+product.name+"</th></tr>");
					for(var i =0 ; i< data.length; i++){
					$('#row_id_'+product.id).append( "<th class=\"text-center\" id=\"row_quan_" + data[i].id+"_"+product.id+"\">0</th> " +
							"<th class=\"text-center\" id=\"row_price_" + data[i].id+"_"+product.id+"\">0 грн</th>");
					}
				}
		});
	}); 
	data.forEach(invoice => {
		invoice.invoiceNode.forEach(product =>{
			$('#row_quan_'+invoice.id+"_"+product.id).text(
					createMeasureProduct( product.sellQuantite ,product.measureProduct)
					+" "+displayProductMeasure( product.measureProduct ,1));
			$('#row_price_'+invoice.id+"_"+product.id).text(displayProductPrice(product.totalSumm)+" грн");
		});
	});
	$("#spiners").hide();
	$('#invoiceTableId').collapse("show");
}


//метод преобразовывает количество ингридиента например 100 = 0.100 кг
function createMeasureProduct(expend, measure){
	var result ;
	if(measure ==="UNIT"){
		result = expend;
	}else {
		if(expend.length < 2){
		result = "0.00" + expend  ;
		}else if(expend.length < 3){
		result = "0.0" + expend  ;
		}else if(expend.length <4){
		result = "0." + expend  ;
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

