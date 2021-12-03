package com.example.sup_fitness

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.sup_fitness.db.UserEntity
import kotlinx.android.synthetic.main.fragment_dialog.*
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import kotlinx.android.synthetic.main.fragment_weight.*
import kotlinx.android.synthetic.main.fragment_weight.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeightFragment: Fragment(), RecyclerViewAdapter.RowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: WeightFragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            recyclerViewAdapter = RecyclerViewAdapter(this@WeightFragment)
            adapter = recyclerViewAdapter
            ItemTouchHelper(simpleCallBack).attachToRecyclerView(this)
            val divider = DividerItemDecoration(activity?.applicationContext, VERTICAL)
            addItemDecoration(divider)
        }

        viewModel = ViewModelProviders.of(this).get(WeightFragmentViewModel::class.java)
        viewModel.getAllUsersObservers().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        val calendar : Calendar =  Calendar.getInstance()

        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val settings : SharedPreferences =  PreferenceManager.getDefaultSharedPreferences(activity);

        val lastDay = settings.getInt("day", 0)

        if (lastDay != currentDay) {
            val editor : SharedPreferences.Editor = settings.edit()
            editor.putInt("day", currentDay)
            editor.apply()

            addBtn.setOnClickListener {
            showWeightDialog()
            }
        }

        else {
            Toast.makeText(context, "Can't add weidght more than a day", Toast.LENGTH_LONG).show()
        }
    }

    private fun showWeightDialog() {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.fragment_dialog)

        dialog.num.maxValue = 500
        dialog.num.minValue = 30

        dialog.okBtn.setOnClickListener {
            val number = dialog.num.value
            val date = SimpleDateFormat("yyyy-MM-dd").format(Date())

            val user = UserEntity(0, number, date)
            viewModel.insertUserInfo(user)

            dialog.dismiss()
        }

        dialog.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    val simpleCallBack = object : ItemTouchHelper.SimpleCallback(0, RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position =  viewHolder.layoutPosition
            val wght = recyclerViewAdapter.items[position]

            viewModel.deleteUserInfo(wght)
        }

    }

    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)

    }

    override fun onItemClickListener(user: UserEntity) {
        TODO()
    }
}


