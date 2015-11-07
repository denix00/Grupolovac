package hr.agrokor.tvz.denisglad.grupolovac;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick (View v){
        Intent trgovine = new Intent(this, new TrgovineActivity().getClass());
        Log.i("nova", "startam trgovine");
        startActivity(trgovine);
    }


    public void onClickPrijava(View v){

        username = (EditText) findViewById(R.id.txtUsernamePr);
        password = (EditText) findViewById(R.id.txtPasswordPr);

        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder("Greška: ");
        if (isEmpty(username)) {
            validationError = true;
            validationErrorMessage.append("prazno korisničko ime");
        }
        if (isEmpty(password)) {
            if (validationError) {
                validationErrorMessage.append(" i ");
            }
            validationError = true;
            validationErrorMessage.append("prazna lozinka");
        }
        validationErrorMessage.append(".");

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(MainActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
        dlg.setTitle("Molim pričekajte.");
        dlg.setMessage("Prijava u tijeku, trenutak molim");
        dlg.show();
        // Call the Parse login method
        ParseUser.logInInBackground(username.getText().toString(), password.getText()
                .toString(), new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {
                dlg.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
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

        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder("Greška: ");
        if (isEmpty(username)) {
            validationError = true;
            validationErrorMessage.append("prazno korisničko ime");
        }
        if (isEmpty(password)) {
            if (validationError) {
                validationErrorMessage.append(" i ");
            }
            validationError = true;
            validationErrorMessage.append("prazna lozinka");
        }
        if (!isMatching(password, password2)) {
            if (validationError) {
                validationErrorMessage.append(" i ");
            }
            validationError = true;
            validationErrorMessage.append("lozinke nisu jednake");
        }
        validationErrorMessage.append(".");

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(MainActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(MainActivity.this);
        dlg.setTitle("Molim pričekajte");
        dlg.setMessage("Registracija u tijeku, trenutak molim");
        dlg.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                dlg.dismiss();
                if (e != null) {
                    // Show the error message
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(MainActivity.this, Meduaktivnost.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText etText1, EditText etText2) {
        if (etText1.getText().toString().equals(etText2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }
}
