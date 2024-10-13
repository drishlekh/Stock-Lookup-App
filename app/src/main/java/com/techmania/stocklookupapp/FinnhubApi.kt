package com.techmania.stocklookupapp

import retrofit2.http.GET
import retrofit2.http.Query

// Defines the API interface for interacting with the Finnhub API
interface FinnhubApi {
    // Fetches the current stock quote for a given symbol
    @GET("quote")
    suspend fun getStockQuote(
        @Query("symbol") symbol: String, // The stock symbol to look up
        @Query("token") token: String // The API token for authentication
    ): StockQuoteResponse

    // Fetches the company profile for a given stock symbol
    @GET("stock/profile2")
    suspend fun getCompanyProfile(
        @Query("symbol") symbol: String, // The stock symbol to look up
        @Query("token") token: String // The API token for authentication
    ): CompanyProfileResponse
}

// Data class representing the response for a stock quote
data class StockQuoteResponse(
    val c: Double = 0.0, // Current price of the stock
    val d: Double = 0.0, // Change in price from the previous close
    val dp: Double = 0.0 // Percentage change in price
)

// Data class representing the response for a company profile
data class CompanyProfileResponse(
    val name: String = "", // Name of the company
    val logo: String = "", // URL of the company's logo
    val finnhubIndustry: String = "" // Industry classification of the company
)