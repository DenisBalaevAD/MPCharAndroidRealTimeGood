package com.example.mpcharandroidrealtimegood

import android.R
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mpcharandroidrealtimegood.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Runnable
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mChart = binding.chart1

        mChart.description.text = ""
        mChart.setNoDataText("No data for the moment")

        mChart.isHighlightPerTapEnabled = true //
        mChart.setScaleEnabled(true)

        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        mChart.setDrawGridBackground(false)

        mChart.setPinchZoom(true)
        mChart.setBackgroundColor(Color.LTGRAY)

        val data = LineData()
        data.setValueTextColor(Color.WHITE)

        mChart.data = data

        val l = mChart.legend

        l.form = Legend.LegendForm.LINE
        l.textColor = Color.WHITE

        val xl = mChart.xAxis
        xl.textColor = Color.WHITE
        xl.setDrawGridLines(false)
        xl.setAvoidFirstLastClipping(true)

        val yl = mChart.axisLeft
        yl.textColor = Color.WHITE
        yl.setAxisMaxValue(140f)
        yl.setDrawGridLines(true)

        val yl2 = mChart.axisRight
        yl2.isEnabled = false
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

            data.addEntry(Entry(set.entryCount.toFloat(),(Math.random() * 120).toFloat() + 5f),0)
            mChart.notifyDataSetChanged()
            mChart.setVisibleXRange(6f,6f)
            mChart.moveViewToX((data.entryCount).toFloat() - 1)

            if (set.entryCount >= 7) {
                set.removeFirst()
                for (i in 0 until set.entryCount) {
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
        set.setDrawCircles(true)
        set.cubicIntensity = 0.2f
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.setColors(ColorTemplate.getHoloBlue())
        set.setCircleColor(ColorTemplate.getHoloBlue())
        set.lineWidth = 2f
        set.circleSize = 4f
        set.fillAlpha = 65
        set.fillColor = ColorTemplate.getHoloBlue()
        set.highLightColor = Color.rgb(244,117,177)
        set.valueTextColor = Color.RED
        set.color = Color.BLACK
        set.valueTextSize = 10f
        set.setDrawFilled(true)

        //val drawable = ResourcesCompat.getDrawable(resources, R.drawable., null)
        val drawable = ContextCompat.getDrawable(this, R.drawable.alert_dark_frame)
        set.fillDrawable = drawable
        return set
    }
}