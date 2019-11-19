var twoReportData
function createReportTwo(report){
	$("#twoReport").remove();
	twoReportData = report;
	var stores =  new Set();
	 var reportBlock = document.createElement('li');
	 reportBlock.className = "list-group-item";
	 reportBlock.id ="twoReport";
	 var body ="";
	 var headTable = "<h4>Доходность магагинов "+report.dateStart+" по "+report.dateEnd+"</h4><table class=\"table table-hover\">" 
		+"<thead><tr><th>Магазин</th><th>Дата</th><th>Поступление</th><th>Расход</th><th>Работники</th><th>Прибыль</th><th>Чек шт</th></tr></thead><tbody id =\"twoReportBody\">";
		
		 report.invoiceData.forEach(row => {
			 var profit= parseFloat(row.cashBox/100) - parseFloat(row.sellQuantity/100);
			 body +="<tr class=\""+ row.dateEnd +"\"><th>"+row.dateEnd+"</th>"
			 +"<th>"+converDate(row.dateStart)+"</th>"
			 +"<th>"+displayProductPrice(row.cashBox)+" грн</th>"
			 +"<th>"+displayProductPrice(row.sellQuantity)+" грн</th>"
			 +"<th>"+displayProductPrice(row.cashBoxStartDay)+" грн</th>"
			 +"<th>"+profit+" грн</th>"
			 +"<th>"+row.orderQuantity +" </th></tr>";
			 
			 if(!stores.has(row.dateEnd)){
				 stores.add(row.dateEnd);
			 };
		 });

		
		var footer = "</tbody></table><div class=\"form-row\" id=\"twoReportFooter\">"
		 +"<div class=\"col-5\"> <button class=\"btn btn-danger\"   onclick=\"cleanReportTwo()\"  title=\"Очистить\"> <i class=\"fas fa-eraser\"></i></button> Очистить отчет \"Доходность\""
		 +"</div><div class=\"col-3\"><select class=\"form-control\" id=\"storeSelect\"></select></div>"
		 +"<div class=\"col-1\"> <button class=\"btn btn-info\"  onclick=\"showChart()\" title=\"построить график\"><i class=\"fas fa-chart-line\"></i> График </button></div>"
		 +"<div class=\"line\"></div><div id=\"report\"></div></li>";

		 reportBlock.innerHTML =  headTable+body+footer;
		 $("#spiners").hide();
		 $("#reportLsit").append(reportBlock);
		 
		 stores.forEach(addres=>{
				$("#storeSelect").append("<option value=\""+addres+"\" >"+addres+"</option>");
			});
}

function cleanReportTwo(){
	$("#twoReport").remove();
}

//показываем график по данным 
function showChart(){
	var storeName = $("#storeSelect").val();
	  var size  = $("tr[class='"+storeName+"']").length
	  var array = new Array(size+1);
	  array[0]= [ 'Дата', 'Ингридиенты', 'Прибыль' ];
	  
	  var total = 0.0 ;
	  var dif = 0.0;
	  var date ="";
	 var cicle =0 ;
	 var iter = 1;
		  $("tr[class='"+storeName+"']").children().each(function() {
			  console.log($(this).text());
			  if( cicle == 1 ){
				  date = $(this).text() ;//дата
		       }else if(cicle == 3) {
				  total = parseFloat($(this).text().split(" ")[0]);//приход
				  console.log(total);
			  }else if( cicle == 5 ){
				  console.log("maja - "+ $(this).text());
				  dif= parseFloat($(this).text().split(" ")[0]);//прибыль
				  console.log(dif);
			  }
			  cicle++;
			  if(cicle == 7){
				  array[iter]= [date , total,dif];
				  cicle =0 ;
				  iter++;
			  }
			  
		  });
		 
	  
	
		  google.charts.load('current', {'packages':['bar']});
		   google.charts.setOnLoadCallback(drawChart);
		   

		     function drawChart() {
		    	 var data = google.visualization.arrayToDataTable(array);

		      var options = {
		        width: 600,
		        height: 400,
		        legend: { position: 'top', maxLines: 3 },
		        bar: { groupWidth: '75%' },
		        isStacked: true,
		      };
		      var chart = new google.charts.Bar(document.getElementById('report'));

		       chart.draw(data, google.charts.Bar.convertOptions(options));
		       
		     }
	
	/* google.charts.load('current', {'packages':['bar']});
     google.charts.setOnLoadCallback(drawChart);
   

     function drawChart() {
    	 var data = google.visualization.arrayToDataTable(array);

    	      var options = {
    	       width: 700,
    	       height: 300,
    	        legend: { position: 'top', maxLines: 4 },
    	        bar: { groupWidth: '75%' },
    	        isStacked: true,
    	      };

       var chart = new google.charts.Bar(document.getElementById('report'));

       chart.draw(data, google.charts.Bar.convertOptions(options));
       
     }*/
}





























function buildChartFoureReport(){
	//console.log(fourReportData);
	 google.charts.load('current', {'packages':['bar']});
     google.charts.setOnLoadCallback(drawChart);
    var test = new Array(20);
    for(var i = 0; i<20 ;i++)
    test[i]= ["Яблоко", 10,10 ];
    test[0]= ['Продукт', 'Ингридиенты', 'Маржа' ];
     function drawChart() {
    	 var data = google.visualization.arrayToDataTable(test);

    	      var options = {
    	      //  width: 500,
    	       // height: 300,
    	        legend: { position: 'top', maxLines: 4 },
    	        bar: { groupWidth: '75%' },
    	        isStacked: true,
    	      };

       var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

       chart.draw(data, google.charts.Bar.convertOptions(options));
     }

}
//buildChartFoureReport();