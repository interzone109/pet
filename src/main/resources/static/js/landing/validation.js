	$('#pass').on('input',  function(){ 
		var pass = $(this).val();
		var repidPass =$('#repidPass').val() ;
		if(pass === repidPass){
			$('#repidPass').css("border-color", "green");
			$(this).css("border-color", "green");
		}else{
			$('#repidPass').css("border-color", "red");
			$(this).css("border-color", "red");
		}
	});

$('#repidPass').on('input',  function(){ 
	var pass = $('#pass').val() ;
	var repidPass =$(this).val();
	if(pass === repidPass){
		$('#pass').css("border-color", "green");
		$(this).css("border-color", "green");
	}else{
		$('#pass').css("border-color", "red");
		$(this).css("border-color", "red");
	}
	});

$("#email").on("input" ,function(){
	var email = $("#email").val();
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	  if( re.test(email)){
		  $('#email').css("border-color", "green");
	  }else{
		  $('#email').css("border-color", "red");
	  }
});