package me.saro.solr.client.jtest;

import com.fasterxml.jackson.core.type.TypeReference;
import me.saro.solr.client.SolrClient;
import me.saro.solr.client.SolrQuery;
import me.saro.solr.client.SolrResult;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SolrClientTest {
    private String url = "http://localhost:8983/solr";
    private String schema = "test-obj";
    private SolrClient solrClient = new SolrClient.Builder().urls(List.of(url)).build();
    private TypeReference<List<TestObj>> typeRef = new TypeReference<List<TestObj>>(){};

    @Test
    public void test() {
//        update();
//        select();
//        delete();
    }

    public void select() {
        SolrQuery query = new SolrQuery().query("*:*").filterQuery("tags:abc").page(10, 0);
        SolrResult res = solrClient.select(schema, query);
        List<TestObj> list = res.docs(typeRef);

        System.out.println("select");
        list.forEach(e -> System.out.println(e));
    }

    public void update() {
        TestObj testObj = new TestObj();
        testObj.topicId = "1";
        testObj.subject = "test-subject";

        // insert or update(if key duplicate)
        solrClient.updateEntities(schema, List.of(testObj));
    }

    public void delete() {
        // delete (use key)
        String key = "1";
        solrClient.delete(schema, List.of(key));
    }

    static class TestObj {
        // key
        public String topicId = "";
        public String subject = "";
        public String tags = "";
        public String content = "";
        public String userName = "";
        public String regDt = "";
    }
}
