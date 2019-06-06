var connectUrl = "http://localhost:8080" ;
$("#collapseConsignmentBody").collapse("show");

// получаем список  магазинов
function getStoreList(){
	request("GET",connectUrl + "/user/stores", addStoreSearchRow );
}
getStoreList();
//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 0 ){// если магазинов у поьзователя нет то просм его создать
		$("#consignmetnStoreSelect").append("<option value=\"0\">Создайте магазин</option>");
		$("#consignmetnStoreDoublerSelect").append("<option value=\"0\">Создайте магазин</option>");
		$("#searchConsignmentBytton").prop("disabled","disabled");// блокируем кнопку поиска
	} else {
		$("#searchConsignmentBytton").prop("disabled",false);// разблокируем кнопку поиска
	data.forEach(store=>{
		//добавлям селекты с названиями лоступных магазинов
		$("#consignmetnStoreSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");
		//дублируем название доступных магазинов для поиска по внутриним перемещениям
		$("#consignmetnStoreDoublerSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");
	});
	$("#consignmetnStoreSelect").val(data[0].id);//устанавливаем selected первому магазину
	//в дублируещем селекте делаем недоступным первый магазин
	$("#consignmetnStoreDoublerSelect [value=" + data[0].id + "]").prop('disabled', 'disabled');
	}
}


//получаем список  партнеров
function getPartnerList(){
	request("GET",connectUrl + "/user/partners", addPartnerSearchRow );
}
getPartnerList();
//добавляем оптионалы для выборки по партнерам
function addPartnerSearchRow(data){
  if ((data.length > 0 )) {
	data.forEach(partner=>{
		$("#consignmetnOnSelect").append("<option value=\""+partner.id+"\">"+partner.company+"</option>");
	});
	}
}
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


// функия собирает данные с инпут полей и отправляет запрос на сервер
// для получение результатов по выборке /user/stores/сonsignment
function postFindConsignmentByValue(){
	var date = $("#dataRangeValue").val().split("-");
	var meta = null ;
	// проверяем доступны ли нам оптион с дубликатами магазинов
	if($("#consignmetnStoreDoublerSelectCol").is(":visible") && !$("#consignmetnStoreDoublerSelect option:selected").is(":disabled") ){
     // добавляем название магазина к поисковому запросу
		meta = $("#consignmetnStoreDoublerSelect option:selected").text();
	}
	// проверяем доступны ли нам оптион с поставщиками
	if($("#consignmetnOnSelectCol").is(":visible") && $("#consignmetnOnSelect option:selected").val() !=="ANY"){
		// добавляем название поставщика к поисковому запросу
		meta = $("#consignmetnOnSelect option:selected").text();
	}
	// делаем простую валидацию дат
	var isValid =  false ;
	if( date.length >1){
		isValid = (date[0].length > 8 && date[1].length >8 && formValidation($("#dataRangeValue"))) ;
	}
	if(isValid){
	// если валидация пройдена формируем джейсон
	var data = JSON.stringify({
		 "storeId": $("#consignmetnStoreSelect").val(),
		 "meta": meta,
        "dateStart": date[0],
        "dateFinish": date[1],
        "consignmentStatus": $("#consignmetnStatusSelect").val()
	   }
	);
	// отправляем поисковый запрос на сервер
	request('POST', connectUrl+'/user/stores/consignment',fillConsignmentTable ,data);
	//сохраняем id магазина
	$("#consignmentTableStoreId").text($("#consignmetnStoreSelect").val());
	// прячем сообщение с ощибкой о валидации
	$("#inputConsignmentFormErrore").collapse("hide");
	$("#dataRangeValue").removeClass("is-valid");
	} else{// показываем сообщение о ощибке при валидации
		$("#dataRangeValue").addClass("is-invalid");
		$("#inputConsignmentFormErrore").collapse("show");
	}
}

