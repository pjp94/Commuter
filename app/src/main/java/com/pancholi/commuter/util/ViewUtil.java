package com.pancholi.commuter.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class ViewUtil {

  public static final int RIGHT_COMPOUND_DRAWABLE_INDEX = 2;

  private ViewUtil() {}

  public static Drawable getDrawableFromTextView(View view, int index) {
    return ((TextView) view).getCompoundDrawablesRelative()[index];
  }
}
