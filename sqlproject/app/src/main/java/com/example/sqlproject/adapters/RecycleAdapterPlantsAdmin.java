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

public class RecycleAdapterPlantsAdmin extends RecyclerView.Adapter<RecycleAdapterPlantsAdmin.MyViewHolder> {

    Context context;
    private final ArrayList<Plant> plants;
    private final ArrayList<Tree> trees;
    private final RecycleAdapterPlantsAdmin.RecycleViewClickListener listener;

    public RecycleAdapterPlantsAdmin(ArrayList<Plant> plants, ArrayList<Tree> trees, RecycleAdapterPlantsAdmin.RecycleViewClickListener listener, Context context) {
        this.plants = plants;
        this.trees = trees;
        this.context = context;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvPlantID;
        private final TextView tvTreeType;
        private final TextView tvTreeID;
        private final TextView tvUserName;
        private final TextView tvUserID;
        private final TextView tvPlantDate;
        private final TextView tvPlantAddress;
        private final TextView tvPrice;
        private final ImageView ivTree;

        public MyViewHolder(final View view) {
            super(view);

            tvPlantID = view.findViewById(R.id.tvADMINplantID);
            tvTreeType = view.findViewById(R.id.tvADMINplantTreeType);
            tvTreeID = view.findViewById(R.id.tvADMINplantTreeID);
            tvUserName = view.findViewById(R.id.tvADMINplantUserName);
            tvUserID = view.findViewById(R.id.tvADMINplantUserID);
            tvPlantDate = view.findViewById(R.id.tvADMINplantDate);
            tvPlantAddress = view.findViewById(R.id.tvADMINplantAddress);
            tvPrice = view.findViewById(R.id.tvADMINplantPrice);
            ivTree = view.findViewById(R.id.ivADMINplantTree);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecycleAdapterPlantsAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plant_admin, parent, false);
        return new RecycleAdapterPlantsAdmin.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterPlantsAdmin.MyViewHolder holder, int position) {
        Plant plant = plants.get(position);
        Tree tree = trees.get(plant.getTreeID() - 1);

        holder.tvPlantID.setText("Plant ID: " + plant.getPlantID());
        holder.tvTreeType.setText(plant.getTreeName() + " tree");
        holder.tvTreeID.setText("Tree ID: " + plant.getTreeID());
        holder.tvUserName.setText("By: " + plant.getUserName());
        holder.tvUserID.setText("User ID: " + plant.getUserID());
        holder.tvPlantDate.setText(String.valueOf(plant.getPlantDate()));
        holder.tvPlantAddress.setText("At: " + plant.getPlantAddress());
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