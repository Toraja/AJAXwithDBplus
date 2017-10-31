/**
 * handles response from server
 */
var ResponseHandler = {
	/**
	 * Set table names
	 */
	setTableNames: function(tableNameJson){

		var tableArray = JSON.parse(tableNameJson);
		var select = document.getElementById("tableList");

		for (var i = 0; i < tableArray.length; i++) {
			var optionElm = document.createElement("option");
			optionElm.setAttribute("value", tableArray[i]);
			optionElm.innerHTML = tableArray[i];
			select.appendChild(optionElm);
		}

		document.getElementById('listColumns').removeAttribute('disabled');
	},

	/**
	 * Create list of textbox for each table column
	 *
	 * @param columnJson
	 * 			columns are passed as json from server
	 */
	listTableColumn: function(columnJson){
		InputHandler.displayFormArea(true);

		var columnArray = JSON.parse(columnJson);
		var form = document.getElementById("condition");
		form.innerHTML = "";

		for (var i = 0; i < columnArray.length; i++) {

			var columnName = columnArray[i];
			var entryId = "_" + columnName;	// prepend '_' to avoid collision

			// label
			var labelElm = document.createElement("label");
			labelElm.setAttribute("class", "formLabel");
			labelElm.setAttribute("for", entryId);
			var labelText = document.createTextNode(columnName + ": ");
			labelElm.appendChild(labelText);

			// textbox
			var inputElm = document.createElement("input");
			inputElm.setAttribute("type", "text");
			inputElm.setAttribute("name", columnName);
			inputElm.setAttribute("id", entryId);
			inputElm.setAttribute("class", "formData");
			inputElm.setAttribute("placeholder", columnName);

			// entry
			var pElm = document.createElement("p");
			pElm.setAttribute("class", "formEntry");
			pElm.appendChild(labelElm);
			pElm.appendChild(inputElm);

			// add to form
			form.appendChild(pElm);
		}
	},

	/**
	 * Display DB result nicely
	 *
	 * @param resultJson
	 * 			matched data is passed as json from server
	 */
	displayResult: function(resultJson){

		var resultArray = JSON.parse(resultJson);

		if(resultArray == null || resultArray.length == 0){
			InputHandler.displayError(Messages.noDataFound);
			return;
		}

		var dataArea = document.getElementById('dataArea');
		dataArea.innerHTML = "";

		var columnNames = Object.keys(resultArray[0]);

		// TABLE
		var table = document.createElement('table');
		table.setAttribute('id', 'format_table');
		dataArea.appendChild(table);

		// table header
		var header = document.createElement('tr');
		table.appendChild(header);
		for(var i = 0; i < columnNames.length; i++){
			var hVal = document.createTextNode(columnNames[i].toUpperCase());
			var th = document.createElement('th');
			th.appendChild(hVal);
			header.appendChild(th);
		}

		// table data
		for (var i = 0; i < resultArray.length; i++) {
			var record = resultArray[i];
			var row = document.createElement('tr');
			for (var j = 0; j < columnNames.length; j++){
				var entityKey = columnNames[j]
				var dVal = document.createTextNode(record[entityKey]);
				var td = document.createElement('td');
				td.appendChild(dVal);
				row.appendChild(td);
			}
			table.appendChild(row);
		}

		// CSV (textarea)
		var textarea = document.createElement('textarea');
		textarea.setAttribute('id', 'format_csv');
		textarea.setAttribute('readonly', 'true');
		dataArea.appendChild(textarea);
		var maxWidth = 0;

		// header
		var columnNames = Object.keys(resultArray[0]);
		var header = columnNames.join().toUpperCase();
		maxWidth = header.length;
		var text = header;

		// data
		for (var i = 0; i < resultArray.length; i++) {
			var valueArray = [];
			for (var j = 0; j < columnNames.length; j++){
				valueArray.push(resultArray[i][columnNames[j]]);
			}
			var row = valueArray.join();
			if (maxWidth < row.length) maxWidth = row.length
			text += "\n" + row;
		}
		textarea.value = text;
		// fit textarea to content
		textarea.rows = resultArray.length + 1;
		textarea.cols = maxWidth;


		// only display checked format
		InputHandler.switchResultFormat(document.querySelector('input[name="format"]:checked').value);
		InputHandler.displayResultArea(true);
	}
}
