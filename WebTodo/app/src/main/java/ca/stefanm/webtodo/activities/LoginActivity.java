package ca.stefanm.webtodo.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ca.stefanm.webtodo.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @BindView(R.id.et_Username)
    EditText username;

    @BindView(R.id.et_Password)
    EditText password;


    @OnClick(R.id.btn_login)
    public void login(){

    }

    @OnClick(R.id.b_register)
    public void register() {

    }

    /**
     * An AsyncTask is conceptually similar to a thread, except for the fact that all AsyncTasks
     * run on the same executor thread. This means that there is a separate single thread that runs
     * all of the AsyncTasks in your app.
     *
     * This means that having mutexes between different AsyncTasks is guaranteed to produce deadlocks.
     * AsyncTasks should not wait for each other.
     *
     * This particular AsyncTask does a little bit of work to prepare an HTTP request with the username
     * and password entered in the form. It prepares an HTTP request to a login or register endpoint
     * with the username and password in the header of the HTTP request. (Be sure to use HTTPS if actually
     * using this in the field!!). Those endpoints then return a JSON Web Token (JWT) which is used
     * to authenticate with the todolist api for all future calls.
     */
    protected abstract class AbstractAuthenticationTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {

            //Be sure to put up a loading dialog spinner here.

            return null;
        }
    }

    protected class LoginTask extends AbstractAuthenticationTask {

    }

    protected class RegisterTask extends AbstractAuthenticationTask {

    }
}
