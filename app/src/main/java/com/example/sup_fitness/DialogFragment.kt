package com.example.sup_fitness

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sup_fitness.db.UserEntity
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_dialog,container, false)

        rootView.num.maxValue = 500
        rootView.num.minValue = 0

        rootView.cancelBtn.setOnClickListener {
            dismiss()
        }

        return rootView
    }
}