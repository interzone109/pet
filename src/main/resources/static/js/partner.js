//кнопка добавления нового партнера
var addLink = document.createElement('a');
addLink.className ="nav-link"
addLink.innerText = "Добавить поставщика";
addLink.id = "addPartnerButtonId";
addLink.dataset.target = "#modalPartnerForm";
addLink.setAttribute("data-toggle", "modal");


//елемент li для хранения кнопки
var newLi = document.createElement('li');
newLi.className = "nav-item ";
newLi.appendChild(addLink);
//добавление елемента в начало списка
var  topBarUl = document.getElementById("topBarContentUl");
topBarUl.prepend(newLi);

var requestGET = new XMLHttpRequest();

requestGET.open('GET', 'http://localhost:8080/partners', true);
    requestGET.onload = function () {
    
      var dataJSON = JSON.parse(this.response);
    
      if (requestGET.status >= 200 && requestGET.status < 400) {
        addPartnerData(dataJSON);
        
      } else {
        console.log('error');
      }
    }
    
    requestGET.send();
    
    
    var comfirePartner = document.getElementById("comfirePartnerButton").addEventListener("focus", addNewPartner);
    
    function addNewPartner(){

  var companyName = document.getElementById("inputCompanyName");
  var companyPhon = document.getElementById("inputCompanyPhon");
  var companyMail = document.getElementById("inputCompanyMail");



var requestPOST = new XMLHttpRequest();

requestPOST.open("POST", 'http://localhost:8080/partners', true);
requestPOST.setRequestHeader("Content-Type", "application/json");
requestPOST.onreadystatechange = function () {
    if ( requestPOST.status === 200) {
        var json = JSON.parse(requestPOST.responseText);
        location.reload()
        console.log("send");
       // console.log(json.value);
    }else {
      console.log('error send data');
    }
};

var data = JSON.stringify([{
  "company": companyName.value,
  "phonNumber": companyPhon.value,
  "partnerMail": companyMail.value
}]);
requestPOST.send(data);


companyName.value="";
companyPhon.value="";
companyMail.value="";
};




function addPartnerData( dataJSON){
//елемент выравнивая карточек с данными
var partnerRow = document.createElement('div');
partnerRow.className="row";

dataJSON.forEach(partner => {
  // елемент с описанием данных о партнере
  var partnerData = document.createElement('div');
  partnerData.className="team-front";
  partnerData.innerHTML ="<h3>"+partner.company+"</h3><p>"
  +partner.phonNumber+"</p><p>"+partner.partnerMail+"</p> ";
  
  //елемент с функциями редактирования
  var partnerDescripion = document.createElement('div');
  partnerDescripion.className="team-back";
  partnerDescripion.innerHTML="<span>"+partner.id+"</span>";
  
  var mainContainner = document.createElement('div');
  mainContainner.className="our-team-main";
  mainContainner.appendChild(partnerData);
  mainContainner.appendChild(partnerDescripion);
  
  var partnerCol = document.createElement('div');
  partnerCol.className="col-lg-4";
  partnerCol.appendChild(mainContainner);
  
  
  partnerRow.appendChild(partnerCol);
});
//елемент содержащий все данные
var partnerContainer = document.createElement('div');
partnerContainer.className="container";
partnerContainer.appendChild(partnerRow);
document.getElementById('partnerCardContainer').appendChild(partnerContainer);
}