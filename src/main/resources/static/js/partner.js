
/******************** GET function ****************************/
//заполняет страницу данными
request('GET', 'http://localhost:8080/user/partners',addPartnerData);
/******************** GET function ****************************/ 

//прячем таблицу с продуктами поставщика
$("#productContent").hide();
//кнопка добавления нового партнера
var addLink = document.createElement('a');
addLink.className ="nav-link";
addLink.href="#";
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

// добавление строк партнеров на страницу
function addPartnerData( dataJSON){
	if( Array.isArray(dataJSON)){
	dataJSON.forEach(partner => {
	  document.getElementById('partnerTable').appendChild( createPartnerElement( partner));
	  $("#partner_id_"+partner.id).on('click',updateModelDialog);
	  $("#partner_product_id_"+partner.id).on('click',showProducts);
	 	});
	}else{
		document.getElementById('partnerTable').appendChild( createPartnerElement( dataJSON));
		 $("#partner_id_"+dataJSON.id).on('click',updateModelDialog);
		 $("#partner_product_id_"+dataJSON.id).on('click',showProducts);
	}
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
		 +"<td id=\"partner_product_id_"+partner.id+"\"><i class=\"fas fa-barcode\" ></i> </td>"
		 + "<td hidden>"+partner.id+"</td>";
		 
		return partnerCol;
}


//функция запрашивает данные о продуктах данного поставщика
function showProducts(){
	$("#partnerContent").hide();
	 request('GET' ,'http://localhost:8080/user/partners/'+this.nextElementSibling.innerText+'/info',displayProductData);

}


function displayProductData(result){
	var products = result.productsModel;
	if(products!=null &&products.length != 0 ){
	products.forEach(product => {
		$("#productTable").append(displayProductRow(product));
	});
	} 
	//если продутов нет, добавляем новый
	else{
	$("#productContent").append(
	 "<caption id=\"productCaption\">" 
	 +"<ul class=\"nav navbar-nav ml-auto\">"
     +"<li class=\"nav-item\"> <a class=\"nav-link\" href=\"#\">Добавить продукт</a> </li>"
     +"<li class=\"nav-item \">"
     +"<a class=\"nav-link\" href=\"#\" onclick=\"hideProduct()\" title=\"вернуться к поставщикам\"> " +result.company+" </a>"
     +"</li> </ul>"
     +"</caption>");
	}
	
	$("#productContent").show();
}

function displayProductRow(product){
	console.log(productRow);
	var productRow = document.createElement('tr');
	  productRow.id = "product_row_id_"+product.id;
	  
	  productRow.innerHTML = "<td>"+product.group+"</td>"
		 + "<td>"+product.name+"</td>"
		 + "<td>"+product.description+"</td>"
		 +"<td>"+displayProductProperties(product.propertiesProduct,1)+"</td>"
		 +"<td>"+displayProductMeasure(product.measureProduct,1)+"</td>"
		 +"<td><i class=\"fas fa-barcode\" onclick=\"hideProduct()\" id=\"return_partner_"+product.id+"\" title=\"вернуться к поставщикам\"></i> " 
		 +"<i class=\"fas fa-edit\" title=\"редактировать\"  data-toggle=\"modal\" data-target=\"#updateProductModal\"></i> " 
		 +" <i class=\"fas fa-plus-circle\" title=\"добавить товар\ data-target=\"#addProductModal\"></i></td>"
		 + "<td hidden>"+product.id+"</td>";
		  
		  return productRow;
} 

function hideProduct(){
	$("#partnerContent").show();
	$("#productContent").hide();
	$("#productTable").empty();
	$("#productCaption").remove();
}



function displayProductMeasure (measure ,convert){
	switch(measure) {
	case "LITER": 
		return (convert === 1)?"литр": "LITER";
	case "KILOGRAM":
		return  (convert === 1)?"кило": "KILOGRAM";
	case "UNIT":
		return (convert === 1)?"един.": "UNIT";
	}	
}




