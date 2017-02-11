package ca.stefanm.webtodo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ca.stefanm.webtodo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

//Could do a todo item editor activity via explicit intent.


//TODO permission request on load. (Maybe in login screen??)

//Main Activity has a list of TodoItems.

//LoginActivity for Logging in. Sends a request with username/pass in headers to login endpoint,
//gets back JWT that is to be sent on future requests.

//Todo item editor is in a Dialog (or a small activity with explicit intent?)


//Menu item in a todo list to show the dialog showing collaborators.

//Menu Item to take bitmap of todo list.

//Assumption is there's one todo list on the websedrver with multiple collaborators. "One room".

//Pull-down to refresh