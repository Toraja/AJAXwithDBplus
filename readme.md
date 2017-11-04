## AJAXwithDBplus

### URL
Local: http://localhost:8080/AJAXwithDBplus  
VMware: http://192.168.153.100:8080/AJAXwithDBplus  

### Design
#### Parts
* Drop-down list to select table name  
* Button to load columns of the selected table  
* Area to display table columns with textbox  
* Button to load the data matching the condition  
  (This button is hidden until the condition is loaded)  
* Area to display the result  
  Result is displayed in table or as CSV  
  (This area is hidden until the condition is loaded)  
* Radio button to switch the display format of the result  
#### Actions
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

#### Interface
* Content-Type is application/json for all responses and request for matching data  
* Key of the parameter for loading column names is 'table'  
* Parameter for querying matching data is converted into Entity class in servlet classes  
* Empty result is handled at client side.  

### Test
#### UT
* Java  
  On eclipse, *Run As* -> *Maven test*  
  DB server must not be running.
* JavaScript
  Open each test html on a browser.
#### IT
* Java
  On eclipse, *Run As* -> *Maven build* -> *AJAXwithDBplus-IT*  
  Servlet container and DB server must be runnng.
* JavaScript
  Open index.html on a browser.

### Packaging
* Run *Maven build* -> *AJAXwithDBplus-Prod*  
  war file is created under *target* directory.

### Issue
* Does not work on IE as Object.values() is not supported.  
* Does not work on Edge when opening local file rather than accessing the server. (Not a real issue)  
* On Edge, selectArea moves when page is loaded.  

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

#### Unused Classes
* StringNonNullEnforcer.java
* StringCapitaliser.java
* DBConnectionCreator.java
* AppProperties.java
* FileUtil.java (Used in AppProperties.java)

