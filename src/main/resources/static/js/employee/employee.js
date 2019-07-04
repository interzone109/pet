var state = new Map();//переменная хранит данные о запросе остатков для разным магазинов
var connectUrl = "http://localhost:8080";
//открытие модального окна
$("#openEmployeeModal").on("click", function (){$("#employeeModalBody").modal("show");});
//настройка датапикера
$("#dateHiringValue").on("focus", function (){$("#datepickers-container").css("z-index","10000");});
//подсказки при заполнении полей в форме
$(document).ready(function() { $('#employeeLoginInput').tooltip(); });
$(document).ready(function() {  $('#employeePassInput').tooltip(); });

$("#addIngridientLins").on("click",postNewEmployee);

//получаем список  работников
request("GET",connectUrl + "/user/rest/employee", createEmployeeTable );
function createEmployeeTable(employees){
	if( Array.isArray(employees) && employees.length !== 0){
		employees.forEach(employee => {
			$('#employeeTableBody').append(createEmployeeTableRow(employee));
		});
	}else{
		$('#employeeTableBody').append(createEmployeeTableRow(employees));
		$("#employeeModalBody").modal("hide");
	}
}

function createEmployeeTableRow(employee){
	var employeeRow = document.createElement('tr');
	  
	employeeRow.innerHTML = "<td id=\"employee_hairing_date_id_"+employee.id+"\">"+employee.hairingDate+"</td>"
		 + "<td id=\"employee_name_id_"+employee.id+"\">"+employee.firstName+"</td>"
		 + "<td id=\"employee_last_name_id_"+employee.id+"\">"+employee.lastName+"</td>"
		 + "<td id=\"employee_salary_id_"+employee.id+"\">"+employee.salary+"</td>"
		 + "<td id=\"employee_store_id_"+employee.id+"\">"+employee.storeName+"</td>"
		 +"<td>  <i class=\"fas fa-edit\"  title=\"редактировать\"  onclick=\"editeEmployeeRow("+employee.id+")\"  ></i> </td>";
		
	return employeeRow;
}


function editeEmployeeRow(employeeId){
	console.log(employeeId);
}



// получаем список  магазинов
request("GET",connectUrl + "/user/stores", addStoreSearchRow );

//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 1 ){// если магазинов у поьзователя нет то просм его создать
		$("#storeSelect").append("<option value=\"0\">Создайте магазин</option>");
		$("#createEmployee").prop("disabled","disabled");// блокируем кнопку поиска
	} else {
		$("#createEmployee").prop("disabled",false);// разблокируем кнопку поиска
		data.forEach(store=>{
			$("#storeSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");//добавлям селекты с названиями лоступных магазинов
			});
		$("#storeSelect").val(data[0].id);//устанавливаем selected первому магазину
		state.set("stores", data);
		
	}	
}
function postNewEmployee(){
	//валидируем обязательные поля
	var isValid = formValidation($("#employeeNameInput"))&
	formValidation($("#employeeSecondNameInput"))&
	formValidation($("#employeeSalaryInput"))&
	formValidation($("#dateHiringValue"));
	
	var isHassAcces = $("#employeeLoginInput").val()!="" && $("#employeePassInput").val()!="" ;
	console.log(isHassAcces);
	if(isValid){
		var employee = JSON.stringify({
				"firstName": $("#employeeNameInput").val(),
				"lastName": $("#employeeSecondNameInput").val(),
		        "salary": $("#employeeSalaryInput").val(),
		        "storeId": $("#storeSelect option:selected").val(),
		        "hairingDate": $("#dateHiringValue").val(),
		        "login": isHassAcces ?$("#employeeLoginInput").val():"",
		        "password":isHassAcces ? $("#employeePassInput").val():""
			   });
		
		 console.log(employee);
		 request("POST",connectUrl + "/user/rest/employee", createEmployeeTable,  employee);
		
	}
}



