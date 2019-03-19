//кнопка добавления нового партнера
var addLink = document.createElement('a');
addLink.className ="nav-link"
addLink.innerText = "Добавить поставщика";
addLink.id = "addPartnerButtonId";
addLink.dataset.target = "#modalPartnerForm";
addLink.setAttribute("data-toggle", "modal");


// елемент li для хранения кнопки
var newLi = document.createElement('li');
newLi.className = "nav-item ";
newLi.appendChild(addLink);
// добавление елемента в начало списка
var  topBarUl = document.getElementById("topBarContentUl");
topBarUl.prepend(newLi);

// добавления функции созадия нового поставщика на кнопку "addPartnerButtonId"
$("#comfirePartnerButton").on("focus", addNewPartner);

// добавления функции обновления поставщика на кнопку "updatePartnerButton"
//функция отправляет PUT запрос для обновления данных о партнере
$("#updatePartnerButton").on("focus", updatePartnerData);
function updatePartnerData(){
	  var requestPUT = new XMLHttpRequest();

	  requestPUT.open("PUT", 'http://localhost:8080/partners/'+$("#partnerId").text()+'/info', true);
	  requestPUT.setRequestHeader("Content-Type", "application/json");
	  requestPUT.onreadystatechange = function () {
	      if (requestPUT.readyState === 4 && requestPUT.status === 200) {
	         var json = JSON.parse(requestPUT.responseText);
	         console.log("PUT update partner data on server");
	         updateParnterRow(json);
	      }else {
	        console.log('error send data');
	      }
	  };

	  var data = JSON.stringify({
	    "company": $("#updateCompanyName").val(),
	    "phonNumber": $("#updateCompanyPhon").val(),
	    "partnerMail":$("#updateCompanyMail").val()
	  });
	  
	  console.log(data);
	  requestPUT.send(data);
}

// функция отправляет данные на POST метод сервера
function addNewPartner(){
  var requestPOST = new XMLHttpRequest();

requestPOST.open("POST", 'http://localhost:8080/partners', true);
requestPOST.setRequestHeader("Content-Type", "application/json");
requestPOST.onreadystatechange = function () {
    if (requestPOST.readyState === 4 && requestPOST.status === 200) {
        var json = JSON.parse(requestPOST.responseText);
       console.log("send new partner data to server");
       console.log(json);
       addPartnerData(json);
    }else {
      console.log('error send data');
    }
};

var data = JSON.stringify([{
	"company": $("#inputCompanyName").val(),
    "phonNumber": $("#inputCompanyPhon").val(),
    "partnerMail":$("#inputCompanyMail").val()
}]);
requestPOST.send(data);

$("#inputCompanyName").val("");
$("#inputCompanyPhon").val("");
$("#inputCompanyMail").val("");
};

// добавление строк партнеров на страницу
function addPartnerData( dataJSON){
	
	dataJSON.forEach(partner => {
	  document.getElementById('partnerTable').appendChild( createPartnerElement( partner));
	  document.getElementById("partner_id_"+partner.id).addEventListener("click",updateModelDialog);
		});
	}

// верстка строк партнеров
function createPartnerElement( partner){

	var partnerCol = document.createElement('tr');
	  partnerCol.id="partner_row_id_"+partner.id;
	  partnerCol.innerHTML = "<td>"+partner.company+"</td>"
		 + "<td>"+partner.phonNumber+"</td>"
		 + "<td>"+partner.partnerMail+"</td>"
		 + "<td id=\"partner_id_"+partner.id+"\" data-toggle=\"modal\" data-target=\"#updateModalPartnerForm\"> " 
		 +"<i class=\"fas fa-edit\" ></i> </td>"
		 + "<td hidden>"+partner.id+"</td>"
		 
		return partnerCol;
}
//передача данных о партнере в модальное окно для обновления
function updateModelDialog(){
	$("#updateCompanyName").val(this.previousElementSibling.previousElementSibling.previousElementSibling.innerText);
	$("#updateCompanyPhon").val(this.previousElementSibling.previousElementSibling.innerText);
	$("#updateCompanyMail").val(this.previousElementSibling.innerText);
	$("#partnerId").text(this.nextElementSibling.innerText);
	
}
//очистить модальное окно по добавлению партнера при закрытии
document.getElementById("closeNewPartner").addEventListener("focus", cleanNewPartner);
function cleanNewPartner(){
	$("#inputCompanyName").val("");
	$("#inputCompanyPhon").val("");
	$("#inputCompanyMail").val("");
}
//очистить модальное окно по обновлению партнера при закрытии

function updateParnterRow( data){
	var tr = document.getElementById("partner_id_"+data.id);
	tr.previousElementSibling.previousElementSibling.previousElementSibling.innerText = data.company ;
	tr.previousElementSibling.previousElementSibling.innerText = data.phonNumber;
	tr.previousElementSibling.innerText = data.partnerMail;
}
//заполняет страницу данными
var requestGET = new XMLHttpRequest();
requestGET.open('GET', 'http://localhost:8080/partners', true);
    requestGET.onload = function () {
    
      var dataJSON = JSON.parse(this.response);
      if (requestGET.status >= 200 && requestGET.status < 400) {
        addPartnerData(dataJSON);
        
      } else {
        console.log('error GET method');
      }
    }
    
    requestGET.send();