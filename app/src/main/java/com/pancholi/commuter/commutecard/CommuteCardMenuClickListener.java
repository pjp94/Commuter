package com.pancholi.commuter.commutecard;

import android.view.MenuItem;

import com.pancholi.commuter.R;

import androidx.appcompat.widget.PopupMenu;

public class CommuteCardMenuClickListener implements PopupMenu.OnMenuItemClickListener {

  private CardMenuItemClickedListener cardMenuItemClickedListener;

  CommuteCardMenuClickListener(CardMenuItemClickedListener cardMenuItemClickedListener) {
    this.cardMenuItemClickedListener = cardMenuItemClickedListener;
  }

  public interface CardMenuItemClickedListener {
    void onEditClicked();
    void onDeleteClicked();
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.cardMenuEdit:
        cardMenuItemClickedListener.onEditClicked();
        return true;
      case R.id.cardMenuDelete:
        cardMenuItemClickedListener.onDeleteClicked();
        return true;
      default:
        return false;
    }
  }
}
