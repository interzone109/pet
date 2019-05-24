function test(){
	console.log("TEST DATA "+ $(this).val());
}

function saveCinsignmentData(){
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
			if($("#ingridient_measureProduct_id_"+ingridientIds[i]).val()!=="шт"){
				quantity *=100 ;
			}
			
			jsonData +=  "\""+ingridientIds[i]+"\":\"\:"+
			+quantity+"quantity"+
			($("#ingridient_price_id_"+ingridientIds[i]).val()*100)+"price\" ,";	
		}
	}
	if(result){
		var storeId = $("#consignmentTableStoreId").text();
		var consignmentId  = $(this).val();
		var data = "{"+jsonData.slice(0, -1)+"}";
		console.log(data);
		request("PUT",connectUrl + "/user/stores/сonsignment/"+storeId+"/"+consignmentId, d ,data);
	}
}
function d(data){
	console.log(data);
}