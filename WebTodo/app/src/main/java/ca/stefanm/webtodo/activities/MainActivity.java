package ca.stefanm.webtodo.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
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

        new FetchListTask().execute();

    }


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



    @OnItemClick(R.id.lv_todoList)
    void onTodoItemClick(int position){

    }

    @OnItemLongClick(R.id.lv_todoList)
    boolean onTodoItemLongClick(int position){
        return true;
    }




}

//Could do a todo item editor activity via explicit intent.



//Main Activity has a list of TodoItems.



//Todo item editor is in a Dialog (or a small activity with explicit intent?)


//Menu item in a todo list to show the dialog showing collaborators.

//Menu Item to take bitmap of todo list.

//Pull-down to refresh (grab code from SayHi for that one)