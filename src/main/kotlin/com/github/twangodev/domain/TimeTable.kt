package com.github.twangodev.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.twangodev.HardSoftScoreSerializer
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty
import org.optaplanner.core.api.domain.solution.PlanningScore
import org.optaplanner.core.api.domain.solution.PlanningSolution
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
import org.optaplanner.core.api.solver.SolverStatus

@PlanningSolution
class TimeTable() {

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "timeslotRange")
    lateinit var timeslotList: List<Timeslot>

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "roomRange")
    lateinit var roomList: List<Room>

    @PlanningEntityCollectionProperty
    lateinit var lessonList: List<Lesson>

    @JsonSerialize(using = HardSoftScoreSerializer::class)
    @PlanningScore
    var hardSoftScore: HardSoftScore? = null

    var solverStatus: SolverStatus? = null

    constructor(timeslotList: List<Timeslot>, roomList: List<Room>, lessonList: List<Lesson>) : this() {
        this.timeslotList = timeslotList
        this.roomList = roomList
        this.lessonList = lessonList
    }

}