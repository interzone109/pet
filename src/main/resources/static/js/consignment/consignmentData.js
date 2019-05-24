// метод создает делает запрос на получение данных из накладной 
function loadConsignmentData (consignmentId){
	$('#consignmentDataTableBodyId tr').remove();
	$("#namePlaceholder").val($("#consignment_meta_id_"+consignmentId).text());//показываем мета данные накладной
	$("#namePlaceholder").collapse("show");//показываем мета данные
	$("#currentConsignmentStatusId").text(displayConsignmentState($("#consignment_state_id_"+consignmentId).text(),2));// сохраняем статус накладной
	
	 $("#collapseConsignmentBody").collapse("hide");
	 $("#collapseConsignmentDataBody").collapse("show");
	 
	 var storeId = $("#consignmentTableStoreId").text();
	 request("GET",connectUrl + "/user/stores/сonsignment/"+storeId+"/"+consignmentId, showConsignmentDataRow );
	 
	 $("#saveDataConsignment").val(consignmentId);
	 $("#addDataConsignment").val(consignmentId);
	 $("#approvedDataConsignment").val(consignmentId);
	 
	 $("#saveDataConsignment").on("click",saveCinsignmentData);
	 $("#addDataConsignment").on("click",test);
	 $("#approvedDataConsignment").on("click",test);
	 
	if($("#currentConsignmentStatusId").text()=== true){
			$("#saveDataConsignment").prop("disabled","disabled");
			$("#addDataConsignment").prop("disabled","disabled");
			$("#approvedDataConsignment").prop("disabled","disabled");
	}else{
			$("#saveDataConsignment").prop("disabled",false);
			$("#addDataConsignment").prop("disabled",false);
			$("#approvedDataConsignment").prop("disabled",false);
	}
 }
 // методразмещает строки с данными из накладной в таблицу
function showConsignmentDataRow(data){
	var isAproved = $("#currentConsignmentStatusId").text()=== true;
	var inputStart = ""; 
	var inputValue = "\">";
	var inputEnd = "";   
	if(!isAproved){
		 inputStart = "><input type=\"text\" class=\"form-control\"";
		 inputValue = "\" value = \" ";
		 inputEnd = "\" >"; 
	}
	data.forEach(ingridient => {
		$('#consignmentDataTableBodyId').append(createNewConsignmentDataRow(ingridient,inputStart, inputValue,inputEnd));
	});
}

//метод формирует строку для таблицы с даными из накладной
function createNewConsignmentDataRow(ingridient, inputStart, inputValue, inputEnd){
	 var ingridientRow = document.createElement('tr');
	 ingridientRow.id = "ingridient_row_id_"+ingridient.id;
		  
	 ingridientRow.innerHTML =  "<td id=\"ingridient_group_id_"+ingridient.id+"\">"+ingridient.group+"</td>"
	 		 +"<td id=\"ingridient_name_id_"+ingridient.id+"\">"+ingridient.name+"</td>"
	 		 
	 		 + "<td"+ inputStart +" id=\"ingridient_quantity_id_"+ingridient.id+inputValue+
	 		 createMeasureProduct(ingridient.propertiesProduct,ingridient.measureProduct )+inputEnd+"</td>"
	 		 
			 + "<td id=\"ingridient_measureProduct_id_"+ingridient.id+"\">"+displayProductMeasure(ingridient.measureProduct,1)+"</td>"
			 
			 + "<td " +inputStart+ "id=\"ingridient_price_id_"+ingridient.id+inputValue+
			 displayProductPrice(ingridient.description)+inputEnd+"</td>"
			 
			 + "<td id=\"ingridient_summ_id_"+ingridient.id+"\">"+
			 displayProductPrice(ingridient.description*ingridient.propertiesProduct)+"</td>"
			 +"<td> <i class=\"fas fa-list-alt\" title=\"вернутся к накладным\" onclick=\hideConsignmentData() ></i>  </td>"
	
	 var ids = $("#consignmentIngridientsId").text();
	 $("#consignmentIngridientsId").text(ids+" "+ingridient.id);
			  return ingridientRow;
}
//метод конвертирует величины измерения
function displayProductMeasure (measure ,convert){
	 
	if( measure ==="LITER" || measure==="литр"){
		return (convert === 1)?"литр": "LITER";
	}else if( measure ==="KILOGRAM" || measure==="кило"){
		return  (convert === 1)?"кило": "KILOGRAM";
	}else if( measure ==="UNIT" || measure==="шт"){
		return  (convert === 1)?"шт": "UNIT";
	}
	
};

// формируем цену в удобнов формате
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
	
//метод преобразовывает количество ингридиента например 100 = 0.100 кг
function createMeasureProduct(expend, measure){
	
var result ;
	if(measure ==="UNIT"){
		return expend ;
	}else {
		if(expend.length < 2){
			result = "0.00" + expend ;
		}else if(expend.length < 3){
			result = "0.0" + expend ;
		}else if(expend.length <4){
			result = "0." + expend ;
		}
		else{
			var strStart = expend.substring(0,expend.length-3);
			var strEnd = expend.substring(expend.length-3 ,expend.length);
			esult = strStart+"."+strEnd  ;
		}
	} 
	
	return result
}
//метод возращает к тамблице с накладными
 function hideConsignmentData(){
	 $("#collapseConsignmentBody").collapse("show");
	 $("#collapseConsignmentDataBody").collapse("hide");
	 $("#namePlaceholder").collapse("hide");
 }