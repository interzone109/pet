$(document).ready(function(){
  $("#searchOnPageTable").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#partnerTable tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});