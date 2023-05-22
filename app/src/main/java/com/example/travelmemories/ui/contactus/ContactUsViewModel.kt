package com.example.travelmemories.ui.contactus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class ContactUsViewModel : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _sendButtonEnabled = MutableLiveData<Boolean>()
    val sendButtonEnabled: LiveData<Boolean> = _sendButtonEnabled

    init {
        _sendButtonEnabled.value = false
    }

    fun setName(name: String) {
        _name.value = name
        validateForm()
    }

    fun setEmail(email: String) {
        _email.value = email
        validateForm()
    }

    fun setMessage(message: String) {
        _message.value = message
        validateForm()
    }

    private fun validateForm() {
        val name = _name.value
        val email = _email.value
        val message = _message.value

        _sendButtonEnabled.value = !name.isNullOrBlank() && !email.isNullOrBlank() && !message.isNullOrBlank()
    }

    fun sendContactMessage(context: Context, name: String, email: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:contact@marian.com")
            putExtra(Intent.EXTRA_SUBJECT, "Contact Message")
            putExtra(Intent.EXTRA_TEXT, "Name: $name\nEmail: $email\nMessage: $message")
        }

        val packageManager = context.packageManager
        if (intent.resolveActivity(packageManager) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            showToast(context, "Message sent successfully")
        } else {
            showToast(context, "No email app found")
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}