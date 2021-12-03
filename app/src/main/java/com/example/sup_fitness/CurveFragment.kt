package com.example.sup_fitness

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.sup_fitness.db.UserEntity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_curve.*
import kotlinx.android.synthetic.main.fragment_weight.*


class CurveFragment : Fragment() {

    private lateinit var viewModel: WeightFragmentViewModel

    var listW = mutableListOf<UserEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curve, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WeightFragmentViewModel::class.java)
        chart()

        refreshBtn.setOnClickListener {
            refresh()
        }

    }

    private fun chart() {
        addData()
        initChart()
        setDataChart()
    }

    private fun addData() {
        listW = viewModel.list
        Log.i("listW",listW.toString())
        Log.i("addData","adding data")

    }

    inner class MyXAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < listW.size) {
                listW[index].date
            } else {
                ""
            }
        }

    }

    private fun initChart() {

        val xAxis: XAxis = chart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        chart.axisLeft.gridLineWidth = 2.5F
        chart.setDrawGridBackground(true)

        //remove right y-axis
        chart.axisRight.isEnabled = false

        //remove legend
        chart.legend.isEnabled = false

        //remove description label
        chart.description.isEnabled = false

        //add animation
        chart.animateX(1000, Easing.EaseInSine)



        chart.setDrawBorders(true)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyXAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

        Log.i("initChart","initiate Chart")

    }

    private fun setDataChart() {
        val entries: ArrayList<Entry> = ArrayList()

            for (i in listW.indices) {
                val list = listW[i]
                Log.i("index",i.toString())
                if (listW.size > 1) {
                    entries.add(Entry(i.toFloat(), list.weight.toFloat()))
                }
            }

        Log.i("setDataChart","setting chart data")

        val lineDataSet = LineDataSet(entries, "Suivi Poids/Date")
        lineDataSet.color = Color.RED
        lineDataSet.lineWidth = 4.0F
        lineDataSet.circleRadius = 6.0F

        lineDataSet.setDrawValues(true)

        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.valueTextSize = 12.5F
        lineDataSet.highlightLineWidth = 8.0F

        val data = LineData(lineDataSet)
        chart.data = data
        chart.invalidate()
    }

    private fun refresh() {
        chart.clearValues()
        viewModel.getAllUsers()
        addData()
        setDataChart()
        chart.notifyDataSetChanged()
        Log.i("refresh","refresh")
        }
}

