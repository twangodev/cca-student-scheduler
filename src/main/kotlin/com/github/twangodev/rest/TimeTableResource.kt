package com.github.twangodev.rest

import com.github.twangodev.domain.Lesson
import com.github.twangodev.domain.Room
import com.github.twangodev.domain.TimeTable
import com.github.twangodev.domain.Timeslot
import org.optaplanner.core.api.score.ScoreManager
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
import org.optaplanner.core.api.solver.SolverManager
import org.optaplanner.core.api.solver.SolverStatus
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/time-table")
class TimeTableResource {

    @Inject lateinit var solverManager: SolverManager<TimeTable, Long>
    @Inject lateinit var scoreManager: ScoreManager<TimeTable, HardSoftScore>

    @GET
    fun getTimeTable() : Response {
        val solution = findById(TIME_TABLE_ID)
        val solverStatus = getSolverStatus()
        scoreManager.updateScore(solution)
        solution.solverStatus = solverStatus
        return Response.ok(solution).build()
    }

    fun getSolverStatus() : SolverStatus{
        return solverManager.getSolverStatus(TIME_TABLE_ID)
    }

    @POST
    @Path("/solve")
    fun solve() {
        solverManager.solveAndListen(TIME_TABLE_ID, { id: Long -> findById(id) }) { timeTable: TimeTable -> onSolvingEnded(timeTable) }
    }

    @POST
    @Path("/stop-solving")
    fun stopSolving() {
        solverManager.terminateEarly(TIME_TABLE_ID)
    }

    @Transactional
    protected fun onSolvingEnded(timeTable: TimeTable) {
        for (lesson in timeTable.lessonList) {
            val attachedLesson = Lesson.findById(lesson.id!!)
            attachedLesson!!.timeslot = lesson.timeslot
            attachedLesson.room = lesson.room
        }
    }

    @Transactional
    fun findById(id: Long?): TimeTable {
        return TimeTable(Timeslot.listAll(), Room.listAll(), Lesson.listAll())
    }

    companion object {
        private const val TIME_TABLE_ID = 1L
    }
}