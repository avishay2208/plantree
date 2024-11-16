package com.example.sqlproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlproject.R;
import com.example.sqlproject.entities.Plant;
import com.example.sqlproject.entities.Tree;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleAdapterPlantsUser extends RecyclerView.Adapter<RecycleAdapterPlantsUser.MyViewHolder> {

    Context context;
    private final ArrayList<Plant> plants;
    private final ArrayList<Tree> trees;
    private final RecycleViewClickListener listener;

    public RecycleAdapterPlantsUser(ArrayList<Plant> plants, ArrayList<Tree> trees, RecycleViewClickListener listener, Context context) {
        this.plants = plants;
        this.trees = trees;
        this.context = context;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvPlantID;
        private final TextView tvTreeType;
        private final TextView tvTreeID;
        private final TextView tvPlantAddress;
        private final TextView tvPlantDate;
        private final TextView tvPrice;
        private final ImageView ivTree;

        public MyViewHolder(final View view) {
            super(view);

            tvPlantID = view.findViewById(R.id.tvUSERplantID);
            tvTreeType = view.findViewById(R.id.tvUSERplantTreeType);
            tvTreeID = view.findViewById(R.id.tvUSERplantTreeID);
            tvPlantAddress = view.findViewById(R.id.tvUSERplantAddress);
            tvPlantDate = view.findViewById(R.id.tvUSERplantDate);
            tvPrice = view.findViewById(R.id.tvUSERplantPrice);
            ivTree = view.findViewById(R.id.ivUSERplantTree);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecycleAdapterPlantsUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_user, parent, false);

        return new RecycleAdapterPlantsUser.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterPlantsUser.MyViewHolder holder, int position) {
        Plant plant = plants.get(position);
        Tree tree = trees.get(plant.getTreeID() - 1);

        holder.tvPlantID.setText("Plant ID: " + plant.getPlantID());
        holder.tvTreeType.setText(plant.getTreeName() + " tree");
        holder.tvTreeID.setText("Tree ID: " + plant.getTreeID());
        holder.tvPlantAddress.setText("At: " + plant.getPlantAddress());
        holder.tvPlantDate.setText(String.valueOf(plant.getPlantDate()));
        holder.tvPrice.setText(plant.getPrice() + "₪");
        Picasso.get().load(tree.getImageUrl()).into(holder.ivTree);
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public interface RecycleViewClickListener {
        void onClick(View v, int position);
    }
}