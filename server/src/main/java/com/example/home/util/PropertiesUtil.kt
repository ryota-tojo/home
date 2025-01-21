package com.example.home.util

import java.util.*

object PropertiesUtil {
    fun loadProperties(fileName: String): Properties {
        val properties = Properties()
        val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
        inputStream.use {
            properties.load(it)
        }
        return properties
    }
}