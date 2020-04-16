package tomi.projekti;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import tomi.projekti.MainActivity;


public class GetJSON extends AsyncTask<Void, Void, JSONObject>
{

    public interface ICallBack{
        void getTapahtumat(JSONObject jsonObject);
    }

    ICallBack mCallBack;
    String paikkaId;
    public GetJSON(String paikkaId,ICallBack mCallBack){
        this.mCallBack = mCallBack;
        this.paikkaId = paikkaId;
    }

    @Override
    protected JSONObject doInBackground(Void... params)
    {

        String str = "";
        if(paikkaId == ""){
            str="https://api.hel.fi/linkedevents/v1/event/?format=json";
        }else{
            str="https://api.hel.fi/linkedevents/v1/event/?location=tprek:"+paikkaId;
        }
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try
        {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            return new JSONObject(stringBuffer.toString());
        }
        catch(Exception ex)
        {
            Log.e("App", "yourDataTask", ex);
            return null;
        }
        finally
        {
            if(bufferedReader != null)
            {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        if(response != null)
        {
            try {
                mCallBack.getTapahtumat(response);
            } catch (Exception ex) {
                Log.e("App", "Failure returning Response from GETJSON", ex);
            }
        }
    }
}