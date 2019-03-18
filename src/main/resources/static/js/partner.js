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
document.getElementById("comfirePartnerButton").addEventListener("focus", addNewPartner);
// добавления функции обновления поставщика на кнопку "updatePartnerButton"
document.getElementById("updatePartnerButton").addEventListener("focus", updatePartner);


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
    
   
 // функция отправляет PUT запрос для обновления данных о партнере
   function updatePartner(){
    	
    	
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
  "company": document.getElementById("inputCompanyName").value,
  "phonNumber": document.getElementById("inputCompanyPhon").value,
  "partnerMail": document.getElementById("inputCompanyMail").value
}]);
requestPOST.send(data);


document.getElementById("inputCompanyName").value="";
document.getElementById("inputCompanyPhon").value="";
document.getElementById("inputCompanyMail").value="";
};

// добавление карточек партнеров на страницу
function addPartnerData( dataJSON){
	
	dataJSON.forEach(partner => {
	  document.getElementById('partnerRow').appendChild( createPartnerElement( partner));
	  //создание ссылки для редактирования поставщика
		var updarePartner = document.createElement('a');
		//updarePartner.innerText = "редактировать";
		updarePartner.innerHTML = "<h2>редактировать</h2>";
		updarePartner.id = "updatePartnerButtonId";
		updarePartner.dataset.target = "#updateModalPartnerForm";
		updarePartner.setAttribute("data-toggle", "modal");
	  
	  
	  document.getElementById("partner_id_"+partner.id).appendChild(updarePartner);
	});
	}

// верстка карточек партнеров
function createPartnerElement( partner){
	
	var partnerCol = document.createElement('div');
	  partnerCol.className="col-lg-4";
	  partnerCol.id="partner_id_"+partner.id;
	  partnerCol.innerHTML = "<div class=\"our-team-main\">"
		+"<div class=\"team-front\">"
		 + "<h3>"+partner.company+"</h3>"
		 + "<p>"+partner.phonNumber+"</p>"
		 + "<p>"+partner.partnerMail+"</p>"
		+"</div> <div class=\"team-back\">"
		+"</div></div>"
		
		return partnerCol;
}

