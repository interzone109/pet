

	$("#infoButton").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseInfo').collapse(status);    
    this.title= (status ==="show")?"activ":"";
    });
    
    /******************** sidebar function ****************************/
	var flag = false ;
    $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
        	 
            $('#sidebar').toggleClass('active');// 768 ширь
        });
    });
    //$(window).on('resize', function(){
     //   var win = $(this); 
       // if (win.width() >= 769 ) {  $('a.dropdown-toggle').show();}
    //   if (win.width() >= 769 && !$('#sidebar').hasClass('active')) {  $('a.dropdown-toggle').hide();  }  });
    /******************** sidebar function ****************************/  
    
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
    	if(type==='PUT' ||type==='POST'){
    		httpRequest.setRequestHeader("Content-Type", "application/json");
    		httpRequest.onreadystatechange = function () {
    		    if (httpRequest.readyState === 4 && httpRequest.status === 200) {
    		        var json = JSON.parse(httpRequest.responseText);
    		       console.log('type '+type+' URL '+url);
    		       console.log(json);
    		       method(json);
    		    }else {
    		     errorHandler(httpRequest);
    		    }
    		};
    		
    		console.log(json);
    		httpRequest.send(json);
    	};
    	
    	if(type==='DELETE'){
    		httpRequest.setRequestHeader("Content-Type", "application/json");
    		httpRequest.onreadystatechange = function () {
    		    if (httpRequest.readyState === 4 && httpRequest.status === 200) {
    		       console.log('type '+type+' URL '+url);
    		       //method(json);
    		    }else {
    		     errorHandler(httpRequest);
    		    }
    		};
    		
    		console.log(json);
    		httpRequest.send(json);
    	}

    };
    /** **************** request method **************************** */
    
    // метод валидирует текстовые инпутполя is-valid
   function formValidation (){
	   var isValid = true ;
    for (var i = 0; i < arguments.length; i++) {
    	var inputElement = $(arguments[i]);
        if($(inputElement).val() === ""){
        	isValid = false ;
        	$(inputElement).addClass("is-invalid");
        }else{
        	$(inputElement).removeClass("is-invalid");
        	$(inputElement).addClass("is-valid");
        }
      }
    
    return isValid ;
   }
    
   function formValidationNumber (){
	   var isValid = true ;
    for (var i = 0; i < arguments.length; i++) {
    	var inputElement = $(arguments[i]);
    	var value = parseFloat($(inputElement).val());
    	//console.log($(inputElement).val()+" - "+value);
        if($(inputElement).val() === "" || isNaN(value) ){
        	isValid = false ;
        	$(inputElement).addClass("is-invalid");
        }else{
        	$(inputElement).removeClass("is-invalid");
        	$(inputElement).addClass("is-valid");
        }
      }
    
    return isValid ;
   }

    
    
  function errorHandler(httpRequest){
	  console.log('error- '+httpRequest.responseText);
	  if ( httpRequest.readyState === 4 && httpRequest.status === 509) {
    	 console.log("parntner limit");
    	 $("#excessOfLimit").collapse('show');
    	 $("#excessOfLimitText").text('Превышен доступный Вам лимит поставщиков'); 
      }else if ( httpRequest.readyState === 4 && httpRequest.status === 409) {
    	 console.log("parntner limit");
    	 $("#excessOfLimit").collapse('show');
    	 $("#excessOfLimitText").text('Превышен доступный Вам лимит продуктов');  
      }else if ( httpRequest.readyState === 4 && httpRequest.status === 424) {
    	 console.log("parntner limit");
    	 $("#excessOfLimit").collapse('show');
    	 $("#excessOfLimitText").text('Игридиент добавлен к поставщику но превышен доступный Вам лимит продуктов');  
      }else if ( httpRequest.readyState === 4 && httpRequest.status === 415) {
    	 console.log("parntner limit");
    	 $("#excessOfLimit").collapse('show');
    	 $("#excessOfLimitText").text('Ошибка удаления');  
      }
      else if ( httpRequest.readyState === 4 && httpRequest.status === 411) {
     	 console.log("parntner limit");
     	 $("#excessOfLimit").collapse('show');
     	 $("#excessOfLimitText").text('Вы исчерпали лимит на создание новых магазинов');  
       }
      else if ( httpRequest.readyState === 4 && httpRequest.status === 400) {
      	 console.log("не удалено");
      	 //$("#excessOfLimit").collapse('show');
      	 //$("#excessOfLimitText").text('');  
        } else if ( httpRequest.readyState === 4 && httpRequest.status === 413) {
         	 console.log("оплата не зачислена");
          	 $("#excessOfLimit").collapse('show');
          	 $("#excessOfLimitText").text('Оплата не зачислена');  
            }
	  
	  $("#spiners").hide();
   }
    
    
  function closeExcessOfLimit(){
	  $("#excessOfLimit").collapse('hide');
  }
  
    
//формируем цену в удобнов формате
  function displayProductPrice(price){ 
  	price =  parseInt(price);
  	  if (isNaN(price)) { 
  		  return 0 ; 
  		  }

  		if(price < 1){
  			return 0;
  		} else if(price < 9){
  			return "0.0"+price;
  		}
  		else if(price <99){
  			return "0."+price;
  		}
  		else if(price >99){
  		return price/100; 
  		}
  	}
    
 function converDate(date){
	var array =  date.split("-");
	return array[2]+"."+array[1]+"."+array[0];
  }
    
    
//метод преобразовывает количество ингридиента например 100 = 0.100 кг
 function createMeasureProduct(expend, measure){
 	var result ;
 	if(measure ==="UNIT"){
 		result = expend;
 	}else {
 		if(expend.toString(10).length < 2){
 		result = "0.00" + expend  ;
 		}else if(expend.toString(10).length < 3){
 		result = "0.0" + expend  ;
 		}else if(expend.toString(10).length <4){
 		result = "0." + expend  ;
 		}
 		else{
 			expend = expend.toString(10);
 		var strStart = expend.substring(0,expend.length-3);
 		var strEnd = expend.substring(expend.length-3 ,expend.length);
 		result = strStart+"."+strEnd  ;
 		
 		}
 	} 
 	return result
 }


 function displayProductMeasure (measure ,convert){
 	 
 	if( measure ==="LITER" || measure==="литр"){
 		return (convert === 1)?"литр": "LITER";
 	}else if( measure ==="KILOGRAM" || measure==="кило"){
 		return  (convert === 1)?"кило": "KILOGRAM";
 	}else if( measure ==="UNIT" || measure==="шт"){
 		return  (convert === 1)?"шт": "UNIT";
 	}
 	
 };
 
 $(function () {
	 $('[data-toggle=tooltip]').tooltip();
 });
 

 $('#ddd').tooltip({placement: 'bottom',trigger: 'manual'}).tooltip('hide');

 var hideTooltip = true ;
$('#ddd').on('click',function(){
	if(hideTooltip){
	 $(this).tooltip('show');
	}else{
		 $(this).tooltip('hide');
	}
	hideTooltip = !hideTooltip;
	 });


 function saveUserActionInfo(action){
	 request('POST', 'http://localhost:8080/user/saveAction', addToJornal, action);
 }
