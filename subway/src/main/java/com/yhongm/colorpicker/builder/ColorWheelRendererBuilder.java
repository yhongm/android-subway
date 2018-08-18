package com.yhongm.colorpicker.builder;


import com.yhongm.colorpicker.ColorPickerView;
import com.yhongm.colorpicker.renderer.ColorWheelRenderer;
import com.yhongm.colorpicker.renderer.FlowerColorWheelRenderer;
import com.yhongm.colorpicker.renderer.SimpleColorWheelRenderer;

public class ColorWheelRendererBuilder {
    public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
        switch (wheelType) {
            case CIRCLE:
                return new SimpleColorWheelRenderer();
            case FLOWER:
                return new FlowerColorWheelRenderer();
        }
        throw new IllegalArgumentException("wrong WHEEL_TYPE");
    }
}