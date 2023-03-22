package me.saro.solr.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper

class SolrResult<T>(
    val httpStatus: Int,
    val httpResponseBodyString: String
) {
    val success: Boolean = httpStatus == 200
    private var _body: Map<String, Any>? = null
    private var _responseHeader: Map<String, Any>? = null
    private var _response: Map<String, Any>? = null

    companion object {
        val objectMapper = ObjectMapper()
        val typeRefMap = object: TypeReference<Map<String, Any>>() {}
    }

    val body: Map<String, Any>
        get() = _body ?: run { objectMapper.readValue(httpResponseBodyString, typeRefMap).also { _body = it } }

    val responseHeader: Map<String, Any>
        get() = _responseHeader ?: run { (body["responseHeader"] as Map<String, Any>).also { _responseHeader = it } }

    val response: Map<String, Any>
        get() = _response ?: run { (body["response"] as Map<String, Any>).also { _response = it } }

    val numFound: Long
        get() = (response["numFound"] as Int).toLong()
        //get() = ((_response?.get("numFound") ?: 0L) as Long)

    val docs: List<Map<String, Any>>
        get() = ((response["docs"] ?: listOf<Map<String, Any>>()) as List<Map<String, Any>>)

    fun <T> docs(valueTypeRef: TypeReference<List<T>>): List<T> =
        objectMapper.convertValue(docs, valueTypeRef)

    override fun toString(): String = httpResponseBodyString
}
