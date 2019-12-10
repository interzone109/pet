var state = new Map();//переменная хранит данные о запросе остатков для разным магазинов
var connectUrl = "http://localhost:8080";
//открытие модального окна
$("#openEmployeeModal").on("click", function (){$("#employeeModalBody").modal("show");});
//настройка датапикера
$("#dateHiringValue").on("focus", function (){$("#datepickers-container").css("z-index","10000");});
//подсказки при заполнении полей в форме
$(document).ready(function() { $('#employeeLoginInput').tooltip(); });
$(document).ready(function() {  $('#employeePassInput').tooltip(); });
var putEmployeeId = 0;
$("#addIngridientLins").on("click",postOrPutEmployee);

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
	  
	employeeRow.innerHTML = "<td id=\"employee_hairing_date_id_"+employee.id+"\">"+converDate(employee.hairingDate)+"</td>"
		 + "<td id=\"employee_name_id_"+employee.id+"\">"+employee.firstName+"</td>"
		 + "<td id=\"employee_last_name_id_"+employee.id+"\">"+employee.lastName+"</td>"
		 + "<td id=\"employee_salary_id_"+employee.id+"\">"+employee.salary+"</td>"
		 + "<td id=\"employee_store_id_"+employee.id+"\">"+employee.storeName+"</td>"
		 + "<td id=\"employee_status_id_"+employee.id+"\">"+employee.status+"</td>"
		 +"<td>  <i class=\"fas fa-edit\" style=\"color:#17a2b8\" title=\"редактировать\"  onclick=\"editeEmployeeRow("+employee.id+")\"  ></i> " 
		 +"<i class=\"fas fa-user-times\" title=\"удалить\" onclick=\"removeEmployeeRow("+employee.id+")\"></i>	</td>";
		
	return employeeRow;
}

//метод обновляет данные о работнике
function editeEmployeeRow(employeeId){
	$("#employeeModalBody").modal("show");//окрываем окно для редактирования данных 
	$("#employeeNameInput").val($("#employee_name_id_"+employeeId).text());
	$("#employeeSecondNameInput").val($("#employee_last_name_id_"+employeeId).text());
	$("#employeeSalaryInput").val($("#employee_salary_id_"+employeeId).text());
	$("#dateHiringValue").val($("#employee_hairing_date_id_"+employeeId).text());
	$("#status").val($("#employee_status_id_"+employeeId).text());
	$("#storeSelect").val($("#storeSelect option:contains("+$("#employee_store_id_"+employeeId).text()+")").val());
	putEmployeeId = employeeId;
}


function removeEmployeeRow(employeeId){
	if($("#employee_status_id_"+employeeId).text()==="на уделение"){
	$("#employee_status_id_"+employeeId).text("востановлен");
	}else{
		$("#employee_status_id_"+employeeId).text("на уделение");
	}
	 editeEmployeeRow(employeeId)
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
function postOrPutEmployee(){
	//валидируем обязательные поля
	var isValid = formValidation($("#employeeNameInput"))&
	formValidation($("#employeeSecondNameInput"))&
	formValidation($("#employeeSalaryInput"))&
	formValidation($("#dateHiringValue"));
	
	var isHassAcces = $("#employeeLoginInput").val()!="" && $("#employeePassInput").val()!="" ;
	if(isValid){
		var employee = JSON.stringify({
				"firstName": $("#employeeNameInput").val(),
				"lastName": $("#employeeSecondNameInput").val(),
		        "salary": $("#employeeSalaryInput").val(),
		        "storeId": $("#storeSelect option:selected").val(),
		        "hairingDate": $("#dateHiringValue").val(),
		        "status": $("#status").val(),
		        "accountAppModel":{
		        	"login": isHassAcces ?$("#employeeLoginInput").val():"",
		    		 "password":isHassAcces ? $("#employeePassInput").val():""
		        }
		        
			   });
		if(putEmployeeId!==0){
			request("PUT",connectUrl + "/user/rest/employee/"+putEmployeeId+"/edit", updateEmployeeRow,  employee);
			putEmployeeId = 0 ;
		}else{
			request("POST",connectUrl + "/user/rest/employee", createEmployeeTable,  employee);
		}
	}
}

function updateEmployeeRow (employee){
	
	 $("#employee_name_id_"+employee.id).text(employee.firstName);
	 $("#employee_last_name_id_"+employee.id).text(employee.lastName);
	 $("#employee_salary_id_"+employee.id).text(employee.salary);
	 $("#employee_hairing_date_id_"+employee.id).text(converDate(employee.hairingDate));
	 $("#employee_store_id_"+employee.id).text(employee.storeName);
	 $("#employee_status_id_"+employee.id).text(employee.status);
}

function converDate(date){
	var dateStr = date.split("-");
	return dateStr[2]+"."+dateStr[1]+"."+dateStr[0];
}


