package hr.agrokor.tvz.denisglad.grupolovac;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Denis on 6.11.2015..
 */
public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        // Enable Local Datastore. Parse.com servis
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "PiKuuWVxVIyqE6XoKklwcKeji6icwpwU4fXVKPOc", "TIoYw8zmxXE4YZDsyzeO9xfFd9KeuFQgtkrEF1C5");
    }
}
