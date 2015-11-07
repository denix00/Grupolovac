package hr.agrokor.tvz.denisglad.grupolovac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.parse.ParseUser;

import java.util.List;
import java.util.Random;

public class ProizvodiActivity extends AppCompatActivity {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String[][] staticListaProizvoda;

    public String nagradniKod;

    private static TextView txtNagradniKod;

    private static String idTrgovine;

    private String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proizvodi);

        Intent intent = getIntent();
        Log.i("check", "Proizvodi, zaprimio kod (prije ifa): "+ intent.getExtras().getString("idTrgovine"));
        if(intent.getStringExtra("idTrgovine") != null){
            idTrgovine = intent.getStringExtra("idTrgovine");
            check = intent.getStringExtra("check");
            nagradniKod = intent.getStringExtra("nagradniKod");

            Log.i("check", "Proizvodi, zaprimio kod: " + idTrgovine);
        }

        //Dohvacanje podataka s parse.com, spremanje u listu trgovina
        //Dohvati sve iz tablice Trgovine
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Proizvodi");
        //Dohvati sve entitete koji imaju unesen grad
        //Dohvati
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    //ako nije doslo do pogreske izvrsi ovo

                    if(nagradniKod != null){
                        txtNagradniKod = (TextView) findViewById(R.id.txtNagradniKod);
                        txtNagradniKod.setVisibility(View.VISIBLE);
                        txtNagradniKod.setText("Nagradni kod: " + nagradniKod);

                        Button skeniraj = (Button) findViewById(R.id.btnSkeniraj);
                        skeniraj.setVisibility(View.INVISIBLE);
                    }

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
                    }

                    if(check.equals("false")){
                        for(int i=0; i < staticListaProizvoda.length; i++){
                            staticListaProizvoda[i][3] = "false";
                        }
                        check = "ne_trebam_vise";
                    }else if (check.equals("true")){
                        for(int i=0; i < staticListaProizvoda.length; i++){
                            staticListaProizvoda[i][3] = "true";
                        }
                        check = "ne_trebam_vise";
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

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String barcode;
            String typ;

            barcode = scanResult.getContents();
            typ = scanResult.getFormatName();

            Log.i("scan", "Barcode " + barcode);

            boolean zastavica = false;
            for(int i = 0; i < staticListaProizvoda.length; i++){
                Log.i("scan", "Provjeravam: " + barcode + " == " + staticListaProizvoda[i][1]);

                if(barcode.equals(staticListaProizvoda[i][1])){
                    staticListaProizvoda[i][3] = "true";
                    Log.i("scan", "Postavljam true za proizvod");
                    zastavica = true;
                    break;
                }
            }

            if(!zastavica){
                Toast.makeText(getApplicationContext(), "Proizvod nije na listi", Toast.LENGTH_LONG).show();
            }

            // koliko ih je odskenirano
            int brojacCheckiranog = 0;
            for(int i=0; i<staticListaProizvoda.length; i++){
                if(staticListaProizvoda[i][3].equals("true"))
                    brojacCheckiranog ++;
            }

            // ako su svi odskenirani, daj kod
            if(brojacCheckiranog == staticListaProizvoda.length){
                if(nagradniKod == null) {
                    nagradniKod = randomKod(10);
                    Log.i("scan", "Nagradni kod: " + nagradniKod);

                    Intent trgovine = new Intent(ProizvodiActivity.this, TrgovineActivity.class);
                    trgovine.putExtra("idTrgovine", idTrgovine);
                    trgovine.putExtra("nagradniKod", nagradniKod);
                    Log.i("check", "Proizvodi, startam trgovine, idTrgovine: " + idTrgovine);
                    startActivity(trgovine);

                }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.odjava) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(ProizvodiActivity.this, Meduaktivnost.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
