package com.github.twangodev.domain

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.optaplanner.core.api.domain.entity.PlanningEntity
import org.optaplanner.core.api.domain.lookup.PlanningId
import org.optaplanner.core.api.domain.variable.PlanningVariable
import javax.persistence.Entity
import javax.persistence.ManyToOne

@PlanningEntity
@Entity
class Lesson() : PanacheEntity() {

    companion object : PanacheCompanion<Lesson>

    @PlanningId
    override var id: Long? = null

    lateinit var name: String
    lateinit var description: String
    lateinit var category: String
    
    @JsonDeserialize(using = Teacher.IdDeserializer::class)
    @ManyToOne
    var teacher: Teacher? = null

    @PlanningVariable(valueRangeProviderRefs = ["timeslotRange"])
    @ManyToOne
    var timeslot : Timeslot? = null
    @PlanningVariable(valueRangeProviderRefs = ["roomRange"])
    @ManyToOne
    var room : Room? = null

    constructor(name: String, description: String, category: String, teacher: Teacher) : this() {
        this.name = name
        this.description = description
        this.category = category
        this.teacher = teacher
    }

}