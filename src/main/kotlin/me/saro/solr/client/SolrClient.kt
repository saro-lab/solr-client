package me.saro.solr.client

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import kotlin.random.Random


class SolrClient(
    private val builder: Builder
) {

    private val urls = builder.urls
    private val urlsCount = urls.size
    private val oneUrl = urls.size == 1

    private val objectMapper = ObjectMapper()

    fun selectAsync(coreName: String, solrQuery: SolrQuery): CompletableFuture<SolrResult<String>> {
        val url = URI.create("$url/$coreName/select?$solrQuery")

        val request = HttpRequest.newBuilder()
            .uri(url)
            .GET()
            .build()

        return HttpClient.newHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { SolrResult(it.statusCode(), it.body()!!) }
    }

    fun select(coreName: String, solrQuery: SolrQuery): SolrResult<String> =
        selectAsync(coreName, solrQuery).get()

    fun updateRawAsync(coreName: String, requestBody: String): CompletableFuture<SolrResult<String>> {
        val url = URI.create("$url/$coreName/update?commit=true")

        val request = HttpRequest.newBuilder()
            .uri(url)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build()

        return HttpClient.newHttpClient()
            .sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply { SolrResult(it.statusCode(), it.body()!!) }
    }

    fun updateEntitiesAsync(coreName: String, entities: List<Any>): CompletableFuture<SolrResult<String>> =
        updateRawAsync(coreName, objectMapper.writeValueAsString(entities))

    fun updateEntities(coreName: String, entities: List<Any>): SolrResult<String> =
        updateEntitiesAsync(coreName, entities).get()

    fun deleteAsync(coreName: String, idList: List<String>): CompletableFuture<SolrResult<String>> =
        updateRawAsync(coreName, """{"delete":${objectMapper.writeValueAsString(idList)}}""")

    fun delete(coreName: String, idList: List<String>): SolrResult<String> =
        deleteAsync(coreName, idList).get()

    private val url: String get() {
        return urls[if (oneUrl) 0 else Random.nextInt(urlsCount)]
    }

    class Builder {
        internal var urls: List<String> = listOf()

        fun urls(urls: List<String>) = apply {
            val urlPattern = Regex("^https?://[\\w\\-.]+:\\d+/solr$")
            if (urls.isEmpty() || urls.any { !urlPattern.matches(it) }) {
                throw IllegalArgumentException("SolrClient: url must be 1 or more and have the correct format. Example) http://localhost:8983/solr")
            }
            this.urls = urls
        }

        fun build() = SolrClient(this)
    }
}
