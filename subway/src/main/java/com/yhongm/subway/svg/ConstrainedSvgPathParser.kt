package com.yhongm.subway.svg


class ConstrainedSvgPathParser private constructor(private val originalWidth: Int, private val originalHeight: Int, private val viewWidth: Int,
                                                   private val viewHeight: Int) : SvgPathParser() {

    override fun transformX(x: Float): Float {
        return x * viewWidth / originalWidth
    }

    override fun transformY(y: Float): Float {
        return y * viewHeight / originalHeight
    }

    class Builder {

        private var originalWidth: Int = 0
        private var originalHeight: Int = 0
        private var viewWidth: Int = 0
        private var viewHeight: Int = 0

        fun originalWidth(originalWidth: Int): Builder {
            this.originalWidth = originalWidth
            return this
        }

        fun originalHeight(originalHeight: Int): Builder {
            this.originalHeight = originalHeight
            return this
        }

        fun viewWidth(width: Int): Builder {
            this.viewWidth = width
            return this
        }

        fun viewHeight(height: Int): Builder {
            this.viewHeight = height
            return this
        }

        fun build(): ConstrainedSvgPathParser {
            return ConstrainedSvgPathParser(originalWidth, originalHeight, viewWidth, viewHeight)
        }
    }
}
