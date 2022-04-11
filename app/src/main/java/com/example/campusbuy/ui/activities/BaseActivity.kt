package com.example.campusbuy.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.campusbuy.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog: Dialog

    @SuppressLint("ShowToast")
    fun showErrorSnackBar(msg: String, errorMsg: Boolean) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view

        if(errorMsg) {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackbarError
                )
            )
        }
        else {
            snackbarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackbarSuccess
                )
            )
        }
        snackbar.show()
    }

    fun showProgressDialog(text: String) {

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit() {
        if(doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRICATION")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}