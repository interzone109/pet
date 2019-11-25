var connectUrl = "http://localhost:8080" ;

var today = new Date();
var lastMonth = new Date();
lastMonth.setMonth(today.getMonth()-1);

var reportPie = JSON.stringify({
	"dateEnd":String(today.getDate()).padStart(2, '0')+ '.' + String(today.getMonth() + 1).padStart(2, '0') + '.' + today.getFullYear(),
	"dateStart":String(lastMonth.getDate()).padStart(2, '0')+ '.' + String(lastMonth.getMonth() + 1).padStart(2, '0') + '.' + lastMonth.getFullYear()
});
var reportProfit = JSON.stringify({
	"dateEnd":String(today.getDate()).padStart(2, '0')+ '.' + String(today.getMonth() + 1).padStart(2, '0') + '.' + today.getFullYear(),
	"dateStart":String(today.getDate()).padStart(2, '0')+ '.' + String(today.getMonth() + 1).padStart(2, '0') + '.' + today.getFullYear()
});


request('POST', connectUrl+'/user/report/data/2',showReportProfit ,reportProfit);
function showReportProfit (reportTwoData){
	var array = [];
	array[0] = [ 'Магазины', 'ОД', 'Закупка','Зарплаты', 'Прибыль' ];
	var arrayIter = 1 ;
	reportTwoData.invoiceData.forEach(row => {
		 var profit= parseFloat(row.cashBox/100) - parseFloat(row.sellQuantity/100) - parseFloat(row.cashBoxStartDay/100) - parseFloat(row.orderQuantity/100);
			array[arrayIter]= [row.dateEnd ,displayProductPrice(row.orderQuantity) , displayProductPrice(row.sellQuantity)
			, displayProductPrice(row.cashBoxStartDay) , profit];
		arrayIter++;
	});
	  google.charts.load('current', {'packages':['bar']});
	   google.charts.setOnLoadCallback(drawChart);
	   

	     function drawChart() {
	    	 var data = google.visualization.arrayToDataTable(array);

	      var options = {
	        width: 800,
	        height: 400,
	        legend: { position: 'top', maxLines: 3 },
	        bar: { groupWidth: '55%' },
	        isStacked: false,
	      };
	      var chart = new google.charts.Bar(document.getElementById('reportProfit'));

	       chart.draw(data, google.charts.Bar.convertOptions(options));
	       $("#reportProfitTitle").text("Продажи на "+String(today.getDate()).padStart(2, '0')+ '.' + String(today.getMonth() + 1).padStart(2, '0') + '.' + today.getFullYear());
	  }
}
//request('POST', connectUrl+'/user/report/data/3',showReportThree ,reportPie);
function showReportThree(reportThreeData){
	
}


$("#spiners").hide();