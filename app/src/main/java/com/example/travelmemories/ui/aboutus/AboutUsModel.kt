package com.example.travelmemories.ui.aboutus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutUsModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is About Us Fragment"
    }
    val text: LiveData<String> = _text
}