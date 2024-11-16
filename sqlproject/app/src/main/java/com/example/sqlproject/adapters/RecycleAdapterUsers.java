package com.example.sqlproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlproject.R;
import com.example.sqlproject.entities.User;

import java.util.List;

public class RecycleAdapterUsers extends RecyclerView.Adapter<RecycleAdapterUsers.MyViewHolder> {
    Context context;
    private final List<User> users;
    private final RecycleAdapterUsers.RecycleViewClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public RecycleAdapterUsers(List<User> users, RecycleAdapterUsers.RecycleViewClickListener listener, Context context) {
        this.users = users;
        this.context = context;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateUsers(List<User> newUsers) {
        this.users.clear();
        this.users.addAll(newUsers);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvUserName;
        private final TextView tvUserID;
        private final TextView tvUserPhone;

        public MyViewHolder(final View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvItemUserName);
            tvUserID = view.findViewById(R.id.tvItemUserID);
            tvUserPhone = view.findViewById(R.id.tvItemUserPhone);

            view.setOnClickListener(this);
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecycleAdapterUsers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new RecycleAdapterUsers.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterUsers.MyViewHolder holder, int position) {
        User user = users.get(position);

        holder.tvUserName.setText(user.getFullName());
        holder.tvUserID.setText("user ID: " + user.getID());
        holder.tvUserPhone.setText(user.getPhoneNumber());

        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FF2B2D30"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#B2C8D6"));
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface RecycleViewClickListener {
        void onClick(View v, int position);
    }
}
