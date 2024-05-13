package com.rvigo.logger.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals

fun <K, V> Map<K, V>.containsEntry(pair: Pair<K, V>): Boolean =
    this.containsKey(pair.first) && this[pair.first] == pair.second

fun assertEmptyMap(data: String?) {
    val map = deserialize<Map<String, String>>(data!!)
    assertEquals(0, map.size)
}

inline fun <reified T> deserialize(data: String): T = jacksonObjectMapper().readValue(data)

inline fun <reified T> T.setPrivateField(field: String, value: Any): T = apply {
    T::class.java.declaredFields
        .find { it.name == field }
        ?.also { it.isAccessible = true }
        ?.set(this, value)
}