/**
 * handles user input
 */
var InputHandler = {

	/**
	 * Get table name that use selected from drop down list
	 * @returns table name
	 */
	getTableName: function(){
		return document.getElementById('tableList').value;
	},

	resetSelection: function(){
		var select = document.getElementById("tableList");
		select.selectedIndex = 0;
		select.focus();
	},

	/**
	 * Check if all the properties of the object is empty
	 *
	 * @param obj an object to be checked
	 * @returns boolean whether all properties are empty
	 */
	isAllFieldEmpty: function(obj){
		var empty = true;
		var propertiesValueArray = Object.values(obj);

		for (var i = 0; i < propertiesValueArray.length; i++) {
			if(propertiesValueArray[i].length > 0){
				empty = false;
				break;
			}
		}

		return empty;
	},

	/**
	 * display error message to the result area
	 *
	 * @param mainMsg message to be written
	 * @param extraMsg message to be written if present
	 */
	displayError: function(mainMsg, extraMsg){
		var msg = mainMsg + (extraMsg == undefined ? "" : "\n\n" + extraMsg)
		var target = document.getElementById('errorArea');
		target.innerHTML = msg;
		target.setAttribute("class", "blink");

		setTimeout(function(){target.removeAttribute("class")}, 3500);
	},

	/**
	 * get all the input value in the form
	 *
	 * @returns An object with its field names being the column names and
	 * 			each value being the inputs.
	 */
	createEntityFromInput: function(){
		var form = document.getElementById('condition');
		var entity = new Object();

		for (var i = 0; i < form.length; i++) {
			var input = form[i];
			entity[input.name] = input.value;
		}

		return entity;
	},

	/**
	 * Hide and display the form area
	 */
	displayFormArea: function(toDisplay){
		var formArea = document.getElementById('formArea');
		if(toDisplay){
			formArea.removeAttribute('hidden');
		} else {
			formArea.setAttribute('hidden', 'true');
		}
	},

	/**
	 * Hide and display the result area
	 */
	displayResultArea: function(toDisplay){
		var displayArea = document.getElementById('resultArea');
		if(toDisplay){
			resultArea.removeAttribute('hidden');
		} else {
			resultArea.setAttribute('hidden', 'true');
		}
	},

	/**
	 * Clear error message
	 */
	clearErrorMessage: function(){
		document.getElementById('errorArea').innerHTML = "";
	},

	/**
	 * Switch the format of result
	 */
	switchResultFormat: function(radioValue){
		var formatedResults = document.getElementById("dataArea").children;
		for(i = 0; i < formatedResults.length; i++){
			formatedResults[i].setAttribute('hidden', 'true');
		}

		document.getElementById(radioValue).removeAttribute('hidden');
	}
}
