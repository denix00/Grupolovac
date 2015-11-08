package hr.agrokor.tvz.denisglad.grupolovac;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by Denis on 28.10.2015..
 */
public class CustomAdapterTrgovina extends ArrayAdapter {

    CustomAdapterTrgovina(Context context, String[][] trgovine){
        super(context, R.layout.trgovine_row, trgovine);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.trgovine_row, parent, false);

        String[] redak = (String[]) getItem(position);
//        Log.i("redak", redak[position]);

        CheckBox checkBox = (CheckBox) customView.findViewById(R.id.checkTrgovina);
        TextView txtGrad = (TextView) customView.findViewById(R.id.txtGrad);
        TextView txtUlica = (TextView) customView.findViewById(R.id.txtUlica);
        TextView txtRok = (TextView) customView.findViewById(R.id.txtRok);

        Log.i("check", "CustomAdapter, redak["+position+"]:" + redak[4]);
        if(redak[4].equals("true")){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
        txtGrad.setText(redak[0]);
        txtUlica.setText(redak[1]);
        txtRok.setText("Rok: " + redak[2]);

        return customView;
    }
}
