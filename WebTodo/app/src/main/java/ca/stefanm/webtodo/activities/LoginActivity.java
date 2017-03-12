package ca.stefanm.webtodo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ca.stefanm.webtodo.R;
import ca.stefanm.webtodo.models.User;
import ca.stefanm.webtodo.webservice.LoginWebServiceClient;
import retrofit2.Call;

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

        User user = new User(username.getText().toString(), "");
        new LoginTask(user, password.getText().toString()).execute();
    }

    @OnClick(R.id.b_register)
    public void register() {
        User user = new User(username.getText().toString(), "");
        new RegisterTask(user, password.getText().toString()).execute();
    }

    @Override
    public void onBackPressed(){
        setResult(RESULT_LOGINFAIL);
        super.onBackPressed();
    }


    public static final int RESULT_LOGGEDIN = 0;
    public static final int RESULT_LOGINFAIL = 1;
    public static final int RESULT_REGISTRATION_OK = 2;
    public static final int RESULT_REGISTRATION_FAIL = 3;

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
    protected abstract class AbstractAuthenticationTask extends AsyncTask<Void, Void, Boolean>{

        protected String progressMessage;
        protected String failMessage;

        protected User mUser;
        protected String mPassword;

        private ProgressDialog mProgressDialog;

        AbstractAuthenticationTask( User user,
                String password,
                String progressMessage,
                String failMessage){
            this.progressMessage = progressMessage;
            this.failMessage = failMessage;
            this.mUser = user;
            this.mPassword = password;
        }

        @Override
        protected void onPreExecute(){

            mProgressDialog = ProgressDialog.show(LoginActivity.this, "Loading", progressMessage);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return doWebRequest(mUser, mPassword);
        }

        @Override
        protected void onPostExecute(Boolean result){


            if (!LoginActivity.this.isFinishing()
                    && mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            if (!result ){
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(failMessage)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setFailResult();
                                finish();
                            }
                        })
                        .create()
                        .show();
            } else {
                setSuccessResult();
                finish();
            }

        }

        abstract boolean doWebRequest(User user, String password);
        abstract void setSuccessResult();
        abstract void setFailResult();
    }

    protected class LoginTask extends AbstractAuthenticationTask {

        LoginTask(User user, String password) {
            super(user, password, "Logging in", "Login failed.");
        }

        @Override
        boolean doWebRequest(User user, String password) {
            return LoginWebServiceClient.INSTANCE.loginExistingUser(LoginActivity.this, user, password);
        }

        @Override
        void setSuccessResult() {
            setResult(RESULT_LOGGEDIN);
        }

        @Override
        void setFailResult() {
            setResult(RESULT_LOGINFAIL);
        }
    }

    protected class RegisterTask extends AbstractAuthenticationTask {

        RegisterTask(User user, String password) {
            super(user, password, "Registering", "Registration failed.");
        }

        @Override
        boolean doWebRequest(User user, String password) {
            return LoginWebServiceClient.INSTANCE.registerNewUser(LoginActivity.this, user , password);
        }

        @Override
        void setSuccessResult() {
            setResult(RESULT_REGISTRATION_OK);
        }

        @Override
        void setFailResult() {
            setResult(RESULT_REGISTRATION_FAIL);
        }
    }
}



//TODO permission request on load. (Maybe in login screen??)

//network permission
//local storage?
