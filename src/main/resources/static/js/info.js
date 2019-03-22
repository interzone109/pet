    $("#infoButton").on("click", function() { 
    var status =(this.title==="")?"show" :"hide";
    $('#collapseInfo').collapse(status);
    this.title= (status ==="show")?"activ":"";
    });
    
    /******************** sidebar function ****************************/
    $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
            $('#sidebar').toggleClass('active');
        });
    });
/******************** sidebar function ****************************/    
    
