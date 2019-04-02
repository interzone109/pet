
var connectUrl ='http://localhost:8080';
//request('GET', connectUrl+'/user/composites', fillStoreTable);//запрос на получения списка ккомпозитных товаров

function showProductPrice(storeId){
	$("#collapseStoreBody").collapse("hide");
	$("#collapseProductStoreBody").collapse("show");
	$("#compositeStoreId").text(storeId);
	
	
	
	request('GET', connectUrl+'/user/stores/assortment/'+storeId, fillStoreTable);//запрос на получения списка ккомпозитных товаров
}

function postNewProductStoreForm(){
	
}
