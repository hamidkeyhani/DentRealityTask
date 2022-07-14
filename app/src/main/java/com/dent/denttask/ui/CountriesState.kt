package com.dent.denttask.ui

import com.dent.denttask.domain.model.Country

data class CountriesState(
    val list: List<Country> = emptyList(),
    val sort: Boolean = true
)