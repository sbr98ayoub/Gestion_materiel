    package com.example.my_parc.model;



    public enum Type {
        ORDINATEUR("Ordinateur"),
        ORDINATEUR_PORTABLE("Ordinateur Portable"),
        ECRAN("Écran"),
        SOURIS("Souris"),
        UNITE_CENTRAL("Unité Centrale"),
        SWITCH("Switch"),
        ROUTEUR("Routeur");

        private final String displayName;

        Type(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }


