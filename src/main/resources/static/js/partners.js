
/** ****************** GET function *************************** */
// заполняет страницу данными после загрузки страницы
request('GET', 'http://localhost:8080/user/partners', addPartnerData);
/** ****************** GET function *************************** */ 
//$("#collapsePartnerBody").show(); !!!!!
$("#collapsePartnerBody").collapse("show");


/** ****************** display product row functions *************************** */


// метод срабатывает при нажатии кнопки списка у поставщика
function loadProductData(id){
	//$("#collapsePartnerBody").hide();// скрываем таблицу с поставщиками
	 $("#collapsePartnerBody").collapse("hide");
	// делаем запрос к серверу на получение списка продуктов
	request('GET' ,'http://localhost:8080/user/partners/'+id+'/info',displayProductData);
	 //$("#collapseProductBody").show();// показываем таблицу с продуктами   !!!!!!!
	 $("#collapseProductBody").collapse("show");
};


// метод проверяет являются ли данные масивом
// и передает их методу формирующие строки для таблицы
function displayProductData(dataJSON){
	
	var productList = dataJSON.productsModel ;
	
	if(productList !== null && productList.length !== 0){
	 if( Array.isArray(productList)){
		 displayNewProductRow(productList);
		}else{
			$('#productTable').append(displayProductRow( dataJSON.productsModel));
		}
	}else{
		$("#productContent").append(
				 "<caption id=\"productCaption\">" 
				 +"<ul class=\"nav navbar-nav ml-auto\">"
			     +"<li class=\"nav-item \">"
			     +"<a class=\"nav-link\" href=\"#\" onclick=\"hideProduct()\" > Вернуться к поставщикам </a>"
			     +"</li> </ul>"
			     +"</caption>");
				}
			
	 $("#productContent").collapse("show");
	//$("#productContent").show(); !!!!!!!!!!!!
	$("#sendUpdateProduct").on("click",postNewProductForm );
	$('#sendUpdateProduct').prop('title', dataJSON.id);
	}
	

//метод формирует строку для таблицы продуктов
function displayProductRow(product){
	var productRow = document.createElement('tr');
	  productRow.id = "product_row_id_"+product.id;
	  
	  productRow.innerHTML = "<td id=\"product_group_id_"+product.id+"\">"+product.group+"</td>"
		 + "<td id=\"product_name_id_"+product.id+"\">"+product.name+"</td>"
		 + "<td id=\"product_description_id_"+product.id+"\">"+product.description+"</td>"
		 +"<td id=\"product_properties_id_"+product.id+"\">"+displayProductProperties(product.propertiesProduct,1)+"</td>"
		 +"<td id=\"product_measure_id_"+product.id+"\">"+displayProductMeasure(product.measureProduct,1)+"</td>"
		 +"<td> <i class=\"fas fa-edit\" title=\"редактировать\" onclick=\"updateProduct()\" ></i> " 
		 +"<i class=\"fas fa-list-alt\" onclick=\"hideProduct()\"  title=\"вернуться к поставщикам\"></i> </td>"
		
		  return productRow;
}

// метод добавляет одну строку
function displayNewProductRow(productList){
	productList.forEach(product => {
		  $('#productTable').append(displayProductRow( product));
		 	});
}

function updateProduct(){
	console.log("update product");
}


function hideProduct(){
	 $("#collapsePartnerBody").collapse("show");
	//$("#collapsePartnerBody").show();// покахываем список поставщиков !!!!!!!!!!!!
	//$("#collapseProductBody").hide();// прячес список продуктов !!!!!!!!!!
	 $("#collapseProductBody").collapse("hide");
	$("#productTable").empty();// чистим строки в таблице с продуктами
	$("#productCaption").remove();// удаляем заголовок списка, если такой есть
};




function displayProductMeasure (measure ,convert){
	 
	if( measure ==="LITER" || measure==="литр"){
		return (convert === 1)?"литр": "LITER";
	}else if( measure ==="KILOGRAM" || measure==="кило"){
		return  (convert === 1)?"кило": "KILOGRAM";
	}else if( measure ==="UNIT" || measure==="един."){
		return  (convert === 1)?"един.": "UNIT";
	}
	
};


function displayProductProperties(properties,convert){
	
	if( properties ==="INGREDIENS" || properties==="ингридиент"){
		return (convert === 1)?"ингридиент": "INGREDIENS";
	}else if( properties ==="CONSUMABLES" || properties==="расходник"){
		return  (convert === 1)?"расходник": "CONSUMABLES";
	}else if( properties ==="PRODUCT_FINAL" || properties==="конечный продукт"){
		return  (convert === 1)?"конечный продукт": "PRODUCT_FINAL";
	}else if( properties ==="PRODUCT_COMPOSITE" || properties==="универсальный"){
		return  (convert === 1)?"универсальный": "PRODUCT_COMPOSITE";
	}

};

/** ****************** display product row functions *************************** */







