QUnit.module("getTableName");
QUnit.test("No value", assert => {
	var select = document.getElementById("tableList");
	select.selectedIndex = 0;

	var value = InputHandler.getTableName();

	assert.equal("" ,value);
});
QUnit.test("Some value", assert => {
	var select = document.getElementById("tableList");
	select.selectedIndex = 1;

	var value = InputHandler.getTableName();

	assert.equal("table" ,value);
});

// skip resetSelection

QUnit.module("createEntityFromInput");
QUnit.test("All filled", assert => {
	var form = document.getElementById("condition");
	for(var i = 0; i < form.length; i++){
		form[i].value = i + 1;
	}

	var obj = {
		one: "1",
		two: "2",
		three: "3",
		four: "4",
		five: "5"
	}

	assert.deepEqual(InputHandler.createEntityFromInput(), obj);
});
QUnit.test("Some filled", assert => {
	document.getElementsByName("two")[0].value = "2";
	document.getElementsByName("three")[0].value = "3";

	var obj = {
		one: "",
		two: "2",
		three: "3",
		four: "",
		five: ""
	}

	assert.deepEqual(InputHandler.createEntityFromInput(), obj);
});

QUnit.test("All empty", assert => {
	var obj = {
		one: "",
		two: "",
		three: "",
		four: "",
		five: ""
	}

	assert.deepEqual(InputHandler.createEntityFromInput(), obj);
});


QUnit.module("isAllFieldEmpty");
QUnit.test("All filled", assert => {
	var obj = {
		one: "1",
		two: "2",
		three: "3",
		four: "4",
		five: "5"
	}

	assert.notOk(InputHandler.isAllFieldEmpty(obj));
});
QUnit.test("Some filled", assert => {
	var obj = {
		one: "",
		two: "2",
		three: "3",
		four: "4",
		five: ""
	}

	assert.notOk(InputHandler.isAllFieldEmpty(obj));
});
QUnit.test("All empty", assert => {
	var obj = {
		one: "",
		two: "",
		three: "",
		four: "",
		five: ""
	}

	assert.ok(InputHandler.isAllFieldEmpty(obj));
});

// Skip displayError()

// Skip hideFormArea()
