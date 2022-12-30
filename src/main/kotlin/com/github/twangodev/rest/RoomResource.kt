package com.github.twangodev.rest

import com.github.twangodev.domain.Room
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/rooms")
class RoomResource {

    @GET
    fun getLessons() : Response {
        return Response.ok(Room.listAll()).build()
    }

    @POST
    @Transactional
    fun createRoom(room: Room) : Response {
        room.persist()
        return Response.created(null).build()
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    fun deleteRoom(@PathParam("id") id: Long) : Response {
        Room.deleteById(id)
        return Response.noContent().build()
    }

}