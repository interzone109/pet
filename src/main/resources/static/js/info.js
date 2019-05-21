

	$("#infoButton").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseInfo').collapse(status);
    this.title= (status ==="show")?"activ":"";
    });
    
    /******************** sidebar function ****************************/
    $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
            $('#sidebar').toggleClass('active');
        });
    });
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
    
    // метод валидирует текстовые инпутполя is-valid
   function formValidation (){
	   var isValid = true ;
    for (var i = 0; i < arguments.length; i++) {
    	var inputElement = $(arguments[i]);
    	var erroreId = $(inputElement).attr('id')+'Errore' ;
    	var varningElement = $("#"+erroreId);
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    