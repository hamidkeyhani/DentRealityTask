package com.dent.denttask.domain.use_case

import com.dent.denttask.domain.model.Country
import com.dent.denttask.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountriesUseCase @Inject constructor(private val countriesRepository: CountriesRepository) {

    operator fun invoke(): Flow<Result<List<Country>>> = flow {
        try {
            emit(Result.success(countriesRepository.getCountries()))
        } catch (throwable: Throwable) {
            emit(Result.failure(throwable))
        }
    }
}