package com.yhongm.subway.xml;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author yhongm
 */
public class Subway implements Parcelable {


    private String name;
    private String color;
    private List<StationsBean> stations;


    protected Subway(Parcel in) {
        name = in.readString();
        color = in.readString();
        stations = in.createTypedArrayList(StationsBean.CREATOR);
    }

    public static final Creator<Subway> CREATOR = new Creator<Subway>() {
        @Override
        public Subway createFromParcel(Parcel in) {
            return new Subway(in);
        }

        @Override
        public Subway[] newArray(int size) {
            return new Subway[size];
        }
    };

    @Override
    public String toString() {
        return "Subway{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", mStations=" + stations +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<StationsBean> getStations() {
        return stations;
    }

    public void setStations(List<StationsBean> stations) {
        this.stations = stations;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(color);
        dest.writeTypedList(stations);
    }


    public static class StationsBean implements Parcelable {
        /**
         * draw_name : <text fill="#000" font-size="16" font-weight="normal" id="svgjsText19493" style="font-size:16;font-family:Helvetica, Arial, sans-serif;text-anchor:start;textLength:1;font-weight:normal;" x="1258.9099999999999" y="881.39"><tspan dy="14.773333344" id="svgjsTspan19494" style="font-size:16;font-family:Helvetica, Arial, sans-serif;text-anchor:start;textLength:1;font-weight:normal;" x="1258.9099999999999">苹果园</tspan></text>
         * draw_img : <ellipse cx="1282.31" cy="907.79" eletype="0" fill="white" id="svgjsEllipse19492" mcx="12933692.29" mcy="4828438.13" name="苹果园" rx="6.5" ry="6.5" stroke="#CC0000" stroke-width="2.5" sx="1282.31" sy="907.79" uid="ff9fdb36fe0574244c5547d9"></ellipse>
         * xy : 1282.31,907.79
         * draw_type : L
         * draw_args :
         * name : 苹果园
         */

        private String draw_name;
        private String draw_img;
        private String xy;
        private String draw_type;
        private String draw_args;
        private String name;

        protected StationsBean(Parcel in) {
            draw_name = in.readString();
            draw_img = in.readString();
            xy = in.readString();
            draw_type = in.readString();
            draw_args = in.readString();
            name = in.readString();
        }

        public final static Creator<StationsBean> CREATOR = new Creator<StationsBean>() {
            @Override
            public StationsBean createFromParcel(Parcel in) {
                return new StationsBean(in);
            }

            @Override
            public StationsBean[] newArray(int size) {
                return new StationsBean[size];
            }
        };

        @Override
        public String toString() {
            return "StationsBean{" +
                    "draw_name='" + draw_name + '\'' +
                    ", draw_img='" + draw_img + '\'' +
                    ", xy='" + xy + '\'' +
                    ", draw_type='" + draw_type + '\'' +
                    ", draw_args='" + draw_args + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        public String getDraw_name() {
            return draw_name;
        }

        public void setDraw_name(String draw_name) {
            this.draw_name = draw_name;
        }

        public String getDraw_img() {
            return draw_img;
        }

        public void setDraw_img(String draw_img) {
            this.draw_img = draw_img;
        }

        public String getXy() {
            return xy;
        }

        public void setXy(String xy) {
            this.xy = xy;
        }

        public String getDraw_type() {
            return draw_type;
        }

        public void setDraw_type(String draw_type) {
            this.draw_type = draw_type;
        }

        public String getDraw_args() {
            return draw_args;
        }

        public void setDraw_args(String draw_args) {
            this.draw_args = draw_args;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * Describe the kinds of special objects contained in this Parcelable
         * instance's marshaled representation. For example, if the object will
         * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
         * the return value of this method must include the
         * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
         *
         * @return a bitmask indicating the set of special object types marshaled
         * by this Parcelable object instance.
         */
        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * Flatten this object in to a Parcel.
         *
         * @param dest  The Parcel in which the object should be written.
         * @param flags Additional flags about how the object should be written.
         *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
         */
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(draw_name);
            dest.writeString(draw_img);
            dest.writeString(xy);
            dest.writeString(draw_type);
            dest.writeString(draw_args);
            dest.writeString(name);
        }
    }
}
