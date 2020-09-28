package com.seawindsolution.podphotographer.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.seawindsolution.podphotographer.R;

import java.util.ArrayList;
import java.util.List;

public class My_Notification_s extends RecyclerView.Adapter<My_Notification_s.ViewHolder> {

    public Context context;
    private List<Notification_PF> arrayList;

    private OnRecyclerViewClickListener mOnClickListener = null;
    private OnRecyclerViewClickContinueListener mOnClickContinueListener = null;

    public interface OnRecyclerViewClickListener {
        void onItemClick(View view, String type, String cid, String pid, String gid);
    }

    public void setOnItemClickListener(OnRecyclerViewClickListener listener) {
        this.mOnClickListener = listener;
    }

    public interface OnRecyclerViewClickContinueListener {
        void onItemClickLisner(View view, String id);
    }

    public void setOnItemClickContinueListener(OnRecyclerViewClickContinueListener listener) {
        this.mOnClickContinueListener = listener;
    }

    public My_Notification_s(Context context, ArrayList<Notification_PF> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_notification_ui, viewGroup, false);

        ViewHolder V = new ViewHolder(view);
        return V;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Notification_PF notification_pf = arrayList.get(i);

        viewHolder.title.setText(notification_pf.getTitle());
        viewHolder.date.setText(notification_pf.getEntDt());
        viewHolder.cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (mOnClickListener != null) {
                        mOnClickListener.onItemClick(v, notification_pf.getType(), notification_pf.getCustomerId(), notification_pf.getPhotographerId(), notification_pf.getGeneralId());
                    }
                } catch (Exception e) {

                }

            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mOnClickContinueListener != null) {
                        mOnClickContinueListener.onItemClickLisner(v, notification_pf.getId());
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;
        CardView cards;
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            cards = (CardView) view.findViewById(R.id.cards);
            imageView = view.findViewById(R.id.ic_close);
        }
    }
}
