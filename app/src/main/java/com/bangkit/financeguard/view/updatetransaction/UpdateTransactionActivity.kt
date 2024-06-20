package com.bangkit.financeguard.view.updatetransaction

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.financeguard.R
import com.bangkit.financeguard.data.ResultState
import com.bangkit.financeguard.databinding.ActivityTransactionBinding
import com.bangkit.financeguard.databinding.ActivityUpdateTransactionBinding
import com.bangkit.financeguard.view.ViewModelFactory

class UpdateTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTransactionBinding
    private val viewModel by viewModels<UpdateTransactionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recordId = intent.getIntExtra("RECORD_ID", -1)

        binding.saveButton.setOnClickListener {
            val penghasilan = binding.penghasilanEditText.text.toString()
            val pengeluaran = binding.pengeluaranEditText.text.toString()
            val tabungan = binding.tabunganEditText.text.toString()
            viewModel.updateRecord(recordId, penghasilan, pengeluaran, tabungan).observe(this) { result ->
                when (result) {
                    is ResultState.Success -> {
                        Toast.makeText(this, "Record updated successfully", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    is ResultState.Error -> {
                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Loading -> {
                        // Show loading indicator if necessary
                    }
                }
            }
        }
    }
}
