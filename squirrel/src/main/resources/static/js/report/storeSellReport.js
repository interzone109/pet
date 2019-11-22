var threeReportData ;
//строим третий отчет
function createReportThree(report){
	$("#threeReport").remove();
	threeReportData= report;
	var reportBlock = document.createElement('li');
	 reportBlock.className = "list-group-item";
	 reportBlock.id ="threeReport";
	 var body ="";
	 var headTable = "<h4>Доля выручки магазинов с "+report.dateStart+" по "+report.dateEnd+"</h4><table class=\"table table-hover\">" 
		+"<thead><tr><th>Магазин</th><th>Период</th><th>Сумма</th></tr></thead><tbody id =\"threeReportBody\">";
		
		 report.invoiceData.forEach(row => {
			 body +="<tr><th>"+row.dateEnd+"</th>"
			 +"<th>"+converDate(row.dateStart.split(":")[0])+"-"+converDate(row.dateStart.split(":")[1])+"</th>"
			 +"<th>"+displayProductPrice(row.cashBox) +" грн</th></tr>";

		 });
	
		 var footer = "</tbody></table><div class=\"form-row\" id=\"threeReportFooter\">"
			 +"<div class=\"col-5\"><button class=\"btn btn-danger\" onclick=\"cleanReportThree()\"><i class=\"fas fa-eraser\"></i> Удалисть отчет</button> </div>"
			 +"<div class=\"col-3\"> <button class=\"btn btn-info\"  onclick=\"drawChartThree()\" title=\"построить график\"><i class=\"fas fa-chart-line\"></i> Построить график </button></div>"
			 +"<div class=\"line\"></div><div id=\"reportThreeChart\"></div><div id='reportFooterThree'></div> </li>";

			 reportBlock.innerHTML =  headTable+body+footer;
			 $("#spiners").hide();
			 $("#reportLsit").append(reportBlock);
 
}

function drawChartThree(){
	var array = [];
	array[0] = ['Адрес', 'Сумма'];
	var arrayIter =1 ;
	threeReportData.invoiceData.forEach(row => {
		array[arrayIter]= [row.dateEnd ,(row.cashBox/100)];
		arrayIter++;
	});
	
	 google.charts.load('current', {'packages':['corechart']});
     google.charts.setOnLoadCallback(drawChart);

     function drawChart() {

       var data = google.visualization.arrayToDataTable(array);

       var options = {
    	width: 600,
   	    height: 400,
         title: 'Доля магазинов в период ' + threeReportData.dateStart+"-"+threeReportData.dateEnd
       };

       var chart = new google.visualization.PieChart(document.getElementById('reportThreeChart'));

       chart.draw(data, options);
     }
}

//чистим весь отчет
function cleanReportThree(){
	$("#threeReport").remove();
}