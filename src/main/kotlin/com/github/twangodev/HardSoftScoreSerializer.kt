package com.github.twangodev

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore

object HardSoftScoreSerializer : JsonSerializer<HardSoftScore>() {

    override fun serialize(value: HardSoftScore?, gen: JsonGenerator, serializer: SerializerProvider?) {
        gen.writeArray(intArrayOf(value!!.hardScore, value.softScore), 0, 2)
    }

}