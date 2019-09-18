package com.pancholi.commuter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pancholi.commuter.database.Commute;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CommuteListAdapter extends RecyclerView.Adapter<CommuteListAdapter.ViewHolder> {

  private Context context;
  private CommuteCardMenuClickListener.CardMenuItemClickedListener cardMenuItemClickedListener;
  private List<Commute> commutes;

  public CommuteListAdapter(Context context,
                            CommuteCardMenuClickListener.CardMenuItemClickedListener listener) {
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
    holder.menu.setOnClickListener(this::showPopupMenu);
  }

  private void showPopupMenu(View v) {
    PopupMenu popupMenu = new PopupMenu(context, v);
    MenuInflater inflater = popupMenu.getMenuInflater();

    inflater.inflate(R.menu.card_menu, popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(new CommuteCardMenuClickListener(cardMenuItemClickedListener));
    popupMenu.show();
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

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      name = itemView.findViewById(R.id.commuteCardName);
      origin = itemView.findViewById(R.id.commuteCardStart);
      destination = itemView.findViewById(R.id.commuteCardEnd);
      menu = itemView.findViewById(R.id.commuteCardMenu);
    }
  }
}
