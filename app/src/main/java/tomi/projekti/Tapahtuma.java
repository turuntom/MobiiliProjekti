package tomi.projekti;

public class Tapahtuma {

    private String nimi; //{name}.fi
    private String short_description; //{short_description}.fi
    private String created_time;
    private String info_url; // {info_url}.fi


    public Tapahtuma(String nimi, String short_description, String created_time, String info_url){
        this.created_time = created_time;
        this.short_description = short_description;
        this.info_url = info_url;
        this.nimi = nimi;
    }

    public String getNimi() {
        return this.nimi;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getInfo_url() {
        return info_url;
    }
}
