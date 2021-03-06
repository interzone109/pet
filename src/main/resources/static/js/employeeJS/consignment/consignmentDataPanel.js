
// метод сохраняет навые данные о количестве и цене товара 
function saveConsignmentData( isUproved){
	//получаем список ид ингрииентов и разбиваем его на масив
	var ingridientIds = $("#consignmentIngridientsId").text().split(" ");
	var result = true ;
	var jsonData ="";
	//проходимся покаждому ид
	for(var i = 1; i< ingridientIds.length ;i++){//делаем валидацию полям с импутами
		var flag = formValidationNumber($("#ingridient_quantity_id_"+ingridientIds[i])  
				, $("#ingridient_price_id_"+ingridientIds[i]));
		//если один из результатов проверки будет false то общий результат примет false и будет не изменным
		if(flag === false){
			result = false;
		}else{
			//проверяем если данная позиция хранится не в ШТ то умножаем на 1000 для хранения
			// веса и литров в целых числах
			var quantity = $("#ingridient_quantity_id_"+ingridientIds[i]).val();
			if($("#ingridient_measureProduct_id_"+ingridientIds[i]).text()!=="шт"){
				quantity *=1000 ;
			}
			// пока результат валидации true формируем json для каждой итерации
			jsonData +=  "\""+ingridientIds[i]+"\":\"\:"+
			+quantity+"quantity"+
			($("#ingridient_price_id_"+ingridientIds[i]).val()*100)+"price\" ,";	
		}
	}
	// если валидация прошла успешно то отправляем данные
	if(result){
		var storeId = $("#consignmentTableStoreId").text();//получаем ид магазина
		var consignmentId =$("#consignmentCurrentId").text();//получаем ид накладной
		var data = "{"+jsonData.slice(0, -1)+"}";//формируем json (удаляем последнюю запятую в строке jsonData)
		
		//если у метода есть входной параметр  isUproved 
		// и он равен true то запрос будет направлен к урлу в котором поменяется статус накладной и она станет недоступной
		 if( isUproved === true){
			request("PUT",connectUrl + "/user/stores/consignment/"+storeId+"/"+consignmentId+"/uproved", updateConsignmentDataRowUproved ,data);
		 }else{
			 request("PUT",connectUrl + "/user/stores/consignment/"+storeId+"/"+consignmentId, updateConsignmentDataRow ,data);
		 }
	}
	
}


//метод обновляет цену, количество и итог
function updateConsignmentDataRow(responce, disabled){

		var ingridientIds = $("#consignmentIngridientsId").text().split(" ");// получаем список ид ингридиента
		for(var i = 1; i< ingridientIds.length ;i++){
			var id = ingridientIds[i] ;// создаем переменую с ид ингридиента
			//			!!!!!!!!  errore fix
			try{
			var dataStr = responce[id].split("quantity");// находим строку с кол и ценой по ид и разбиаем ее на две строки 
			}catch{
				return console.log("errore");
			}
			// создаем переменую из dataStr[0] разбив ее и формируем вывод в зависимости от меры хранения -шт, кг , л
			var quantity = createMeasureProduct (dataStr[0].split(":")[1],$("#ingridient_measureProduct_id_"+id).text());
			var price = displayProductPrice( dataStr[1].split("price")[0] );// создаем переменную с ценой 
			// устанавливаем переменные в поля
			$("#ingridient_quantity_id_"+id).val(quantity);
			$("#ingridient_price_id_"+id).val(price);
			// формируем общую цену для строки
			var totalPrice = $("#ingridient_measureProduct_id_"+id).text()==="шт"
				 ?dataStr[1].split("price")[0] * dataStr[0].split(":")[1]
				 :(dataStr[1].split("price")[0] * dataStr[0].split(":")[1])/1000 ;
			$("#ingridient_summ_id_"+id).text(displayProductPrice(totalPrice));
			 
			 if(disabled === true){
				 //блокиреум поля накладной
				$("#ingridient_quantity_id_"+id).attr("disabled","disabled");
				$("#ingridient_price_id_"+id).attr("disabled","disabled");
				var consignmentId =$("#consignmentCurrentId").text();
				$("#currentConsignmentStatusId").text("true");
				
			 }
		} 
		
		updateTotalSumm();
	
}
//метод обновляет все строки и блокирует их для изменений
function updateConsignmentDataRowUproved(responce){
	  updateConsignmentDataRow(responce, true);
	
}

