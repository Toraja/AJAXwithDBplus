// Functions must be spied before they are attached to an element by addEventListener
window.RequestHandler = {
	getTables: sinon.spy(),
	getColumns: sinon.spy(),
	getData: sinon.spy()
};
window.InputHandler = {
	clearErrorMessage: sinon.spy(),
	switchResultFormat: sinon.spy(),
	resetSelection: sinon.spy()
}

// source test code
// The dynamically loaded script is run after this script has completed
// It seems that QUnit.test waits until window.onload completes
// so the below script gets run before test.
var script = document.createElement("script");
script.setAttribute("src", "../../WebContent/js/main.js");
script.setAttribute("async", "false");	// this seems optional
document.body.appendChild(script);


QUnit.module("init", {
	afterEach: function(){
		window.RequestHandler.getTables.reset();
		window.RequestHandler.getColumns.reset();
		window.RequestHandler.getData.reset();
		window.InputHandler.clearErrorMessage.reset();
		window.InputHandler.switchResultFormat.reset();
	}
});

QUnit.test("Start up", assert => {
	assert.ok(window.RequestHandler.getTables.calledOnce, "getTables was called once? -> " +
		window.RequestHandler.getTables.callCount);

	assert.ok(window.InputHandler.resetSelection.calledOnce, "resetSelection was called once? -> " +
		window.InputHandler.resetSelection.callCount);
});

QUnit.test("Click 'List' button", assert => {
	document.getElementById("listColumns").click();

	assert.ok(window.RequestHandler.getColumns.calledOnce, "getColumns was called once? -> " +
		window.RequestHandler.getColumns.callCount);
	assert.ok(window.InputHandler.clearErrorMessage.calledOnce, "clearErrorMessage was called once? -> " +
		window.InputHandler.clearErrorMessage.callCount);
});

QUnit.test("Click 'Find' button", assert => {
	document.getElementById("findEmployee").click();

	assert.ok(window.RequestHandler.getData.calledOnce, "getData was called once? -> " +
		window.RequestHandler.getData.callCount);
	assert.ok(window.InputHandler.clearErrorMessage.calledOnce, "clearErrorMessage was called once? -> " +
		window.InputHandler.clearErrorMessage.callCount);
});

QUnit.test("Click radio button", assert => {
document.querySelector('input[value="format_csv"]').click();
document.querySelector('input[value="format_table"]').click();

	assert.ok(window.InputHandler.switchResultFormat.calledTwice, "switchResultFormat was called twice? -> " +
		window.InputHandler.switchResultFormat.callCount);
	assert.ok(window.InputHandler.switchResultFormat.firstCall.calledWithExactly("format_csv"));
	assert.ok(window.InputHandler.switchResultFormat.secondCall.calledWithExactly("format_table"));
});
