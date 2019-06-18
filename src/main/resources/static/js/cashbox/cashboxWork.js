var state = new Map();//переменная хранит данные о запросax
var connectUrl = "http://localhost:8080" ;


//получаем список  магазинов
request("GET",connectUrl + "/user/stores", addStoreSearchRow );

//добавляем оптионалы для выборки по магазинам
function addStoreSearchRow(data){
	if (data.length < 1 ){// если магазинов у поьзователя нет то просм его создать
		$("#storeSelect").append("<option value=\"NONE\">Создайте магазин</option>");
	} else {
		data.forEach(store=>{
			$("#storeSelect").append("<option value=\""+store.id+"\" selected=\"selected\">"+store.address+"</option>");//добавлям селекты с названиями лоступных магазинов
			});
		$("#storeSelect").val(data[0].id);//устанавливаем selected 
		state.set("stores", data);
	}	
}


function getStoreCashBox(){
	var storeId = $("#storeSelect option:selected").val();
	//var isGroup = $('#groupPoducts').is(":checked");
	
	request('GET', connectUrl+'/user/stores/assortment/'+storeId, fillCashBoxData);
}

function fillCashBoxData(data){
	var i = 0;
	var state = new Map();
	data.forEach(product => {

		var cashBoxRow = document.createElement('div');
		cashBoxRow.className = "row";
		
		cashBoxRow.append(createNewStoreProductElem( product));
		
		console.log(cashBoxRow);
		if(i === 5 || i === data.length-1){
			console.log(i);
			i = 0 ;
			$('#cashBoxContainer').append('<br>');
			$('#cashBoxContainer').append(cashBoxRow);
		}
		i++ ;
	});
	
}


function createNewStoreProductElem(product){
	 var productElem = document.createElement('div');
	 productElem.className = "col" 
	 productElem.innerHTML = ""+product.name;
	
	return productElem;
}




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
	        console.log('error '+type+' URL '+url);
	      }
	    }
	httpRequest.send();
	};
	if(type==='PUT' ||type==='POST' ){
		httpRequest.setRequestHeader("Content-Type", "application/json");
		httpRequest.onreadystatechange = function () {
		    if (httpRequest.readyState === 4 && httpRequest.status === 200) {
		        var json = JSON.parse(httpRequest.responseText);
		       console.log('type '+type+' URL '+url);
		       console.log(json);
		       method(json);
		    }else {
		      console.log('error send data type '+type+' URL '+url);
		    }
		};
		
		console.log(json);
		httpRequest.send(json);
	};

};
/** **************** request method **************************** */