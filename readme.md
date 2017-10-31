# AJAXwithDBplus
<mark>TODO</mark> <span style="color: green"></span>  

* Maven auto test
* Maven switch config depending on the type of test
  How to change URL in JS files?

* IT (Use Mysql and Tomcat on local machine)
    * js  
      &#9745; main.js  
      &#9745; InputHandler.js  
      &#9745; RequestHandler.js  
      &#9745; ResponseHandler.js  
      &#9745; const.js  
    * java  
      &#9745; TableGetter.java  
      &#9745; ColumnGetter.java  
      &#9745; MatchedRowsRetriever.java  
      &#9745; DatabaseHandler.java  
      &#9746; Strings.java  
      &#9746; DBQueryDeserialiser.java  

* ST (Use Mysql and Tomcat on vmware)
    * js  
      &#9744; main.js  
      &#9744; InputHandler.js  
      &#9744; RequestHandler.js  
      &#9744; ResponseHandler.js  
      &#9745; const.js  
    * java  
      &#9744; TableGetter.java  
      &#9744; ColumnGetter.java  
      &#9744; MatchedRowsRetriever.java  
      &#9744; DatabaseHandler.java  
      &#9746; Strings.java  
      &#9746; DBQueryDeserialiser.java  


### Note
#### Hibernate
**Hibernate How to**: [Maven and Hibernate tutorial][Maven and Hibernate tutorial]

**Procedure**
1. Reverse engineering
2. place generated xml under classpath (at least hibernate.cfg.xml)
3. Setup jars (See below)

**Required jar**: hibernate-core, hibernate-jpa-2.1-api  
\* org.hibernate.javax.persistence:hibernate-jpa-2.1-api and javax.persistence:persistence-api has
same package (javax.persistence) but different methods so the error below will occur if
persistence-api is used with hibernate-core.  
*java.lang.NoSuchMethodError: org.hibernate.engine.spi.SessionFactoryImplementor.getProperties()Ljava/util/Map;*

**Issues**  
<span style="color: red">org.hibernate.engine.jndi.JndiException: Error parsing JNDI name []</span>  
In hibernate.cfg.xml there is a session-factory tag and it has empty name attribute like below.  
`<session-factory name="">`  
Removing the name attribute will solve the issue.  
Reference: [Hibernate Tools: Error parsing JNDI name - Stack Overflow][Hibernate Tools: Error parsing JNDI name - Stack Overflow]

<span style="color: red">java.lang.IllegalArgumentException: org.hibernate.hql.internal.ast.QuerySyntaxException: emp2 is not mapped [FROM emp2 WHERE fname = Tom AND lname = Jones AND department = Accounting]</span>  
A *mapping* tag for the class needs to be added.  
`<mapping resource="Emp2.hbm.xml"></mapping>`  
Table name in FROM clause is case-sensitive and must match the Class name (not table name).  

<!-- Reference -->
[Maven and Hibernate tutorial]:http://www.mastertheboss.com/jboss-frameworks/maven-tutorials/maven-hibernate-jpa/maven-and-hibernate-4-tutorial
[Hibernate Tools: Error parsing JNDI name - Stack Overflow]:https://stackoverflow.com/questions/10552362/hibernate-tools-error-parsing-jndi-name

#### Tomcat
**Credential for https connection**
Reference: [How to configure Tomcat to support SSL or https][How to configure Tomcat to support SSL or https]  
Password: ajaxwithdbplus  
Name: Tom Jones  
Other info: Unknown  
--> https connection did not work after all. Because SSL lacking?

<!-- Reference -->
[How to configure Tomcat to support SSL or https]:https://www.mkyong.com/tomcat/how-to-configure-tomcat-to-support-ssl-or-https/

#### Unused Classes
* StringNonNullEnforcer.java
* StringCapitaliser.java
* DBConnectionCreator.java
* AppProperties.java
* FileUtil.java (Used in AppProperties.java)

## Flow
### Parts
* Drop-down list to select table name  
* Button to load columns of the selected table  
* Area to display table columns with textbox  
* Button to load the data matching the condition  
  (This button is hidden until the condition is loaded)  
* Area to display the result  
  Result is display in table or as CSV  
  (This area is hidden until the condition is loaded)  
* Radio button to switch the display format of the result  
### Actions
**On page load**  
1. Access DB and get the list of tables  
2. Set the list to drop-down list  

**Load button**  
1. Get the name of columns of the table selected in drop-down list  
2. Display column names alongside textbox in table tag  

**Retrieve button**  
1. Get the value of each textbox  
2. Query DB with the values  
3. Put text in the TextArea as CSV  
4. One for GET and another for POST

### Interface
* Content-Type is application/json for all responses and request for matching data  
* Key of the parameter for loading column names is 'table'  
* Parameter for querying matching data is converted into Entity class in servlet classes  
* Empty result is handled at client side.  

## Issue
* Does not work on IE as Object.values() is not supported.  
* Does not work on Edge when opening local file rather than accessing the server. (Not a real issue)  
* On Edge, selectArea moves when page is loaded.  

