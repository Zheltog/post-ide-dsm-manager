package su.nsk.iae.post.dsm.manager.requests

import com.fasterxml.jackson.databind.ObjectMapper

data class DsmRequestBody(
    val id: String,
    val root: String,
    val fileName: String,
    val ast: String
) {

    fun toJsonString(): String = ObjectMapper()
        .readTree(
            """{"id": "$id", "root": "$root", "fileName": "$fileName", "ast": "$ast"}"""
        ).toPrettyString()
}