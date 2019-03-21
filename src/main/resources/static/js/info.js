    $("#infoButton").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseInfo').collapse(status);
    this.title= (status ==="show")?"activ":"";
    });