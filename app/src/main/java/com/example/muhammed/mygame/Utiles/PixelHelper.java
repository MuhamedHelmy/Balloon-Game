package com.example.muhammed.mygame.Utiles;

/**
 * Created by Muhammed on 16/11/2017.
 */

import android.content.Context;
import android.util.TypedValue;

    public class PixelHelper {

        public static int pixelsToDp(int px, Context context) {
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, px,
                    context.getResources().getDisplayMetrics());
        }

    }

