package com.bangkit.financeguard.view.transaction

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.financeguard.data.ResultState
import com.bangkit.financeguard.databinding.ActivityTransactionBinding
import com.bangkit.financeguard.view.ViewModelFactory

class TransactionActivity : AppCompatActivity() {
    private val viewModel by viewModels<TransactionViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val penghasilan = binding.penghasilanEditText.text.toString()
            val pengeluaran = binding.pengeluaranEditText.text.toString()
            val tabungan = binding.tabunganEditText.text.toString()

            viewModel.storeRecord(penghasilan, pengeluaran, tabungan).observe(this) { result ->
                when (result) {
                    is ResultState.Success -> {
                        Toast.makeText(this, "Transaction Saved", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    is ResultState.Error -> {
                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Loading -> {
                        // Handle loading state if needed
                    }
                }
            }
        }
    }
}
