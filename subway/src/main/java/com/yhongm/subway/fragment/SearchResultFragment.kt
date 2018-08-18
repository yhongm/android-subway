package com.yhongm.subway.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.yhongm.subway.DataObject
import com.yhongm.subway.MainActivity
import com.yhongm.subway.view.CustomScrollView
import com.yhongm.subway.R
import com.yhongm.subway.view.SubwayPopWindow
import com.yhongm.subway.svg.RenderSvgPathView
import com.yhongm.subway.xml.Subway
import com.yhongm.subway.xml.SvgUtil

class SearchResultFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.fragment_search_result, container, false)

        spv = view.findViewById(R.id.search_result_spv)

        csv = view.findViewById<CustomScrollView>(R.id.search_result_csv_v)

        hsv = view.findViewById<HorizontalScrollView>(R.id.search_result_hsv_v)

        vpopwindow = SubwayPopWindow(activity!!.applicationContext)
        getIntentResult()

        return view

    }

    var spv: RenderSvgPathView? = null
    var csv: CustomScrollView? = null
    var hsv: HorizontalScrollView? = null
    var vpopwindow: SubwayPopWindow? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun getIntentResult() {
        var list = arguments?.getParcelableArrayList<Subway>("results")

        val svgs = SvgUtil.getInstance().subway2Svg(list)
        spv!!.setPvgs(svgs)
        spv!!.start()
        spv!!.setListener(object : RenderSvgPathView.RenderSvgLister {
            override fun firstContent(x: Int, y: Int) {
                hsv!!.scrollTo(x, y)

            }

            override fun clickStationName(name: String, view: View, x: Int, y: Int) {


                val stations = DataObject.parseStations()
                val list1 = stations!!.filter {
                    it.name.equals(name)
                }
                var firstend = list1[0].firstend

                val firstAndEnd = firstend.split("||||||")
                var first = ""
                var end = ""
                if (firstAndEnd.size > 1) {
                    first = firstAndEnd[0].replaceFirst("::::::", ":").replace("::::::", "-->")
                    end = firstAndEnd[1].replaceFirst("::::::", ":").replace("::::::", "-->")
                } else if (firstAndEnd.size == 1) {
                    first = firstAndEnd[0].replaceFirst("::::::", ":").replace("::::::", "-->")
                    end = ""
                }
                if (vpopwindow!!.isShowing) {
                    vpopwindow!!.dismiss()
                }
                vpopwindow!!.setContent(first, end)
                vpopwindow!!.setTitle(name)
                vpopwindow!!.show(view, x, y)

            }

        })
    }

    fun setScale(d: Double) {
        spv!!.setScale(d)
    }

    fun setIsShowStationName(b: Boolean) {
        spv!!.setIsShowStationName(b)
    }

    fun setIsShowStationLocation(b: Boolean) {
        spv!!.setIsShowStationLocation(b)
    }

    fun start() {
        getIntentResult()
    }
}