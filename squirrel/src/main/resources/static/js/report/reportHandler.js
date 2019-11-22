var connectUrl = "http://localhost:8080" ;
$("#spiners").hide();
//устанавливаем период по умолчанию
function fillDate(){
	var today = new Date();
	var tomorrow = new Date();
	tomorrow.setDate(today.getDate()+1);
	
	var dd = String(today.getDate()).padStart(2, '0');
	var mm = String(today.getMonth() + 1).padStart(2, '0');
	var yyyy = today.getFullYear();

	var period = String(today.getDate()).padStart(2, '0')
	+ '.' + String(today.getMonth() + 1).padStart(2, '0')
	+ '.' + today.getFullYear()+"-"
	+String(tomorrow.getDate()).padStart(2, '0')
	+ '.' + String(tomorrow.getMonth() + 1).padStart(2, '0')
	+ '.' + tomorrow.getFullYear();
	$("#dataRangeValue").val(period);//для поиска
	
}
fillDate();



function find(){
	$("#spiners").show();
	var date = $("#dataRangeValue").val().split("-");
	var report = JSON.stringify({
		"dateStart":date[0],
		"dateEnd":date[1]
	});
	var reportNum =$("#reportSelector").val();
	if(reportNum==1){
			request('POST', connectUrl+'/user/report/data/1',createReportOne ,report);
	}
	else if(reportNum==2){
			request('POST', connectUrl+'/user/report/data/2',createReportTwo ,report);
	}
	else if(reportNum==3){
			request('POST', connectUrl+'/user/report/data/3',createReportThree ,report);
	}
	else if(reportNum==4){
			request('POST', connectUrl+'/user/report/data/4',createReportFour ,report);
	}

}

function deleteReports(){
	$("#firstReport").remove();
	$("#twoReport").remove();
	$("#threeReport").remove();
	$("#fourReport").remove();
}









