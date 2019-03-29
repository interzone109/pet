
var connectUrl = "http://localhost:8080" ;


request("GET", connectUrl+"/user/composites", displayCompositeRow);
$("#collapseCompositeProductBody").collapse("show");



/************************ display composite product row *****************************/

function displayCompositeRow(compositeProducts){
	if( Array.isArray(compositeProducts)){
		compositeProducts.forEach(cProd => {
			$('#productTableBody').append(	createCompositeRow(cProd));
		});
	}else{
		$('#productTableBody').append(	createCompositeRow(compositeProducts));
	}
}

function createCompositeRow(cProd){
	
	var compositeCol = document.createElement('tr');
	compositeCol.id="product_row_id_"+cProd.id;
	compositeCol.innerHTML =
		   "<td id=\"product_group_id_"+cProd.id+"\">"+cProd.group+"</td>"
		 +"<td id=\"product_name_id_"+cProd.id+"\">"+cProd.name+"</td>"
		 + "<td id=\"product_measure_id_"+cProd.id+"\">"+displayProductMeasure(cProd.measureProduct,1)+"</td>"
		 + "<td>  <i class=\"fas fa-edit\"  title=\"редактировать\"  onclick=\"updateCompositeRow("+cProd.id+")\"  ></i>"
		 +"<i class=\"fas fa-list-alt\" title=\"просмотреть ингридиенты\"  onclick=\"loadProductData("+cProd.id+")\"></i> </td>";
		 	 
		return compositeCol;
	
}



/************************ display composite product row *****************************/



/************************ update composite product row *****************************/

$("#updateCompositeProductButton").on("click",PUTCompositeModal);

function updateCompositeRow(id){
	
	$("#updateModalCompositeProductForm").modal('show'); 
	$("#updateCompositeProductGroup").val($("#product_group_id_"+id).text());
	$("#updateCompositeProductName").val($("#product_name_id_"+id).text()); 
	$("#sendCompositeId").text(id);
	$("#updateCompositeMeasureSelect option[value="+$("#product_measure_id_"+id).text()+"]").prop('selected', true);
}



function PUTCompositeModal(){

	var data = JSON.stringify({
		"id": $("#sendCompositeId").text(),
		"name": $("#updateCompositeProductName").val(),
        "group": $("#updateCompositeProductGroup").val(),
        "measureProduct":displayProductMeasure( $("#updateCompositeMeasureSelect option:selected" ).text(), 2)
	   }
	);
	
	request("PUT", connectUrl+"/user/composites/", updateCompositeRowData,data);
	
}

function updateCompositeRowData(data){
	$("#product_group_id_"+data.id).text(data.group);
	$("#product_name_id_"+data.id).text(data.name);
	$("#product_measure_id_"+data.id).text(displayProductMeasure( data.measureProduct, 1));
}




/************************ update composite product row *****************************/






/************************ POST new composite product ***************************************/

function postNewCompositeProduct(){
	var data = JSON.stringify({
		 "name": $("#inputCompositeName").val(),
       "group": $("#inputCompositeGroup").val(),
       "measureProduct":displayProductMeasure( $("#measureSelect option:selected" ).text(), 2)
	   }
	);
	request("POST", connectUrl+"/user/composites", displayCompositeRow,data);
	
	$("#inputCompositeName").val('');
    $("#inputCompositeGroup").val('');
    $("#measureSelect option[value="+"шт"+"]").prop('selected', true);
}


/************************ POST new composite product ***************************************/







/************************ GET create ingridient table ***************************************/


//прячем таблицу композитного продукта, делаем запрос к серверу и отображаем данные в новой таблицу
function loadProductData (id){
	$("#collapseCompositeProductBody").collapse("hide");
	$("#currentProductId").text(id);
	request("GET", connectUrl+"/user/composites/"+id+"/edit", displayIngridientsRow);
	$("#collapseCompositeIngridientBody").collapse("show");
	
}



