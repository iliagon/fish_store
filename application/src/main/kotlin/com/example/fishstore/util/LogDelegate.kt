package com.example.fishstore.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.jvm.javaField

class LogDelegate : ReadOnlyProperty<Any?, Logger> {

    private lateinit var logger: Logger

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger {
        if (!::logger.isInitialized) {
            logger = LoggerFactory.getLogger(getClassForLogging(thisRef, property))
        }
        return logger
    }

    private fun getClassForLogging(thisRef: Any?, property: KProperty<*>): Class<*> {
        return if (thisRef != null) {
            val javaClass = thisRef.javaClass
            javaClass.enclosingClass?.takeIf { it.kotlin.companionObject?.java == javaClass } ?: javaClass
        } else property.javaField!!.declaringClass //in case the logger property declared as top-level declaration
    }
}