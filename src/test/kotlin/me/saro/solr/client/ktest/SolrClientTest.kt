package me.saro.solr.client.ktest

import com.fasterxml.jackson.core.type.TypeReference
import me.saro.solr.client.SolrClient
import me.saro.solr.client.SolrQuery
import me.saro.solr.client.SolrResult
import org.junit.jupiter.api.Test
import java.util.function.Consumer

class SolrClientTest {
    private val url = "http://localhost:8983/solr"
    private val schema = "test-obj"
    private val solrClient = SolrClient.Builder().urls(listOf(url)).build()
    private val typeRef = object : TypeReference<List<TestObj>>() {}

    @Test
    fun test() {
//        update()
//        select()
//        delete()
    }

    fun select() {
        val query = SolrQuery().query("*:*").filterQuery("tags:abc").page(10, 0)
        val res: SolrResult<*> = solrClient.select(schema, query)
        val list: List<TestObj> = res.docs(typeRef)

        println("select")
        list.forEach(Consumer { e: TestObj? -> println(e) })
    }

    fun update() {
        val testObj = TestObj()
        testObj.topicId = "1"
        testObj.subject = "test-subject"

        // insert or update(if key duplicate)
        solrClient.updateEntities(schema, listOf(testObj))
    }

    fun delete() {
        // delete (use key)
        val key = "1"
        solrClient.delete(schema, listOf(key))
    }

    internal class TestObj {
        // key
        var topicId = ""
        var subject = ""
        var tags = ""
        var content = ""
        var userName = ""
        var regDt = ""
    }
}