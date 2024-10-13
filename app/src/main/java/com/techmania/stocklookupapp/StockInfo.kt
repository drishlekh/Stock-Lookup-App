package com.techmania.stocklookupapp

// Data class representing the stock information retrieved from the API
data class StockInfo(
    val symbol: String, // The stock symbol (e.g., "AAPL" for Apple Inc.)
    val companyName: String, // The full name of the company (e.g., "Apple Inc.")
    val price: Double, // The current price of the stock
    val change: Double, // The change in stock price from the previous close
    val changePercent: Double // The percentage change in stock price
)
