/**
 * Define messages
 */
const Messages = {
		somethingWrong: "Something wrong with this site. \nComplain to the customer service if you'd like.",

		reloadPage: "Failed to load contents. Reload the page?",

		serverUnavailable: "Unable to connect to server.",

		enterOneField: "Please enter at least one field.",

		selectTableName: "Please select a table name from the list.",

		noDataFound: "No data was found."
}
Object.freeze(Messages); // <- this makes an object immutalbe


/**
 * Define URLs
 */
const Urls = {
		root: "http://${ap.host}:8080/AJAXwithDBplus",

		tableGetter: "/TableGetter",

		columnGetter: "/ColumnGetter",

		matchedRowsRetriever: "/MatchedRowsRetriever",

		getUrl: function(sub){
			return this.root + this[sub];
		}
}
Object.freeze(Urls);
