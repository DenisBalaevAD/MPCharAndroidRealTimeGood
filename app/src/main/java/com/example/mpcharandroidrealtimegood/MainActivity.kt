package com.example.mpcharandroidrealtimegood

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mpcharandroidrealtimegood.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Runnable

class YAxisValueFormatter: ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val valueNew = if(value.toString().contains("-")) {
            (value * -1).toString()
        }else{
            value.toString()
        }

        if (valueNew.contains(".")){
            return valueNew.substring(0, valueNew.indexOf("."))
        }
        return valueNew
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mChart = binding.chart1

        mChart.apply {
            description.text = ""
            setNoDataText("No data for the moment")

            isHighlightPerTapEnabled = true //
            setScaleEnabled(true)

            isDragEnabled = true
            setScaleEnabled(true)
            setGridBackgroundColor(Color.GRAY)
            setDrawGridBackground(true)

            setPinchZoom(true)
            setBackgroundColor(Color.LTGRAY)
        }
        val data = LineData()
        data.setValueTextColor(Color.WHITE)

        mChart.data = data

        mChart.legend.apply {
            form = Legend.LegendForm.LINE
            textColor = Color.WHITE
        }

        mChart.xAxis.apply {
            textColor = Color.WHITE
            setDrawGridLines(false)
            setAvoidFirstLastClipping(true)
            isEnabled = false
        }

        mChart.axisLeft.apply {
            textColor = Color.BLUE
            axisMinimum = 0f
            granularity = 1f
            setDrawGridLines(false)
            isGranularityEnabled = true
        }

        val yl2 = mChart.axisRight
        yl2.isEnabled = false

        val data2 = mChart.data

        var set = data2.getDataSetByIndex(0)
        if(set == null){
            set = createSet()
            data2.addDataSet(set)
        }

        scaleSetting(10f)
    }

    @Suppress("SameParameterValue")
    private fun scaleSetting (valueMaxAxisY: Float){
        var isZero = false
        var maxAxisY = valueMaxAxisY

        val yAxisLabel = arrayListOf<String>()

        while (!isZero) {
            yAxisLabel.add(maxAxisY.toString())
            maxAxisY-=0.5f
            if (maxAxisY < 0f) isZero = true
        }

        mChart.axisLeft.apply {
            axisMaximum = valueMaxAxisY
            labelCount = yAxisLabel.count()
        }

        /*for (i in valueMaxAxisY.toInt() downTo 0){
            yAxisLabel.add(i.toString())
        }*/

        yAxisLabel.forEach { Log.d("dt",it) }

        mChart.axisLeft.valueFormatter = (object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return yAxisLabel[value.toInt()]
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Thread(Runnable {

            while (true) {
                runOnUiThread(Runnable {
                    addEntry()
                })

                try {
                    Thread.sleep(600)
                } catch (e: InterruptedException) {

                }
            }
        }).start()
    }

    private fun addEntry(){
        val data = mChart.data

        if(data != null){
            var set = data.getDataSetByIndex(0)

            if(set == null){
                set = createSet()
                data.addDataSet(set)
            }

            data.addEntry(Entry(set.entryCount.toFloat() ,(0..mChart.axisLeft.mAxisMaximum.toInt()).random().toFloat()),0)
            mChart.notifyDataSetChanged()
            mChart.setVisibleXRange(6f,6f)
            mChart.moveViewToX((data.entryCount).toFloat())

            if (set.entryCount >= 8) {
                set.removeFirst()
                for (i in 0 until set.entryCount - 1) {
                    val entryToChange = set.getEntryForIndex(i)
                    entryToChange.x = entryToChange.x - 1
                }
            }

        }

        /*val dataChart = mChart.getData();

        if (dataChart != null) {

            var set = dataChart.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                dataChart.addDataSet(set);
            }

            dataChart.addEntry(
                Entry(
                    set.entryCount.toFloat(),
                    (Math.random() * 120).toFloat() + 5f
                ), 0
            )
            dataChart.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();
            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(300f);
            mChart.moveViewToX(dataChart.entryCount.toFloat());

            if (set.entryCount == 9) {
                set.removeFirst()
                // change Indexes - move to beginning by 1
                for (i in 0 until set.entryCount) {
                    val entryToChange = set.getEntryForIndex(i)
                    entryToChange.x = entryToChange.x - 1
                }
            }
        }*/
    }

    private fun createSet ():LineDataSet{
        val set = LineDataSet(null,"SPL Db")
        set.apply {
            setDrawCircles(true)
            cubicIntensity = 0.2f
            axisDependency = YAxis.AxisDependency.LEFT
            setColors(ColorTemplate.getHoloBlue())
            setCircleColor(ColorTemplate.getHoloBlue())
            lineWidth = 2f
            circleSize = 4f
            fillAlpha = 65
            fillColor = ColorTemplate.getHoloBlue()
            highLightColor = Color.rgb(244,117,177)
            valueTextSize = 10f
            setDrawFilled(true)

            color = Color.GRAY
            valueTextColor = ContextCompat.getColor(applicationContext,R.color.black)
            setDrawValues(false)
            isHighlightEnabled = true
            setDrawHighlightIndicators(false)
            setDrawCircles(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        //val drawable = ResourcesCompat.getDrawable(resources, R.drawable., null)
        val drawable = ContextCompat.getDrawable(this, R.drawable.font)
        set.fillDrawable = drawable
        return set
    }
}