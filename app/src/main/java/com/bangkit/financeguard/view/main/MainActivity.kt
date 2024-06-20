package com.bangkit.financeguard.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.financeguard.R
import com.bangkit.financeguard.databinding.ActivityMainBinding
import com.bangkit.financeguard.view.ViewModelFactory
import com.bangkit.financeguard.view.transaction.TransactionActivity
import com.bangkit.financeguard.view.updatetransaction.UpdateTransactionActivity
import com.bangkit.financeguard.view.welcome.WelcomeActivity
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private val transactionActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.fetchAndSaveSpecificRecord()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.currentRecord.observe(this) { record ->
                    if (record != null) {
                        binding.penghasilanBulanan.text = "Penghasilan: ${formatCurrency(record.penghasilanBulanan)}"
                        binding.pengeluaranBulanan.text = "Pengeluaran: ${formatCurrency(record.pengeluaranBulanan)}"
                        binding.tabunganBulanan.text = "Tabungan: ${formatCurrency(record.tabunganBulanan)}"
                        binding.label.text = "Kesehatan Keuangan: ${record.label}"
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, TransactionActivity::class.java)
            transactionActivityResultLauncher.launch(intent)
        }

        binding.fabUpdate.setOnClickListener {
            val record = viewModel.currentRecord.value
            if (record != null) {
                val intent = Intent(this, UpdateTransactionActivity::class.java)
                intent.putExtra("RECORD_ID", record.id)
                transactionActivityResultLauncher.launch(intent)
            } else {
                Toast.makeText(this, "No record selected", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchAndSaveSpecificRecord()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about -> {
                Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun formatCurrency(amount: Int?): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        return numberFormat.format(amount ?: 0).replace("Rp", "Rp ")
    }
}
