package tomi.projekti;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class TapahtumaAdapter extends ArrayAdapter<Tapahtuma> {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<Tapahtuma> tapahtumat;

    TextView nimi;
    TextView aika;
    TextView info;
    TextView selostus;


    public TapahtumaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Tapahtuma> tapahtumat) {
        super(context, resource, tapahtumat);
        this.layoutInflater = LayoutInflater.from(context);
        this.tapahtumat = tapahtumat;
        this.context = context;
    }

    @Override
    public void addAll(@NonNull Collection<? extends Tapahtuma> collection) {
        super.addAll(collection);
        tapahtumat.clear();
        tapahtumat.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void add(@Nullable Tapahtuma object) {
        super.add(object);
        tapahtumat.add(object);
        notifyDataSetChanged();
    }


    //https://stackoverflow.com/questions/34383763/how-to-open-a-popup-window-from-an-adapter-class

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final View view = layoutInflater.inflate(R.layout.tapahtuma_adapteri_layout, parent, false);

        nimi = (TextView)view.findViewById(R.id.nimiTextView);
        /*aika = (TextView)view.findViewById(R.id.aikaTextView);
        info = (TextView)view.findViewById(R.id.infoTextView);
        selostus = (TextView)view.findViewById(R.id.selostusTextView);*/

        final Tapahtuma t = tapahtumat.get(position);

        nimi.setText(t.getNimi());
        /*aika.setText(t.getStart_time());
        info.setText(t.getInfo_url());
        selostus.setText(t.getShort_description());*/


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Sulje tietokenttä painamalla", Toast.LENGTH_SHORT).show();
                showTapahtumaInfo(view,t);

            }
        });


        return view;
    }

    public void showTapahtumaInfo(View view, Tapahtuma tapahtuma){
        View tapahtumaView = layoutInflater.inflate(R.layout.tapahtuma_pop, null);
        final PopupWindow tapahtumaWindow = new PopupWindow(tapahtumaView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView popNimi = tapahtumaView.findViewById(R.id.nimiPopText);
        TextView popAika = tapahtumaView.findViewById(R.id.aikaPopText);
        TextView popInfo = tapahtumaView.findViewById(R.id.infoPopText);
        TextView popLyhyt = tapahtumaView.findViewById(R.id.lyhytPopText);
        TextView popPitka = tapahtumaView.findViewById(R.id.pitkaPopText);

        Log.d("niminimi", "showTapahtumaInfo: "+tapahtuma.getNimi());
        popNimi.setText(tapahtuma.getNimi());
        Log.d("niminimi", "showTapahtumaInfo: "+tapahtuma.getStart_time());
        popAika.setText(tapahtuma.getStart_time());
        popInfo.setText(tapahtuma.getInfo_url());
        popLyhyt.setText(tapahtuma.getShort_description());
        popPitka.setText(tapahtuma.getLong_description());

        tapahtumaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapahtumaWindow.dismiss();
            }
        });
        tapahtumaWindow.showAsDropDown(tapahtumaView,0,0);
    }
}
