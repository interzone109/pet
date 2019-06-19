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

var storeid ;
function getStoreCashBox(){
	 storeId = $("#storeSelect option:selected").val();
	//var isGroup = $('#groupPoducts').is(":checked");
	
	request('GET', connectUrl+'/user/stores/assortment/'+storeId, fillCashBoxData);
}

function fillCashBoxData(data){
	state.set("storeId_"+storeId, data);
	var cashBoxRow = document.createElement('div');
	cashBoxRow.className = "row";
	
for(var  i = 0 , t= 0; i <data.length; i++){
		cashBoxRow.append(createNewStoreProductElem( data[i]));
		if( t ===2 || i === data.length-1){
			console.log(t)
			t = 0 ;
			//$('#cashBoxContainer').append('<br>');
			$('#cashBoxContainer').append(cashBoxRow);
			var cashBoxRow = document.createElement('div');
			cashBoxRow.className = "row";
		}else{
		t++;
		}
	}
}


function createNewStoreProductElem(product){
	 var productElem = document.createElement('div');
	 productElem.className = "col"; 
	 productElem.innerHTML = 
		 
		 "<div class=\"card border-info mb-3 text-center\" style=\"max-width: 18rem;\" >"
		 	+"<div class=\"card-header\"title=\""+product.name +"\">"+product.name.substring(0, 12) +"</div>"
		 		+"<div class=\"card-body text-info\">"
		 		+"<h5 class=\"card-title\"> <i class=\"fas fa-minus\"></i>" 
		 		+"1" 
		 		+"<i class=\"fas fa-plus\"></i> </h5>"
		 		+"<button class=\"btn btn-success col\"title=\"добавить\">"+displayProductPrice(product.propertiesProduct) +"</button>"
		 	+"</div>"
		 +"</div>";
		 
		 
		 
		var t =  "<div class=\"card-counter \">"
		 +"<span class=\"count-numbers\">12 грн</span>"
		 +"<span class=\"count-name\" title=\""+product.name+"\">"+product.name.substring(0, 12)+"</span>"
		 +"<div class=\"\"> <i class=\"fas fa-minus-square\"></i>  1   <i class=\"fas fa-plus-square\"></i> шт</div>"
		 	+"</div>" ;
	 
	 
	
	 
	
	return productElem;
}







function displayProductPrice(price){
	if(price < 9){
		return "0.0"+price+" грн";
	}
	else if(price <99){
		return "0."+price+" грн";
	}
	else if(price >99){
	return price/100+" грн"; 
	}
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