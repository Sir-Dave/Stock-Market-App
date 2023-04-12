package com.sirdave.composeapp.presentation.company_listings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirdave.composeapp.domain.repository.StockRepository
import com.sirdave.composeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyListingsState())

    var searchJob: Job? = null

    fun onEvent(event: CompanyListingsEvent){
        when (event){
            is CompanyListingsEvent.Refresh ->{
                getListings(fetchFromRemote = true)
            }

            is CompanyListingsEvent.OnSearchQueryChanged ->{
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500.milliseconds)
                    getListings()
                }
            }
        }
    }

    private fun getListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false){

        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query)
                .collect{ result ->
                    when (result){
                        is Resource.Success ->{
                            result.data?.let { listings ->
                                state = state.copy(companies = listings)
                            }
                        }

                        is Resource.Error -> Unit
                        is Resource.Loading ->{
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}