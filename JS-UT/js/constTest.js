QUnit.module("Messages");
QUnit.test("Try to modify const", assert => {
	assert.ok(Object.isFrozen(Messages));
});

QUnit.module("Urls");
QUnit.test("getUrls", assert => {
	assert.equal(Urls.getUrl("columnGetter"), "http://localhost:8080/AJAXwithDBplus/ColumnGetter");
});
QUnit.test("Try to modify const", assert => {
	assert.ok(Object.isFrozen(Urls));
});

try{
	Urls.columnGetter = "abc";
}
catch(e){
	console.log(typeof e);
}
