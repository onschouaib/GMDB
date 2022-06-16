package com.dam.gmdb.commons;

public interface NodesNames {

    //clé du fichier shared preferences
    String UPLOAD_PREFS = "dataInsertIntoFireBase";
    // Table Films
    String TABLE_FILM = "films";

    // Les clés pour l'association des champs db **/
    String KEY_TITRE = "titre";
    String KEY_TITRE_MINUSCULE = "titre_minuscule";
    String KEY_ANNEE = "annee";
    String KEY_ACTEURS = "acteurs";
    String KEY_AFFICHE = "affiche";
    String KEY_SYNOPSIS = "synopsis";

    // Les varibles lièes aux emplacements de stockage de Firebase
    String collection = "films";
    String imageFolder = "affiches";
}

