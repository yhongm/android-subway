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


package com.yhongm.subway.xml

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

class SvgUtil private constructor() {

    private fun isReverse(name: String): Boolean {

        val reverse = arrayOf("9号线", "14号线西段", "14号线东段", "昌平线", "房山线", "s1线", "燕房线", "西郊线")
        val contains = reverse.contains(name)
        return contains
    }

    fun parseSubway(subway: Subway): SVG {
        subway.color
        subway.name
        var draw_name_img: String = ""
        var pathData = ""
        var first: Boolean = true
        subway.stations.forEach continuing@{
            draw_name_img += it.draw_name
            draw_name_img += it.draw_img
            var x = it.xy.split(",")[0].toFloat()
            var y = it.xy.split(",")[1].toFloat()
            if (first) {
                pathData += 'M' + it.xy
                first = false
                return@continuing
            }

            if (isReverse(subway.name)) {
                if (it.draw_type.equals("L")) {
                    pathData += 'L' + it.xy

                }
                pathData += it.draw_args
            } else {
                pathData += it.draw_args
                if (it.draw_type.equals("L")) {
                    pathData += 'L' + it.xy

                }
            }


        }

        var draw_path = "<path d=\"$pathData\" eletype=\"1\" fill=\"none\" stroke=\"${subway.color}\" stroke-width=\"8\"></path>"
        var start = "<?xml version=\"1.0\" encoding=\"utf-8\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1 Tiny//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11-tiny.dtd\"><svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" baseProfile=\"tiny\" version=\"1.1\" viewBox=\"0 0 3486 1821\" x=\"0px\" xmlns=\"http://www.w3.org/2000/svg\" y=\"0px\" xml:space=\"preserve\">\n"
        var end = "</svg>"
        var draw_group = "$start <g id=\"${subway.name}\">  $draw_path  $draw_name_img </g> $end"

        val svg = XMLUtil.readXml(draw_group.byteInputStream(Charsets.UTF_8))
        svg!!.color = subway.color
        svg!!.name = subway.name
        return svg!!

    }

    fun parseFile(inputStream: InputStream): ArrayList<SVG> {
        val list = parseFileToSubway(inputStream)
        return subway2Svg(list)
    }

    fun subway2Svg(list: ArrayList<Subway>?): ArrayList<SVG> {
        var svgs: ArrayList<SVG> = ArrayList()
        if (list != null) {
            list.forEach {
                val svg = svgUtil!!.parseSubway(it)
                svgs.add(svg)

            }
        }
        return svgs
    }

    fun parseFileToSubway(inputStream: InputStream): ArrayList<Subway> {
        var bytes = ByteArray(0)
        bytes = ByteArray(inputStream.available())
        inputStream.read(bytes)

        val str = String(bytes)
        var gson = Gson()

        val type = object : TypeToken<ArrayList<Subway>>() {
        }.type
        val list = gson.fromJson<ArrayList<Subway>>(str, type)
        return list
    }

    companion object {
        private var svgUtil: SvgUtil? = null
        fun getInstance(): SvgUtil {
            if (svgUtil == null) {
                svgUtil = SvgUtil()
            }
            return svgUtil!!
        }
    }


}