//формирует таблицу с ингридиентами
function displayIngridientsRow(ingridietsMap){

	if (ingridietsMap.length !== 0 && Array.isArray(ingridietsMap) ){
		ingridietsMap.forEach(ingridient => {
			$('#ingridientTableBody').append(createIngridientsRow(ingridient));
		});
	}else{
		$("#ingridientTableHeader").append(
				 "<caption id=\"ingridientCaption\">" 
				 +"<ul class=\"nav navbar-nav ml-auto\">"
			     +"<li class=\"nav-item \">"
			     +"<a class=\"nav-link\" href=\"#\" onclick=\"hideProduct()\" > Вернуться к продуктам </a>"
			     +"</li> </ul>"
			     +"</caption>");
				
	}
}



//формирует строку с ингридиентами
function createIngridientsRow(ingridiet){
	var ingridietCol = document.createElement('tr');
	ingridietCol.id="ingridiet_row_id_"+ingridiet.id;
	ingridietCol.innerHTML =
		   "<td id=\"ingridiet_group_id_"+ingridiet.id+"\">"+ingridiet.group+"</td>"
		 +"<td id=\"ingridiet_name_id_"+ingridiet.id+"\">"+ingridiet.name+"</td>"
		 + "<td id=\"ingridiet_description_id_"+ingridiet.id+"\">"+createMeasureProduct(ingridiet.description, ingridiet.measureProduct)+"</td>"
		 + "<td>  <i class=\"fas fa-edit\"  title=\"редактировать расход\"  onclick=\"updateIngridieteRow("+ingridiet.id+")\"  ></i>"
		 +"<i class=\"fas fa-list-alt\" title=\"вернуться к продуктам\"  onclick=\"hideProduct()\"></i> " 
		 +"<i class=\"fas fa-trash-alt\" title=\"удалить\"  onclick=\"removeIngridient("+ingridiet.id+")\"></i></td>";
		 	 
		$("#listIngridientsId").append(document.createTextNode(ingridiet.id+","));
		
		return ingridietCol;
}



// метод скрывает таблицу с ингрилдиентами и развертывает таблицу с конечным продуктом
function hideProduct(){
	
$("#collapseCompositeProductBody").collapse("show");

$("#collapseCompositeIngridientBody").collapse("hide");
$("#ingridientTableBody").empty();// чистим строки в таблице с продуктами
$("#ingridientCaption").remove();// удаляем заголовок списка, если такой есть


var ids = document.getElementById("listIngridientsId").innerText.split(',');
if( ids.length > 0){
	ids.forEach(id => {
		$('#option_ingridient_id_'+id).show()
	});
}


$("#listIngridientsId").text("");

}




// метод преобразовывает количество ингридиента например 100 = 0.100 кг
function createMeasureProduct(expend, measure){
	
	var result ;
	if(measure ==="UNIT"){
		result = expend+" шт";
	}else {
		var meas = " "+ displayProductMeasure(measure ,1);
		
		if(expend.length < 2){
		result = "0.00" + expend + meas ;
		}else if(expend.length < 3){
		result = "0.0" + expend + meas ;
		}else if(expend.length <4){
		result = "0." + expend + meas ;
		}
		else{
		
		var strStart = expend.substring(0,expend.length-3);
		var strEnd = expend.substring(expend.length-3 ,expend.length);
		result = strStart+"."+strEnd + meas ;
		}
		
	} 
	
	return result
}







/************************ GET create ingridient table ***************************************/












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
		console.log(type);
		httpRequest.send(json);
	};

};
/** **************** request method **************************** */



/******************** search function ****************************/
$(document).ready(function(){
  $("#searchOnPageTable").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#productTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
/******************** search function ****************************/

/******************** sidebar function ****************************/
$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});
/******************** sidebar function ****************************/  


function displayProductMeasure (measure ,convert){
	 
	if( measure ==="LITER" || measure==="л"){
		return (convert === 1)?"л": "LITER";
	}else if( measure ==="KILOGRAM" || measure==="кг"){
		return  (convert === 1)?"кг": "KILOGRAM";
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
