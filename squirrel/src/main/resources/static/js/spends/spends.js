var connectUrl = "http://localhost:8080" ;

//получаем список  магазинов
request("GET",connectUrl + "/user/stores", addStoreSearchRow );
//fix store viev
//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 1 ){// если магазинов у поьзователя нет то просм его создать
		$("#storeSelect").append("<option value=\"NONE\">Создайте магазин</option>");
	} else {
		data.forEach(store=>{
			$("#storeSelect").append("<option value=\""+store.id+"\" >"+store.address+"</option>");//добавлям селекты с названиями лоступных магазинов
			$("#storeSelectModal").append("<option value=\""+store.id+"\" >"+store.address+"</option>");//добавлям в модальное окно
		});
		$("#storeSelect").val(data[0].id);//устанавливаем selected 
	}	
}

//настройка датапикера
$("#payDate").on("focus", function (){$("#datepickers-container").css("z-index","10000");});

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
	$("#dataRangeValue").val(period);//для поиска
	
}
fillDate();



//функия собирает данные с инпут полей и отправляет запрос на сервер
//для получение результатов по выборке /user/stores/сonsignment
function postFindSpendsByValue(){
	var date = $("#dataRangeValue").val().split("-");
	console.log(date[0]);
	console.log(date[1]);
	// делаем простую валидацию дат
	var isValid =  false ;
	if( date.length >1){
		isValid = (date[0].length > 8 && date[1].length >8 && formValidation($("#dataRangeValue"))) ;
	}
	if(isValid){
	// если валидация пройдена формируем джейсон
	var data = JSON.stringify({
		 "storeId": $("#storeSelect").val(),
		 "dateStart": date[0],
		 "dateEnd": date[1]
	   });
	console.log(data);
	// отправляем поисковый запрос на сервер
	request('POST', connectUrl+'/user/spends/data/find',fillSpendtTable ,data);
	
	}
	console.log("not valid data");
}

	
	
//метод заполняет таблицу с затратами
function fillSpendtTable(data ){
	$("#spendsTableBodyId tr").remove();//удаляем старый результат поиска
	if( Array.isArray(data) && data.length !== 0){
		data.forEach(spend => {
			$('#spendsTableBodyId').append(createNewSpendRow( spend));
		});
	}else if(data.length === 0)  {
	
		$('#spendsTableFooter').text("Pасходы не найдены");
	} else{
		$('#spendsTableBodyId').append(createNewSpendRow( data));
	}
	
}


// метод создает строку для таблицы с расходом
 function createNewSpendRow(spend){
	 var spendRow = document.createElement('tr');
	 spendRow.id = "spend_row_id_"+spend.id;
	 var storeName = (spend.storeName==="" || spend.storeName===" ")?"Без магазина":spend.storeName;

	 spendRow.innerHTML = "<td id=\"spend_name_id_"+spend.id+"\">"+spend.name+"</td>"
	 		+ "<td id=\"spend_cost_id_"+spend.id+"\">"+displayProductPrice(spend.cost)+" грн</td>"
			+ "<td id=\"spend_date_id_"+spend.id+"\">"+converDate(spend.dateStart)+" - "+converDate(spend.dateEnd)+"</td>"
			+ "<td id=\"spend_interval_id_"+spend.id+"\">"+intrvalForm(spend.interval) +"</td>"
			+ "<td id=\"spend_status_id_"+spend.id+"\">"+((spend.open)?"Открыт ":"Закрыт") +"</td>"
			+ "<td id=\"spend_storeId_id_"+spend.id+"\">"+storeName+"</td>"
			+"<td><span class=\"badge badge-success\"> <i class=\"fas fa-edit\" title=\"редактировать \" onclick=\"editeSpend("+spend.id+")\" ></i></span> " 
			+" <span class=\"badge badge-danger\"><i class=\"fas fa-trash-alt\" title=\"удалить\"  onclick=\"deleteSpend("+spend.id+")\"></i></span></td>";
	 
			 return spendRow;
 }
 
 function intrvalForm(interval){
	if(interval >= 0){
		return interval+" дней";
	} else if(interval < 0){
		return interval.toString().split("-")[1]+" месяц";
	} 
 }
 
 