/** ****************** display partner row functions *************************** */ 
// метод проверяет являются ли данные масивом
// и передает их методу формирующие строки для таблицы
function addPartnerData( dataJSON){
	if( Array.isArray(dataJSON)){
	dataJSON.forEach(partner => {
	  $('#partnerTable').append(displayPartnerRow( partner));
	 	});
	}else{
		$('#partnerTable').append(displayPartnerRow( dataJSON));
	}
};

function displayPartnerRow( partner){

	var partnerCol = document.createElement('tr');
	  partnerCol.id="partner_row_id_"+partner.id;
	  partnerCol.innerHTML = "<td id=\"partner_company_id_"+partner.id+"\">"+partner.company+"</td>"
		 + "<td id=\"partner_phon_id_"+partner.id+"\">"+partner.phonNumber+"</td>"
		 + "<td id=\"partner_mail_id_"+partner.id+"\">"+partner.partnerMail+"</td>"
		 + "<td>  <i class=\"fas fa-edit\"  title=\"редактировать\"  onclick=\"updatePartnerRow("+partner.id+")\"  ></i>"
		 +"<i class=\"fas fa-list-alt\" title=\"просмотреть товары поставщика\"  onclick=\"loadProductData("+partner.id+")\"></i> </td>";
		 
		 
		return partnerCol;
};
/** ****************** display partner row functions *************************** */ 




/**
 * ****************** POST function create new partner json and send on server
 * ***************************
 */
// метод формирует json, отправляет данные на сервер
// и добавляет вернувшиеся данные на страницу
function postNewPartnerForm(){

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

/**
 * ****************** POST function create new partner json and send on server
 * ***************************
 */



/**
 * ****************** POST function create new product json and send on server
 * ***************************
 */



// метод формирует json, отправляет данные на сервер
// и добавляет вернувшиеся данные на страницу
function postNewProductForm(){
	console.log("post new prod "+ this.title);
	var data = JSON.stringify([
		 {
			 "name": $("#inputProductName").val(),
	         "description": $("#inputProductDescription").val(),
	         "group": $("#inputProductGroup").val(),
	         "partner": null,
	         "propertiesProduct":displayProductProperties( $("#propertieSelect option:selected" ).text() , 2),
	         "measureProduct":   displayProductMeasure( $("#measureSelect option:selected" ).text(), 2)
		   }
		]);

	request('POST', 'http://localhost:8080/user/partners/'+this.title+'/edit',displayNewProductRow ,data);

	$("#inputProductName").val("");
	$("#inputProductDescription").val("");
	$("#inputProductGroup").val("");
	
};




/**
 * ****************** POST function create new partner json and send on server
 * ***************************
 */





/**
 * ****************** PUT function update partner row
 * ***************************
 */
// срадабывает при нажатии кнопки реактирования
// функция вызывает модальное окно для обновленния данных о поставщике
function updatePartnerRow(id){
	$('#updateModalPartnerForm').modal('show'); 
	$("#updateCompanyName").val($("#partner_company_id_"+id).text());
	$("#updateCompanyPhon").val($("#partner_phon_id_"+id).text());
	$("#updateCompanyMail").val($("#partner_mail_id_"+id).text());
	$("#updatePartnerButton").on("click", sendUpdatePartnerRow);
	$("#partnerIdModalUpdate").text(id);
	
};

// функция форминует json и отправляет на сервер PUT запрос
function sendUpdatePartnerRow(id){
		  var data = JSON.stringify({
		    "company": $("#updateCompanyName").val(),
		    "phonNumber": $("#updateCompanyPhon").val(),
		    "partnerMail":$("#updateCompanyMail").val()
		  });
		  request('PUT', 'http://localhost:8080/user/partners/'+$("#partnerIdModalUpdate").text()+'/info',reusePartnerRow ,data);
	
};

// метод изменяет данные в поле на полученные данные
function reusePartnerRow(data){
	$("#partner_company_id_"+data.id).text(data.company);
	$("#partner_phon_id_"+data.id).text( data.phonNumber);
	$("#partner_mail_id_"+data.id).text(data.partnerMail);
};

/**
 * ****************** PUT function update partner row
 * ***************************
 */




/** **************** request method **************************** */
function request(type, url, method , json){
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
	};
	if(type==='PUT' ||type==='POST' ){
		httpRequest.setRequestHeader("Content-Type", "application/json");
		httpRequest.onreadystatechange = function () {
		    if (httpRequest.readyState === 4 && httpRequest.status === 200) {
		        var json = JSON.parse(httpRequest.responseText);
		       console.log('type '+type+' method '+url);
		       console.log(json);
		       method(json);
		    }else {
		      console.log('error send data type '+type+' method '+url);
		    }
		};
		
		console.log(json);
		httpRequest.send(json);
	};

};
/** **************** request method **************************** */


/** ****************** search function *************************** */

$(document).ready(function(){
	  $("#searchOnPageTable").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    var tableName =  ($("#collapsePartnerBody").is(':visible')) ?"partnerTable" :"productTable" ;
	    $("#"+tableName +" tr").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});



/** ****************** search function *************************** */