//метод заполняет таблицу с накладными
function fillConsignmentTable(data ){
	$("#consignmentTableBodyId tr").remove();//удаляем старый результат поиска
	
	$("#collapseConsignmentBody").collapse("show");
	if( Array.isArray(data) && data.length !== 0){
		data.forEach(consignment => {
			$('#consignmentTableBodyId').append(createNewConsignmentRow( consignment));
		});
		$('#consignmentTableFooter').text("");
	}else  {
		$('#consignmentTableFooter').text("Накладные не найдены");
	} 
	
}


// метод создает строку для таблицы с накладными
 function createNewConsignmentRow(consignment){
	 var consignmentRow = document.createElement('tr');
	 consignmentRow.id = "consignment_row_id_"+consignment.id;
		  
	 consignmentRow.innerHTML = "<td id=\"consignment_id_"+consignment.id+"\">"+consignment.id+"</td>"
			 + "<td id=\"consignment_date_id_"+consignment.id+"\">"+consignment.date+"</td>"
			 + "<td id=\"consignment_meta_id_"+consignment.id+"\"title=\""+consignment.meta.split(":%:")[0]+"\">"+consignment.meta.split(":%:")[1]+"</td>"
			 + "<td id=\"consignment_status_id_"+consignment.id+"\">"+displayConsignmentStatus(consignment.consignmentStatus, 1)+"</td>"
			 + "<td id=\"consignment_state_id_"+consignment.id+"\">"+displayConsignmentState(consignment.approved, 1)+"</td>"
			 +"<td> <i class=\"fas fa-list-alt\" title=\"открыть\" onclick=\loadConsignmentData("+consignment.id+") ></i>  </td>"

			  return consignmentRow;
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
 
 // метод форматирует статус накладной
 function displayConsignmentState(state,convert ){
	 if(  state === "проведено" || state === true){
	 		return (convert === 1)?"проведено": true;
	 	}else {
	 		return  (convert === 1)?"не проведено": false;
	 	}
 }

// функция меняет данные для создания выборки на основе выбраного статуса накладной
 $('#consignmetnStatusSelect').on('change', function() {
	 var partnerSelect = $('#consignmetnOnSelectCol');
	 var storeDoublerSelect =$('#consignmetnStoreDoublerSelectCol');

		if( this.value ==="ARRIVAL" || this.value ==="RETURN" ){
			$(partnerSelect).show();
			$(storeDoublerSelect).hide();
	 	}else if(this.value ==="CONSAMPTION" || this.value ==="WRITE-OFF"){
	 		$(partnerSelect).hide();
			$(storeDoublerSelect).hide();
	 	}else if( this.value ==="HAULING" ){
	 		$(partnerSelect).hide();
			$(storeDoublerSelect).show();
	 	} 
	});

 // метод не позволяет выбрать перемещение на один и тот же магазин - 
 // перемещение с маг1 на маг1
 $('#consignmetnStoreSelect').on('change', function() {
	 if($('#consignmetnStatusSelect option:selected').val() === "HAULING"){
	 $("#consignmetnStoreDoublerSelect option:disabled").prop('disabled', false);
	 $("#consignmetnStoreDoublerSelect [value=" + this.value  + "]").prop('disabled', 'disabled');
 }
 });
 $('#consignmetnStoreDoublerSelect').on('change', function() {
	 if($('#consignmetnStatusSelect option:selected').val() === "HAULING"){
	 $("#consignmetnStoreSelect option:disabled").prop('disabled', false);
	 $("#consignmetnStoreSelect [value=" + this.value  + "]").prop('disabled', 'disabled');
 }
 });
 

 /******************** search function ****************************/

 $(document).ready(function(){
 	  $("#searchOnPageTable").on("keyup", function() {
 	    var value = $(this).val().toLowerCase();
 	    var tableName =  ($("#collapseConsignmentBody").is(':visible')) ?"consignmentTableBodyId" :"consignmentDataTableBodyId" ;
 	    $("#"+tableName +" tr").filter(function() {
 	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
 	    });
 	  });
 	});

 /******************** search function ****************************/

