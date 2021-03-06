

/** ****************** GET function *************************** */
// заполняет страницу данными после загрузки страницы
var connectUrl = "http://localhost:8080" ;

request('GET', connectUrl+'/user/partners', addPartnerData);
/** ****************** GET function *************************** */ 
$("#collapsePartnerBody").show();

/** ****************** display partner row functions *************************** */ 
//метод проверяет являются ли данные масивом
//и передает их методу формирующие строки для таблицы
function addPartnerData( dataJSON){
	if( Array.isArray(dataJSON)){
	dataJSON.forEach(partner => {
	  $('#partnerTable').append(displayPartnerRow( partner));
	 	});
	}else{
		$('#partnerTable').append(displayPartnerRow( dataJSON));
	}
};

//метод формирует строку для таблицы с партнерами
function displayPartnerRow( partner){

	var partnerCol = document.createElement('tr');
	  partnerCol.id="partner_row_id_"+partner.id;
	  partnerCol.innerHTML = "<td id=\"partner_company_id_"+partner.id+"\">"+partner.company+"</td>"
		 + "<td id=\"partner_phon_id_"+partner.id+"\">"+partner.phonNumber+"</td>"
		 + "<td id=\"partner_mail_id_"+partner.id+"\">"+partner.partnerMail+"</td>"
		 + "<td> <span class=\"badge badge-success\"> <i class=\"fas fa-edit\"  title=\"редактировать\"  onclick=\"updatePartnerRow("+partner.id+")\"  ></i></span>"
		 +" <span class=\"badge badge-info\"><i class=\"fas fa-list-alt\" title=\"просмотреть товары поставщика\"  onclick=\"loadProductData("+partner.id+")\"></i></span> </td>";
		 
		 
		return partnerCol;
};
/** ****************** display partner row functions *************************** */ 



/** ****************** display product row functions *************************** */
$("#spiners").hide();
// метод срабатывает при нажатии кнопки списка продуктов у поставщика
function loadProductData(id){
	 $("#collapsePartnerBody").hide();// скрываем таблицу с поставщиками
	 
	$("#namePlaceholder").val("Поставщик - "+$("#partner_company_id_"+id).text());// находим название партнера по ид и устанавливаем его
	$("#namePlaceholder").collapse("show");//показываем название компании
	 
	
	// делаем запрос к серверу на получение списка продуктов
	 request('GET' ,connectUrl+'/user/partners/'+id+'/info',displayProductDataGET);
	 $("#spiners").show();
	 $("#curentPartnerProductList").text(id);
};


// метод проверяет являются ли данные масивом
// и передает их методу формирующие строки для таблицы продукты
function displayProductDataGET(dataJSON){
	 
	var productList = dataJSON.productsModel ;
	if(productList !== null && productList.length !== 0){
	 if( Array.isArray(productList)){
		 productList.forEach(product => {
			  $('#productTable').append(displayProductRow( product));
			 });
		}else{
			$('#productTable').append(displayProductRow( dataJSON));
		}
	}
			
	$("#productContent").collapse("show");
	$('#sendUpdateProduct').prop('title', dataJSON.id);
	
	$("#spiners").hide();
	$("#collapseProductBody").collapse("show");// показываем таблицу с продуктами
	
	}

$("#sendUpdateProduct").on("click",postNewProductForm );




//метод формирует строку для таблицы продуктов
function displayProductRow(product){
	var productRow = document.createElement('tr');
	  productRow.id = "product_row_id_"+product.id;
	  
	  productRow.innerHTML = "<td id=\"product_group_id_"+product.id+"\">"+product.group+"</td>"
		 + "<td id=\"product_name_id_"+product.id+"\">"+product.name+"</td>"
		 + "<td id=\"product_description_id_"+product.id+"\">"+product.description+"</td>"
		 +"<td id=\"product_properties_id_"+product.id+"\">"+displayProductProperties(product.propertiesProduct,1)+"</td>"
		 +"<td id=\"product_measure_id_"+product.id+"\">"+displayProductMeasure(product.measureProduct,1)+"</td>"
		 +"<td> <span class=\"badge badge-success\"> <i class=\"fas fa-edit\" title=\"редактировать\" onclick=\"updateProduct("+product.id+")\" ></i></span>  " 
		 +"<span class=\"badge badge-info\"><i class=\"fas fa-list-alt\" onclick=\"hideProduct()\"  title=\"вернуться к поставщикам\"></i></span></td>"
		
		  return productRow;
}


/********************  PUT update product row  ****************************/






// метод вызывает модальное окно для обновления записей о продуктах поставщика
function updateProduct(id){

	//устанавливаем в поля текущие значения
	$('#updateModalProductForm').modal('show'); 
	$("#updateProductGroup").val($("#product_group_id_"+id).text());
	$("#updateProductName").val($("#product_name_id_"+id).text());
	$("#updateProductDescription").val($("#product_description_id_"+id).text());
	$("#updatePropertieSelect option[value="+$("#product_properties_id_"+id).text()+"]").prop('selected', true);
	$("#updateMeasureSelect option[value="+$("#product_measure_id_"+id).text()+"]").prop('selected', true);
	 // устонавливаем слушателя на кнопку обновления
	$("#updateProductButton").on("click", sendUpdateProductRow);
	$("#sendProductId").text(id);
}

