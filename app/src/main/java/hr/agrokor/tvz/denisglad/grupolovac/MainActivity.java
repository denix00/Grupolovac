package hr.agrokor.tvz.denisglad.grupolovac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/*********************************************************************************
 *                          MOLIM PROCITATI
 * *****************************************************************************
 *
 * MainActivity nije klasa koja se pokrece s aplikacijom, nego je to Meduaktivnost.
 *
 *
 * Aplikaciji treba dodati spremanje podataka na online bazu, odbrojavanje izmedu trenutka
 * kada je dobiven kod i kada je iskoristen. Ako nije iskoristen u tom vremenu (30 min), tada
 * korisnik mora ponovno skenirat kodove proizvoda u toj trgovini - zastita protiv zloupotrebe
 * (uz ostale nabrojane stvari u prezentaciji).
 *
 *
 * Parse.com je nerelacijska baza podataka (nazalost, ustanovljeno prekasno), pa je potrebno
 * napraviti punokrvnu web aplikaciju (iza koje je relacijska baza, npr. MySQL) s API-jem na
 * koji ce se spojiti aplikacija pa da moze i citati iz baze, i pisati u nju kako spada. 
 *
 */


public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickPrijava(View v){

        username = (EditText) findViewById(R.id.txtUsernamePr);
        password = (EditText) findViewById(R.id.txtPasswordPr);

        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder("Greška: ");
        if (prazno(username)) {
            validationError = true;
            validationErrorMessage.append("prazno korisničko ime");
        }
        if (prazno(password)) {
            if (validationError) {
                validationErrorMessage.append(" i ");
            }
            validationError = true;
            validationErrorMessage.append("prazna lozinka");
        }
        validationErrorMessage.append(".");

        if (validationError) {
            Toast.makeText(MainActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }


        final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
        dlg.setTitle("Molim pričekajte.");
        dlg.setMessage("Prijava u tijeku, trenutak molim");
        dlg.show();

        ParseUser.logInInBackground(username.getText().toString(), password.getText()
                .toString(), new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                dlg.dismiss();
                if (e != null) {

                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(MainActivity.this, Meduaktivnost.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    public void onClickRegistracija(View v){

        username = (EditText) findViewById(R.id.txtUsernameReg);
        password = (EditText) findViewById(R.id.txtPasswordReg);
        password2 = (EditText) findViewById(R.id.txtPassword2Reg);

        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder("Greška: ");
        if (prazno(username)) {
            validationError = true;
            validationErrorMessage.append("prazno korisničko ime");
        }
        if (prazno(password)) {
            if (validationError) {
                validationErrorMessage.append(" i ");
            }
            validationError = true;
            validationErrorMessage.append("prazna lozinka");
        }
        if (!istaLozinka(password, password2)) {
            if (validationError) {
                validationErrorMessage.append(" i ");
            }
            validationError = true;
            validationErrorMessage.append("lozinke nisu jednake");
        }
        validationErrorMessage.append(".");


        if (validationError) {
            Toast.makeText(MainActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }


        final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
        dlg.setTitle("Molim pričekajte");
        dlg.setMessage("Registracija u tijeku, trenutak molim");
        dlg.show();


        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                dlg.dismiss();
                if (e != null) {

                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(MainActivity.this, Meduaktivnost.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    private boolean prazno(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean istaLozinka(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }
}
