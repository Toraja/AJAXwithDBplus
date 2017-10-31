// ---------- test setup ----------

// This needs to be called as XHR must be stubbed before tested code creates one
window.server = sinon.fakeServer.create();
// This is required before sourcing main.js as the absence of these causes an error
window.ResponseHandler = {
	listTableColumn: sinon.spy()
}

// source test code
// The dynamically loaded script is run after this script has completed
// It seems that QUnit.test waits until window.onload completes
// so the below script gets run before test.
script = document.createElement("script");
script.setAttribute("src", "../../WebContent/main/js/main.js");
script.setAttribute("async", "false");	// this seems optional
document.body.appendChild(script);

// --------------------------------

QUnit.module("listTableColumn", {
	before: () => {
		window.correctUrl = "http://localhost:8080/AJAXwithDBplus/ColumnGetter";
		window.wrongUrl = "http://localhost:8080/AJAXwithDBplus/WrongAddress";
		window.MIMEType = { "Content-Type": "text/plain" };

		window.listTableColumnSpy = window.ResponseHandler.listTableColumn;
	},
	afterEach: () => {
		window.listTableColumnSpy.reset();
	}
});

// QUnit.test probably waits until window.onload completes
QUnit.test("regular", assert => {
	var responseText = "OK response";
	// To test locally, the path must includes the domain as well
	// window.server.respondWith("GET", "/AJAXwithDBplus/ColumnGetter", [200, MIMEType, responseText]);
	window.server.respondWith("GET", correctUrl, [200, MIMEType, responseText]);
	window.server.respond();	// calling respond() multiple times only does not make server response multiple times

	assert.ok(window.listTableColumnSpy.calledOnce, "listTableColumn was called once?");
	assert.ok(window.listTableColumnSpy.calledWithExactly(responseText),
		"listTableColumn was called with '" + responseText + "' ?");
	console.log("listTableColumn.callCount: " + window.listTableColumnSpy.callCount +
		", calledwith: " + window.listTableColumnSpy.args);
});

QUnit.test("Error response", assert => {
	var alertSpy = sinon.spy(window, "alert");
	var alertMsg = "Something wrong with this site. \nComplain to the customer service if you'd like."
	window.server.respondWith("GET", window.wrongUrl, [400, MIMEType, ""]);
	window.server.requests[0].open("GET", window.wrongUrl, true);
	window.server.requests[0].send();
	window.server.respond();

	assert.ok(alertSpy.calledWithExactly(alertMsg), "alert was called with '" + alertMsg + "' ? -> " + alertSpy.args);
	assert.ok(window.listTableColumnSpy.notCalled, "listTableColumn was called " + window.listTableColumnSpy.callCount + " times");
	console.log("listTableColumn.callCount: " + window.listTableColumnSpy.callCount);
});

// sinon.server.requests[0].error() does not work when reusing XHR
// So this test has to be the first to run
QUnit.skip("XHR.onerror", assert => {
	var confirmSpy = sinon.spy(window, 'confirm');
	var confirmMsg = "Failed to load contents. Reload the page?";
	window.server.requests[0].error();

	assert.ok(confirmSpy.calledWithExactly(confirmMsg), "confirm was called with '" + confirmMsg + "' ? -> " + confirmSpy.args);
	assert.ok(window.listTableColumnSpy.notCalled, "listTableColumn was called " + window.listTableColumnSpy.callCount + " times");
	console.log("listTableColumn.callCount: " + window.listTableColumnSpy.callCount);
});

// This is required before sourcing main.js as the absence of these causes an error
// sendInfo has to be spy() before it is attached to an element using addEventListener
window.RequestHandler = {
	sendInfo: sinon.spy()
}

QUnit.module("sendInfo", {
	afterEach: function(){
		window.RequestHandler.sendInfo.reset();
	}
});

QUnit.test("regular", assert => {
	// trigger test method
	document.getElementById("findEmployee").click();

	assert.ok(window.RequestHandler.sendInfo.calledOnce, "sendInfo was called once? -> " +
		window.RequestHandler.sendInfo.callCount);
});
