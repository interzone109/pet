 request("GET", connectUrl+"/user/products_list", fillAvailableFild);
$('#erroreIngridient').collapse("hide");


//заполняем таблицу ингридиентов данными с сервера
function fillAvailableFild(productList){
	
	if( Array.isArray(productList)){
		productList.forEach(product => {
			$('#avalbleIngridientGroupId').append(	createIngridientRow(product));
		});
	}else{
		var emptyElement = document.createElement('optgroup').innerText = "отсутствуют";
		$('#avalbleIngridientGroupId').append(emptyElement);
	}
	
	
}

// создание елемента для option с данными об ингридиенте
function createIngridientRow(prod){
	
	var ingridient = document.createElement('option');
	ingridient.id="option_ingridient_id_"+prod.id;
	
	var name ;
	if( prod.name.length < 8){
		name = prod.name;
	}else{
		name = prod.name.substring(0, 8)+"...";
	}
	ingridient.value = prod.id;
	ingridient.innerText = name; 
	ingridient.classList.add(prod.measureProduct);
	ingridient.setAttribute('title', prod.group +" : "+prod.name);
	return ingridient;
	
}


//метод прячет ингридиенты которые уже есть в данном продукте
// и выводит список ингридиентов доступных для добавления
function addIngridients(){
	$("#ingridientModalBody").modal("show");
	
	//при открытии модального окна возращаем в изначальное состояние
	// данные об ингридиентах
	 $('#selectedIngridientGroupId').children().each(function (){
		 var id = $(this).val();
		 $('#option_ingridient_id_'+id).prop( "disabled", false );
		 $(this).remove();
	 });
	
	
	$('#selectedIngridientGroupId').empty();
	$('#erroreIngridient').collapse("hide");
	var ids = document.getElementById("listIngridientsId").innerText.split(',');
	if( ids.length > 0){
		ids.forEach(id => {
			$('#option_ingridient_id_'+id).hide()
		});
	}
}




//устанавливаем название последнего выбраного елемента
function setIngridientName(){
	selectedIngridient = $('#avalbleIngridientGroupId option:selected').text()!==""?
			$('#avalbleIngridientGroupId option:selected').text() :$('#selectedIngridientGroupId option:selected').text();
 $('#displayChooseIngridient').text( selectedIngridient.length >4?selectedIngridient.substring(0,4) :selectedIngridient);
}




// переносим елементы между колонками 
function moveIngridient(direction){
	var selectedIngridient ;
	var quantity =  $('#ingridientQuantity').val();
	var res = parseFloat(quantity);
	
	if(direction === 1 ){
		
	if($('#ingridientQuantity').val().length < 1)
	{
		//alert("заполните поле расход");
		$('#erroreIngridient').text("заполните поле расход");
		$('#erroreIngridient').collapse("show");
	} 
	else if(  $('#avalbleIngridientGroupId option:selected').length === 0 )
	{
		//alert("выберите ингридиент");
		$('#erroreIngridient').text("выберите ингридиент");
		$('#erroreIngridient').collapse("show");
	}
	else if(isNaN(res))
	{
		$('#erroreIngridient').text("допускаются только числовые значения");
		$('#erroreIngridient').collapse("show");
	}
     else if(quantity.length > 0 && !isNaN(res))
     {

	 selectedIngridient = $('#avalbleIngridientGroupId option:selected');// получаем выбранный елемент
	 var clone = $(selectedIngridient).clone();// создаем его клон
	  
	 clone.attr('title', res); // устанавливаем колу title который хранит количество 
	 clone.attr('id', "clone_ingridient_id_"+selectedIngridient.val());// устанавливаем ему id
	 $('#selectedIngridientGroupId').append(clone);// переносим клон в selectedIngridientGroupId
	 $(selectedIngridient).prop( "disabled", true );// прячем выбраный елемент
	 $(selectedIngridient).prop('selected' , false);
	 $(clone).prop('selected' , false);
	 $('#erroreIngridient').collapse("hide");
	 }

	 
	 
	}else{
	 selectedIngridient = $('#selectedIngridientGroupId option:selected');
	 var id = $(selectedIngridient).val();
	 
	 $('#option_ingridient_id_'+id).prop( "disabled", false );
	 $(selectedIngridient).remove();
	}
}



// функция открывает модальное окно для обновления данных о расходе ингридиента !!!!!!!!
function updateIngridieteRow (id){	
	$('#updateRate').val($('#ingridiet_description_id_'+id).text().split(" ")[0] );
	$('#sendIngridientId').text(id);
	$('#ingridientMeasyrValue').text($('#ingridiet_description_id_'+id).text().split(" ")[1]);
	$('#updateModalIngridientQuantityForm').modal("show");
}




$('#updateRateButton').on("click", sendUpdateIngridientRate);
// метод получает значение из поля с новым расходом для ингридиента
//и обновляет данные
function sendUpdateIngridientRate (){
	if(formValidation($('#updateRate'))){
	var value = $('#updateRate').val();	
	var t = $('#ingridientMeasyrValue').text()==="шт" ? parseFloat(value) :   parseFloat(value)*1000 ;
	
	request("PUT", connectUrl+"/user/composites/"+$("#currentProductId").text()+"/edit/"+$('#sendIngridientId').text(),
			updateIngridientsRateRow,  JSON.stringify(t) );
	
	
	$('#updateModalIngridientQuantityForm').modal("hide");
	$('#updateRate').removeClass("is-valid");
	}
}

//метод обновляет данные о расходе в строке ингридиента
function updateIngridientsRateRow(data){
	// метод формирует итоговую строку
	$('#ingridiet_description_id_'+data.id).text(createMeasureProduct(data.description, data.measureProduct));	
}



function removeIngridient (id){
	
	$('#ingridiet_group_id_'+id).text("Удаленно");
	request("DELETE", connectUrl+"/user/composites/"+id+"/edit/");
}







$('#addIngridientLins').on("click",sendNewIngridient);
function sendNewIngridient(){
	if ( $('#selectedIngridientGroupId').children().length >0){
	var requestStr ="{" ;
	
	 $('#selectedIngridientGroupId').children().each(function (){
		var r = $(this).val() ;
		var t = $(this).hasClass('UNIT') ? parseFloat($(this).attr('title')) :   parseFloat($(this).attr('title'))*1000 ;
		requestStr += "\""+r+"\"" + ":" + t +",";
	 });
	 
	 var json = requestStr.substring(0, requestStr.length - 1);
	 json += "}";
	 
	if(json.length > 3){
	request("POST", connectUrl+"/user/composites/"+$("#currentProductId").text()+"/edit", displayIngridientsRow, json );
	
	 $('#ingridientModalBody').modal("hide");
	}
	}
}