//метод отправляет запрос на закрытие накладной 
function approvedDataConsignmentMethod(){
	
	$("#currentConsignmentStatusId").text("true");
	$("#consignment_state_id_"+$("#consignmentCurrentId").text()).text("проведено");
	
	saveConsignmentData(true);
	$("#saveDataConsignment").prop("disabled","disabled");
	$("#addDataConsignment").prop("disabled","disabled");
	$("#approvedDataConsignment").prop("disabled","disabled");
}

// модальное окно для добавления ингридиентов
function openAddIngridientModal(){
	$("#addIngridientToConsignmentModal").modal("show");
	var consignmentId= $("#consignmentCurrentId").text();	//получаем ид накладной
	var meta = $("#consignment_meta_id_"+consignmentId).prop("title");	//получаем мета данные накладной
	var id ;
	
	if(meta === "user")
	{
		var ids =$("#consignmentIngridientsId").text().split(" ");//получаем список ид ингрдиентов
		var jsonData = "";//формируем json 
		for(var i = 1; i< ids.length ;i++){
			jsonData += "\""+ids[i]+"\"" +" ,";
		}
		var data = "["+jsonData.slice(0, -1)+"]";	
		request("POST",connectUrl + "/user/products_list", addIngridientList ,data);
	}
	else if( meta === "userConsamption"){
		id = $("#consignmentTableStoreId").text();//получаем ид текущего магазина
		request("GET",connectUrl + "/user/stores/assortment/"+id+"/leftovers", addStoreIngridientList );
	}
	else if (meta.endsWith(":store"))//fix
	{
		id = meta.split(":store")[0];//получаем ид магазина отправителя
		//получаем остатки магаина отправителя
		request("GET",connectUrl + "/user/stores/assortment/"+id+"/leftovers", addStoreIngridientList );
	}
	else if(meta.endsWith(":partner"))
	{
		id = meta.split(":partner")[0];
		request("GET",connectUrl + "/user/partners/"+id+"/info", addPartnerIngridientList );
	}
}

function addStoreIngridientList(data){
	console.log(data);
	addIngridientList(data);
	
}

function addPartnerIngridientList(data){
	addIngridientList(data.productsModel);
}
// метод формирует селекты для добавления в накладную
var ingridientData ;
function addIngridientList(data){
	$("#ingridientSelectGroup").empty();
	ingridientData = data;
	if( Array.isArray(data) && data.length !== 0){
		var ids =$("#consignmentIngridientsId").text();//получаем id уже имеющихся в накладной позиций
		data.forEach(ingridient => {
			if(!ids.includes(" "+ingridient.id)){
			$("#ingridientSelectGroup").append("<option value=\""
					+ingridient.id+"\">"
					+ingridient.name+"</option>");
			}
		});
}
}
// метод добавляем ингридиенты к накладной
function addSelectValueToConsingment(){
	var addIngridient =[];
	 while($("#ingridientSelectGroup option:selected").val() !== undefined){// находим выбранный елемент
		 var id = $("#ingridientSelectGroup option:selected").val();// получаем id ингридиента
		 var ingridient = ingridientData.find(element => element.id ==id);//получаем данные по id
		 ingridient.description = "0";
		 ingridient.propertiesProduct = "0";
		 ingridient.partner = "0";
		 addIngridient.push(ingridient);//добавляем в масив
		
		 $("#ingridientSelectGroup  option[value="+id+"]").remove();
	 }//отображаем данные из масива
	 showConsignmentDataRow(addIngridient);
}

