package com.epam.nyekilajos.coroutinespoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.epam.nyekilajos.coroutinespoc.model.Beer
import com.epam.nyekilajos.coroutinespoc.service.BeerService
import com.epam.nyekilajos.coroutinespoc.service.BeerServiceException
import kotlinx.coroutines.Dispatchers

class BeerListViewModel(private val beerService: BeerService) : ViewModel() {

    val beers: LiveData<List<Beer>> = liveData(Dispatchers.IO) {
        emit(
            try {
                beerService.getBeers().also {
                    loading.postValue(false)
                }
            } catch (e: BeerServiceException) {
                error.postValue(e.localizedMessage)
                emptyList<Beer>()
            }
        )
    }

    val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = true }

    val error: MutableLiveData<String> = MutableLiveData()
}
