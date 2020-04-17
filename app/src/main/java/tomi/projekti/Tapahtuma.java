package tomi.projekti;

public class Tapahtuma {

    private String nimi; //{name}.fi
    private String short_description; //{short_description}.fi
    private String long_description;
    private String start_time;
    private String info_url; // {info_url}.fi


    public Tapahtuma(String nimi, String short_description, String created_time, String info_url, String long_description){
        this.start_time = start_time;
        this.short_description = short_description;
        this.info_url = info_url;
        this.nimi = nimi;
        this.long_description = long_description;
    }

    public String getNimi() {
        return this.nimi;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getInfo_url() {
        return info_url;
    }

    public String getLong_description() {
        return long_description;
    }
}
