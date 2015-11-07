package hr.agrokor.tvz.denisglad.grupolovac;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrgovineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trgovine);


        //Dohvacanje podataka s parse.com, spremanje u listu trgovina
        //Dohvati sve iz tablice Trgovine

        ParseQuery<ParseObject> queryTrgovine = ParseQuery.getQuery("Trgovine");

        ParseQuery<ParseObject> queryUsers = ParseQuery.getQuery("_User");
        queryUsers.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserTrgovina");
        query.whereMatchesQuery("idTrgovine", queryTrgovine);
        query.whereMatchesQuery("idUser", queryUsers);

        ParseQuery<ParseObject> trgovine = ParseQuery.getQuery("Trgovine");
        trgovine.whereMatchesQuery("idTrgovine", query);

        Log.i("upit", "Id korisnika: " + ParseUser.getCurrentUser().getObjectId());
        Log.i("upit", "Username: " + ParseUser.getCurrentUser().getUsername());

        trgovine.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    //ako nije doslo do pogreske izvrsi ovo

                    Log.i("upit", "Upit je prošao, veličina:" + list.size());

                    for(ParseObject list2 : list){
                        Log.i("upit", "U foru: " + list2.getObjectId());
                    }

                    /*
                    final String[][] listaTrgovina = new String[list.size()][4];

                    //prodi kroz sve dohvacene entitete
                    for (int i = 0; i < list.size(); i++) {
                        String grad = list.get(i).getString("grad");
                        String ulica = list.get(i).getString("ulica");
                        String rok = list.get(i).getDate("rok").toString();
                        String id = list.get(i).getString("objectId");

                        listaTrgovina[i][0] = grad;
                        listaTrgovina[i][1] = ulica;
                        listaTrgovina[i][2] = rok;
                        listaTrgovina[i][3] = id;
                    }

                    ListAdapter trgovineAdapter = new CustomAdapterTrgovina(getApplicationContext(), listaTrgovina);

                    ListView trgovineListView = (ListView) findViewById(R.id.listTrgovine);
                    trgovineListView.setAdapter(trgovineAdapter);

                    trgovineListView.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    // proslijedivanje ID-a trgovine metodi
                                    proizvodiActivity(listaTrgovina[position][3]);

                                }
                            }
                    );
*/

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

    private void proizvodiActivity(String idTrgovine){

        Intent proizvodi = new Intent(getApplicationContext(), ProizvodiActivity.class);
        proizvodi.putExtra("idTrgovine", idTrgovine);
        startActivity(proizvodi);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.odjava) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(TrgovineActivity.this, Meduaktivnost.class));
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
