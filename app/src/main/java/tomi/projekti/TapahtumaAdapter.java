package tomi.projekti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class TapahtumaAdapter extends ArrayAdapter<Tapahtuma> {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<Tapahtuma> tapahtumat;

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.tapahtuma_adapteri_layout, parent, false);

        TextView nimi = (TextView)view.findViewById(R.id.nimiTextView);
        TextView aika = (TextView)view.findViewById(R.id.aikaTextView);
        TextView info = (TextView)view.findViewById(R.id.infoTextView);
        TextView selostus = (TextView)view.findViewById(R.id.selostusTextView);

        Tapahtuma t = tapahtumat.get(position);

        nimi.setText(t.getNimi());
        aika.setText(t.getCreated_time());
        info.setText(t.getInfo_url());
        selostus.setText(t.getShort_description());


        return view;
    }
}