// формируем жсон и отправляем его на сервер
function sendUpdateProductRow(){
	if(formValidation($("#updateProductName"))){
	
	var data = JSON.stringify({
			 "name": $("#updateProductName").val(),
	         "description": $("#updateProductDescription").val(),
	         "group": $("#updateProductGroup").val(),
	         "partner": null,
	         "propertiesProduct":displayProductProperties( $("#updatePropertieSelect option:selected" ).text() , 2),
	         "measureProduct":   displayProductMeasure( $("#updateMeasureSelect option:selected" ).text(), 2)
		   }
		);
	
	request('PUT', connectUrl+'/user/partners/'+$("#curentPartnerProductList").text()+'/edit/'+$("#sendProductId").text(),updateProductRow ,data);
	$("#updateProductName").removeClass("is-valid");
	$("#updateModalProductForm").modal("hide");
	
	} 
}
// обновляет поля в таблице с продуктами
function updateProductRow (productRow){

	
	$("#product_group_id_"+productRow.id).text(productRow.group);
	$("#product_name_id_"+productRow.id).text(productRow.name);
	$("#product_description_id_"+productRow.id).text(productRow.description);
	$("#product_properties_id_"+productRow.id).text(displayProductProperties(productRow.propertiesProduct,1));
	$("#product_measure_id_"+productRow.id).text(displayProductMeasure(productRow.measureProduct,1));
}




/********************  PUT update product row  ****************************/





// метод прячит таблицу с продуктами и заменяет ее таблицей с поставщиками
function hideProduct(){
	 $("#namePlaceholder").collapse("hide"); // прячем название компании
	 $("#collapsePartnerBody").show();// покахываем список поставщиков
	 $("#collapseProductBody").collapse("hide"); // прячем список продуктов
	$("#productTable").empty();// чистим строки в таблице с продуктами
	$("#productCaption").remove();// удаляем заголовок списка, если такой есть
};




function displayProductMeasure (measure ,convert){
	 
	if( measure ==="LITER" || measure==="литр"){
		return (convert === 1)?"литр": "LITER";
	}else if( measure ==="KILOGRAM" || measure==="кило"){
		return  (convert === 1)?"кило": "KILOGRAM";
	}else if( measure ==="UNIT" || measure==="шт"){
		return  (convert === 1)?"шт": "UNIT";
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





/**
 * ****************** POST function create new partner json and send on server
 * ***************************
 */
// метод формирует json, отправляет данные на сервер
// и добавляет вернувшиеся данные на страницу
function postNewPartnerForm(){
	var company = $("#inputCompanyName");
    var phonNumber = $("#inputCompanyPhon");
    var partnerMail = $("#inputCompanyMail");
	
	
	if(formValidation(company, phonNumber,partnerMail)){

	var data = JSON.stringify({
		"company": $(company).val(),
	    "phonNumber": $(phonNumber).val(),
	    "partnerMail":$(partnerMail).val()
	});

	request('POST',connectUrl+'/user/partners',addPartnerData ,data);

	$(company).val("");
	$(phonNumber).val("");
	$(partnerMail).val("");
	
	$(company).removeClass("is-valid");
	$(phonNumber).removeClass("is-valid");
	$(partnerMail).removeClass("is-valid");
	
	$("#inputPartnerFormErrore").collapse("hide");
}else{
	$("#inputPartnerFormErrore").collapse("show");
}
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
	var name = $("#inputProductName");
	if(formValidation(name)){
	var data = JSON.stringify(
		 {
			 "name": $("#inputProductName").val(),
	         "description": $("#inputProductDescription").val(),
	         "group": $("#inputProductGroup").val(),
	         "rate": $("#inputCompositeProductRate" ).val(),
	         "propertiesProduct":displayProductProperties( $("#propertieSelect option:selected" ).text() , 2),
	         "measureProduct":   displayProductMeasure( $("#measureSelect option:selected" ).text(), 2)
		   }
		);
	//var createComposite = parseInt($("#createProductCheckbox" ).val(),10) >=0;
	console.log($("#inputCompositeProductRate" ).val()<=0);
	var createComposite = $("#inputCompositeProductRate" ).val()<=0;
	if(createComposite){
	request('POST', connectUrl+'/user/partners/'+this.title+'/edit',displayPostProductRow ,data);
	}else{
	request('POST', connectUrl+'/user/partners/'+this.title+'/edit/coposite',displayPostProductRow ,data);
	}

	$("#inputProductName").val("");
	$("#inputProductDescription").val("");
	$("#inputProductGroup").val("");
	$("#inputCompositeProductRate" ).val("0")
	$("#inputPartnerProductFormErrore").collapse("hide");
	$("#inputProductName").removeClass("is-valid");
	}else{
		$("#inputPartnerProductFormErrore").collapse("show");
	}
};

function displayPostProductRow (data){
	$('#productTable').append(displayProductRow( data));
}


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
	var isValid  = formValidation($("#updateCompanyName") ,$("#updateCompanyPhon") ,$("#updateCompanyMail") );
	if(isValid){
		  var data = JSON.stringify({
		    "company": $("#updateCompanyName").val(),
		    "phonNumber": $("#updateCompanyPhon").val(),
		    "partnerMail":$("#updateCompanyMail").val()
		  });
		  request('PUT', connectUrl+'/user/partners/'+$("#partnerIdModalUpdate").text()+'/info',reusePartnerRow ,data);
		  
		  
		  $("#updateCompanyName").removeClass("is-valid");
		  $("#updateCompanyPhon").removeClass("is-valid");
		  $("#updateCompanyMail").removeClass("is-valid");
			$("#updateModalPartnerForm").modal("hide");
	}
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




