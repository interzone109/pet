//устонавливаем функции кнопкам которые будут работать с накладными	 
$("#saveDataConsignment").on("click",saveConsignmentData);
$("#addDataConsignment").on("click",openAddIngridientModal);
$("#approvedDataConsignment").on("click",approvedDataConsignmentMethod);
// метод  делает запрос на получение данных из накладной 
function loadConsignmentData (consignmentId){
	$('#consignmentDataTableBodyId tr').remove();//чистим таблицу от старых данных
	
	$("#namePlaceholder").val($("#consignment_meta_id_"+consignmentId).text());//показываем мета данные накладной
	$("#namePlaceholder").collapse("show");//показываем мета данные
	$("#currentConsignmentStatusId").text(displayConsignmentState($("#consignment_state_id_"+consignmentId).text(),2));// сохраняем статус накладной
	// скрываем список накладных
	 $("#collapseConsignmentBody").collapse("hide");
	 //показываем список ингридиентов в накладной
	 $("#collapseConsignmentDataBody").collapse("show");
	 //получаем ид магазина и делаем запрос на получение данных из накладной
	 var storeId = $("#consignmentTableStoreId").text();
	 $("#consignmentIngridientsId").text("");
	 request("GET",connectUrl + "/user/stores/consignment/"+storeId+"/"+consignmentId, showConsignmentDataRow );

	 //сохраняем ид накладной
	 $("#consignmentCurrentId").text(consignmentId);
	 

	 // в зависимости от статуса накладной кнопки доступны либо нет
	if($("#currentConsignmentStatusId").text()=== "true"){
			$("#saveDataConsignment").prop("disabled","disabled");
			$("#addDataConsignment").prop("disabled","disabled");
			$("#approvedDataConsignment").prop("disabled","disabled");
	}else{
			$("#saveDataConsignment").prop("disabled",false);
			$("#addDataConsignment").prop("disabled",false);
			$("#approvedDataConsignment").prop("disabled",false);
	}
 }

 // метод размещает строки с данными из накладной в таблицу
//если накладная не проведена то поля с кол. и ценой то елементы будут инпуты
//еслипроведена то текст
function showConsignmentDataRow(data,removable){
	
	var isAproved = $("#currentConsignmentStatusId").text()=== "true";
	//строки с шаблонами верстки
	var inputStart = ""; 
	var inputValue = "\">";
	var inputEnd = "";   
	if(!isAproved){//проверяем статус накладной
		 inputStart = "><input type=\"text\" class=\"form-control\"";
		 inputValue = "\" value = \" ";
		 inputEnd = "\" >"; 
	}
	//формируем строки с данными для каждой позиции из накладной
	if(data.length !== 0){
	data.forEach(ingridient => {
		$('#consignmentDataTableBodyId').append(createNewConsignmentDataRow(ingridient,inputStart, inputValue,inputEnd ,removable));
	});
	}
	//пересчитываем итоговую сумму
	updateTotalSumm();
}

//метод формирует строку для таблицы с даными из накладной
function createNewConsignmentDataRow(ingridient, inputStart, inputValue, inputEnd ,removable){
	var end = "</td>";
	if(removable){
		end ="<span class=\"badge badge-danger\"><i class=\"fas fa-trash-alt\" title=\"удалить\"  onclick=\"removeIngridient("+ingridient.id+")\"></i></span></td>";
	}
	 var ingridientRow = document.createElement('tr');
	 ingridientRow.id = "ingridient_row_id_"+ingridient.id;
	 
	 var totalPrice = ingridient.measureProduct==="UNIT"
		 ?ingridient.description*ingridient.propertiesProduct
		 :(ingridient.description*ingridient.propertiesProduct)/1000 ;
			  
	 ingridientRow.innerHTML =  "<td id=\"ingridient_group_id_"+ingridient.id+"\">"+ingridient.group+"</td>"
	 		 +"<td id=\"ingridient_name_id_"+ingridient.id+"\">"+ingridient.name+"</td>"
	 		 //количество в поле propertiesProduct   
	 		 + "<td"+ inputStart +" id=\"ingridient_quantity_id_"+ingridient.id+inputValue+
	 		 createMeasureProduct(ingridient.propertiesProduct,ingridient.measureProduct )+inputEnd+"</td>"
	 		 //остаток ингридиентов в партии (актуально для приходных накладных)
	 		+"<td id=\"ingridient_current_quantity_id_"+ingridient.id+"\">" +
	 		createMeasureProduct(ingridient.partner,ingridient.measureProduct )+"</td>"
	 		 
			 + "<td id=\"ingridient_measureProduct_id_"+ingridient.id+"\">"+displayProductMeasure(ingridient.measureProduct,1)+"</td>"
			 // цена в поле description
			 + "<td " +inputStart+ "id=\"ingridient_price_id_"+ingridient.id+inputValue+
			 displayProductPrice(ingridient.description)+inputEnd+"</td>"
			 
			 + "<td id=\"ingridient_summ_id_"+ingridient.id+"\">"+
			 displayProductPrice(totalPrice)+"</td>"
			 +"<td> <span class=\"badge badge-info\"><i class=\"fas fa-list-alt\" title=\"вернутся к накладным\" onclick=\hideConsignmentData() ></i></span> "
			 +end;
	 var ids = $("#consignmentIngridientsId").text();
	 $("#consignmentIngridientsId").text(ids+" "+ingridient.id);
			  return ingridientRow;
}

function removeIngridient(id){
	$("#ingridient_row_id_"+id).remove();
	var ids = $("#consignmentIngridientsId").text();
	var splitIds = ids.split(" ");
	var res =" " ;
	for( var i = 0 ; i <splitIds.length; i++ ){
		if(splitIds[i] ==  id){ 
			continue;
		}else{
			res+=" "+ splitIds[i];
		}
	}
	$("#consignmentIngridientsId").text(" "+res.trim());
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
	price =  parseInt(price);
	  if (isNaN(price)) { 
		  return 0 ; 
		  }

		if(price < 1){
			return 0;
		} else if(price < 9){
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
	if(measure ==="UNIT" || measure ==="шт"  ){
		return expend ;
	}else {
		if(expend.toString(10).length < 2){
			result = "0.00" + expend ;
		}else if(expend.toString(10).length < 3){
			result = "0.0" + expend ;
		}else if(expend.toString(10).length <4){
			result = "0." + expend ;
		}
		else{
			var strStart = expend.substring(0,expend.length-3);
			var strEnd = expend.substring(expend.length-3 ,expend.length);
			result = strStart+"."+strEnd  ;
		}
	} 
	
	return result
}
//метод возращает к таблице с накладными
 function hideConsignmentData(){
	 $("#collapseConsignmentBody").collapse("show");
	 $("#collapseConsignmentDataBody").collapse("hide");
	 $("#namePlaceholder").collapse("hide");
 }
 
 //метод формирует итог по цене
 function updateTotalSumm(){
	 var ingridientIds = $("#consignmentIngridientsId").text().split(" ");
	var summ = 0;
	 for(var i = 1; i< ingridientIds.length ;i++){ 
		summ+= parseFloat($("#ingridient_summ_id_"+ingridientIds[i]).text());
		 } 
	 
	 $("#totalSumm").text(summ);
 }
 
 
 
 