package tomi.projekti;

public class Paikka {
    private String nimi;
    private String id;

    public Paikka(String nimi, String id) {
        this.nimi = nimi;
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getId() {
        return id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setId(String id) {
        this.id = id;
    }
}
