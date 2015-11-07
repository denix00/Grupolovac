package hr.agrokor.tvz.denisglad.grupolovac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Random;

public class ProizvodiActivity extends AppCompatActivity {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String[][] staticListaProizvoda;

    public static String nagradniKod;

    private static int brojacCheckiranog;

    private static TextView txtNagradniKod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proizvodi);



        //Dohvacanje podataka s parse.com, spremanje u listu trgovina
        //Dohvati sve iz tablice Trgovine
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Proizvodi");
        //Dohvati sve entitete koji imaju unesen grad
        //Dohvati
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    //ako nije doslo do pogreske izvrsi ovo

                    final String[][] listaProizvoda = new String[list.size()][4];

                    //prodi kroz sve dohvacene entitete
                    for (int i = 0; i < list.size(); i++) {
                        String naziv = list.get(i).getString("naziv");
                        String barcode = list.get(i).getString("barcode");
                        String id = list.get(i).getString("objectId");
                        String skeniran = String.valueOf(list.get(i).getBoolean("skenirano"));

                        listaProizvoda[i][0] = naziv;
                        listaProizvoda[i][1] = barcode;
                        listaProizvoda[i][2] = id;
                        listaProizvoda[i][3] = "false";

                    }

                    //spremanje za kasnije koristenje kod skeniranja
                    if (staticListaProizvoda == null) {
                        staticListaProizvoda = listaProizvoda;
                        brojacCheckiranog = 0;
                    }

                    ListAdapter proizvodiAdapter = new CustomAdapterProizvod(getApplicationContext(), staticListaProizvoda);


                    ListView proizvodiListView = (ListView) findViewById(R.id.listProizvodi);
                    proizvodiListView.setAdapter(proizvodiAdapter);


                }
                //doslo je do pogreske, umjesto naziva grada sorenu napomenu da je doslo do pogreske pri dohvatu podataka
                else {
                    Toast.makeText(getBaseContext(), "Nisam dohvatio trgovine", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        //Dohvacanje podataka s parse.com - kraj koda
    }

    public void onClickSkeniraj(View v){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();


        Log.i("scan", "button metoda");
        Log.i("scan", staticListaProizvoda[0][1]);
        Log.i("scan", staticListaProizvoda[1][1]);
        Log.i("scan", staticListaProizvoda[2][1]);
        Log.i("scan", staticListaProizvoda[3][1]);
        Log.i("scan", staticListaProizvoda[4][1]);
        Log.i("scan", "button metoda");

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String barcode;
            String typ;

            barcode = scanResult.getContents();
            typ = scanResult.getFormatName();

            Log.i("scan", "Barcode " + barcode);


            Log.i("scan", "nakon dohvata");
            Log.i("scan", staticListaProizvoda[0][1]);
            Log.i("scan", staticListaProizvoda[1][1]);
            Log.i("scan", staticListaProizvoda[2][1]);
            Log.i("scan", staticListaProizvoda[3][1]);
            Log.i("scan", staticListaProizvoda[4][1]);
            Log.i("scan", "nakon dohvata");


            boolean zastavica = false;
            for(int i = 0; i < staticListaProizvoda.length; i++){
                Log.i("scan", "Provjeravam: " + barcode + " == " + staticListaProizvoda[i][1]);

                if(barcode.equals(staticListaProizvoda[i][1])){
                    staticListaProizvoda[i][3] = "true";
                    Log.i("scan", "Postavljam true za proizvod");
                    brojacCheckiranog ++;
                    zastavica = true;
                    break;
                }
            }

            if(!zastavica){
                Toast.makeText(getApplicationContext(), "Proizvod nije na listi", Toast.LENGTH_LONG).show();
            }

            // dok su svi skupljeni, daj kod i prikazi i njega, inace prikazi bez koda podatke
            if(brojacCheckiranog == staticListaProizvoda.length){
                nagradniKod = randomKod(10);
                Log.i("scan", "Nagradni kod: " + nagradniKod);
                //finish();
            }

            prikaziPodatke();

        }else{
            Toast.makeText(getApplicationContext(), "Došlo je do pogreške", Toast.LENGTH_LONG).show();
        }
    }


    public void dohvatiPodatke(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Proizvodi");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    final String[][] listaProizvoda = new String[list.size()][4];

                    //prodi kroz sve dohvacene entitete
                    for (int i = 0; i < list.size(); i++) {
                        String naziv = list.get(i).getString("naziv");
                        String barcode = list.get(i).getString("barcode");
                        String id = list.get(i).getString("objectId");
                        String skeniran = String.valueOf(list.get(i).getBoolean("skenirano"));

                        listaProizvoda[i][0] = naziv;
                        listaProizvoda[i][1] = barcode;
                        listaProizvoda[i][2] = id;
                        listaProizvoda[i][3] = "false";
                    }

                    staticListaProizvoda = listaProizvoda;
                } else {
                    Toast.makeText(getBaseContext(), "Nisam dohvatio trgovine", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void prikaziPodatke(){
        ListAdapter proizvodiAdapter = new CustomAdapterProizvod(getApplicationContext(), staticListaProizvoda);

        ListView proizvodiListView = (ListView) findViewById(R.id.listProizvodi);
        proizvodiListView.setAdapter(proizvodiAdapter);

        if(nagradniKod != null){
            txtNagradniKod = (TextView) findViewById(R.id.txtNagradniKod);
            txtNagradniKod.setVisibility(View.VISIBLE);
            txtNagradniKod.setText("Nagradni kod: " + nagradniKod);

            Button skeniraj = (Button) findViewById(R.id.btnSkeniraj);
            skeniraj.setVisibility(View.INVISIBLE);
        }
    }

    private String randomKod(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
