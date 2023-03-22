### SARO Solr Client 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/me.saro/solr-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/me.saro/solr-client)
[![GitHub license](https://img.shields.io/github/license/saro-lab/solr-client.svg)](https://github.com/saro-lab/solr-client/blob/master/LICENSE)

# QUICK START

## gradle kts
```
implementation('me.saro:solr-client:1.0')
```

## gradle
```
compile 'me.saro:solr-client:1.0'
```

## maven
``` xml
<dependency>
  <groupId>me.saro</groupId>
  <artifactId>solr-client</artifactId>
  <version>1.0</version>
</dependency>
```

## Kotlin Example
###  init
```
val url = "http://localhost:8983/solr"
val schema = "test-obj"
val solrClient = SolrClient.Builder().urls(listOf(url)).build()
val typeRef = object : TypeReference<List<TestObj>>() {}

class TestObj {
    // key
    var topicId = ""
    var subject = ""
    var tags = ""
    var content = ""
    var userName = ""
    var regDt = ""
}
```
### select
```
val query = SolrQuery().query("*:*").filterQuery("tags:abc").page(10, 0)
val res: SolrResult<*> = solrClient.select(schema, query)
val list: List<TestObj> = res.docs(typeRef)

println("select")
list.forEach(Consumer { e: TestObj? -> println(e) })
```
### insert or update (if key duplicate)
```
val testObj = TestObj()
testObj.topicId = "1"
testObj.subject = "test-subject"

solrClient.updateEntities(schema, listOf(testObj))
```
### delete (use key)
```
val key = "1"
solrClient.delete(schema, listOf(key))
```


## Java Example
###  init
```
String url = "http://localhost:8983/solr";
String schema = "test-obj";
SolrClient solrClient = new SolrClient.Builder().urls(List.of(url)).build();
TypeReference<List<TestObj>> typeRef = new TypeReference<List<TestObj>>(){};

class TestObj {
    // key
    public String topicId = "";
    public String subject = "";
    public String tags = "";
    public String content = "";
    public String userName = "";
    public String regDt = "";
}
```
### select
```
SolrQuery query = new SolrQuery().query("*:*").filterQuery("tags:abc").page(10, 0);
SolrResult res = solrClient.select(schema, query);
List<TestObj> list = res.docs(typeRef);

System.out.println("select");
list.forEach(e -> System.out.println(e));
```
### insert or update (if key duplicate)
```
Obj = new TestObj();
testObj.topicId = "1";
testObj.subject = "test-subject";

solrClient.updateEntities(schema, List.of(testObj));
```
### delete (use key)
```
String key = "1";
solrClient.delete(schema, List.of(key));
```


## A practical example of using Kotlin. 
- This example is an actual usage example from the website https://gs.saro.me.

![SolrService.java](https://raw.githubusercontent.com/saro-lab/solr-client/master/docs/SolrService.png)

![ForumDocumentRepositoryAdapter.java](https://raw.githubusercontent.com/saro-lab/solr-client/master/docs/ForumDocumentRepositoryAdapter.png)


## test code
- [Kotlin](https://github.com/saro-lab/solr-client/blob/master/src/test/kotlin/me/saro/solr/client/ktest/SolrClientTest.kt)
- [Java](https://github.com/saro-lab/solr-client/blob/master/src/test/java/me/saro/solr/client/jtest/SolrClientTest.java)

## repository
- https://repo1.maven.org/maven2/me/saro/solr-client/
- https://mvnrepository.com/artifact/me.saro/solr-client

## see
- [가리사니 개발자공간](https://gs.saro.me)
