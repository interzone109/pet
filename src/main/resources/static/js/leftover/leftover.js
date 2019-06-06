
var connectUrl = "http://localhost:8080";
// получаем список  магазинов
request("GET",connectUrl + "/user/stores", addStoreSearchRow );

//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 0 ){// если магазинов у поьзователя нет то просм его создать
		$("#storeSelect").append("<option value=\"0\">Создайте магазин</option>");
		//$("#searchConsignmentBytton").prop("disabled","disabled");// блокируем кнопку поиска
	} else {
		//$("#searchConsignmentBytton").prop("disabled",false);// разблокируем кнопку поиска
	data.forEach(store=>{
		//добавлям селекты с названиями лоступных магазинов
		$("#storeSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");
		});
		$("#storeSelect").val(data[0].id);//устанавливаем selected первому магазину
	}
}
