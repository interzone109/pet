



/******************** search function ****************************/
$(document).ready(function(){
  $("#searchOnPageTable").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#productTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
/******************** search function ****************************/

/******************** sidebar function ****************************/
$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});
/******************** sidebar function ****************************/  