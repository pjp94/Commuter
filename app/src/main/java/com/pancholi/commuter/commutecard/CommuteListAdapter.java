package com.pancholi.commuter.commutecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pancholi.commuter.R;
import com.pancholi.commuter.database.Commute;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CommuteListAdapter extends RecyclerView.Adapter<CommuteListAdapter.ViewHolder> {

  private Context context;
  private CardMenuItemClickedListener cardMenuItemClickedListener;
  private List<Commute> commutes;

  public interface CardMenuItemClickedListener {
    void onEditClicked(Commute commute);
    void onDeleteClicked(Commute commute);
  }

  public CommuteListAdapter(Context context,
                            CardMenuItemClickedListener listener) {
    this.context = context;
    this.cardMenuItemClickedListener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.commute_card, parent, false);

    return new ViewHolder(cardView);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    if (commutes == null) {
      return;
    }

    Commute commute = commutes.get(position);
    String title = commute.getName();
    String origin = commute.getOrigin();
    String destination = commute.getDestination();

    if (title != null && !title.isEmpty()) {
      holder.name.setText(title);
    } else {
      holder.name.setVisibility(View.GONE);
    }

    holder.origin.setText(origin);
    holder.destination.setText(destination);
    holder.menu.setOnClickListener(v -> showPopupMenu(v, commute));
  }

  private void showPopupMenu(View v, Commute commute) {
    PopupMenu popupMenu = new PopupMenu(context, v);
    MenuInflater inflater = popupMenu.getMenuInflater();

    inflater.inflate(R.menu.card_menu, popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(
            new CommuteCardMenuClickListener(getItemClickedListener(commute)));
    popupMenu.show();
  }

  private CommuteCardMenuClickListener.CardMenuItemClickedListener getItemClickedListener(Commute commute) {
    return new CommuteCardMenuClickListener.CardMenuItemClickedListener() {
      @Override
      public void onEditClicked() {
        cardMenuItemClickedListener.onEditClicked(commute);
      }

      @Override
      public void onDeleteClicked() {
        cardMenuItemClickedListener.onDeleteClicked(commute);
      }
    };
  }

  @Override
  public int getItemCount() {
    return commutes == null ? 0 : commutes.size();
  }

  public void setCommutes(List<Commute> commutes) {
    this.commutes = commutes;
    notifyDataSetChanged();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    final TextView name;
    final TextView origin;
    final TextView destination;
    final ImageButton menu;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.commuteCardName);
      origin = itemView.findViewById(R.id.commuteCardStart);
      destination = itemView.findViewById(R.id.commuteCardEnd);
      menu = itemView.findViewById(R.id.commuteCardMenu);
    }
  }
}
