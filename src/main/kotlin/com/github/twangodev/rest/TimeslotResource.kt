package com.github.twangodev.rest

import com.github.twangodev.domain.Timeslot
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/timeslots")
class TimeslotResource {

    @GET
    @Path("/{day}")
    fun getTimeslotsByDay(@PathParam("day") dayString: String): Response {
        val dayOfWeek = DayOfWeek.valueOf(dayString.uppercase())
        return Response.ok(Timeslot.findByDayOfWeekSorted(dayOfWeek)).build()
    }

    data class IncomingTimeslot(
        val name: String,
        val description: String,
        val daysOfWeek: List<DayOfWeek>,
        val startTime: LocalTime,
        val endTime: LocalTime
    )

    @POST
    @Transactional
    fun createTimeslot(incomingTimeslots: IncomingTimeslot): Response {

        val badRequest = ErrorBuilder()
        if (incomingTimeslots.name.isBlank())

            badRequest.add("NAME", "Name cannot be blank")
        if (incomingTimeslots.daysOfWeek.isEmpty())
            badRequest.add("MULTI_DAY_SELECTOR", "At least one day of week must be specified")
        if (incomingTimeslots.startTime.isAfter(incomingTimeslots.endTime))
            badRequest.add("START_TIME", "Start time must be before end time")
        if (!badRequest.isEmpty()) return badRequest.buildToResponse(400)

        val conflicts = ErrorBuilder()
        incomingTimeslots.daysOfWeek.forEach {
            val dayConflicts = Timeslot.findConflicts(it, incomingTimeslots.startTime, incomingTimeslots.endTime)
            if (dayConflicts.isNotEmpty())
                conflicts.add("CONFLICT", "Conflicts with existing timeslots", dayConflicts)
        }
        if (!conflicts.isEmpty()) return conflicts.buildToResponse(409)

        incomingTimeslots.daysOfWeek.forEach {day ->
            incomingTimeslots.let {
                Timeslot(it.name, it.description, day, it.startTime, it.endTime).persist()
            }
        }

        return Response.created(null).build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun deleteTimeslot(@PathParam("id") id: Long): Response {
        Timeslot.deleteById(id)
        return Response.noContent().build()
    }

}