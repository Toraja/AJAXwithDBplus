QUnit.module("getTables", {
	before: () => {
		window.url = /^http:\/\/.*:8080\/AJAXwithDBplus\/TableGetter/;
		window.MIMEType = { "Content-Type": "application/json" };
		window.responseText = '["t1","t2"]';
		window.ResponseHandler = {
			setTableNames: sinon.spy()
		};
		window.InputHandler = {
			displayError: sinon.spy()
		};
		window.confirm = sinon.stub();
	},
	beforeEach: () => {
		window.server = sinon.fakeServer.create();
	},
	afterEach: () => {
		window.ResponseHandler.setTableNames.reset();
		window.InputHandler.displayError.reset();
		window.server.restore();
	}
});
QUnit.test("Regular", assert => {
	RequestHandler.getTables();

	server.respondWith("GET", url, [200, MIMEType, responseText]);
	server.respond();

	assert.ok(ResponseHandler.setTableNames.calledOnce,
		"ResponseHandler.setTableNames was called " + ResponseHandler.setTableNames.callCount + " time(s)");
});
QUnit.test("Response status is not OK", assert => {
	RequestHandler.getTables();

	server.respondWith("GET", url, [202, MIMEType, responseText]);
	server.respond();

	assert.ok(ResponseHandler.setTableNames.notCalled,
		"ResponseHandler.setTableNames was called " + ResponseHandler.setTableNames.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});
QUnit.test("Server unavailable", assert => {
	RequestHandler.getTables();

	window.server.requests[0].error();

	assert.ok(ResponseHandler.setTableNames.notCalled,
		"ResponseHandler.setTableNames was called " + ResponseHandler.setTableNames.callCount + " time(s)");
	assert.ok(confirm.calledOnce, "confirm was called " + confirm.callCount + " time(s)");
});

QUnit.module("getColumns", {
	before: () => {
		window.url = /^http:\/\/.*:8080\/AJAXwithDBplus\/ColumnGetter/;
		window.MIMEType = { "Content-Type": "application/json" };
		window.responseText = "Normal response text";
		window.ResponseHandler = {
			listTableColumn: sinon.spy()
		};
	},
	beforeEach: () => {
		window.server = sinon.fakeServer.create();
		window.InputHandler = {
			getTableName: sinon.stub().callsFake(function(){return "table"}),
			displayError: sinon.spy(),
			hideFormArea: sinon.spy(),
			displayFormArea: sinon.spy()
		};
	},
	afterEach: () => {
		window.ResponseHandler.listTableColumn.reset();
		window.InputHandler.displayError.reset();
		window.server.restore();
	}
});
QUnit.test("Regular", assert => {
	RequestHandler.getColumns();

	server.respondWith("GET", url, [200, MIMEType, responseText]);
	server.respond();

	assert.ok(ResponseHandler.listTableColumn.calledOnce,
		"ResponseHandler.listTableColumn was called " + ResponseHandler.listTableColumn.callCount + " time(s)");
});
QUnit.test("table name is empty", assert => {
	InputHandler.getTableName = sinon.stub().callsFake(function(){return ""});
	RequestHandler.getColumns();

	assert.ok(ResponseHandler.listTableColumn.notCalled,
		"ResponseHandler.listTableColumn was called " + ResponseHandler.listTableColumn.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});
QUnit.test("Response status is not OK", assert => {
	RequestHandler.getColumns();

	server.respondWith("GET", url, [202, MIMEType, responseText]);
	server.respond();

	assert.ok(ResponseHandler.listTableColumn.notCalled,
		"ResponseHandler.listTableColumn was called " + ResponseHandler.listTableColumn.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});
QUnit.test("Server unavailable", assert => {
	RequestHandler.getColumns();

	window.server.requests[0].error();

	assert.ok(ResponseHandler.listTableColumn.notCalled,
		"ResponseHandler.listTableColumn was called " + ResponseHandler.listTableColumn.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});

QUnit.module("getData", {
	before: () => {
		window.url = /^http:\/\/.*:8080\/AJAXwithDBplus\/MatchedRowsRetriever/;
		window.MIMEType = { "Content-Type": "application/json" };
		window.responseText = "Normal response text";
		window.ResponseHandler = {
			displayResult: sinon.spy()
		};
	},
	beforeEach: () => {
		window.InputHandler = {
			displayError: sinon.spy(),
			createEntityFromInput: function(){
				return {
					one: "1",
					two: "2",
					three: "3"
				};
			},
			isAllFieldEmpty: function(obj){
				return false;
			}
		};
		window.server = sinon.fakeServer.create();
	},
	afterEach: () => {
		window.server.restore();
		window.InputHandler.displayError.reset();
		window.ResponseHandler.displayResult.reset();
	}
});
QUnit.test("Regular", assert => {
	RequestHandler.getData();

	server.respondWith("POST", url, [200, MIMEType, responseText]);
	server.respond();

	assert.ok(ResponseHandler.displayResult.calledOnce,
		"ResponseHandler.displayResult was called " + ResponseHandler.displayResult.callCount + " time(s)");
});
QUnit.test("All input is empty", assert => {
	InputHandler.isAllFieldEmpty = function(){return true};
	RequestHandler.getData();

	assert.ok(ResponseHandler.displayResult.notCalled,
		"ResponseHandler.displayResult was called " + ResponseHandler.displayResult.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});
QUnit.test("Response status is not OK", assert => {
	RequestHandler.getData();

	server.respondWith("POST", url, [202, MIMEType, responseText]);
	server.respond();

	assert.ok(ResponseHandler.displayResult.notCalled,
		"ResponseHandler.displayResult was called " + ResponseHandler.displayResult.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});
QUnit.test("Server unavailable", assert => {
	RequestHandler.getData();

	window.server.requests[0].error();

	assert.ok(ResponseHandler.displayResult.notCalled,
		"ResponseHandler.displayResult was called " + ResponseHandler.displayResult.callCount + " time(s)");
	assert.ok(InputHandler.displayError.calledOnce, "InputHandler.displayError was called " + InputHandler.displayError.callCount + " time(s)");
});
