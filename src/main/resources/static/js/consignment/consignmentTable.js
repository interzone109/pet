var connectUrl = "http://localhost:8080" ;
// получаем список созданых магазинов
function getStoreList(){
	request("GET",connectUrl + "/user/stores", addStoreSearchRow );
}
getStoreList();


//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 0 ){
		$("#consignmetnStoreSelect").append("<option value=\"0\">Создайте магазин</option>");
	} else {
	data.forEach(store=>{
		$("#consignmetnStoreSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");
	});
	$("#consignmetnStoreSelect").val(data[0].id);//устанавливаем selected первому магазину
	}
}

// функия собирает данные с инпут полей и отправляет запрос на сервер
// для получение результатов по выборке /user/stores/сonsignment
function postFindConsignmentByValue(){
	var date = $("#dataRangeValue").val().split("-");

	var data = JSON.stringify({
		 "storeId": $("#consignmetnStoreSelect").val(),
        "dateStart": date[0],
        "dateFinish": date[1],
        "consignmentStatus": $("#consignmetnStatusSelect").val()
	   }
	);
request('POST', connectUrl+'/user/stores/сonsignment',fillConsignmentTable ,data);
}

//метод заполняет таблицу с продуктами
function fillConsignmentTable(data ){
	$("#collapseConsignmentBody").collapse("show");
	
	if( Array.isArray(data)){
		data.forEach(consignment => {
			$('#consignmentTableBodyId').append(createNewConsignmentRow( consignment));
		});
	}else{
		  $('#consignmentTableBodyId').append(createNewConsignmentRow( data));
	}
	
}


// метод создает строку для таблицы с накладными
 function createNewConsignmentRow(consignment){
	 var productRow = document.createElement('tr');
	 productRow.id = "consignment_row_id_"+consignment.id;
		  
	 productRow.innerHTML = "<td id=\"consignment_id_"+consignment.id+"\">"+consignment.id+"</td>"
			 + "<td id=\"consignment_date_id_"+consignment.id+"\">"+consignment.date+"</td>"
			 + "<td id=\"consignment_meta_id_"+consignment.id+"\">"+consignment.meta+"</td>"
			 + "<td id=\"consignment_status_id_"+consignment.id+"\">"+displayConsignmentStatus(consignment.consignmentStatus, 1)+"</td>"
			 + "<td id=\"consignment_state_id_"+consignment.id+"\">"+displayConsignmentState(consignment.isApproved, 1)+"</td>"
			 +"<td> <i class=\"fas fa-edit\" title=\"открыть\" onclick=\"test123("+consignment.id+")\" ></i>  </td>"

			  return productRow;
 }

 
 
//выводим статус
 function displayConsignmentStatus(status ,convert){
 	 
 	if( status ==="ARRIVAL" || status==="приход"){
 		return (convert === 1)?"приход": "ARRIVAL";
 	}else if( status ==="CONSAMPTION" || status==="расход"){
 		return  (convert === 1)?"расход": "CONSAMPTION";
 	}else if( status ==="HAULING" || status==="перемещение"){
 		return  (convert === 1)?"перемещение": "HAULING";
 	}else if( status ==="RETURN" || status==="возврат"){
 		return  (convert === 1)?"возврат": "RETURN";
 	}else if( status ==="WRITE-OFF" || status==="списание"){
 		return  (convert === 1)?"списание": "WRITE-OFF";
 	}
 }
 
 
 function displayConsignmentState(state,convert ){
	 if( state || state==="проведено"){
	 		return (convert === 1)?"проведено": true;
	 	}else if( !state  || state==="не проведено"){
	 		return  (convert === 1)?"не проведено": false;
 }
 }
 
 function test123 (data){
	 console.log(data);
 }






