//создаем отчет об оборачеваемых средствах 
function createReportOne (report){
	 $("#firstReport").remove();
	 var reportBlock = document.createElement('li');
	 reportBlock.className = "list-group-item";
	 reportBlock.id ="firstReport";
		var headTable = "<h4>Оборачиваемые средства с "+report.dateStart+" по "+report.dateEnd+"</h4><table class=\"table table-hover\">" 
		+"<thead><tr><th>Магазин</th><th>Наименование</th><th>Сумма</th><th>Количество</th> </tr></thead><tbody id =\"firstReportBody\">";
		 var body ="";
		 var footerRow ="";
		 var totalSumm = 0  ;
		 var summary = 0  ;
		 var currentStore ="";
		 report.productReportData.forEach(row => {
			 
			 if( currentStore !== row.description && currentStore !==""){ 
				 body += "<tr><th><b>"+ currentStore +"</b></th> <th><b>Итого</b></th><th><b title=\"итого на магазине\">"+  displayProductPrice(totalSumm) +" грн</b></th>" 
				 +"<th class=\"text-center\"><span class=\"badge badge-success\"><i class=\"fas fa-sort-amount-up\" title=\"скрыть\" onclick=\"hideStoreBody(this.id)\" id=\""+ currentStore 
				 +"\" style=\"width: 25px; height: 15px;\"></i></span></th></tr>";
				 currentStore= row.description;
				 totalSumm = 0  ;
				 }
			 
			 
			 body += "<tr class=\""+ row.description +"\"><td>"+ row.description +"</td> <td>"+ row.name +"</td><td>"+ displayProductPrice(row.rate) +" грн</td><td>"
			 + createMeasureProduct(row.quantity, row.measureProduct)+" "+displayProductMeasure(row.measureProduct,1)+"</td></tr>";
			 totalSumm+= row.rate;
			 if(currentStore ===""){
				 currentStore = row.description;
			 }
			 summary += row.rate;
		 });
		 
		 
		 body += "<tr><th><b>"+ currentStore +"</b> </th><th><b>Итого</b></th><th><b title=\"итого на магазине\">"+  displayProductPrice(totalSumm) +" грн</b></th>" 
		 +"<th class=\"text-center\"><span class=\"badge badge-success\"><i class=\"fas fa-sort-amount-up\" title=\"скрыть\" onclick=\"hideStoreBody(this.id)\" id=\""+ currentStore 
		 +"\" style=\"width: 25px; height: 15px;\"></i></span></th></tr>";
		 
		 
		 var footer = "</tbody><tfoot><tr><th>Оборачиваемые средства</th><th>Итого:</th><th id=\"circulatingAssetsId\">"+displayProductPrice(summary)+" грн</th>" 
		 +"<th class=\"text-center\"><span class=\"badge badge-info\"><i class=\"fas fa-sort-amount-up\" title=\"скрыть\" onclick=\"hideFirstReportBody()\" style=\"width: 25px; height: 15px;\"></i></span></th></tr></tfoot></table>"
		 +"<div class=\"col\"> <button class=\"btn btn-danger\"   onclick=\"deleteReportsCircular()\"  title=\"Очистить\"> <i class=\"fas fa-eraser\"></i></button> Очистить отчет \"Оборачиваемые средства\"</li>" ;
		 reportBlock.innerHTML =  headTable+body+footer;
		 $("#spiners").hide();
		 $("#reportLsit").append(reportBlock);
}

//скрываем тело отчеа оставляя только итоги
function hideFirstReportBody(){
	if($("#firstReportBody").is(":hidden")){
		$("#firstReportBody").show();
	}else{
		$("#firstReportBody").hide();
	}
}


//скрываем тело по названию магазина
function hideStoreBody(id){
	if($("tr[class='"+id+"']").is(":hidden")){
	 $("tr[class='"+id+"']").show();
	}else{
		 $("tr[class='"+id+"']").hide();
	}


}
// удаляем отчет об оборачиваемых средствах
function deleteReportsCircular(){ $("#firstReport").remove();}








