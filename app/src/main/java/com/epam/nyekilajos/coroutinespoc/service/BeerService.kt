package com.epam.nyekilajos.coroutinespoc.service

import com.epam.nyekilajos.coroutinespoc.model.Beer
import retrofit2.http.GET

interface BeerService {

    @GET("beers")
    suspend fun getBeers(): List<Beer>
}

class BeerServiceException(message: String) : RuntimeException(message)
