package com.github.twangodev.rest

import javax.ws.rs.core.Response

data class ErrorBuilder(
    private var errors: List<Error>
) {

    constructor() : this(emptyList())

    class Error(
        val title: String,
        val description: String,
        val data: Any? = null
    )

    fun add(title: String, description: String) {
        errors = errors + Error(title, description)
    }

    fun add(title: String, description: String, data: Any) {
        errors = errors + Error(title, description, data)
    }

    fun buildToResponse(status: Int): Response {
        return Response.status(status).entity(errors).build()
    }

    fun isEmpty() = errors.isEmpty()

}