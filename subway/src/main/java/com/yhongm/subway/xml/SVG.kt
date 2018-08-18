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


class SVG {
    var svgAttribute: SVGAttribute? = null
    var g: G? = null

    var name: String? = null
    var color: String? = null

    var isSelect: Boolean = true

    override fun toString(): String {
        return "SVG(svgAttribute=$svgAttribute, g=$g)"
    }

}

class SVGAttribute {
    var viewBox: ViewBox? = null
    var version: String? = null
    var baseProfile: String? = null
    var x: String? = null
    var y: String? = null
    override fun toString(): String {
        return "SVGAttribute(viewBox=$viewBox, version=$version, baseProfile=$baseProfile, x=$x, y=$y)"
    }
}

class ViewBox {
    var minX: Int = 0
    var minY: Int = 0
    var width: Int = 0
    var height: Int = 0
    override fun toString(): String {
        return "ViewBox(minX=$minX, minY=$minY, width=$width, height=$height)"
    }
}

class G {
    var id: Int = 0
    var path: String? = null
    var pathStrokeWidth: Float = 0f
    var texts: ArrayList<Text>? = null
    var ellipses: ArrayList<Ellipse>? = null
    //    var circles: ArrayList<Circle>? = null
    var innerSvgs: ArrayList<InnerSvg>? = null

    override fun toString(): String {
        return "G(id=$id, path=$path, texts=$texts, ellipses=$ellipses)"
    }
}

class Text {
    var fill: String? = "#000"
    var fontSize: Float = 16f
    var fontWeight: String? = "normal"
    var content: String = ""
    var x: Float = 0f
    var y: Float = 0f
    var dy: Float = 0f
    override fun toString(): String {
        return "Text(fill=$fill, fontSize=$fontSize, fontWeight=$fontWeight, content='$content', x='$x', y='$y', dy='$dy')"
    }


}

class Ellipse {
    var cx: Float = 0f
    var cy: Float = 0f
    var rx: Float = 0f
    var ry: Float = 0f
    var fill: Int = 0
    var stroke: Int = 0
    var strokeWidth: String? = null
    var opacity: String? = null
    override fun toString(): String {
        return "Ellipse(cx=$cx, cy=$cy, rx=$rx, ry=$ry, fill=$fill)"
    }
}
//<circle cx="52.5" cy="52.5" fill="#ffffff" opacity="1.00" r="46.5" stroke="#231715" stroke-width="12"></circle>

class Circle {
    var cx: Float = 0f
    var cy: Float = 0f
    var fill: String? = null

    var r: Float = 0f
    var stroke: String? = null
    var strokeWidth: String? = null
    var opacity: String? = null
    override fun toString(): String {
        return "Circle(cx=$cx, cy=$cy, fill=$fill, r=$r, stroke=$stroke, strokeWidth=$strokeWidth, opacity=$opacity)"
    }
}

class InnerSvg {
    var width: Float = 0f
    var height: Float = 0f
    var circle: Circle? = null
    var viewBox: ViewBox? = null
    var version: String? = null
    var baseProfile: String? = null
    var x: Float = 0f
    var y: Float = 0f
    override fun toString(): String {
        return "InnerSvg(width=$width, height=$height, circle=$circle, viewBox=$viewBox, version=$version, baseProfile=$baseProfile, x=$x, y=$y)"
    }

}