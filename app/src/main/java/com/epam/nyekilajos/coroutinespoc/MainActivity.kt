package com.epam.nyekilajos.coroutinespoc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epam.nyekilajos.coroutinespoc.databinding.ActivityMainBinding
import com.epam.nyekilajos.coroutinespoc.service.BeerService
import com.epam.nyekilajos.coroutinespoc.ui.BeersAdapter
import com.epam.nyekilajos.coroutinespoc.viewmodel.BeerListViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val beerListViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return BeerListViewModel(getBeerService()) as T
                }
            }
        ).get(BeerListViewModel::class.java)

        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            lifecycleOwner = this@MainActivity
            adapter = BeersAdapter()
            vm = beerListViewModel
        }

        beerListViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}

private const val BEERS_SERVICE_BASE_URL = "https://api.punkapi.com/v2/"
private const val TIMEOUT = 25000L

private fun getBeerService(): BeerService {
    return Retrofit.Builder()
        .baseUrl(BEERS_SERVICE_BASE_URL)
        .client(
            OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BeerService::class.java)
}
