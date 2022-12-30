package com.github.twangodev.domain

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.time.DayOfWeek
import java.time.LocalTime
import javax.persistence.Entity

@Entity
class Timeslot() : PanacheEntity() {

    companion object : PanacheCompanion<Timeslot> {
        fun findByDayOfWeekSorted(dayOfWeek: DayOfWeek): List<Timeslot> {
            return list("dayOfWeek", dayOfWeek).sortedBy { it.startTime }
        }

        private const val CONFLICT_QUERY = """
            dayOfWeek = ?1
            AND (
                (startTime >= ?2 AND startTime < ?3)
                OR (endTime > ?2 AND endTime <= ?3)
                OR (startTime <= ?2 AND endTime >= ?3)
            )
        """
        fun findConflicts(dayOfWeek: DayOfWeek, startTime: LocalTime, endTime: LocalTime): List<Timeslot> {
            return list(CONFLICT_QUERY, dayOfWeek, startTime, endTime)
        }
    }

    lateinit var name: String
    lateinit var description: String
    lateinit var dayOfWeek: DayOfWeek
    lateinit var startTime: LocalTime
    lateinit var endTime: LocalTime

    constructor(name: String, description: String, dayOfWeek: DayOfWeek, startTime: LocalTime, endTime: LocalTime) : this() {
        this.name = name
        this.description = description
        this.dayOfWeek = dayOfWeek
        this.startTime = startTime
        this.endTime = endTime
    }

}