function displayProductProperties(properties,convert){
	switch(properties) {
	case 'INGREDIENS':  
		return (convert === 1)?"ингридиент": "INGREDIENS";
	case 'CONSUMABLES':  
		return (convert === 1)?"расходник": "CONSUMABLES";
	case 'PRODUCT_FINAL':
		return (convert === 1)?"кон. продукт": "PRODUCT_FINAL";
	case 'PRODUCT_COMPOSITE':
		return (convert === 1)?"универс": "PRODUCT_COMPOSITE";
	}	
}









//передача данных о партнере в модальное окно для обновления
function updateModelDialog(){
	$("#updateCompanyName").val(this.previousElementSibling.previousElementSibling.previousElementSibling.innerText);
	$("#updateCompanyPhon").val(this.previousElementSibling.previousElementSibling.innerText);
	$("#updateCompanyMail").val(this.previousElementSibling.innerText);
	$("#partnerId").text(this.nextElementSibling.nextElementSibling.innerText);
	
}





//очистить модальное окно по добавлению партнера при закрытии
document.getElementById("closeNewPartner").addEventListener("focus", cleanNewPartner);

//очистить модальное окно по обновлению партнера при закрытии
function cleanNewPartner(){
	$("#inputCompanyName").val("");
	$("#inputCompanyPhon").val("");
	$("#inputCompanyMail").val("");
}







// обновления строчки данных о партнере
function updateParnterRow( data){
	var tr = document.getElementById("partner_id_"+data.id);
	tr.previousElementSibling.previousElementSibling.previousElementSibling.innerText = data.company ;
	tr.previousElementSibling.previousElementSibling.innerText = data.phonNumber;
	tr.previousElementSibling.innerText = data.partnerMail;
}








/******************** PUT function ****************************/
function updatePartnerData(){
	  var data = JSON.stringify({
	    "company": $("#updateCompanyName").val(),
	    "phonNumber": $("#updateCompanyPhon").val(),
	    "partnerMail":$("#updateCompanyMail").val()
	  });
	  request('PUT', 'http://localhost:8080/user/partners/'+$("#partnerId").text()+'/info',updateParnterRow ,data);
}
/******************** PUT function ****************************/







/******************** POST function ****************************/

// функция отправляет данные на POST метод сервера
function addNewPartner(){

	var data = JSON.stringify({
		"company": $("#inputCompanyName").val(),
	    "phonNumber": $("#inputCompanyPhon").val(),
	    "partnerMail":$("#inputCompanyMail").val()
	});

	request('POST', 'http://localhost:8080/user/partners',addPartnerData ,data);

	$("#inputCompanyName").val("");
	$("#inputCompanyPhon").val("");
	$("#inputCompanyMail").val("");
};

/******************** POST function ****************************/







/****************** request method *****************************/

function request(type , url,method , json){
	var httpRequest = new XMLHttpRequest();
	httpRequest.open(type,  url, true);
	if(type==='GET'){
	httpRequest.onload = function () {
	      var dataJSON = JSON.parse(this.response);
	      if (httpRequest.status >= 200 && httpRequest.status < 400) {
	    	  method(dataJSON);
	      } else {
	        console.log('error '+type+' method '+url);
	      }
	    }
	httpRequest.send();
	}
	if(type==='PUT' ||type==='POST' ){
		httpRequest.setRequestHeader("Content-Type", "application/json");
		httpRequest.onreadystatechange = function () {
		    if (httpRequest.readyState === 4 && httpRequest.status === 200) {
		        var json = JSON.parse(httpRequest.responseText);
		       console.log('type '+type+' method '+url);
		       console.log(json);
		       method(json);
		    }else {
		      console.log('error send data');
		    }
		};
		
		console.log(json);
		httpRequest.send(json);
	}

}

/****************** request method *****************************/
    




/******************** search function ****************************/
    $(document).ready(function(){
    	  $("#searchOnPageTable").on("keyup", function() {
    	    var value = $(this).val().toLowerCase();
    	    $("#partnerTable tr").filter(function() {
    	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    	    });
    	  });
    	});
/******************** search function ****************************/
    
    
/******************** sidebar function ****************************/
    $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
            $('#sidebar').toggleClass('active');
        });
    });
/******************** sidebar function ****************************/    

    