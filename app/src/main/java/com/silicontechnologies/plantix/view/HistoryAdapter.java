package com.silicontechnologies.plantix.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silicontechnologies.plantix.R;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

  private Context context;

  public HistoryAdapter(Context context) {
    this.context = context;
  }

  @NonNull
  @Override
  public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View inflate = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
    return new HistoryViewHolder(inflate);
  }

  @Override
  public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 1;
  }

  public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public HistoryViewHolder(View itemView) {
      super(itemView);
    }
  }
}
