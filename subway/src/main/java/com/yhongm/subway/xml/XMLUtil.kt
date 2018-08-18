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

import android.graphics.Color
import android.util.Log
import android.util.Xml
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

object XMLUtil {
    fun readXml(ips: InputStream): SVG? {
        val newPullParser = Xml.newPullParser()
        var svg: SVG? = null
        var svgAttribute: SVGAttribute? = null
        var svgG: G? = null

        var currentGpath: String? = null

        var stack: Stack<String>? = null

        var text: Text? = null

        var ellipse: Ellipse? = null

        var innerSvg: InnerSvg? = null
        var circle: Circle? = null

        try {
            newPullParser.setInput(ips, "UTF-8")
            var eventType = newPullParser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = newPullParser.name
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {//文档开始
                        svg = SVG()
                        svgAttribute = SVGAttribute()
                        svgG = G()
                        svgG.texts = ArrayList<Text>()
                        svgG.ellipses = ArrayList<Ellipse>()
                        svgG.innerSvgs = ArrayList<InnerSvg>()
                        stack = Stack()
                    }
                    XmlPullParser.START_TAG -> {//元素开始

                        when {
                            name.equals("svg") -> {
                                stack!!.push(name)
                                if (stack.size == 1) {
                                    var viewBox = newPullParser.getAttributeValue(null, "viewBox")
                                    if (viewBox != null) {
                                        val list = viewBox.split(" ")
                                        var vb: ViewBox = ViewBox()
                                        vb.minX = list.get(0).toInt()
                                        vb.minY = list.get(1).toInt()
                                        vb.width = list.get(2).toInt()
                                        vb.height = list.get(3).toInt()
                                        svgAttribute!!.viewBox = vb
                                    }
                                    var version = newPullParser.getAttributeValue(null, "version")
                                    if (version != null) {
                                        svgAttribute!!.version = version
                                    }
                                    var baseProfile = newPullParser.getAttributeValue(null, "baseProfile")
                                    if (baseProfile != null) {
                                        svgAttribute!!.baseProfile = baseProfile
                                    }
                                    var x = newPullParser.getAttributeValue(null, "x")
                                    if (x != null) {
                                        svgAttribute!!.x = x
                                    }
                                    var y = newPullParser.getAttributeValue(null, "y")
                                    if (y != null) {
                                        svgAttribute!!.y = y
                                    }
                                    svg!!.svgAttribute = svgAttribute
                                } else if (stack.size == 2) {
                                    innerSvg = InnerSvg()

                                    var viewBox = newPullParser.getAttributeValue(null, "viewBox")
                                    if (viewBox == null) {
                                        viewBox = newPullParser.getAttributeValue(null, "viewbox")
                                    }
                                    var x = newPullParser.getAttributeValue(null, "x")

                                    if (x != null) {
                                        innerSvg.x = x.toFloat()
                                    }
                                    var y = newPullParser.getAttributeValue(null, "y")
                                    if (y != null) {
                                        innerSvg.y = y.toFloat()
                                    }
                                    var width = newPullParser.getAttributeValue(null, "width")
                                    if (width != null) {
                                        innerSvg.width = width.toFloat()
                                    }
                                    var height = newPullParser.getAttributeValue(null, "height")
                                    if (height != null) {
                                        innerSvg.height = height.toFloat()
                                    }

                                    if (viewBox != null) {
                                        val list = viewBox.split(" ")
                                        var vb = ViewBox()
                                        vb.minX = list.get(0).toInt()
                                        vb.minY = list.get(1).toInt()
                                        vb.width = list.get(2).toInt()
                                        vb.height = list.get(3).toInt()
                                        innerSvg.viewBox = vb
                                    }
//                                    var content = newPullParser.getAttributeValue(null, "name")
//                                    if (content != null) {
//                                        text.content = content
//                                    }
                                }
                            }
                            name.equals("path") -> {
                                stack!!.push(name)
                                if (stack.size == 2) {
                                    var d = newPullParser.getAttributeValue(null, "d")
                                    var pathStrokeWidth = newPullParser.getAttributeValue(null, "stroke-width");
                                    svgG!!.path = d
                                    svgG!!.pathStrokeWidth = pathStrokeWidth.toFloat()
                                }
                            }
                            name.equals("text") -> {
                                stack!!.push(name)

                                text = Text()
                                var fill = newPullParser.getAttributeValue(null, "fill")
                                if (fill != null) {
                                    text.fill = fill
                                }
                                var fontSize = newPullParser.getAttributeValue(null, "font-size")
                                if (fontSize != null) {
                                    text.fontSize = fontSize.toFloat()
                                }
                                var fontWeight = newPullParser.getAttributeValue(null, "font-weight")
                                if (fontWeight != null) {
                                    text.fontWeight = fontWeight
                                }
                                var x = newPullParser.getAttributeValue(null, "x")
                                if (x != null) {
                                    text.x = x.toFloat()
                                }
                                var y = newPullParser.getAttributeValue(null, "y")
                                if (y != null) {
                                    text.y = y.toFloat()
                                }


                            }
                            name.equals("tspan") -> {
                                stack!!.push(name)
                                var dy = newPullParser.getAttributeValue(null, "dy")
                                val content = parseText(newPullParser)
                                text!!.dy = dy.toFloat()

                                text!!.content = content
                            }

                            name.equals("ellipse") -> {
                                stack!!.push(name)

                                ellipse = Ellipse()
                                var cx = newPullParser.getAttributeValue(null, "cx")
                                if (cx != null) {
                                    ellipse.cx = cx.toFloat()
                                }
                                var cy = newPullParser.getAttributeValue(null, "cy")
                                if (cy != null) {
                                    ellipse.cy = cy.toFloat()
                                }
                                var rx = newPullParser.getAttributeValue(null, "rx")
                                if (rx != null) {
                                    ellipse.rx = rx.toFloat()
                                }
                                var ry = newPullParser.getAttributeValue(null, "ry")
                                if (ry != null) {
                                    ellipse.ry = ry.toFloat()
                                }
                                var fill = newPullParser.getAttributeValue(null, "fill")
                                if (fill != null) {
                                    ellipse.fill = Color.parseColor(fill)
                                }
                                var opacity = newPullParser.getAttributeValue(null, "opacity")
                                if (opacity != null) {
                                    ellipse.opacity = opacity
                                }

                                var stroke = newPullParser.getAttributeValue(null, "stroke")
                                if (stroke != null) {
                                    ellipse.stroke = Color.parseColor(stroke)
                                }
                                var strokeWidth = newPullParser.getAttributeValue(null, "stroke-width")
                                if (strokeWidth != null) {
                                    ellipse.strokeWidth = strokeWidth
                                }
                                svgG!!.ellipses!!.add(ellipse)

                            }

                            name.equals("circle") -> {
                                stack!!.push(name)
                                circle = Circle()
                                var cx = newPullParser.getAttributeValue(null, "cx")
                                if (cx != null) {
                                    circle.cx = cx.toFloat()
                                }
                                var cy = newPullParser.getAttributeValue(null, "cy")
                                if (cy != null) {
                                    circle.cy = cy.toFloat()
                                }
                                var r = newPullParser.getAttributeValue(null, "r")
                                if (r != null) {
                                    circle.r = r.toFloat()
                                }
                                var fill = newPullParser.getAttributeValue(null, "fill")
                                if (fill != null) {
                                    circle.fill = fill
                                }
                                var opacity = newPullParser.getAttributeValue(null, "opacity")
                                if (opacity != null) {
                                    circle.opacity = opacity
                                }

                                var stroke = newPullParser.getAttributeValue(null, "stroke")
                                if (stroke != null) {
                                    circle.stroke = stroke
                                }
                                var strokeWidth = newPullParser.getAttributeValue(null, "stroke-width")
                                if (strokeWidth != null) {
                                    circle.strokeWidth = strokeWidth
                                }
                                innerSvg!!.circle = circle


                            }
                        // 2018/5/3,yhongm
                        }

                    }
                    XmlPullParser.END_TAG -> {//元素结束
                        var name = newPullParser.name
                        when {
                            name.equals("svg") -> {
                                if (stack!!.size == 2) {
                                    svgG!!.innerSvgs!!.add(innerSvg!!)
                                }
                                stack!!.pop()


                            }
                            name.equals("path") -> {
                                stack!!.pop()

                            }
                            name.equals("text") -> {
                                stack!!.pop()
                                svgG!!.texts!!.add(text!!)


                            }
                            name.equals("tspan") -> {
                                stack!!.pop()

                            }

                            name.equals("ellipse") -> {
                                stack!!.pop()


                            }
                            name.equals("circle") -> {
                                stack!!.pop()
                            }
                        }

                    }
                    XmlPullParser.END_DOCUMENT -> {
                    }
                }
                eventType = newPullParser.next()
            }
            ips.close()
            svg!!.g = svgG
            return svg
        } catch (e: Throwable) {
            return null
        }
        return null
    }

    private fun parseText(parser: XmlPullParser): String {
        val type = parser.next()
        if (type.equals(XmlPullParser.TEXT)) {
        }
        return parser.text
    }

    fun readStation(stationInputStream: InputStream?): ArrayList<Station>? {
        var stations: ArrayList<Station>? = null

        var station: Station? = null
        try {
            var newXmlPullParser = Xml.newPullParser()
            newXmlPullParser.setInput(stationInputStream, "UTF-8")
            var eventType = newXmlPullParser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name = newXmlPullParser.name
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                        stations = ArrayList<Station>()
                    }
                    XmlPullParser.START_TAG -> {
                        when {
                            name.equals("s") -> {
                                station = Station()

                                var name = newXmlPullParser.getAttributeValue(null, "name")
                                station.name = name
                                var firstend = newXmlPullParser.getAttributeValue(null, "firstend")
                                station.firstend = firstend
                                var linename = newXmlPullParser.getAttributeValue(null, "linename")
                                linename.split("||||||").forEach {
                                    if (it.equals("14号线")) {
                                        val stationsList = arrayListOf<String>("张郭庄", "园博园", "大瓦窑", "郭庄子", "大井", "七里庄", "西局")
                                        if (stationsList.contains(name)) {
                                            station.linenames.add(it + "西段")
                                        } else {
                                            station.linenames.add(it + "东段")
                                        }
                                    } else {
                                        station.linenames.add(it)
                                    }

                                }


                            }

                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when {
                            name.equals("s") -> {
                                stations!!.add(station!!)
                            }
                        }
                    }


                }
                eventType = newXmlPullParser.next()

            }
            stationInputStream!!.close()
            return stations!!
        } catch (e: Exception) {
            return null
        }
        return null
    }


}