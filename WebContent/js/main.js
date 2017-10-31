/**
 * main script
 */
RequestHandler.getTables();
InputHandler.resetSelection();

window.onload = function(){
	// add event listner
	var listColumns = document.getElementById("listColumns");
	var findEmployee = document.getElementById("findEmployee");
	listColumns.addEventListener("click", InputHandler.clearErrorMessage);
	listColumns.addEventListener("click", RequestHandler.getColumns);
	findEmployee.addEventListener("click", InputHandler.clearErrorMessage);
	findEmployee.addEventListener("click", RequestHandler.getData);

	var radios = document.getElementsByName("format");
	for (var i = 0; i < radios.length; i++){
		radios[i].addEventListener("change", function(){
			InputHandler.switchResultFormat(this.value);
		});
	}
};
