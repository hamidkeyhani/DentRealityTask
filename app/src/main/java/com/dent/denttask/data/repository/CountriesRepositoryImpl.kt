package com.dent.denttask.data.repository

import com.dent.denttask.data.local.FileManagement
import com.dent.denttask.domain.model.Country
import com.dent.denttask.domain.repository.CountriesRepository

class CountriesRepositoryImpl constructor(private val fileManagement: FileManagement) :
    CountriesRepository {
    override suspend fun getCountries(): List<Country> {
        return fileManagement.fetchCountriesListAsJson()
    }
}