package com.dent.denttask.data.local

import android.app.Application
import com.dent.denttask.common.Constants.COUNTRIES_LIST_FILE_NAME
import com.dent.denttask.domain.model.Country
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class FileManagement @Inject constructor(private val app: Application, private val gson: Gson) {

    private fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            val buffer = app.assets.open(fileName).bufferedReader()
            jsonString = buffer.readText()
            buffer.close()
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun fetchCountriesListAsJson(): List<Country> {
        val jsonFileAsString = getJsonDataFromAsset(COUNTRIES_LIST_FILE_NAME)
        val listWebSiteType = object : TypeToken<List<Country>>() {}.type
        return gson.fromJson(jsonFileAsString, listWebSiteType)
    }

}