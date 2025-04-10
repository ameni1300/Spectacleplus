package com.example.spectacleplus;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class TicketBookingActivity extends AppCompatActivity {

    private EditText inputNom, inputEmail, inputTelephone;
    private TextView ticketCountText, totalPrix, totalTicket;
    private ImageButton btnPlus, btnMoins;
    private Button boutonPaiement;
    private int ticketCount = 1;
    private int ticketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Initialisation des éléments de l'interface
        ImageView imageSpectacle = findViewById(R.id.image_spectacle);
        TextView titreSpectacle = findViewById(R.id.titre_spectacle);
        TextView titree = findViewById(R.id.ticket_detailstitre);
        TextView placeSpectacle = findViewById(R.id.places_dispo);
        inputNom = findViewById(R.id.input_nom);
        inputEmail = findViewById(R.id.input_email);
        inputTelephone = findViewById(R.id.input_telephone);
        ticketCountText = findViewById(R.id.ticket_count_text);
        totalPrix = findViewById(R.id.total_price);
        totalTicket = findViewById(R.id.totalticket);
        btnPlus = findViewById(R.id.btn_plus);
        btnMoins = findViewById(R.id.btn_moins);
        boutonPaiement = findViewById(R.id.bouton_paiement);
        TextView heureSpectacle = findViewById(R.id.session_time);
        TextView lieuSpectacle = findViewById(R.id.theater_location);
        TextView dateSpectacle = findViewById(R.id.session_date);

        // Récupération des données de l'intent
        Intent intent = getIntent();
        String titre = intent.getStringExtra("titre");
        String date = intent.getStringExtra("date");
        String heure = intent.getStringExtra("heure");
        String lieu = intent.getStringExtra("lieu");
        String prix = intent.getStringExtra("prix"); // Exemple : "20DT"
        String imageUrl = intent.getStringExtra("image");
        int placeDispo = intent.getIntExtra("place_dispo", 0);

        // Affichage des informations
        titreSpectacle.setText(titre);
        titree.setText(titre);

        if (date != null && date.contains("T")) {
            String[] parts = date.split("T");
            dateSpectacle.setText(parts[0]);
        } else {
            dateSpectacle.setText(date);
        }

        heureSpectacle.setText(heure);
        lieuSpectacle.setText(lieu);
        placeSpectacle.setText("Places disponibles : " + placeDispo);

        // Chargement de l'image
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageSpectacle);

        // Nettoyage et conversion du prix
        if (prix != null && prix.contains("DT")) {
            prix = prix.replace("DT", "").trim();
        }

        try {
            ticketPrice = Integer.parseInt(prix);
        } catch (NumberFormatException e) {
            ticketPrice = 0; // Valeur par défaut si erreur
            e.printStackTrace();
        }

        // Affichage initial du prix
        updatePrice();
        updateTicketCount();

        // Gestion des boutons + et -
        btnPlus.setOnClickListener(v -> {
            if (ticketCount < placeDispo) {
                ticketCount++;
                updatePrice();
                updateTicketCount();
            }
        });

        btnMoins.setOnClickListener(v -> {
            if (ticketCount > 1) {
                ticketCount--;
                updatePrice();
                updateTicketCount();
            }
        });

        // Activation du bouton de paiement uniquement si tous les champs sont remplis
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        inputNom.addTextChangedListener(textWatcher);
        inputEmail.addTextChangedListener(textWatcher);
        inputTelephone.addTextChangedListener(textWatcher);

        // Bouton paiement
        boutonPaiement.setOnClickListener(v -> {
            Intent bintent = new Intent(TicketBookingActivity.this, PaymentActivity.class);
            bintent.putExtra("nom", inputNom.getText().toString());
            bintent.putExtra("email", inputEmail.getText().toString());
            bintent.putExtra("telephone", inputTelephone.getText().toString());
            bintent.putExtra("tickets", ticketCount);
            bintent.putExtra("prix", updatePrice());
            bintent.putExtra("titre", titre);
            bintent.putExtra("date", date);
            bintent.putExtra("heure", heure);
            bintent.putExtra("lieu", lieu);


            startActivity(bintent);
        });
    }

    private int updatePrice() {
        int total = ticketCount * ticketPrice;
        String totalString = total + " DT";
        totalPrix.setText(totalString);
        return total;
    }

    private void updateTicketCount() {
        ticketCountText.setText(String.valueOf(ticketCount));
        totalTicket.setText(String.valueOf(ticketCount));
    }

    private void checkFields() {
        String nom = inputNom.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String telephone = inputTelephone.getText().toString().trim();

        boolean isEmailValid = email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        boolean isPhoneValid = telephone.matches("\\d{8}");

        boutonPaiement.setEnabled(!nom.isEmpty() && !email.isEmpty() && !telephone.isEmpty()  && isEmailValid && isPhoneValid);
    }
}