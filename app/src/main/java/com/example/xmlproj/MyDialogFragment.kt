package com.example.xmlproj

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Fragment Dialog")
            .setMessage("This is DialogFragment")
            .setPositiveButton("OK", null)
            .create()
    }
}