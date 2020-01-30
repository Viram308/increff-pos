
//INITIALIZATION CODE
function init(){
$('#startDate').datepicker({
	uiLibrary: 'bootstrap4',
	iconsLibrary: 'fontawesome',
    maxDate: function () {
   	   return $('#endDate').val();
   }
});
$('#endDate').datepicker({
      uiLibrary: 'bootstrap4',
            iconsLibrary: 'fontawesome',
            minDate: function () {
                return $('#startDate').val();
            }
});
        
}
$(document).ready(init);
