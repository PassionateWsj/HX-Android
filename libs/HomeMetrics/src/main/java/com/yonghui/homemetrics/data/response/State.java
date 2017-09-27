package com.yonghui.homemetrics.data.response;

/**
 * Created by CANC on 2017/4/6.
 */

public class State {

    /**
     * arrow : up
     * color : #63A69F
     */

    private String arrow;
    private String color;

    public String getArrow() {
        return arrow;
    }

    public void setArrow(String arrow) {
        this.arrow = arrow;
    }

    public String getColor() {
        if (color.equals("#F2E1AC")) {
            color = "#F4BC45";
        }
        else if (color.equals("#F2836B")) {
            color = "#F57685";
        }
        else if (color.equals("#63A69F")) {
            color = "#91C941";
        }
        return color;
    }

    public void setColor(String color) {
        if (color.equals("#F2E1AC")) {
            color = "#F4BC45";
        }
        else if (color.equals("#F2836B")) {
            color = "#F57685";
        }
        else if (color.equals("#63A69F")) {
            color = "#91C941";
        }
        this.color = color;
    }
}
