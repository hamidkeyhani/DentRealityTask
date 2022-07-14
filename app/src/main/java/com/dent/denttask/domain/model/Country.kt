package com.dent.denttask.domain.model

data class Country(
    val countryCode: String,
    val capital: String,
    val timezones: List<String>,
    val name: String,
    val latlng: List<Double>
)
