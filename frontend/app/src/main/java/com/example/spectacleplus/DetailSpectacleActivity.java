package com.example.spectacleplus;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class DetailSpectacleActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private String lieu; // Store location as class variable

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_spectacle);

        // Initialize views
        ImageView imageSpectacle = findViewById(R.id.image_spectacle);
        TextView titreSpectacle = findViewById(R.id.titre_spectacle);
        TextView descriptionSpectacle = findViewById(R.id.description_spectacle);
        TextView dateSpectacle = findViewById(R.id.date_spectacle);
        TextView heureSpectacle = findViewById(R.id.heure_spectacle);
        TextView lieuSpectacle = findViewById(R.id.lieu_spectacle);
        TextView prixSpectacle = findViewById(R.id.prix_spectacle);
        Button boutonAchat = findViewById(R.id.bouton_achat);
        TextView placeSpectacle = findViewById(R.id.places_dispo);

        // Get data from intent
        Intent intent = getIntent();
        String titre = intent.getStringExtra("titre");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String heure = intent.getStringExtra("heureDebut");
        lieu = intent.getStringExtra("lieu"); // Store location in class variable
        String prix = intent.getStringExtra("prix");
        String imageUrl = intent.getStringExtra("image");
        int placeDispo = intent.getIntExtra("place_dispo", 0);

        // Display data
        titreSpectacle.setText(titre);
        descriptionSpectacle.setText(description);

        // Format date
        if (date != null && date.contains("T")) {
            dateSpectacle.setText(date.split("T")[0]);
        } else {
            dateSpectacle.setText(date);
        }

        heureSpectacle.setText(heure);
        lieuSpectacle.setText(lieu);
        prixSpectacle.setText(prix);
        placeSpectacle.setText(getString(R.string.places_disponibles, placeDispo));

        // Load image
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageSpectacle);

        // Set click listeners
        setupClickListeners(titre, heure, prix, lieu, date, placeDispo, imageUrl);
    }

    private void setupClickListeners(String titre, String heure, String prix,
                                     String lieu, String date, int placeDispo,
                                     String imageUrl) {
        // Location click
        findViewById(R.id.lieu_spectacle).setOnClickListener(v -> {
            if (checkLocationPermission()) {
                openGoogleMaps();
            }
        });

        // Booking button click
        findViewById(R.id.bouton_achat).setOnClickListener(v -> {
            Intent bookingIntent = new Intent(this, TicketBookingActivity.class);
            bookingIntent.putExtra("titre", titre);
            bookingIntent.putExtra("heure", heure);
            bookingIntent.putExtra("prix", prix);
            bookingIntent.putExtra("lieu", lieu);
            bookingIntent.putExtra("date", date);
            bookingIntent.putExtra("place_dispo", placeDispo);
            bookingIntent.putExtra("image", imageUrl);
            startActivity(bookingIntent);
        });
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return false;
        }
        return true;
    }

    private void openGoogleMaps() {
        try {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(lieu));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            // Fallback to browser
            Uri webpage = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(lieu));
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(browserIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGoogleMaps();
            } else {
                Toast.makeText(this,
                        getString(R.string.location_permission_required),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}