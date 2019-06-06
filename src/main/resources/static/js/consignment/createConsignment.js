var createConsignment ; 

// метод создает шаблон накладной 
function createNewConsignment (){
	var meta = null ;
	// формируем мета данные накладной
	switch ($("#consignmetnStatusSelect").val()) {
	  case "ARRIVAL":
		  if($("#consignmetnOnSelectCol").is(":visible") && $("#consignmetnOnSelect option:selected").val() ==="ANY"){
			  meta = "user:%:Поступление новых ингридиентов на *"+$("#consignmetnStoreSelect option:selected").text()+"*";
			  $("#hiddenLable").hide();
		  }else{
			  meta = $("#consignmetnOnSelect option:selected").val()+":partner:%:Поступление новых ингридиентов на *"
			  +$("#consignmetnStoreSelect option:selected").text()
			  +"* от *"+$("#consignmetnOnSelect option:selected").text()+"*";
			  
			  $("#toNameModal").text($("#consignmetnOnSelect option:selected").text());
			  $("#hiddenLable").show();
		  }
	    break;
	  case "CONSAMPTION":
		  meta = "userConsamption:%:Расход ингридиентов c *"+$("#consignmetnStoreSelect option:selected").text()+"*";
		  $("#hiddenLable").hide();
	    break;
	  case "HAULING":
		  meta = $("#consignmetnStoreDoublerSelect option:selected").val()+":store:%:Перемещение ингридиентов с *"
		  +$("#consignmetnStoreSelect option:selected").text()
		  +"* на *"+$("#consignmetnStoreDoublerSelect option:selected").text()+"*";
		  $("#toNameModal").text($("#consignmetnStoreDoublerSelect option:selected").text());
		  $("#hiddenLable").show();
	    break;
	  case "RETURN":
		  if($("#consignmetnOnSelectCol").is(":visible") && $("#consignmetnOnSelect option:selected").val() !=="ANY"){
		  meta = $("#consignmetnOnSelect option:selected").val()+":partner:%:Возврат ингридиентов с *"
		  +$("#consignmetnStoreSelect option:selected").text()
		  +"* на *"+$("#consignmetnOnSelect option:selected").text()+"*";
		  $("#toNameModal").text($("#consignmetnOnSelect option:selected").text());
		  $("#hiddenLable").show();
		  }else{
			  console.log("display erore");
			  return ;
		  }
		break;
	  case "WRITE-OFF":
		  meta = "user:%:Списание ингридиентов с *"+$("#consignmetnStoreSelect option:selected").text()+"*";
		  $("#hiddenLable").hide();
		 break;
	}
	var date =$("#dataRangeValue").val();
		if(date.includes("-")){
			date= date.split("-")[0];
			
		}
	
	createConsignment =JSON.stringify({
		 "storeId": $("#consignmetnStoreSelect").val(),
		 "meta": meta,
        "date": date,
        "consignmentStatus": $("#consignmetnStatusSelect").val()
	   });



$("#createDateModal").text(date);
$("#createStatusModal").text($("#consignmetnStatusSelect option:selected").text());
$("#fromNameModal").text($("#consignmetnStoreSelect option:selected").text());
$("#createMetaModal").text( meta.split(":%:")[1]);
	
$("#createNewConsignmentModal").modal("show");

}


function createNewConsignmentRequest(){
	//сохраняем id магазина
	$("#consignmentTableStoreId").text($("#consignmetnStoreSelect").val());
	$("#createNewConsignmentModal").modal("hide");
	
	request("POST",connectUrl + "/user/stores/consignment/create", fillConsignmentTable, createConsignment );
	
}







