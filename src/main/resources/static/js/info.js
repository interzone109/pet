

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
    	if(type==='PUT' ||type==='POST' || type==='DELETE'){
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
   }
    
    
  function closeExcessOfLimit(){
	  $("#excessOfLimit").collapse('hide');
  }
  
    
    
    
    
    
    