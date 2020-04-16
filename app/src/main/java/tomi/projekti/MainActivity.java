package tomi.projekti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {


    ListView listView;
    TapahtumaAdapter tapahtumaAdapter;
    ArrayList<Tapahtuma> tapahtumaLista;
    ArrayList<Paikka> paikkaLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paikkaLista = new ArrayList<>();
        paikkaLista.add(new Paikka("Malmitalo","8740"));
        paikkaLista.add(new Paikka("Myyrmäki Library","18241"));
        paikkaLista.add(new Paikka("Koivukylä Library","19572"));

        tapahtumaLista = new ArrayList<>();
        listView = findViewById(R.id.listView);
        tapahtumaAdapter = new TapahtumaAdapter(MainActivity.this, R.layout.tapahtuma_adapteri_layout,tapahtumaLista);
        listView.setAdapter(tapahtumaAdapter);
        final EditText paikkaIdText = (EditText)findViewById(R.id.placeIdEditText);
        View.OnClickListener kuuntelija = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Haetaan tapahtumia odota hetki", Toast.LENGTH_SHORT).show();
                String paikkaId = getPaikkaIdFromString(paikkaIdText.getText().toString());
                Log.d("paikkaD", "onClick: "+paikkaId+"!!!!");
                GetJSON getJSON = (GetJSON) new GetJSON(paikkaId,new GetJSON.ICallBack() {
                    @Override
                    public void getTapahtumat(JSONObject tapahtumat) {
                        Log.d("getTapah", "getTapahtumaLista: "+tapahtumat.toString());
                        ArrayList tapahtumaLista = parseJSONtoTapahtumaList(tapahtumat);
                        tapahtumaAdapter.addAll(tapahtumaLista);
                    }
                }).execute();

            }
        };

        Button nappi = findViewById(R.id.button);
        nappi.setOnClickListener(kuuntelija);
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

                    JSONObject nameobj = obj.getJSONObject("name");
                    String name = nameobj.getString("fi");

                    String shortdesc = "";
                    try{
                        JSONObject shortdescObj = obj.getJSONObject("short_description");
                        shortdesc = shortdescObj.getString("fi");
                    }catch (Exception e){
                        shortdesc = "Ei saatavilla";
                    }

                    String createdtime = obj.getString("created_time");
                    Tapahtuma t = new Tapahtuma(name, shortdesc, createdtime, infoUrl);
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
