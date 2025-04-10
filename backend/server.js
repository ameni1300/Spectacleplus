const express = require('express');
const cors = require('cors');
require('dotenv').config();
const db = require('./db');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const auth = require('./middleware/auth'); // Importation du middleware

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT || 5000;
app.use('/public', express.static('public'));
//app.use('/public/images', express.static(__dirname + 'public/images'));
// Route principale
app.get('/spectacles', (req, res) => {
    const sql = 'SELECT * FROM spectacles';
    db.query(sql, (err, result) => {
        if (err) return res.status(500).json(err);
        res.json(result);
    });
});

app.post('/reserver', (req, res) => {
    const {
        nom,
        email,
        telephone,
        tickets,
        prix_total,
        titre_spectacle,
        date_spectacle,
        heure_spectacle,
        lieu_spectacle
    } = req.body;

    const sql = `INSERT INTO client_reservation 
        (nom, email, telephone, tickets, prix_total, titre_spectacle, date_spectacle, heure_spectacle, lieu_spectacle) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`;

    db.query(sql, [nom, email, telephone, tickets, prix_total, titre_spectacle, date_spectacle, heure_spectacle, lieu_spectacle], (err, result) => {
        if (err) {
            console.error('Erreur lors de l\'insertion :', err);
            return res.status(500).json({ message: 'Erreur lors de la réservation' });
        }
        res.status(201).json({ message: 'Réservation enregistrée avec succès' });
    });
});


// Démarrer le serveur
app.listen(PORT, () => {
    console.log(`Serveur démarré sur http://localhost:${PORT}`);
});
