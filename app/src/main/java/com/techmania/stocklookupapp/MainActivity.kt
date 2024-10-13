package com.techmania.stocklookupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    // ViewModel instance to manage UI-related data in a lifecycle-conscious way
    private val viewModel: StockViewModel by viewModels()

    // UI components
    private lateinit var etStockSymbol: EditText // Input field for stock symbol
    private lateinit var btnSearch: Button // Button to trigger stock search
    private lateinit var tvCompanyName: TextView // TextView to display the company name
    private lateinit var tvSymbol: TextView // TextView to display the stock symbol
    private lateinit var tvStockPrice: TextView // TextView to display the current stock price
    private lateinit var tvChange: TextView // TextView to display the change in stock price
    private lateinit var tvChangePercent: TextView // TextView to display the percentage change
    private lateinit var progressBar: ProgressBar // ProgressBar to indicate loading state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Calls the superclass's onCreate method
        setContentView(R.layout.activity_main) // Sets the layout for this activity

        initializeViews() // Initializes UI components
        setupObservers() // Sets up observers for LiveData from the ViewModel
        setupListeners() // Sets up listeners for user interactions
    }

    private fun initializeViews() {
        // Finds and assigns UI components to their respective variables
        etStockSymbol = findViewById(R.id.etStockSymbol)
        btnSearch = findViewById(R.id.btnSearch)
        tvCompanyName = findViewById(R.id.tvCompanyName)
        tvSymbol = findViewById(R.id.tvSymbol)
        tvStockPrice = findViewById(R.id.tvStockPrice)
        tvChange = findViewById(R.id.tvChange)
        tvChangePercent = findViewById(R.id.tvChangePercent)
        progressBar = findViewById(R.id.progressBar)

        // Hides the progress bar initially
        progressBar.visibility = View.GONE
    }

    private fun setupObservers() {
        // Observes stock information from the ViewModel
        viewModel.stockInfo.observe(this) { stockInfo ->
            if (stockInfo != null) {
                // Updates UI with stock information
                tvCompanyName.text = stockInfo.companyName
                tvSymbol.text = stockInfo.symbol
                tvStockPrice.text = "Price: ${String.format("%.2f", stockInfo.price)}"
                tvChange.text = "Change: ${String.format("%.2f", stockInfo.change)}"
                tvChangePercent.text = "Change Percent: ${String.format("%.2f", stockInfo.changePercent)}%"
            } else {
                // Clears stock information if no data is available
                clearStockInfo()
            }
        }

        // Observes loading state from the ViewModel
        viewModel.isLoading.observe(this) { isLoading ->
            // Enables or disables the search button based on loading state
            btnSearch.isEnabled = !isLoading
            // Shows or hides the progress bar based on loading state
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Resets progress bar when loading is complete
            if (!isLoading) {
                progressBar.progress = 0
            }
        }

        // Observes error messages from the ViewModel
        viewModel.error.observe(this) { error ->
            if (!error.isNullOrBlank()) {
                // Displays a toast message for any errors
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                // Clears stock information on error
                clearStockInfo()
            }
        }

        // Observes progress updates from the ViewModel
        viewModel.progress.observe(this) { progress ->
            // Updates the progress bar with the current progress value
            progressBar.progress = progress
        }
    }

    private fun setupListeners() {
        // Sets a click listener for the search button
        btnSearch.setOnClickListener {
            val symbol = etStockSymbol.text.toString().trim() // Gets the stock symbol from input
            if (symbol.isNotEmpty()) {
                // Fetches stock information if the input is not empty
                viewModel.fetchStockInfo(symbol)
            } else {
                // Displays a toast message if no symbol is entered
                Toast.makeText(this, "Please enter a stock symbol", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearStockInfo() {
        // Clears all displayed stock information
        tvCompanyName.text = ""
        tvSymbol.text = ""
        tvStockPrice.text = ""
        tvChange.text = ""
        tvChangePercent.text = ""
    }
}