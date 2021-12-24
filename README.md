# Data Query

## Note:
To view the readme in http style view (like in github) use https://markdownlivepreview.com/

## Description:
Your task is to implement a service able to store and retrieve data. 
Data does not have to be persisted meaning storing it in-memory is enough. 
Data structure is pre-defined as following:

| Fields                                        | Example     | 
| -----------                                   | ----------- | 
| id - text <br>title - text<br> content - text <br>views - integer number<br>timestamp - integer number|{ "id": "first-post", "title": "My First Post","content": "Hello World!","views": 1,"timestamp": 1555832341 }
 
      
### API requirements
API consists of two end-points - one to store data and one to retrieve it.  

| Endpoint                                        | Example     | Response  | 
| -----------                                     | ----------- |-----------|
| Query<br> Takes query as input and returns matching entries. Query format is defined below.| query("EQUAL(id,"abc")") | [{"id":"abc","title":"Alphabet","content":"A, B, C, ...","views":1,"timestamp":1555832341}]     |
| Store<br> Take entity and stores it. ID must remain unique. If record with given ID already exists, it should be overwritten.| store({"id":"abc","title":"Alphabet","content":"A, B, C, ...","views":1,"timestamp":1555832341}) | |
 
### Query
| Operator                                        | Example     | 
| -----------                                     | ----------- |
| EQUAL(property,value) <br> Filters only values which have matching property value.| EQUAL(id,"first-post") <br> EQUAL(views,100)   |
| GREATER_THAN(property,value) <br> Filters only values for which property is greater than the given value. Valid only for number values.| GREATER_THAN(views,100) | 
| LESS_THAN(property,value) <br> Filters only values for which property is less than the given value. Valid only for number values.| LESS_THAN(views,100) |
| AND(a,b) <br> Filters only values for which both a and b are true.| AND(EQUAL(id,"first-post"),EQUAL(views,100)) | 
| OR(a,b) <br> Filters only values for which either a or b is true (or both).| OR(EQUAL(id,"first-post"),EQUAL(id,"second-post")) | 
| NOT(a) <br> Filters only values for which a is false.| NOT(EQUAL(id,"first-post")) |                                                                                                                            

## How do I start?

<i>DataQueryServiceImpl</i> is the entry point of the Data Query server. Start with running <i>DataQueryServerIT</i> and implementing the missing logic  

## Troubleshooting
If you get trouble to maven test the project, please try to reload all maven project, via maven plugin on your IDE. 
or from terminal: <br> `mvn clean install -U`

If you still get error running maven, in the format of:
`Transfer failed for <REPO_MAVEN_APACHE_URL> Received fatal alert: protocol_version`
Try to add the following lines to your pom.xml: <br>
` <properties>  
   <maven.compiler.source>1.8</maven.compiler.source> 
   <maven.compiler.target>1.8</maven.compiler.target>  
  </properties>`  