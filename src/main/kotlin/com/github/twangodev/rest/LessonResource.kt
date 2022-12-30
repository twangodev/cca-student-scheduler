package com.github.twangodev.rest

import com.github.twangodev.domain.Lesson
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/lessons")
class LessonResource {

    @GET
    fun getLessons(): Response {
        return Response.ok(Lesson.listAll()).build()
    }

    @POST
    @Transactional
    fun createLesson(lesson: Lesson): Response {
        lesson.persist()
        return Response.created(null).build()
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    fun deleteLesson(@PathParam("id") id: Long): Response {
        Lesson.deleteById(id)
        return Response.noContent().build()
    }

}