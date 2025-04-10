package com.example.spectacleplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    private LinearLayout cardApplePay, cardMasterCard, cardPayPal, cardCarteJeune;
    private Button buttonPayer;
    private String selectedPaymentMethod = null;
    private String nom, email, telephone, titre, date, heure, lieu, dateS;
    private int prix, tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Initialisation des vues
        cardApplePay = findViewById(R.id.cardApplePay);
        cardMasterCard = findViewById(R.id.cardMasterCard);
        cardPayPal = findViewById(R.id.cardPayPal);
        cardCarteJeune = findViewById(R.id.cardCarteJeune);
        buttonPayer = findViewById(R.id.buttonPayer);
        TextView textMontant = findViewById(R.id.textMontant);

        // Récupération des données de l'intent
        Intent intent = getIntent();
        nom = intent.getStringExtra("nom");
        email = intent.getStringExtra("email");
        telephone = intent.getStringExtra("telephone");
        titre = intent.getStringExtra("titre");
        date = intent.getStringExtra("date");
        heure = intent.getStringExtra("heure");
        lieu = intent.getStringExtra("lieu");
        prix = intent.getIntExtra("prix", 0);
        tickets = intent.getIntExtra("tickets", 0);

        // Traitement de la date
        dateS = date; // Initialisation avec la date complète
        if (date != null && date.contains("T")) {
            String[] parts = date.split("T");
            dateS = parts[0]; // On ne garde que la partie date
        }

        textMontant.setText("Montant total : " + prix + " DT");

        // Gestion des méthodes de paiement
        setupPaymentMethods();

        // Action du bouton Payer
        buttonPayer.setOnClickListener(v -> {
            if (selectedPaymentMethod == null) {
                Toast.makeText(this, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
            } else {
                enregistrerReservation();
            }
        });
    }

    private void setupPaymentMethods() {
        cardApplePay.setOnClickListener(v -> selectPaymentMethod("Visa", cardApplePay));
        cardMasterCard.setOnClickListener(v -> selectPaymentMethod("MasterCard", cardMasterCard));
        cardPayPal.setOnClickListener(v -> selectPaymentMethod("E-Dinar Smart", cardPayPal));
        cardCarteJeune.setOnClickListener(v -> selectPaymentMethod("Carte Jeune", cardCarteJeune));
    }

    private void selectPaymentMethod(String method, LinearLayout card) {
        deselectAll();
        card.setBackgroundResource(R.drawable.payment_option_selection);
        selectedPaymentMethod = method;
        buttonPayer.setEnabled(true);
    }

    private void deselectAll() {
        cardApplePay.setBackgroundResource(R.drawable.payment_option_background);
        cardMasterCard.setBackgroundResource(R.drawable.payment_option_background);
        cardPayPal.setBackgroundResource(R.drawable.payment_option_background);
        cardCarteJeune.setBackgroundResource(R.drawable.payment_option_background);
    }

    private void enregistrerReservation() {
        // Création de l'objet Reservation
        Reservation reservation = new Reservation(
                nom,
                email,
                telephone,
                tickets,
                prix,
                titre,
                dateS, // Utilisation de dateS traitée
                heure,
                lieu
        );

        Log.d("RESERVATION_DATA", new Gson().toJson(reservation));

        // Envoi à l'API
        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.enregistrerReservation(reservation);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("API_ERROR", "Code: " + response.code() + " | " + error);
                        runOnUiThread(() ->
                                Toast.makeText(PaymentActivity.this,
                                        "Erreur serveur: " + error, Toast.LENGTH_LONG).show());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                // Redirection vers l'écran de succès
                Intent successIntent = new Intent(PaymentActivity.this, PaymentSuccessActivity.class);
                successIntent.putExtra("nom", nom);
                successIntent.putExtra("email", email);
                successIntent.putExtra("tickets", tickets);
                successIntent.putExtra("total", prix);
                successIntent.putExtra("titre", titre);
                successIntent.putExtra("date", date);
                successIntent.putExtra("heure", heure);
                successIntent.putExtra("lieu", lieu);
                startActivity(successIntent);
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("NETWORK_ERROR", "Erreur: " + t.getMessage());
                runOnUiThread(() ->
                        Toast.makeText(PaymentActivity.this,
                                "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}