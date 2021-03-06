package com.epam.nyekilajos.coroutinespoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.epam.nyekilajos.coroutinespoc.model.Beer
import com.epam.nyekilajos.coroutinespoc.position.SensorPositionProvider
import com.epam.nyekilajos.coroutinespoc.position.toDegrees
import com.epam.nyekilajos.coroutinespoc.service.BeerService
import com.epam.nyekilajos.coroutinespoc.service.BeerServiceException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlin.math.absoluteValue

private const val POUR_DEGREE_THRESHOLD = 45

@FlowPreview
@ExperimentalCoroutinesApi
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

    val drunkenBeers: LiveData<Int> = liveData(Dispatchers.Default) {
        emit(0)

        var glassPouring = false

        SensorPositionProvider.INSTANCE.positionReadings.collect { position ->
            val rollInDegrees = position.roll.absoluteValue.toDegrees()
            if (glassPouring) {
                if (rollInDegrees < POUR_DEGREE_THRESHOLD) {
                    glassPouring = false
                    emit(latestValue?.plus(1) ?: 1)
                }
            } else {
                if (rollInDegrees > POUR_DEGREE_THRESHOLD) {
                    glassPouring = true
                }
            }
        }
    }

    val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = true }

    val error: MutableLiveData<String> = MutableLiveData()

}
