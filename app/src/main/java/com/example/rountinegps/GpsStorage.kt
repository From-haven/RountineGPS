package com.example.rountinegps

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GpsStorage(private val context: Context) {

    private val fileName = "gps_record.json"
    private val gson = Gson()

    fun save(points: List<GpsPoint>) {
        val json = gson.toJson(points)
        context.openFileOutput(fileName, Context.MODE_PRIVATE)
            .use { it.write(json.toByteArray()) }
    }

    fun load(): MutableList<GpsPoint> {
        return try {
            val json = context.openFileInput(fileName)
                .bufferedReader().readText()
            val type = object : TypeToken<List<GpsPoint>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}