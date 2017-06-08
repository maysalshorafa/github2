package com.pos.leaders.leaderspossystem.Printer;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.pos.leaders.leaderspossystem.Tools.CONSTANT;

/**
 * Created by Karam on 25/02/2017.
 */

public class Block {
    public String text;
    public float textSize;
    public int textWidth;
    public int textColor;
    public Paint.Align align;
    public int width;

    public Block(String text, float textSize, int textColor,int width) {
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.textWidth = Typeface.NORMAL;
        this.align = Paint.Align.RIGHT;
        this.width = width;
    }
    public Block(String text, float textSize, int textColor) {
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.textWidth = Typeface.NORMAL;
        this.align = Paint.Align.RIGHT;
        this.width = CONSTANT.PRINTER_PAGE_WIDTH;
    }
    public Block(String text, float textSize, int textColor,Paint.Align align,int width) {
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.textWidth = Typeface.NORMAL;
        this.align = align;
        this.width = width;
    }

    public Block Right(){
        align = Paint.Align.RIGHT;
        return this;
    }

    public Block Left(){
        align = Paint.Align.LEFT;
        return this;
    }

    public Block Center(){
        align = Paint.Align.CENTER;
        return this;
    }

    public Block Bold() {
        textWidth = Typeface.BOLD;
        return this;
    }

    public Block Normal() {
        textWidth = Typeface.NORMAL;
        return this;
    }

    public Block Italic() {
        textWidth = Typeface.ITALIC;
        return this;
    }

    public Block Black() {
        textColor = Color.BLACK;
        return this;
    }

    public Block setSize(float fs) {
        textSize = fs;
        return this;
    }

}

