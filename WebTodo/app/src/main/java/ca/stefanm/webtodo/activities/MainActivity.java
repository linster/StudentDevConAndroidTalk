package ca.stefanm.webtodo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import ca.stefanm.webtodo.ListToGalleryIntentService;
import ca.stefanm.webtodo.R;
import ca.stefanm.webtodo.StorageController;
import ca.stefanm.webtodo.adapters.TodoItemListAdapter;
import ca.stefanm.webtodo.localstorage.Session;
import ca.stefanm.webtodo.models.TodoItem;
import ca.stefanm.webtodo.models.TodoList;
import ca.stefanm.webtodo.models.User;
import ca.stefanm.webtodo.webservice.TodoListWebServiceClient;
import hugo.weaving.DebugLog;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        todoItemListAdapter = new TodoItemListAdapter(this, R.layout.listitem_todo, new ArrayList<TodoItem>());
    }

    @Override
    @DebugLog
    protected void onResume(){
        super.onResume();

        todoListView.setAdapter(todoItemListAdapter);

        if (warningNoLoginDialog != null && warningNoLoginDialog.isShowing()){
            warningNoLoginDialog.dismiss();
        }

        String authToken = new Session(getApplicationContext()).getCurrentUser().getAuthToken();

        if ( TextUtils.isEmpty(authToken) ) {
            Intent startLoginActivity = new Intent(this, LoginActivity.class);
            startActivityForResult(startLoginActivity, LOGIN_REQ);
        } else {
            new FetchListTask().execute();
        }
    }

    @Override
    public void onPause() {

        if (warningNoLoginDialog != null && warningNoLoginDialog.isShowing()){
            warningNoLoginDialog.dismiss();
        }

        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        int id = menuItem.getItemId();

        switch (id){
            case R.id.menu_refresh:
                new FetchListTask().execute();
                break;
            case R.id.menu_logout:
                //Set an empty user
                (new Session(this)).setCurrentUser(new User("", ""));
                todoItemListAdapter.clear();
                todoItemListAdapter.notifyDataSetChanged();
                showLoginWarningDialog();
                break;
            case R.id.menu_list_to_image:
                //Intent service to take the whole list into a picture and save it to the gallery.
                Intent startPictureServiceIntent = new Intent(this, ListToGalleryIntentService.class);
                startService(startPictureServiceIntent);
                break;
        }


        return super.onOptionsItemSelected(menuItem);
    }

    


    public static int LOGIN_REQ = 1;

    @BindView(R.id.lv_todoList)
    ListView todoListView;

    TodoItemListAdapter todoItemListAdapter;


    class FetchListTask extends AsyncTask<Void, Void, StorageController.StorageControllerResult<TodoList>> {

        ProgressDialog mProgressDialog;

        @Override
        public void onPreExecute(){
            mProgressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Fetching TODO List...");
        }

        @Override
        @DebugLog
        protected StorageController.StorageControllerResult<TodoList> doInBackground(Void... params) {
            //Fetch list in background.
            return StorageController.INSTANCE.getTodoList(MainActivity.this);
        }

        @Override
        @DebugLog
        protected void onPostExecute(StorageController.StorageControllerResult<TodoList> storageControllerResult){
            //Update UI on UI thread.
            if (storageControllerResult.getSuccess() && storageControllerResult.getData() != null){
                todoItemListAdapter.clear();
                todoItemListAdapter.addAll(storageControllerResult.getData().getItems());
                todoItemListAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch todo list", Toast.LENGTH_SHORT).show();
            }

            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == LOGIN_REQ){
            switch (resultCode){
                case LoginActivity.RESULT_LOGGEDIN:
                case LoginActivity.RESULT_REGISTRATION_OK:
                    new FetchListTask().execute();
                default:
                    showLoginWarningDialog();
            }
        }
    }


    @OnItemClick(R.id.lv_todoList)
    void onTodoItemClick(int position, long id){
        Intent startEditorIntent = new Intent(this, EditTodoItemPopupActivity.class);
        startEditorIntent.putExtra("itemid", todoItemListAdapter.getItem(position).getId());
        startActivity(startEditorIntent);
    }

    @OnItemLongClick(R.id.lv_todoList)
    boolean onTodoItemLongClick(int position){
        Log.d(TAG, "Lpos:" + position);
        return true;

        //Contextual action bar?

    }


    @OnClick(R.id.btn_float_add)
    void addItem(){

        Intent startEditorIntent = new Intent(this, EditTodoItemPopupActivity.class);
        startEditorIntent.putExtra("itemid", -1);
        startActivity(startEditorIntent);
    }


    private AlertDialog warningNoLoginDialog;

    private void showLoginWarningDialog(){
        warningNoLoginDialog = new AlertDialog.Builder(this)
                .setTitle("Unauthorized")
                .setMessage("You must login or register to use this application.")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startLoginActivity = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(startLoginActivity, LOGIN_REQ);
                    }
                })
                .setNegativeButton("Close Application", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }







}






//Could do a todo item editor activity via explicit intent.




//Todo item editor is in a Dialog (or a small activity with explicit intent?)

//TODO Menu
//Menu Item to take bitmap of todo list. (Service)


//TODO Pull-down to refresh (grab code from SayHi for that one)