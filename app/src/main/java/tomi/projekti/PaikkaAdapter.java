package tomi.projekti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class PaikkaAdapter extends ArrayAdapter<Paikka> {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<Paikka> paikat;

    public PaikkaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Paikka> paikat) {
        super(context, resource, paikat);
        this.layoutInflater = LayoutInflater.from(context);
        this.paikat = paikat;
        this.context = context;
    }

    @Override
    public void addAll(@NonNull Collection<? extends Paikka> collection) {
        super.addAll(collection);
        paikat.clear();
        paikat.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void add(@Nullable Paikka object) {
        super.add(object);
        paikat.add(object);
        notifyDataSetChanged();
    }

    public Paikka getPaikka(int position){
        return paikat.get(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView text = new TextView(context);
        text.setText(paikat.get(position).getNimi());
        return text;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);


        Paikka p = paikat.get(position);



        return view;
    }
}
