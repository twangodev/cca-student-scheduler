package com.github.twangodev.domain

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Entity

@Entity
class Room() : PanacheEntity() {

    companion object : PanacheCompanion<Room>

    lateinit var name: String
    lateinit var description: String
    var capacity: Int = Int.MAX_VALUE

}