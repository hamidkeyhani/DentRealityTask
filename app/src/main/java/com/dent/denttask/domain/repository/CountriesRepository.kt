package com.dent.denttask.domain.repository

import com.dent.denttask.domain.model.Country

interface CountriesRepository {
    suspend fun getCountries(): List<Country>
}