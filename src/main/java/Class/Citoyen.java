package Class;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Citoyen {

    private long uid;
    private final String nom;
    private final String prenom;

    private final char sexe;

    private final int age;
    private final LocalDate dateDeNaissance;
    private final String villeDeNaissance;

    private final float taille;

    private final String adresse;
    private final int codePostale;
    private final String ville;
    private final String departement;
    private final String region;

    private List<Citoyen> amis;

    public Citoyen(String nom,
                   String prenom,
                   char sexe,
                   int age,
                   LocalDate dateDeNaissance,
                   String villeDeNaissance,
                   float taille,
                   String adresse,
                   int codePostale,
                   String ville,
                   String departement,
                   String region) {

        this.nom                = nom;
        this.prenom             = prenom;
        this.sexe               = sexe;
        this.age                = age;
        this.dateDeNaissance    = dateDeNaissance;
        this.villeDeNaissance   = villeDeNaissance;
        this.taille             = taille;
        this.adresse            = adresse;
        this.codePostale        = codePostale;
        this.ville              = ville;
        this.departement        = departement;
        this.region             = region;

        this.amis               = new ArrayList<>();
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public char getSexe() {
        return sexe;
    }

    public int getAge() {
        return age;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public String getVilleDeNaissance() {
        return villeDeNaissance;
    }

    public float getTaille() {
        return taille;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getCodePostale() {
        return codePostale;
    }

    public String getVille() {
        return ville;
    }

    public String getDepartement() {
        return departement;
    }

    public String getRegion() {
        return region;
    }

    public void ajouterAmis(Citoyen c) {
        this.amis.add(c);
    }

    public List<Citoyen> getAmis() {
        return amis;
    }
}
