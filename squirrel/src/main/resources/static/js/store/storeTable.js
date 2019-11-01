$("#collapseStoreBody").collapse("show");//раскрываем таблицу с магазинами
var connectUrl ='http://localhost:8080';
request('GET', connectUrl+'/user/stores', fillStoreTable);//запрос на получения списка ТТ
//кнопка для отправления обновленных данных Store на сервер 
$("#updateStoreButton").on("focus", updateStoreData);



//метод добавляет строки с данными о ТТ в конец таблицы
function fillStoreTable (dataJSON){
	if( Array.isArray(dataJSON)){
		dataJSON.forEach(storeRow => {
			$('#storeTable').append(createNewStoreRow( storeRow));
		});
	}else{
		  $('#storeTable').append(createNewStoreRow( dataJSON));
	}
}


//метод создает строку с данными о ТТ 
function createNewStoreRow(store){
	var storeRow = document.createElement('tr');
	storeRow.id = "store_row_id_"+store.id;
	  
	storeRow.innerHTML = "<td id=\"store_address_id_"+store.id+"\">"+store.address+"</td>"
		 + "<td id=\"store_phone_id_"+store.id+"\">"+store.phone+"</td>"
		 + "<td id=\"store_mail_id_"+store.id+"\">"+store.mail+"</td>"
		 

		 +"<td><span class=\"badge badge-success\"> <i class=\"fas fa-edit\" title=\"редактировать\" onclick=\"updateStoreDataModal("+store.id+")\" ></i></span> " 
		 +"<span class=\"badge badge-info\"><i class=\"fas fa-list-alt\" title=\"товар и цена\" onclick=\"showProductPrice("+store.id+")\"  ></i></span> " 
		 
		 +"</td>"
		
		  return storeRow;
}



//функция создает новый объект Store и отправляет на сервер
function postNewStoreForm(){
	// формируем жсон и отправляем его на сервер
	var adress = $("#inputStoreAddress");
	var phone = $("#inputStorePhone");
	var mail =$("#inputStoreMail") ;
		var isValid =  formValidation(adress, phone, mail);
		if(isValid){
		var data = JSON.stringify({
				 "address": $(adress).val(),
		         "phone": $(phone).val(),
		         "mail": $(mail).val()
			   }
			);
		
		request('POST', connectUrl+'/user/stores/',fillStoreTable ,data);
	
		// меняем цвет контура инпута
		$(adress).removeClass("is-valid");
		$(phone).removeClass("is-valid");
		$(mail).removeClass("is-valid");
		// чистим значения
		$(adress).val("");
        $(phone).val("");
        $(mail).val("");
        // убираем предупреждение об ошибке
		$("#inputStoreFormErrore").collapse("hide");
		}else{
			// показываем предупреждение об ошибке
			$("#inputStoreFormErrore").collapse("show");
		}
}


//  метод открывает модальное окно для обновления данных о ТТ
function updateStoreDataModal(id){
	$("#updateModalStoreForm").modal('show'); 
	
	$("#updateStoreId").text(id);// ид выбраной ТТ
	// заполняем анные в форме поумолчанию данными из таблицы
	$("#updateStoreAddress").val($("#store_address_id_"+id).text());//текущий адрес
	$("#updateStorePhone").val($("#store_phone_id_"+id).text());//текущий телефон
	$("#updateStoreMail").val($("#store_mail_id_"+id).text());//текущий	 имеил
	
}

//метод срабатывает при нажатии кнопки обновить в модальном окне
function updateStoreData(){
	var adress = $("#updateStoreAddress");
	var phone = $("#updateStorePhone");
	var mail =$("#updateStoreMail") ;
		var isValid =  formValidation(adress, phone, mail);
		if(isValid){
	//формируем жсон с обновленными данными
	var data = JSON.stringify({
		 "address": $("#updateStoreAddress").val(),
        "phone": $("#updateStorePhone").val(),
        "mail": $("#updateStoreMail").val()
	   }
	);
	// отправляем на сервер
	request('PUT', connectUrl+'/user/stores/'+$("#updateStoreId").text(),updateStoreTable ,data);
	
	// меняем цвет контура инпута
	$(adress).removeClass("is-valid");
	$(phone).removeClass("is-valid");
	$(mail).removeClass("is-valid");
	
	$("#updateModalStoreForm").modal("hide");
	}
}


//заменяем данные в строке ТТ на обновленные
function updateStoreTable(data){
	$("#store_address_id_"+data.id).text(data.address);//текущий адрес
	$("#store_phone_id_"+data.id).text(data.phone);//текущий телефон
	$("#store_mail_id_"+data.id).text(data.mail);//текущий	 имеил
}


// скрываем все таблици и показываем таблицу с магазинами
function showStoreTable(){
	$("#collapseStoreBody").collapse("show");
	$("#collapseProductStoreBody").collapse("hide");//скрываем таблицу с продукт -цена
	$("#storeProductTable").empty();// чистим строки в таблице с продуктами
	$("#productPriceIds").text(" ");// обнуляем список с Ids продуктов
	$("#namePlaceholder").collapse("hide")//прячем алерт с азванием магазина
	
}



/** ****************** search function *************************** */

$(document).ready(function(){
	  $("#searchOnPageTable").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    var tableName =  ($("#collapseStoreBody").is(':visible')) ?"storeTable" :"storeProductTable" ;
	    $("#"+tableName +" tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});

/** ****************** search function *************************** */

