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
	$("#consignmetnStoreSelect").val(data[0].id);
	}
}

// функия собирает данные с инпут полей и отправляет запрос на сервер
// для получение результатов по выборке /user/stores/сonsignment
function postFindConsignmentByValue(){
	
}