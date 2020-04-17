package tomi.projekti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    TextView paikkaTextView;
    ListView listView;
    TapahtumaAdapter tapahtumaAdapter;
    PaikkaAdapter paikkaAdapter;
    ArrayList<Tapahtuma> tapahtumaLista;
    ArrayList<Paikka> paikkaLista;
    ArrayList<String> divisionList;
    String paikkaId;
    String tapahtumaUrl;
    GetJSON getJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paikkaLista = new ArrayList<>();
        paikkaTextView = findViewById(R.id.paikkaTextView);
        tapahtumaLista = new ArrayList<>();
        divisionList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        tapahtumaAdapter = new TapahtumaAdapter(MainActivity.this, R.layout.tapahtuma_adapteri_layout,tapahtumaLista);
        paikkaAdapter = new PaikkaAdapter(this, android.R.layout.simple_spinner_item,paikkaLista);
        paikkaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        final Spinner paikkaSpinner = (Spinner)findViewById(R.id.spinner);
        paikkaSpinner.setAdapter(paikkaAdapter);
        listView.setAdapter(tapahtumaAdapter);
        final EditText haePaikkaEditText = (EditText)findViewById(R.id.haePaikkaEditText);
        paikkaId = "";




        //Tapahtumien haku
        View.OnClickListener tapahtumaHakuKuuntelija = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Haetaan tapahtumia odota hetki", Toast.LENGTH_SHORT).show();

                Log.d("paikkaD", "onClick: "+paikkaId+"!!!!");
                tapahtumaUrl = "";
                Log.d("urli", "onClick PAIKKAID: "+paikkaId);
                if(paikkaId == "" || !paikkaId.contains("tprek")){
                    String division = "";
                    try{
                        division = divisionList.get(0);
                        Log.d("urli", "onClick DIVISION: "+division);
                        tapahtumaUrl = "https://api.hel.fi/linkedevents/v1/event/?division="+division;
                        paikkaTextView.setText(division);
                    }catch (Exception ignored){
                        tapahtumaUrl = "https://api.hel.fi/linkedevents/v1/event/?format=json";
                    }
                }else if(paikkaId.contains("tprek")){
                    tapahtumaUrl = "https://api.hel.fi/linkedevents/v1/event/?location="+paikkaId;
                }else{
                    tapahtumaUrl = "https://api.hel.fi/linkedevents/v1/event/?format=json";
                }

                Log.d("urli", "onClick URLI: "+tapahtumaUrl);

                getJSON = (GetJSON) new GetJSON(tapahtumaUrl,new GetJSON.ICallBack() {
                    @Override
                    public void getJSONResponse(JSONObject tapahtumat) {
                        ArrayList tapahtumaLista = parseJSONtoTapahtumaList(tapahtumat);
                        tapahtumaAdapter.addAll(tapahtumaLista);
                    }
                }).execute();
                if(tapahtumaAdapter.getCount() == 0){
                    Log.d("TYHJA LISTA ALERT", "onClick: TAPAHTUMA LISTA ON TYHJÄ Lista on tyhjä");
                }



            }
        };
        Button nappi = findViewById(R.id.button);
        nappi.setOnClickListener(tapahtumaHakuKuuntelija);
        //tapahtumien haku loppuu

        //paikkojen haku alkaa

        View.OnClickListener paikkaHakuKuuntelija = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Haetaan paikkoja odota hetki", Toast.LENGTH_SHORT).show();
                String paikkaHakuUrl = "";
                String haettavaPaikka = haePaikkaEditText.getText().toString();
                paikkaHakuUrl = "https://api.hel.fi/linkedevents/v1/search/?type=place&input="+haettavaPaikka;
                getJSON = (GetJSON) new GetJSON(paikkaHakuUrl, new GetJSON.ICallBack() {
                    @Override
                    public void getJSONResponse(JSONObject jsonObject) {
                        paikkaLista = parseJSONtoPaikkaList(jsonObject);
                        paikkaAdapter.addAll(paikkaLista);
                        Toast.makeText(MainActivity.this, "Paikat haettu, valitse paikka Spinneristä ylempää", Toast.LENGTH_SHORT).show();
                        paikkaTextView.setText("Kaikki alueet");
                        //tänne vois olla joku animaatio spinneriin
                    }
                }).execute();
            }
        };

        Button paikkaNappi = findViewById(R.id.haePaikkaButton);
        paikkaNappi.setOnClickListener(paikkaHakuKuuntelija);
        //paikkojen haku loppuu


        //paikkojen spinneri alkaa
        paikkaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paikkaTextView.setText(paikkaAdapter.getPaikka(position).getNimi());
                paikkaId = paikkaAdapter.getPaikka(position).getId();
                if(!paikkaId.contains("tprek")){
                    Log.d("paikka", "onItemSelected: "+paikkaId);
                    Toast.makeText(MainActivity.this, "Paikalla ei ole validia ID:tä, valitse toinen paikka tai tee haku koko alueelle.", Toast.LENGTH_LONG).show();
                }
                divisionList = paikkaAdapter.getPaikka(position).getDivisions();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paikkaTextView.setText("Kaikki alueet");
            }
        });

    }
        //paikkojen spinneri loppuu

    public ArrayList<Paikka> parseJSONtoPaikkaList(JSONObject jsonObject){
        ArrayList<Paikka> paikkaLista = new ArrayList<>();
        if(jsonObject != null){
            try{
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);
                    String tprekId = "", nimi = "";
                    ArrayList<String> divisions = new ArrayList<>();
                    try{
                        JSONArray divJsonArr = obj.getJSONArray("divisions");

                        for(int j = 0; i < divJsonArr.length();i++){
                            JSONObject arrObj = divJsonArr.getJSONObject(i);
                            JSONObject nameObj = arrObj.getJSONObject("name");
                            String alueenNimi = nameObj.getString("fi");
                            divisions.add(alueenNimi);
                        }
                    }catch (Exception e){
                        Log.e("App", "parseJSONtoPaikkaList error parsin divisions array");
                    }



                    try{
                        tprekId = obj.getString("id");
                    }catch (Exception e){
                        Log.e("App", "parseJSONtoPaikkaList: EI OLE TPREK tällä objektilla");
                    }

                    try{
                        JSONObject nimiObj = obj.getJSONObject("name");
                        nimi = nimiObj.getString("fi");
                    }catch (Exception e){
                        Log.e("App", "parseJSONtoPaikkaList: ");
                    }

                    if(nimi != "" && tprekId != ""){
                        Paikka p = new Paikka(nimi,tprekId,divisions);
                        paikkaLista.add(p);
                    }

                }
            }catch(Exception e){
                Log.e("App", "Error parsin' paikkalist");

            }
        }

        return paikkaLista;
    }

    public ArrayList<Tapahtuma> parseJSONtoTapahtumaList(JSONObject jsonObject){
        ArrayList<Tapahtuma> tapahtumaLista = new ArrayList();
        if(jsonObject != null)
        {
            try {

                JSONArray data = jsonObject.getJSONArray("data");
                Log.d("JSONOBJ", "onPostExecute: "+data.toString());
                for (int i = 0; i < data.length(); i++) {

                    JSONObject obj = data.getJSONObject(i);

                    String infoUrl = "";
                    try{
                        JSONObject info_url = obj.getJSONObject("info_url");
                        infoUrl = info_url.getString("fi");
                    }catch (Exception e){
                        infoUrl = "Ei saatavilla";
                    }

                    String name = "";
                    try{
                        JSONObject nameobj = obj.getJSONObject("name");
                        name = nameobj.getString("fi");
                        if(name == ""){
                            name = nameobj.getString("en");
                        }
                        if(name == ""){
                            name = nameobj.getString("sv");
                        }
                    }catch (Exception ignored){
                        continue;
                    }


                    String shortdesc = "";
                    try{
                        JSONObject shortdescObj = obj.getJSONObject("short_description");
                        shortdesc = shortdescObj.getString("fi");
                    }catch (Exception e){
                        shortdesc = "Ei saatavilla";
                    }
                    String longdesc = "";
                    try{
                        JSONObject longDescObj = obj.getJSONObject("description");
                        longdesc = longDescObj.getString("fi");
                    }catch (Exception ignored){}

                    String starttime = obj.getString("start_time");
                    Tapahtuma t = new Tapahtuma(name, shortdesc, starttime, infoUrl,longdesc);
                    tapahtumaLista.add(t);

                }

                Log.e("App", "Parsed tapahtumat successfully!" );
            } catch (Exception ex) {
                Log.e("App", "Failure parsin' tapahtumat", ex);
            }
        }
        return tapahtumaLista;
    }

    public String getPaikkaIdFromString(String paikanNimi){
        Paikka haettuPaikka = null;
        for (Paikka p: paikkaLista)
        {
            if(p.getNimi().toLowerCase().equals(paikanNimi.toLowerCase())){
                haettuPaikka = p;
                break;
            }else if(p.getNimi().toLowerCase().contains(paikanNimi.toLowerCase())){
                haettuPaikka = p;
            }
        }
        if(haettuPaikka == null){
            return "";
        }
        return haettuPaikka.getId();
    }

}
