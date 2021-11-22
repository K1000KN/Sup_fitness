package com.example.sup_fitness

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.example.sup_fitness.db.UserEntity
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_weight.*
import kotlinx.android.synthetic.main.fragment_weight.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeightFragment: Fragment(), RecyclerViewAdapter.RowClickListener {

    private lateinit var viewOfLayout: View
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: WeightFragmentViewModel
    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_weight, container, false)
        val rootView: View = inflater.inflate(R.layout.fragment_dialog,container, false)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            recyclerViewAdapter = RecyclerViewAdapter(this@WeightFragment)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(activity?.applicationContext, VERTICAL)
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(WeightFragmentViewModel::class.java)
        viewModel.getAllUsersObservers().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        rootView.okBtn.setOnClickListener {
            val number = rootView.num
            val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

            val user = UserEntity(0, number, date)
            viewModel.insertUserInfo(user)
        }

        viewOfLayout.addBtn.setOnClickListener {
            var dialog = DialogFragment()

            fragmentManager?.let { it1 -> dialog.show(it1,"customDialog") }
        }
        return viewOfLayout
    }

    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        TODO("Not yet implemented")
    }
}

