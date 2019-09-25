package com.pancholi.commuter.commutecard;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.TextView;

import com.pancholi.commuter.util.ViewUtil;

public class AddressClearDrawable {

  private TextView parentView;
  private Drawable drawable;

  public AddressClearDrawable(TextView parentView) {
    this.parentView = parentView;
    this.drawable = ViewUtil.getDrawableFromTextView(parentView,
            ViewUtil.RIGHT_COMPOUND_DRAWABLE_INDEX);
  }

  public boolean isDrawablePressed(MotionEvent event) {
    return event.getRawX() >=
            parentView.getRight() -
            drawable.getBounds().width() -
            parentView.getPaddingRight() -
            parentView.getCompoundDrawablePadding();
  }

  public void setDrawableEnd(boolean showDrawable) {
    Drawable endDrawable = showDrawable ? drawable : null;
    parentView.setCompoundDrawablesRelative(null, null, endDrawable, null);
  }

  public boolean notVisible() {
    return !drawable.isVisible();
  }
}
