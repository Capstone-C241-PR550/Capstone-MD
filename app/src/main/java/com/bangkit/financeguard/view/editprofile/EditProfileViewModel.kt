//package com.bangkit.financeguard.view.editprofile
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.asLiveData
//import androidx.lifecycle.liveData
//import com.bangkit.financeguard.data.Repository
//import com.bangkit.financeguard.data.ResultState
//import com.bangkit.financeguard.data.pref.UserModel
//import com.bangkit.financeguard.data.remote.response.ProfileUpdateResponse
//import java.io.File
//
//class EditProfileViewModel(private val repository: Repository) : ViewModel() {
//    fun getSession(): LiveData<UserModel> {
//        return repository.getSession().asLiveData()
//    }
//
//    fun updateProfile(
//        imageFile: File?,
//        name: String?
//    ): LiveData<ResultState<ProfileUpdateResponse>> {
//        if (imageFile == null && name == null) {
//            return liveData { emit(ResultState.Error("No changes to update")) }
//        }
//        return repository.updateProfile(name, imageFile)
//    }
//}