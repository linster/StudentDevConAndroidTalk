package ca.stefanm.webtodo.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import ca.stefanm.webtodo.R;
import ca.stefanm.webtodo.models.TodoList;
import ca.stefanm.webtodo.webservice.TodoListWebServiceClient;
import hugo.weaving.DebugLog;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    @DebugLog
    protected void onResume(){
        super.onResume();
        new TestTask().execute();
    }




    @BindView(R.id.lv_todoList)
    ListView todoListView;


    @OnItemClick(R.id.lv_todoList)
    void onTodoItemClick(int position){

    }

    @OnItemLongClick(R.id.lv_todoList)
    void onTodoItemLongClick(int position){

    }



    public class TestTask extends AsyncTask<Void, Void, Void>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "Task");
            try {
                TodoList todoList = TodoListWebServiceClient.INSTANCE.getClient(MainActivity.this).getTodoListById(1).execute().body();
                Log.d(TAG, todoList.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e){
                e.printStackTrace();
            }
            return null;
        }
    }

}

//Could do a todo item editor activity via explicit intent.



//Main Activity has a list of TodoItems.



//Todo item editor is in a Dialog (or a small activity with explicit intent?)


//Menu item in a todo list to show the dialog showing collaborators.

//Menu Item to take bitmap of todo list.

//Assumption is there's one todo list on the websedrver with multiple collaborators. "One room".

//Pull-down to refresh (grab code from SayHi for that one)