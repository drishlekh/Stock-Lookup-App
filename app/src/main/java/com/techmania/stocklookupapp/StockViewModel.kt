package com.techmania.stocklookupapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// ViewModel class to manage stock data and handle API interactions
class StockViewModel : ViewModel() {

    // LiveData to track the progress of data fetching
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    // LiveData to hold stock information
    private val _stockInfo = MutableLiveData<StockInfo?>()
    val stockInfo: LiveData<StockInfo?> = _stockInfo

    // LiveData to indicate loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData to hold error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Retrofit instance for making API calls
    private val api: FinnhubApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/") // Base URL for the Finnhub API
            .addConverterFactory(GsonConverterFactory.create()) // Converter for JSON responses
            .build()
            .create(FinnhubApi::class.java) // Creating the API interface
    }

    // API key for authentication with the Finnhub API
    private val apiKey = "cs5cnghr01qo1hu1e5qgcs5cnghr01qo1hu1e5r0" 

    // Function to fetch stock information based on the provided symbol
    fun fetchStockInfo(symbol: String) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Setting loading state to true
            _error.postValue(null) // Clearing any previous error messages
            _stockInfo.postValue(null) // Clearing previous stock information
            _progress.postValue(0) // Resetting progress to 0

            try {
                _progress.postValue(25) // Update progress to 25%
                val quoteResponse = api.getStockQuote(symbol, apiKey) // Fetch stock quote
                _progress.postValue(50) // Update progress to 50%
                val profileResponse = api.getCompanyProfile(symbol, apiKey) // Fetch company profile
                _progress.postValue(75) // Update progress to 75%

                // Check if we have valid price data
                if (quoteResponse.c > 0) {
                    // Update stock information if valid data is received
                    _stockInfo.postValue(StockInfo(
                        symbol = symbol,
                        companyName = profileResponse.name.ifEmpty { symbol }, // Use symbol if name is empty
                        price = quoteResponse.c,
                        change = quoteResponse.d,
                        changePercent = quoteResponse.dp
                    ))
                    _progress.postValue(100) // Update progress to 100%
                } else {
                    // Posting an error message if the stock symbol is invalid or no data is available
                    _error.postValue("Invalid stock symbol or no data available")
                }
            } catch (e: Exception) {
                // Handling exceptions and posting appropriate error messages
                val errorMessage = when (e) {
                    is IOException -> "No internet connection. Please check your network." // network errors
                    is HttpException -> {
                        when (e.code()) {
                            401 -> "Authentication error. Please check your API key." // authentication errors
                            404 -> "Invalid stock symbol" // not found errors
                            else -> "Error fetching stock data: ${e.code()}" // other HTTP errors
                        }
                    }
                    else -> "An unexpected error occurred: ${e.message}" // unexpected errors
                }
                _error.postValue(errorMessage) // Posting the error message
            } finally {
                _isLoading.postValue(false) // Setting loading state to false
            }
        }
    }
}