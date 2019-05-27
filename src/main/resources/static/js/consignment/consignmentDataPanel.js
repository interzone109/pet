function test(){
	console.log("TEST DATA "+ $(this).val());
}
// метод сохраняет навые данные о количестве и цене товара 
function saveConsignmentData( isUproved){
	var ingridientIds = $("#consignmentIngridientsId").text().split(" ");
	var result = true ;
	var jsonData ="";
	
	for(var i = 1; i< ingridientIds.length ;i++){
		var flag = formValidationNumber($("#ingridient_quantity_id_"+ingridientIds[i])  
				, $("#ingridient_price_id_"+ingridientIds[i]));
		if(flag === false){
			result = false;
		}else{
			var quantity = $("#ingridient_quantity_id_"+ingridientIds[i]).val();
			if($("#ingridient_measureProduct_id_"+ingridientIds[i]).text()!=="шт"){
				quantity *=1000 ;
			}
			
			jsonData +=  "\""+ingridientIds[i]+"\":\"\:"+
			+quantity+"quantity"+
			($("#ingridient_price_id_"+ingridientIds[i]).val()*100)+"price\" ,";	
		}
	}
	if(result){
		var storeId = $("#consignmentTableStoreId").text();
		var consignmentId =$("#consignmentCurrentId").text();
		var data = "{"+jsonData.slice(0, -1)+"}";
		
		 if( isUproved !== true){
		request("PUT",connectUrl + "/user/stores/сonsignment/"+storeId+"/"+consignmentId, updateConsignmentDataRow ,data);
		 }else{
		request("PUT",connectUrl + "/user/stores/сonsignment/"+storeId+"/"+consignmentId+"/uproved", updateConsignmentDataRowUproved ,data);
		 }
	}
	
}
//метод обновляет цену, количество и итог
function updateConsignmentDataRow(responce, disabled){
		var ingridientIds = $("#consignmentIngridientsId").text().split(" ");
		for(var i = 1; i< ingridientIds.length ;i++){
			var id = ingridientIds[i] ;
			var dataStr = responce[id].split("quantity");
			var quantity = createMeasureProduct (dataStr[0].split(":")[1],$("#ingridient_measureProduct_id_"+id).text());
			var price = displayProductPrice( dataStr[1].split("price")[0] );

			$("#ingridient_quantity_id_"+id).val(quantity);
			$("#ingridient_price_id_"+id).val(price);
			
			$("#ingridient_summ_id_"+id).text(displayProductPrice(dataStr[1].split("price")[0] * dataStr[0].split(":")[1]));
			 if(disabled === true){
				 $("#ingridient_quantity_id_"+id).prop("disabled","disabled");
				$("#ingridient_price_id_"+id).prop("disabled","disabled");
				$("#ingridient_summ_id_"+id).prop("disabled","disabled");
			 }
		}
		updateTotalSumm();
}
//метод обновляет все строки и блокирует их для изменений
function updateConsignmentDataRowUproved(responce){
	updateConsignmentDataRow(responce, true);
	
}

//метод отправляет запрос на закрытие накладной 
function approvedDataConsignment(){
	
	$("#currentConsignmentStatusId").text("true");
	$("#consignment_status_id_"+$("#consignmentCurrentId").text()).text("проведено");
	
	saveConsignmentData(true);
	$("#saveDataConsignment").prop("disabled","disabled");
	$("#addDataConsignment").prop("disabled","disabled");
	$("#approvedDataConsignment").prop("disabled","disabled");
}

// модеальное окно для добавления ингридиентов
function openAddIngridientModal(){
	
	$("#addIngridientToConsignmentModal").modal("show");
}





