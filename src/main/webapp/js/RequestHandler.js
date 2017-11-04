/**
 * handles user request
 */
var RequestHandler = {

	getTables: function(){
		// display error message if no result was obtained,
		// and ask user to reload the page.
		var url = Urls.getUrl("tableGetter");
		var request = new XMLHttpRequest();

		request.onload = function(){
			if (request.status != 200) {
				InputHandler.displayError(Messages.somethingWrong, request.responseText);
				return;
			}

			ResponseHandler.setTableNames(request.responseText);
		};
		request.onerror = function(){
			var toReload = confirm(Messages.reloadPage);
			if(toReload) location.reload();
		}
		request.open("GET", url, true);
		request.send();
	},

	getColumns: function(){

		window.tableNameToQuery = InputHandler.getTableName();
		if(!window.tableNameToQuery){
			InputHandler.displayError(Messages.selectTableName);
			return;
		}

		var url = Urls.getUrl("columnGetter");
		url += "?table=" + window.tableNameToQuery;
		var request = new XMLHttpRequest();

		request.onload = function(){
			if (request.status != 200) {
				InputHandler.displayFormArea(false);
				InputHandler.displayError(Messages.somethingWrong, request.responseText);
				return;
			}

			ResponseHandler.listTableColumn(request.responseText);
		};
		request.onerror = function(){
			InputHandler.displayFormArea(false);
			InputHandler.displayError(Messages.serverUnavailable);
		}
		request.open("GET", url, true);
		request.send();
	},

	getData: function(){
		var url = Urls.getUrl("matchedRowsRetriever");
		var entity = InputHandler.createEntityFromInput();	// store user input as an object

		var inputAllEmpty = InputHandler.isAllFieldEmpty(entity);
		if (inputAllEmpty) {
			InputHandler.displayError(Messages.enterOneField);
			return;
		}

		var queryObj = {
			tableName: window.tableNameToQuery,
			condition: entity
		};

		var request = new XMLHttpRequest();
		request.onload = function() {
			if(request.status!= 200){
				InputHandler.displayError(Messages.somethingWrong, request.responseText);
				return;
			}

			ResponseHandler.displayResult(request.responseText);
		}
		request.onerror = function(){
			InputHandler.displayError(Messages.serverUnavailable);
		}
		request.open("POST", url, true);
		request.send(JSON.stringify(queryObj));
	}
}
