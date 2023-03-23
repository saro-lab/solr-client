package me.saro.solr.client

import java.net.URLEncoder

class SolrQuery(
    private val params: StringBuilder = StringBuilder(200)
) {
    companion object {
        private val escapeRegex = Regex("[+\\-&|!(){}\\[\\]^~*?:/]")
        @JvmStatic
        fun escape(originText: String) = escapeRegex.replace(originText, "\\$1")
    }

    fun query(query: String) =
        encodeParam("q", URLEncoder.encode(query, Charsets.UTF_8))

    fun filterQuery(filterQuery: String) =
        encodeParam("fq", URLEncoder.encode(filterQuery, Charsets.UTF_8))

    fun queryOperatorMode(and: Boolean) =
        encodeParam("q.op", if (and) "AND" else "OR")

    fun fieldList(fieldList: String) =
        encodeParam("fl", URLEncoder.encode(fieldList, Charsets.UTF_8))

    fun page(unit: Int, page: Int) =
        encodeParam("start", (unit * page).toString())
            .encodeParam("rows", unit.toString())

    fun param(key: String, value: String) =
        encodeParam(URLEncoder.encode(key, Charsets.UTF_8), URLEncoder.encode(value, Charsets.UTF_8))

    fun indent(indent: Boolean) =
        encodeParam("indent", if (indent) "true" else "false")

    private fun encodeParam(key: String, value: String) = apply {
        params.append('&').append(key).append('=').append(value)
    }

    override fun toString(): String =
        params.takeIf { params.length > 1 }?.substring(1) ?: ""
}
