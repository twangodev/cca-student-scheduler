package com.github.twangodev.rest

import com.github.twangodev.domain.Teacher
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/teachers")
class TeacherResource {

    @GET
    fun getTeachers() : Response {
        return Response.ok(Teacher.listAll().sortedBy { it.name }).build()
    }

    @POST
    @Transactional
    fun addTeacher(teacher: Teacher) : Response {
        teacher.persist()
        return Response.ok(teacher).build()
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    fun deleteTeacher(@PathParam("id") id: Long) : Response {
        Teacher.findById(id)?.delete()
        return Response.ok().build()
    }

}