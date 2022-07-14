package com.dent.denttask.ui

import androidx.lifecycle.ViewModel
import com.dent.denttask.domain.model.Country
import com.dent.denttask.domain.use_case.CountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val countriesUseCase: CountriesUseCase) :
    ViewModel() {

    private val _countriesState = MutableStateFlow(CountriesState())
    val countriesState: StateFlow<CountriesState> = _countriesState

    private val errorListener = MutableSharedFlow<String?>()

    fun fetchList(): Flow<Result<List<Country>>> {
        return countriesUseCase.invoke().onEach { result ->
            if (result.isSuccess) {
                _countriesState.value =
                    countriesState.value.copy(list = result.getOrThrow())
            } else {
                result.onFailure {
                    errorListener.emit(it.localizedMessage)
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}