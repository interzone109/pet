var state = new Map();//переменная хранит данные о запросax
var connectUrl = "http://localhost:8080";

// получаем список  магазинов
request("GET",connectUrl + "/user/stores", addStoreSearchRow );

//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 1 ){// если магазинов у поьзователя нет то просм его создать
		$("#storeSelect").append("<option value=\"NONE\">Создайте магазин</option>");
	} else {
		data.forEach(store=>{
			$("#storeSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");//добавлям селекты с названиями лоступных магазинов
			});
		$("#storeSelect").val(data[0].id);//устанавливаем selected 
		state.set("stores", data);
	}	
}

//запрос на получения списка сотрудников
request("GET",connectUrl + "/user/rest/employee", function(data){state.set("employee", data);updateEmployeeSelect();} );


$("#storeSelect").on("change", updateEmployeeSelect);
function updateEmployeeSelect(){
	var value = $("#storeSelect option:selected").val();
	if(value !=="NONE"){
		$("#employeeSelector").prop("disabled",false);
		var employees = state.get("employee").filter(employee=>employee.storeId == ""+value);
		$("#employeeSelector").empty();
		$("#employeeSelectorDoubler").empty();
		addEmployeeSearchRow($("#employeeSelector"),employees );
		addEmployeeSearchRow($("#employeeSelectorDoubler"),employees );
	}
}
// добавляем в селект список сотрудников закрепленный за ТТ
function addEmployeeSearchRow (select, data){
	console.log(data.length);
	if (data.length < 1 ){
		$(select).append("<option value=\"NONE\">Добавте работника</option>");
	} else {
		data.forEach(employee=>{
			$(select).append("<option value=\""+employee.id+"\" selected=\"selected\">"+employee.firstName+" "+employee.lastName+"</option>");
			});
		$(select).val(data[0].id);//устанавливаем selected 
	}
}


$("#interfaceSelector").on("change", function(){
	$("#interfaceSelector").val()==="1" 
	?$("#employeeSelectorDoubler").prop("disabled","disabled")
	: $("#employeeSelectorDoubler").prop("disabled",false);
	
	$("#interfaceSelector").val()==="1" 
	?$("#cashBoxDescription").text("Стандартного вида интерфейс кассы")
	:$("#cashBoxDescription").text(" касса разбиваеться на два участка Кассира и исполнителя заказа");
	
});
















