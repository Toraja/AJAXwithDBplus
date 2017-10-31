QUnit.module("setTableNames");
QUnit.test("Regular", assert => {
	var response = ["apple","banana","cherry"];
	ResponseHandler.setTableNames(JSON.stringify(response));

	var options = document.getElementById("tableList").children;

	for(var i = 0; i < response.length; i++){
		var expected = response[i];
		var optionTagName = options[i+1].nodeName;
		var optionText = options[i+1].innerHTML;
		var optionValue = options[i+1].value;

		assert.equal(optionTagName, 'OPTION');
		assert.equal(optionText, expected);
		assert.equal(optionValue, expected);
	}
});

QUnit.module("listTableColumn", {
	beforeEach: () => {
		window.InputHandler = {
			displayFormArea: sinon.spy()
		};
	}
});
QUnit.test("Display", assert => {
	var response = ["title","name","sex"];
	ResponseHandler.listTableColumn(JSON.stringify(response));

	assert.ok(InputHandler.displayFormArea.calledOnce);
});
QUnit.test("Regular", assert => {
	var response = ["title","name","sex"];
	ResponseHandler.listTableColumn(JSON.stringify(response));

	var columns = document.getElementById("condition").children;

	for(var i = 0; i < response.length; i++){
		var columnTagName = columns[i].nodeName;
		var label = columns[i].children[0];
		var input = columns[i].children[1];
		var labelTagName = label.nodeName;
		var labelFor = label.getAttribute('for');
		var inputTagName = input.nodeName;
		var inputType = input.type;
		var inputName = input.name;

		assert.equal(columnTagName, 'P');
		assert.equal(labelTagName, 'LABEL');
		assert.equal(labelFor, "_" + response[i]);
		assert.equal(inputTagName, 'INPUT');
		assert.equal(inputType, 'text');
		assert.equal(inputName, response[i]);
	}
});

QUnit.module("displayResult", {
	beforeEach: () => {
		window.InputHandler = {
			displayError: sinon.spy(),
			switchResultFormat: sinon.spy(),
			displayResultArea: sinon.spy()
		};
		window.response = [{"id":5,"fname":"Tom","lname":"Jones","department":"Accounting","city":"Dubai","hobby":"Fishing"},{"id":6,"fname":"Tom","lname":"Jones","department":"Accounting","city":"Paris","hobby":"Fishing"},{"id":7,"fname":"Tom","lname":"Jones","department":"Sales","city":"Dubai","hobby":"Fishing"},{"id":8,"fname":"Tom","lname":"Jones","department":"Sales","city":"Paris","hobby":"Fishing"}];
	}
});
// Details are checked by eye in a later test
QUnit.test("Regular - table", assert => {
	ResponseHandler.displayResult(JSON.stringify(response));

	// var table = document.getElementById('dataArea').getElementsByTagName('table')[0];
	var table = document.querySelector('#dataArea > table');
	var rowNum = table.rows.length;
	var headerColNum = table.rows[0].cells.length
	var dataColNum = table.rows[2].cells.length
	assert.equal(rowNum, response.length + 1);
	assert.equal(headerColNum, Object.keys(response[0]).length);
	assert.equal(dataColNum, Object.keys(response[1]).length);
});
QUnit.test("Regular - csv", assert => {
	ResponseHandler.displayResult(JSON.stringify(response));

	// var textarea = document.getElementById('dataArea').getElementsByTagName('textarea')[0];
	var textarea = document.querySelector('#dataArea > textarea');
	var actual = textarea.value;
	var expected = "ID,FNAME,LNAME,DEPARTMENT,CITY,HOBBY" + "\n"
		+ "5,Tom,Jones,Accounting,Dubai,Fishing" + "\n"
		+ "6,Tom,Jones,Accounting,Paris,Fishing" + "\n"
		+ "7,Tom,Jones,Sales,Dubai,Fishing" + "\n"
		+ "8,Tom,Jones,Sales,Paris,Fishing"
	assert.equal(actual, expected);
});
QUnit.test("No data", assert => {
	ResponseHandler.displayResult('[]');

	assert.ok(InputHandler.displayError.calledOnce);
});
QUnit.test("Null", assert => {
	ResponseHandler.displayResult('null');

	assert.ok(InputHandler.displayError.calledOnce);
});
