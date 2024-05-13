package com.rvigo.logger.boot.starter

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.assertj.core.api.MapAssert
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import kotlin.reflect.KClass

fun <K, V> MapAssert<K, V>.doesNotHaveValueSatisfying(condition: Condition<V>): MapAssert<K, V> =
    noneSatisfy { _, candidate -> assertThat(candidate).satisfies(condition) }

fun setupContextRunner(vararg configurations: KClass<*>): ApplicationContextRunner =
    ApplicationContextRunner().withConfiguration(
        AutoConfigurations.of(
            *configurations.map(KClass<*>::java).toTypedArray()
        )
    )