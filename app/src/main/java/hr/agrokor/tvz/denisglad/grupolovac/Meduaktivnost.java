package hr.agrokor.tvz.denisglad.grupolovac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

/**
 * Created by Denis on 7.11.2015..
 */
public class Meduaktivnost extends AppCompatActivity {

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // ako je prijavljen, odi na trgovine
        // inace se vrati na prijavu/registraciju
        if(ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, TrgovineActivity.class));
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
