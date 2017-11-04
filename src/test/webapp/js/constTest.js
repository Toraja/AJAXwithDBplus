QUnit.module("Messages");
QUnit.test("Try to modify const", assert => {
	assert.ok(Object.isFrozen(Messages));
});

QUnit.module("Urls");
QUnit.test("getUrls", assert => {
	assert.ok(/http:\/\/.*:8080\/AJAXwithDBplus\/ColumnGetter/.test(Urls.getUrl("columnGetter")));
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
