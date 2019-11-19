var fourReportData ;
//создаем отчет об маржинальности продукта 
function createReportFour (report){
	$("#fourReport").remove();
	fourReportData = report;
	
	
	 var reportBlock = document.createElement('li');
	 reportBlock.className = "list-group-item";
	 reportBlock.id ="fourReport";
	 var body ="";
		var headTable = "<h4>Маржинальность продукта "+report.dateStart+" по "+report.dateEnd+"</h4><table class=\"table table-hover\">" 
		+"<thead><tr><th>Магазин</th><th>Наименование</th><th>Цена продажи</th><th>Себестоимость</th><th>Маржинальность</th> </tr></thead><tbody id =\"fourReportBody\">";
		
		 report.compositeProductReportData.forEach(row => {
			 var maginalityr= parseFloat(row.totalSumm/100) - parseFloat(row.sellQuantite/100);
			 body +="<tr><th>"+row.group+"</th>"
			 +"<th>"+row.name+"</th>"
			 +"<th>"+displayProductPrice(row.totalSumm)+" грн</th>"
			 +"<th>"+displayProductPrice(row.sellQuantite)+" грн</th>"
			+ "<th>"+maginalityr+" грн</th></tr>" ;
		 });

		
		var footer = "</tbody></table>"
		 +"<div class=\"col\"> <button class=\"btn btn-danger\"   onclick=\"cleanFourReport()\"  title=\"Очистить\"> <i class=\"fas fa-eraser\"></i></button> Очистить отчет \"Маржинальность продукта\"</li>" ;
		 reportBlock.innerHTML =  headTable+body+footer;
		 $("#spiners").hide();
		 $("#reportLsit").append(reportBlock);
		
}

function cleanFourReport(){
	$("#fourReport").remove();
}

