package hr.agrokor.tvz.denisglad.grupolovac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Denis on 31.10.2015..
 */
public class CustomAdapterProizvod extends ArrayAdapter{

    CustomAdapterProizvod(Context context, String[][] proizvodi){
        super(context, R.layout.proizvodi_row, proizvodi);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.proizvodi_row, parent, false);

        String[] redak = (String[]) getItem(position);
//        Log.i("redak", redak[position]);

        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.checkProizvod);
        TextView txtNaziv = (TextView) customView.findViewById(R.id.txtNazivProizvoda);

        if(redak[3] == "true"){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        txtNaziv.setText(redak[0]);

        /*


//        Log.i("redak", redak[position]);
        CheckBox checkProizvod = (CheckBox) customView.findViewById(R.id.checkProizvod);
        TextView txtNaziv = (TextView) customView.findViewById(R.id.txtNazivProizvoda);

        checkProizvod.setChecked(true);
        txtNaziv.setText((String)getItem(position));

*/
        return customView;
    }
}
