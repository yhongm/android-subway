/**
 *                   Copyright [yhongm]
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at

 *         http://www.apache.org/licenses/LICENSE-2.0

 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */

package com.yhongm.subway.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.CardView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yhongm.subway.DataObject
import com.yhongm.subway.MainActivity
import com.yhongm.subway.R
import com.yhongm.subway.SearchAdapter
import com.yhongm.subway.view.WaitDialog
import com.yhongm.subway.xml.*

class SearchFragment : Fragment() {
    var startLineName: String? = null
    var startLineStationName: String? = null
    var endLineName: String? = null
    var endLineStationName: String? = null

    var spStartLineName: Spinner? = null
    var spStartLineStationName: Spinner? = null
    var spEndLineName: Spinner? = null
    var spEndLineStationName: Spinner? = null
    var btnSearch: Button? = null
    var listview: ListView? = null
    var cardview: CardView? = null
    var constraintLayout: ConstraintLayout? = null
    var emptyTextView: TextView? = null
    var mSvgs: ArrayList<SVG>? = null
    var waitDialog: WaitDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.fragment_search, container, false)


        constraintLayout = view.findViewById(R.id.search_constraint_layout)

        spStartLineName = view.findViewById<Spinner>(R.id.spinner_start_linename)

        spStartLineStationName = view.findViewById<Spinner>(R.id.spinner_start_stationname)


        spEndLineName = view.findViewById<Spinner>(R.id.spinner_end_linename)
        spEndLineStationName = view.findViewById<Spinner>(R.id.spinner_end_stationname)


        btnSearch = view.findViewById<Button>(R.id.btn_search)

        listview = view.findViewById<ListView>(R.id.search_listview)

        constraintLayout!!.addView(emptyTextView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))


        listview!!.emptyView = emptyTextView
        bindSpinder(mSvgs!!, spStartLineName!!, spStartLineStationName!!, object : SelectStationListener {
            override fun selectStation(station: String?) {
                startLineStationName = station
            }

            override fun selectLine(line: String?) {
                startLineName = line
            }

        })
        bindSpinder(mSvgs!!, spEndLineName!!, spEndLineStationName!!, object : SelectStationListener {
            override fun selectStation(station: String?) {
                endLineStationName = station
            }

            override fun selectLine(line: String?) {
                endLineName = line
            }
        })
        btnSearch!!.setOnClickListener({
            if (startLineStationName.equals(endLineStationName)) {
                Snackbar.make(it, "起点终点不能相同", Snackbar.LENGTH_SHORT).setAction("取消", {

                }).show()
            } else {

                clickSearch(startLineName!!, startLineStationName!!, endLineName!!, endLineStationName!!, mSvgs!!)

            }

        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        waitDialog = WaitDialog(activity!!, "规划中...")
        mSvgs = parseSvgs()


        emptyTextView = TextView(activity)

        emptyTextView!!.text = "未查询"
        emptyTextView!!.textSize = 20f
        emptyTextView!!.setTextColor(resources.getColor(R.color.colorPrimary))
        emptyTextView!!.gravity = Gravity.CENTER


    }

    private fun clickSearch(startLineName: String, startLineStationName: String, endLineName: String, endLineStationName: String, mSvgs: ArrayList<SVG>) {
        waitDialog!!.show()
        Thread(Runnable {

            var hashMaps = ArrayList<HashMap<String, String>>()
            val sResult = search(startLineStationName, endLineStationName, startLineName, endLineName, mSvgs)
            for (search in sResult) {
                var sb = StringBuilder()
                var totalSize = 0
                var hashMap = HashMap<String, String>()
                var r = ArrayList<String>()
                search.withIndex().forEach {
                    if (it.index != 0) {
                        sb.append(">")
                        sb.append(it.value.name)
                    } else {
                        sb.append(it.value.name)
                    }
                    totalSize += it.value.stations.size

                }
                r.add(sb.toString())
                hashMap.put(r.toString(), totalSize.toString())
                hashMaps.add(hashMap)

            }
            activity!!.runOnUiThread({
                var sa = SearchAdapter(activity!!.applicationContext, hashMaps)
                listview!!.adapter = sa
                waitDialog!!.dismiss()
                listview!!.setOnItemClickListener { parent, view, position, id ->
                    val subways = sResult[position]

                    (activity as MainActivity).searchResult(subways)
                }
            })
        }).start()


    }


    fun search(startStation: String, endStation: String, sline: String, eline: String, mSvgs: ArrayList<SVG>): List<ArrayList<Subway>> {
        val stations = DataObject.parseStations()
        var tempSubway: Subway? = null
        var tempSubways: ArrayList<Subway>
        var startStation = startStation
        var endStation = endStation

        var hashMap: HashMap<String, ArrayList<String>> = HashMap()
        mSvgs!!.map {
            val texts = it.g!!.texts
            var array = ArrayList<String>()
            texts!!.forEach { text ->
                array.add(text.content)
            }
            hashMap.put(it.name!!, array)

        }

        val stationList = stations!!.filter {
            it.linenames.size > 1
        }
        var output = search(sline, startStation!!, eline, stationList)
        var results = ArrayList<LinkedHashMap<String, String>>()
        output.forEach {
            var map = LinkedHashMap<String, String>()
            it.forEach {

                map.put(it.key, it.value)
            }

            results.add(map)

        }

        var finalResults = ArrayList<ArrayList<Subway>>()


        results.forEach { r ->


            tempSubways = ArrayList<Subway>()
            var mSubways = DataObject.parseFileToSubway()


            for ((index, ri) in r.iterator().withIndex()) {
                if (index <= r.size - 2) {
                    tempSubway = mSubways!!.filter { it.name.equals(ri.key) }[0]
                    var findex = hashMap.get(ri.key)!!.indexOf(ri.value)
                    var iri = r.entries.elementAt(index = index + 1)
                    var eindex = hashMap.get(ri.key)!!.indexOf(iri.value)
                    var intRange: IntRange

                    if (findex < eindex) {
                        intRange = IntRange(findex, eindex)
                    } else {
                        intRange = IntRange(eindex, findex)
                    }
                    tempSubway!!.stations = tempSubway!!.stations!!.slice(intRange)
                    tempSubways.add(tempSubway!!)
                } else if (index == r.size - 1) {
                    tempSubway = mSubways!!.filter { it.name.equals(ri.key) }[0]


                    var findex = hashMap.get(ri.key)!!.indexOf(ri.value)
                    var eindex = hashMap.get(ri.key)!!.indexOf(endStation)


                    val intRange: IntRange

                    if (findex < eindex) {
                        intRange = IntRange(findex, eindex)
                    } else {
                        intRange = IntRange(eindex, findex)
                    }

                    tempSubway!!.stations = tempSubway!!.stations!!.slice(intRange)
                    tempSubways.add(tempSubway!!)
                }


            }

            finalResults.add(tempSubways)
        }

        finalResults.sortBy {

            var size = 0
            it.forEach {
                size += it.stations.size
            }
            size
        }


        finalResults = finalResults.filter {
            it.size < 5
        } as ArrayList<ArrayList<Subway>>


        return finalResults
    }

    private fun search(sLine: String, sStation: String, eLine: String, list: List<Station>): ArrayList<LinkedHashMap<String, String>> {
        var commonStationLineMap = LinkedHashMap<String, String>()
        var output = ArrayList<LinkedHashMap<String, String>>()
        commonStationLineMap[sLine] = sStation
        if (!sLine.equals(eLine)) {
            var test = loopSearch(list, sLine, eLine, commonStationLineMap, output)
            test.forEach {

                var tcommonStationLineMap = LinkedHashMap<String, String>()
                tcommonStationLineMap.putAll(commonStationLineMap)
                tcommonStationLineMap.put(it.key, it.value)
                val test2 = loopSearch(list, it.key, eline = eLine, cm = tcommonStationLineMap, output = output)

                test2.forEach {
                    var ttcommonStationLineMap = LinkedHashMap<String, String>()
                    ttcommonStationLineMap.putAll(tcommonStationLineMap)
                    ttcommonStationLineMap.put(it.key, it.value)

                    val test3 = loopSearch(list, it.key, eLine, cm = ttcommonStationLineMap, output = output)

                }

            }
        } else {
            var lhm = LinkedHashMap<String, String>()
            lhm.put(sLine, sStation)
            output.add(lhm)
        }
        return output

    }

    @Synchronized
    private fun loopSearch(list: List<Station>, sline: String, eline: String, cm: LinkedHashMap<String, String>, output: ArrayList<LinkedHashMap<String, String>>): LinkedHashMap<String, String> {
        var commonLineStation = cm
        var stationLineMap = LinkedHashMap<String, String>()

        var isSearch: Boolean = false
        var test = list.filter {
            it.linenames.contains(sline)
        }
        test.forEach { station ->
            station.linenames.forEach { linename ->
                if (!linename.equals(eline)) {
                    if (!commonLineStation!!.contains(linename)) {
                        var lines = stationLineMap.map { it.key }
                        if (!lines!!.contains(linename)) {
                            stationLineMap.put(linename, station.name)

                        }
                    }
                } else {

                    commonLineStation.put(linename, station.name)
                    isSearch = true
                    if (!output.contains(commonLineStation)) {
                        output.add(commonLineStation)
                    }


                }

            }
        }
        if (isSearch) {
            stationLineMap.clear()
        }
        return stationLineMap
    }

    fun bindSpinder(mSvgs: ArrayList<SVG>, pSpinner: Spinner, cSpinner: Spinner, ssl: SelectStationListener): Unit {
        val mSvgList = mSvgs.map {
            it.name
        }


        var mSvgArray = mSvgList.toTypedArray()
        val pAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, mSvgArray)
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pSpinner.adapter = pAdapter
        pSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        pos: Int, id: Long) {
                ssl.selectLine(mSvgArray[pos])

                var svg = mSvgs.filter {
                    it.name.equals(mSvgArray[pos])

                }
                var mStationsArray = svg[0].g!!.texts!!.map { it.content }
                var cAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, mStationsArray.toTypedArray())

                cSpinner.adapter = cAdapter
                cSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        ssl.selectStation(mStationsArray[position])
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    interface SelectStationListener {
        fun selectStation(station: String?)
        fun selectLine(line: String?)
    }

    private fun parseSvgs(): ArrayList<SVG> {
        return DataObject.parseSvg()
    }
}


