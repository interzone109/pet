 request("GET", connectUrl+"/user/products_list", fillAvailableFild);
$('#erroreIngridient').collapse("hide");




//при закрытии модального окна возращаем в изначальное состояние
// данные об ингридиентах
$("#ingridientModalBody").on("hidden.bs.modal", function () {
	 $('#selectedIngridientGroupId').children().each(function (){
		 var id = $(this).val();
		 $('#option_ingridient_id_'+id).prop( "disabled", false );
		 $(this).remove();
	 });
	});



//заполняем таблицу ингридиентов данными с сервера
function fillAvailableFild(productList){
	
	if( Array.isArray(productList)){
		productList.forEach(product => {
			$('#avalbleIngridientGroupId').append(	createIngridientRow(product));
		});
	}else{
		var emptyElement = document.createElement('option').innerText = "отсутствуют";
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
	ingridient.setAttribute('title', prod.group +" : "+prod.name);
	return ingridient;
	
}


//метод прячет ингридиенты которые уже есть в данном продукте
// и выводит список ингридиентов доступных для добавления
function addIngridients(){
	$("#ingridientModalBody").modal("show");
	
	$('#selectedIngridientGroupId').empty();
	$('#erroreIngridient').collapse("hide");
	var ids = document.getElementById("listIngridientsId").innerText.split(',');
	if( ids.length > 0){
		ids.forEach(id => {
			$('#option_ingridient_id_'+id).hide()
		});
	}
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
	 $(quantity).val("");// обнуляем поле ввода
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




function updateIngridieteRow (id){
	console.log(id+" update");
}


$('#addIngridientLins').on("focus",sendNewIngridient);


function sendNewIngridient(){
	console.log('sendNewIngridient');
	let map = new Map();
	 $('#selectedIngridientGroupId').children().each(function (){
		 //console.log("id=" $(this).val() +"  quantity"+ $(this).attr('title'));
		
		map.set( $(this).val(), $(this).attr('title'));
		console.log(map);
		 
	 });
}















/** **************** request method **************************** */
/*function request(type, url, method , json){
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
		console.log(type);
		httpRequest.send(json);
	};

};*/
/** **************** request method **************************** */