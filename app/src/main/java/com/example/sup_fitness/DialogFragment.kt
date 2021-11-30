package com.example.sup_fitness

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.sup_fitness.db.UserEntity
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_weight.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DialogFragment : DialogFragment() {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: WeightFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog,container, false)
    }

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(WeightFragmentViewModel::class.java)
        viewModel.getAllUsersObservers().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        num.maxValue = 500
        num.minValue = 0
        okBtn.setOnClickListener {
            val number = num.value
            val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

            val user = UserEntity(0, number, date)
            viewModel.insertUserInfo(user)
        }


        cancelBtn.setOnClickListener {
            dismiss()
        }
    }
}