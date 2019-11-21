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
		+"<thead><tr><th>Магазин</th><th>Дата</th><th title=\"операционная деятельность\">ОД</th><th>Закупка</th><th>Работники</th><th>Приход</th><th>Прибыль</th></tr></thead><tbody id =\"twoReportBody\">";
		
		 report.invoiceData.forEach(row => {
			 var profit= parseFloat(row.cashBox/100) - parseFloat(row.sellQuantity/100) - parseFloat(row.cashBoxStartDay/100) - parseFloat(row.orderQuantity/100);
			 body +="<tr class=\""+ row.dateEnd +"\"><th>"+row.dateEnd+"</th>"
			 +"<th>"+converDate(row.dateStart)+"</th>"
			 +"<th>"+displayProductPrice(row.orderQuantity)+" грн</th>"
			 +"<th>"+displayProductPrice(row.sellQuantity)+" грн</th>"
			 +"<th>"+displayProductPrice(row.cashBoxStartDay)+" грн</th>"
			 +"<th>"+displayProductPrice(row.cashBox)+" грн</th>"
			 +"<th>"+profit.toFixed(2); +" грн</th></tr>";
			 
			 if(!stores.has(row.dateEnd)){
				 stores.add(row.dateEnd);
			 };
		 });

		
		var footer = "</tbody></table><div class=\"form-row\" id=\"twoReportFooter\">"
		 +"<div class=\"col-3\"> <button class=\"btn btn-danger\"   onclick=\"cleanReportTwo()\"  title=\"Очистить\"> <i class=\"fas fa-eraser\"></i> Удалисть отчет</button>"
		 +"</div><div class=\"col-3\"><select class=\"form-control\" id=\"storeSelect\"></select></div>"
		 +"<div class=\"col-1\"> <button class=\"btn btn-info\"  onclick=\"showChart()\" title=\"построить график\"><i class=\"fas fa-chart-line\"></i> График </button></div>"
		 +"<div class=\" col-3\" ><label class=\"switch\" > <input type=\"checkbox\" onclick=\"switchInput()\"> <span class=\"slider\"></span> </label></div>"
		 +"<div class=\"line\"></div><div id=\"report\"></div><div id='reportFooter'></div> </li>";

		 reportBlock.innerHTML =  headTable+body+footer;
		 $("#spiners").hide();
		 $("#reportLsit").append(reportBlock);
		 
		 stores.forEach(addres=>{
				$("#storeSelect").append("<option value=\""+addres+"\" >"+addres+"</option>");
			});
}
//чистим весь отчет
function cleanReportTwo(){
	$("#twoReport").remove();
}

function cleanReportTwoChart(){
	$("#report").empty();
	$("#reportFooter").remove();
}


//показываем график по данным 
function showChart(){
	$("#report").empty();
	var storeName = $("#storeSelect").val();
	  var size  = $("tr[class='"+storeName+"']").length
	  var array = new Array(size+1);
	  array[0]= [ 'Дата', 'ОД', 'Закупка','Зарплаты', 'Прибыль' ];
	  var data = [];
	 
	  var arrayIter = 1;
	 for(var row = 0; row < size; row++){
		var store =  $("tr[class='"+storeName+"']")[row];
		var cicle =0 ;
		var dataIter = 0;
		
		 $(store ).children().each(function() {
	 
			  if(cicle == 1){
			  data[dataIter] = $(this).text().split(" ")[0];
			  dataIter++;
			  }else  if(cicle > 1 && cicle != 5){
				  data[dataIter] = parseFloat($(this).text().split(" ")[0]);
				  dataIter++;
			  }		  
			  cicle++;
			  if(cicle == 7){
				  array[arrayIter]= data;
				  data =[];
				  cicle =0 ;
				  dataIter = 0 ;
				  arrayIter++;
			  }
			  
		  });
		  
	 } 
	var cleanReportChart = "<div class=\"col-5\"> <button class=\"btn btn-danger\" onclick=\"cleanReportTwoChart()\"  title=\"Удлить таблицу\"> <i class=\"fas fa-eraser\"></i> Удлить таблицу</button></div>"
		 
		conctrucktChart(array);
	$("#reportFooter").prepend(cleanReportChart);
}
//строим график 
var lastData ;
function conctrucktChart(array){
	lastData = array;
	  google.charts.load('current', {'packages':['bar']});
	   google.charts.setOnLoadCallback(drawChart);
	   

	     function drawChart() {
	    	 var data = google.visualization.arrayToDataTable(array);

	      var options = {
	        width: 600,
	        height: 400,
	        legend: { position: 'top', maxLines: 3 },
	        bar: { groupWidth: '55%' },
	        isStacked: switchResult,
	      };
	      var chart = new google.charts.Bar(document.getElementById('report'));

	       chart.draw(data, google.charts.Bar.convertOptions(options));
	       
	  }
}

//переключатель для формы заполнения интервала платежа
var switchResult = false ;
function switchInput(){
	conctrucktChart(lastData);
	 if(switchResult){
		 $("#intervalMonthSpend").prop( "disabled", true );
		 $("#intervalDaySpend").prop( "disabled", false );
	 }else{
		 $("#intervalMonthSpend").prop( "disabled", false );
		 $("#intervalDaySpend").prop( "disabled", true );
	 }
	 switchResult = !switchResult;
}





