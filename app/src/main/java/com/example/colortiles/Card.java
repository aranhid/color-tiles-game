package com.example.colortiles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;


public class Card {

    Paint p = new Paint();
    int color;
    int currentColorId;
    float x, y, width, height;
    List<Integer> colors;

    public Card(float x, float y, float width, float height, List<Integer> colors, int colorId) {
        this.currentColorId = colorId;
        this.colors = colors;
        setColor(currentColorId);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isTouched(float touchX, float touchY) {
        if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
            return true;
        } else return false;
    }

    public void switchColor() {
        if (currentColorId + 1 >= colors.size()) {
            currentColorId = 0;
        }
        else currentColorId++;
        setColor(currentColorId);
    }

    public void setColor(int colorId) {
        this.color = colors.get(colorId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas c) {
        p.setColor(color);
        c.drawRoundRect(x, y, x + width, y + height, 25, 25, p);
    }
}