//показываем модальное окно для создания нового платежа
 function createNewSpends(){
	 $("#nameSpend").val("");
	 $("#costSpend").val(0);
	 $('#intervalDaySpend').val(0);
	 $("#intervalMonthSpend option[value='-1']").prop('selected', true);
	 $("#storeSelectModal option[value='0']").prop('selected', true);
	 $("#payDate").val("");
	 $("#spendStatus option[value='false']").prop('selected', true);
	 
	 $("#spendModalBody").modal("show");
	 $("#comfirmSpendButton").text("Создать");
	 $("#comfirmSpendButton").on("click", sendPOSTCreateSpend);
	 var today = new Date();
		var date =  String(today.getDate()).padStart(2, '0')
			+ '.' + String(today.getMonth() + 1).padStart(2, '0')
			+ '.' + today.getFullYear();
	$("#payDate").val(date);//дата для создания расхода
 }

 //переключатель для формы заполнения интервала платежа
 function switchInput(type){
	 if(type==="day"){
		 $("#intervalMonthSpend").prop( "disabled", true );
		 $("#intervalDaySpend").prop( "disabled", false );
	 }else{
		 $("#intervalMonthSpend").prop( "disabled", false );
		 $("#intervalDaySpend").prop( "disabled", true );
	 }
 }
 

 
 
 // создаем запрос на добавление расхода
 function sendPOSTCreateSpend(){
	 var isValid = formValidation( $("#nameSpend"), $("#costSpend"), $("#payDate") );
	 if(isValid){
		 var spedn = JSON.stringify({
			 "name":$("#nameSpend").val(),
			 "cost":$("#costSpend").val()*100,
			 "step":($("#spendStatus").val() ===true 
					 ?$("#costSpend").val()*100
					 :0 ),
			 "interval":(($('#intervalDaySpend').prop("disabled") === false)
					? ($('#intervalDaySpend').val() <0 || ($('#intervalDaySpend').val()==="")
							?0
							:$('#intervalDaySpend').val())
		 				:$("#intervalMonthSpend").val()) ,
		 	"dateStart": $("#payDate").val(),
		 	"isOpen":$("#spendStatus").val(),
		 	"storeId":$("#storeSelectModal").val()
		 });
		 console.log(spedn);
		 
		
		 request('POST', connectUrl+'/user/spends/data',fillSpendtTable ,spedn);
		
		 
		 
		
		 $("#nameSpend").val("");
		 $("#costSpend").val(0);
		 $('#intervalDaySpend').val(0);
		 $("#intervalMonthSpend option[value='-1']").prop('selected', true);
		 $("#storeSelectModal option[value='0']").prop('selected', true);
		 $("#payDate").val("");
		 $("#spendStatus option[value='false']").prop('selected', true);
		 
		 $("#spendModalBody").modal("hide");
	 }
	
 }
 
 //редактирование расxода
 function editeSpend(spendId){
	 
	 $("#nameSpend").val( $("#spend_name_id_"+spendId).text());
	 $("#costSpend").val(parseFloat($("#spend_cost_id_"+spendId).text().split(" ")[0], 2));
	 $('#intervalDaySpend').val(0);
	 $("#intervalMonthSpend option[value='-1']").prop('selected', true);
	 $("#storeSelectModal option[value='0']").prop('selected', true);
	 $("#payDate").val("");
	 $("#spendStatus option[value='false']").prop('selected', true);
	 
	 $("#spendModalBody").modal("show");
 }
 
 //удаление расxода
 function deleteSpend(spendId){
	 
	 
	 $("#deleteForm").modal("show");
	 $("#deleteRowId").text(spendId);
	 
	// request('DELETE', connectUrl+'/user/spends/data/'+spendId);
	// $("#spend_row_id_"+spendId).remove();
 }
 function deleteSpendsRow(){
	
	 if($("#deleteRowId").text()!==0){
	 request('DELETE', connectUrl+'/user/spends/data/'+ $("#deleteRowId").text());
	 $("#spend_row_id_"+ $("#deleteRowId").text()).remove();
	 $("#deleteForm").modal("hide");
	 $("#deleteRowId").text("");
	 }
 }
 
 
 
