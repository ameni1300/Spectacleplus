package com.example.spectacleplus;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Assurez-vous que c'est le bon nom de layout (main.xml)

        // Initialisation des boutons
        /*Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);*/
        Button btnnous = findViewById(R.id.btn_nous);

        Button btnExplorer = findViewById(R.id.btn_explorer);

        // Gestion du clic sur le bouton "Se connecter"
        /*btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de login
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                // Animation de transition (optionnelle)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
         // Gestion du clic sur le bouton "Créer un compte"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité d'inscription
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

                // Animation de transition (optionnelle)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });*/



        btnExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de acceuil
                Intent intent = new Intent(MainActivity.this, Acceuil.class);
                startActivity(intent);

                // Animation de transition (optionnelle)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        btnnous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirection vers l'activité de acceuil
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);

                // Animation de transition (optionnelle)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        // Ici vous pourrez ajouter plus tard une vérification si l'utilisateur est déjà connecté
        // pour le rediriger directement vers l'écran d'accueil (AccueilActivity)
    }
}


