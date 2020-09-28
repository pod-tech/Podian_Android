package com.seawindsolution.podphotographer.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.seawindsolution.podphotographer.Pojo.Chat_adapter_p;
import com.seawindsolution.podphotographer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ronak Gopani on 27/12/19 at 4:30 PM.
 */
class Chat_adapter extends RecyclerView.Adapter<Chat_adapter.ViewHolder> {

    public String CustomerId, Type, Title, Lat, Lng, address;
    public Context context;
    private List<Chat_adapter_p> arrayList;

    private Chat_adapter.OnRecyclerViewClickListener mOnClickListener = null;
    private Chat_adapter.OnRecyclerViewClickContinueListener mOnClickContinueListener = null;

    public Chat_adapter(Context context, ArrayList<Chat_adapter_p> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public interface OnRecyclerViewClickListener {
        void onItemClick(View view, String id, String role);
    }

    public void setOnItemClickListener(Chat_adapter.OnRecyclerViewClickListener listener) {
        this.mOnClickListener = listener;
    }

    public interface OnRecyclerViewClickContinueListener {
        void onItemClickLisner(View view, String id, String role, String add, String lat, String lon);
    }

    public void setOnItemClickContinueListener(Chat_adapter.OnRecyclerViewClickContinueListener listener) {
        this.mOnClickContinueListener = listener;
    }

    @NonNull
    @Override
    public Chat_adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_ui_integrate, viewGroup, false);

        Chat_adapter.ViewHolder V = new Chat_adapter.ViewHolder(view);
        return V;
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_adapter.ViewHolder viewHolder, int i) {
        Chat_adapter_p chat_adapter_p = arrayList.get(i);

        if (chat_adapter_p.getuType().equals("c")) {
            viewHolder.text_message_body.setVisibility(View.VISIBLE);
            viewHolder.image_message_profile.setVisibility(View.VISIBLE);
            viewHolder.text_message_name.setVisibility(View.VISIBLE);
            viewHolder.text_message_body.setText(chat_adapter_p.getMessage());

            viewHolder.text_message_body_s.setVisibility(View.GONE);
            viewHolder.image_message_profile_s.setVisibility(View.GONE);
            viewHolder.text_message_name_s.setVisibility(View.GONE);
        } else {

            viewHolder.text_message_body.setVisibility(View.GONE);
            viewHolder.image_message_profile.setVisibility(View.GONE);
            viewHolder.text_message_name.setVisibility(View.GONE);

            viewHolder.text_message_body_s.setText(chat_adapter_p.getMessage());
            viewHolder.text_message_body_s.setVisibility(View.VISIBLE);
            viewHolder.image_message_profile_s.setVisibility(View.VISIBLE);
            viewHolder.text_message_name_s.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = chat_adapter_p.getReceiver().toString();

                System.out.println(name.startsWith("https://www.google.com/maps/search/"));    // true

                if ( chat_adapter_p.getIsLocation().equals("Yes")) {

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(chat_adapter_p.getMessage().toString()));
                    context.startActivity(intent);
                } else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_message_body, text_message_body_s, text_message_name, text_message_name_s;
        ImageView image_message_profile, image_message_profile_s;

        ViewHolder(View view) {
            super(view);

            text_message_body = view.findViewById(R.id.text_message_body);
            text_message_name = view.findViewById(R.id.text_message_name);
            image_message_profile = view.findViewById(R.id.image_message_profile);

            text_message_body_s = view.findViewById(R.id.text_message_body_s);
            image_message_profile_s = view.findViewById(R.id.image_message_profile_s);
            text_message_name_s = view.findViewById(R.id.text_message_name_s);

        }
    }
}
