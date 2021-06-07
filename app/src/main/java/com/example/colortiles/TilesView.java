package com.example.colortiles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TilesView extends View {

    int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;

    float tmpWidth = displayWidth / 5;
    float tmpHeight = displayWidth / 5;
    float tmpX = tmpWidth / 3;
    float tmpY = tmpWidth / 3;

    ArrayList<Card> cards = new ArrayList<>();
    List<Integer> colors;

    boolean isOnPauseNow = false;

    int counter = 0;

    int redCount;
    int blueCount;
    int greenCount;

    Card firstCard = null;

    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorsAndTiles();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Card c : cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //      Log.d("myTag", "onTouchEvent: " + cards.get(3).color);
        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow) {
            for (Card c : cards) {
                if (c.isTouched(x, y)) {
                    c.switchColor();
                    invalidate();
                    PauseTask task = new PauseTask();
                    task.execute();
                    return true;
                }
            }
        }
        invalidate();
        return true;
    }

    public void setColorsAndTiles() {
        colors = Arrays.asList(
                getResources().getColor(R.color.tileColorR),
                getResources().getColor(R.color.tileColorG),
                getResources().getColor(R.color.tileColorB)
        );

        Collections.shuffle(colors);
        int cnt = 0;
        for (int i = 0; i < 16; i++) {
            cards.add(new Card(tmpX, tmpY, tmpWidth, tmpHeight, colors, cnt));
            cnt++;
            if (cnt == 3) {
                cnt = 0;
                Collections.shuffle(colors);
            }

            tmpX += tmpWidth + tmpWidth / 10;
            if (((i + 1) % 4) == 0) {
                tmpX = tmpWidth / 3;
                tmpY += tmpWidth + tmpWidth / 10;
            }
        }

        tmpWidth = displayWidth / 5;
        tmpHeight = displayWidth / 5;
        tmpX = tmpWidth / 3;
        tmpY = tmpWidth / 3;
    }

    public void newGame() {
        cards.clear();
        setColorsAndTiles();
        invalidate();
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            firstCard = cards.get(0);
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i).color == firstCard.color) counter++;
                if (cards.get(i).color == colors.get(0)) redCount++;
                if (cards.get(i).color == colors.get(1)) greenCount++;
                if (cards.get(i).color == colors.get(2)) blueCount++;
            }

            Log.d("myTag", "onPostExecute: " + counter);
            if (counter == 16) {
                Toast toast = Toast.makeText(getContext(),
                        "Победа!", Toast.LENGTH_SHORT);
                toast.show();
                newGame();
            }
            counter = 0;
        }
    }
}