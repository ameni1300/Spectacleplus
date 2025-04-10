package com.example.spectacleplus;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SpectacleAdapter extends RecyclerView.Adapter<SpectacleAdapter.ViewHolder> {
    private List<Spectacle> spectacleList;
    private Context context;

    public SpectacleAdapter(Context context, List<Spectacle> spectacleList) {
        this.context = context;
        this.spectacleList = spectacleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spectacle, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spectacle spectacle = spectacleList.get(position);
        String imageUrl = "http://192.168.100.12:5000/public/images/" + spectacle.getimageResId();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);

        holder.titre.setText(spectacle.getTitre());
        holder.artiste.setText(spectacle.getNomArtiste());
        String fullDate = spectacle.getDate();
        String dateOnly = fullDate.split("T")[0]; // Prend seulement "2025-04-07"
        holder.date.setText(dateOnly);



        holder.localisation.setText(spectacle.getlocalisation());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailSpectacleActivity.class);
            intent.putExtra("titre", spectacle.getTitre());
            intent.putExtra("date", spectacle.getDate() );
            intent.putExtra("heureDebut", spectacle.getHeureDebut() );
            intent.putExtra("prix", spectacle.getPrixTicket());
            intent.putExtra("image","http://192.168.100.12:5000/public/images/" + spectacle.getimageResId());
            intent.putExtra("description", spectacle.getDescription());
            intent.putExtra("lieu", spectacle.getLieu());
            intent.putExtra("nomArtiste", spectacle.getNomArtiste());
            intent.putExtra("localisation", spectacle.getlocalisation());
            intent.putExtra("place_dispo", spectacle.getplacedispo());

            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return spectacleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titre, date, artiste,localisation;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_image);
            titre = itemView.findViewById(R.id.tv_titre);
            artiste = itemView.findViewById(R.id.tv_artiste);
            date = itemView.findViewById(R.id.spectacle_date);
            localisation = itemView.findViewById(R.id.spectacle_localisation);

        }
    }
}
