var state = new Map();//переменная хранит данные о запросax
var connectUrl = "http://localhost:8080" ;
$("#spiners").hide();


// метод формирует запрос для получения инвойса
function getStoreCashBox(){
		 var cashStart = $("#startCashBoxDay").val();
		 var today = new Date();
		 var dd = String(today.getDate()).padStart(2, '0');
		 var mm = String(today.getMonth() + 1).padStart(2, '0');
		 var yyyy = today.getFullYear();
		 
		 var data  = JSON.stringify({
				"dateStart": dd+"."+mm+"."+yyyy,
		        "cashBoxStartDay": cashStart*100,
		        "cashBox":0,
		        "currentSell":0,
		        "orderQuantity":0
			   }
			);
		 
		 request('POST', connectUrl+'/employee/stores/invoice/cashBox/', getOrCreateInvoice,data);
		 
		 $('#collapseOptional').collapse("hide"); 
}

//метод получает новый инвойс для выбрного магазина и отображает данные из него
function getOrCreateInvoice(data){
	updateCashboxInfo(data);
	 
	 request('GET', connectUrl+'/employee/stores/assortment/', fillCashBoxData);
	 
}

function updateCashboxInfo(data){
	var storeId = $("#cuurent_store_id").text();
	var  storeName = $('#storeSelect option[value="' + storeId + '"]').text();
	$("#storeNameinvoiceInfo").text(storeName);
	
	$("#cashboxStart").text(displayProductPrice(data.cashBoxStartDay));
	$("#cashboxCurrent").text(displayProductPrice(data.cashBox));
	$("#sellQyantity").text(data.sellQuantity);
	$("#orderQuantity").text(data.orderQuantity);
	
}


//метод формирет поля с карточкамитовара
function fillCashBoxData(data){
	 $("#cashBoxContainer").empty();
	state.set("storeId_"+$("#cuurent_store_id").text(), data);
	var cashBoxRow = document.createElement('div');
	cashBoxRow.className = "row";
	
for(var  i = 0 , t= 0; i <data.length; i++){
		cashBoxRow.append(createNewStoreProductElem( data[i]));
		if( t ===2 || i === data.length-1){
			t = 0 ;
			$('#cashBoxContainer').append(cashBoxRow);
			var cashBoxRow = document.createElement('div');
			cashBoxRow.className = "row";
		}else{
		t++;
		}
	}
}

//создаем карточку с товаром
function createNewStoreProductElem(product){
	 var productElem = document.createElement('div');
	 productElem.className = "col"; 
	 productElem.innerHTML =  "<div class=\"card border-info mb-3 text-center\" style=\"max-width: 18rem;\" >"
		 	+"<div class=\"card-header text-info\"title=\""+product.name +"\">"+product.name.substring(0, 12) +"</div>"
		 	+"<div class=\" text-primary\">"+product.group+"</div>"
		 //+"<div class=\" text\">"+product.name+"</div>"
		 		+"<div class=\"card-body text-info \">"
		 		+"<h5 class=\"card-title row \"> " 
		 		+"<i class=\"fas fa-minus col \" onclick=\"count(-1,"+product.id+")\"></i>" 
		 		+"<input value=\"1\" class=\"col form-control  p-1\" id=\"input_product_id_"+product.id+"\"> " 
		 		+"<i class=\"fas fa-plus col \" onclick=\"count(1,"+product.id+")\"></i> </h5>"
		 		+"<button class=\"btn btn-success col\"title=\""+product.propertiesProduct+"\" id=\"sell_button_id_"+product.id +"\" onclick=\"addProductToBusket("+product.id+")\">"
		 		+displayProductPrice(product.propertiesProduct) +"</button>"
		 	+"</div>"
		 +"</div>";
	 return productElem;
}


//метод увеличивает счетчик количества товара
function count(sing, productId){
	var value = parseInt($("#input_product_id_"+productId).val());
	$("#input_product_id_"+productId).val(value+(sing)<=0 ? 1 :value+(sing ));
	
	var price = Number($("#sell_button_id_"+productId).attr("title")) * Number($("#input_product_id_"+productId).val());
	$("#sell_button_id_"+productId).text(displayProductPrice(price));
	
}

// показывает карточку с настройками для кассы
$("#collapseOptionalButton").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseOptional').collapse(status);    
    this.title= (status ==="show")?"activ":"";
    });

//показывает карточку с информацией о кассе
$("#invoiceMeta").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseInvoiceMeta').collapse(status);    
    this.title= (status ==="show")?"activ":"";
    });

//показывает карточку с информацией о странице
$("#infoAboutPage").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseInfo').collapse(status);    
    this.title= (status ==="show")?"activ":"";
    });


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