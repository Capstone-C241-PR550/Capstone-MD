package com.bangkit.financeguard.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.financeguard.data.Repository
import com.bangkit.financeguard.di.Injection
import com.bangkit.financeguard.view.transaction.TransactionViewModel
import com.bangkit.financeguard.view.login.LoginViewModel
import com.bangkit.financeguard.view.main.MainViewModel
import com.bangkit.financeguard.view.profile.ProfileViewModel
import com.bangkit.financeguard.view.register.RegisterViewModel
import com.bangkit.financeguard.view.updatetransaction.UpdateTransactionViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(TransactionViewModel::class.java) -> {
                TransactionViewModel(repository) as T
            }

            modelClass.isAssignableFrom(UpdateTransactionViewModel::class.java) -> {
                UpdateTransactionViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
