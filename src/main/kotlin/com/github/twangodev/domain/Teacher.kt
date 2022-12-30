package com.github.twangodev.domain

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.github.twangodev.domain.util.Gender
import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Teacher : PanacheEntity() {

    override var id: Long? = null

    companion object : PanacheCompanion<Teacher>

    lateinit var prefix: String
    lateinit var name: String
    lateinit var email: String
    lateinit var gender: Gender

    @OneToMany
    var preferredTimeslots: List<Timeslot> = emptyList()

    @OneToMany
    var unavailableTimeslots: List<Timeslot> = emptyList()

    object IdDeserializer : JsonDeserializer<Teacher>() {

        override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Teacher? {
            return p0?.valueAsLong?.let { Teacher.findById(it) }
        }

    }

}