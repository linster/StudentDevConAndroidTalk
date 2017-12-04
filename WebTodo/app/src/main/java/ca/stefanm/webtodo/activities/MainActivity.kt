package ca.stefanm.webtodo.activities

import android.app.LoaderManager
import android.app.ProgressDialog
import android.content.*
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast

import java.io.IOException
import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemClick
import butterknife.OnItemLongClick
import ca.stefanm.webtodo.ListToGalleryIntentService
import ca.stefanm.webtodo.R
import ca.stefanm.webtodo.StorageController
import ca.stefanm.webtodo.adapters.TodoItemListAdapter
import ca.stefanm.webtodo.localstorage.Session
import ca.stefanm.webtodo.models.TodoItem
import ca.stefanm.webtodo.models.TodoList
import ca.stefanm.webtodo.models.User
import ca.stefanm.webtodo.webservice.TodoListWebServiceClient
import hugo.weaving.DebugLog

class MainActivity : AppCompatActivity(),
        LoaderManager.LoaderCallbacks<StorageController.StorageControllerResult<TodoList>>{

    internal var fetchListLoader : FetchListLoader? = null

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<StorageController.StorageControllerResult<TodoList>> {
        fetchListLoader = FetchListLoader(this)
        return fetchListLoader!!
    }

    override fun onLoaderReset(p0: Loader<StorageController.StorageControllerResult<TodoList>>?) {
        progressDialog?.dismiss()
    }

    override fun onLoadFinished(loader: Loader<StorageController.StorageControllerResult<TodoList>>?,
                                result: StorageController.StorageControllerResult<TodoList>?) {

        if (result == null){
            Log.d("WTF", "RESULT NULLLL?!@?")
            return
        }

        //Update UI on UI thread.
        if (result.success && result.data != null) {
            todoItemListAdapter.clear()
            todoItemListAdapter.addAll(result.data.items)
            todoItemListAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this@MainActivity, "Failed to fetch todo list", Toast.LENGTH_SHORT).show()
        }
        progressDialog?.dismiss()
    }


    @BindView(R.id.lv_todoList)
    internal lateinit var todoListView: ListView

    internal lateinit var todoItemListAdapter: TodoItemListAdapter

    internal lateinit var loaderManager: LoaderManager

    internal var progressDialog : ProgressDialog? = null

    private var warningNoLoginDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ButterKnife.setDebug(true)
        ButterKnife.bind(this)


        todoItemListAdapter = TodoItemListAdapter(this, R.layout.listitem_todo, ArrayList())

        loaderManager = getLoaderManager()

        loaderManager.initLoader(0, null, this).forceLoad()
    }

    @DebugLog
    override fun onResume() {
        super.onResume()

        todoListView.adapter = todoItemListAdapter

        if (warningNoLoginDialog != null && warningNoLoginDialog!!.isShowing) {
            warningNoLoginDialog!!.dismiss()
        }

        val authToken = Session(applicationContext).currentUser.authToken

        if (TextUtils.isEmpty(authToken)) {
            val startLoginActivity = Intent(this, LoginActivity::class.java)
            startActivityForResult(startLoginActivity, LOGIN_REQ)
        } else {
            fetchData()
        }
    }

    public override fun onPause() {

        if (warningNoLoginDialog?.isShowing == true) {
            warningNoLoginDialog?.dismiss()
        }

        super.onPause()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.mainactivitymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {

        val id = menuItem.itemId

        when (id) {
            R.id.menu_refresh -> fetchData()
            R.id.menu_logout -> {
                //Set an empty user
                Session(this).currentUser = User("", "")
                todoItemListAdapter.clear()
                todoItemListAdapter.notifyDataSetChanged()
                showLoginWarningDialog()
            }
            R.id.menu_list_to_image -> {
                //Intent service to take the whole list into a picture and save it to the gallery.
                val startPictureServiceIntent = Intent(this, ListToGalleryIntentService::class.java)
                startService(startPictureServiceIntent)
            }
        }


        return super.onOptionsItemSelected(menuItem)
    }


    internal class FetchListLoader(context: Context
    ) : AsyncTaskLoader<StorageController.StorageControllerResult<TodoList>>(context) {

        @DebugLog
        override fun onStartLoading() {
            Log.d("FetchListLoader", "onStartLoading()")
            super.onStartLoading()
        }

        override fun loadInBackground(): StorageController.StorageControllerResult<TodoList> {
            Log.d("Loader", "Loading in background")
            return StorageController.getTodoList(context.applicationContext)
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == LOGIN_REQ) {
            when (resultCode) {
                LoginActivity.RESULT_LOGGEDIN, LoginActivity.RESULT_REGISTRATION_OK -> {
                    fetchData()
                    showLoginWarningDialog()
                }
                else -> showLoginWarningDialog()
            }
        }
    }


    @OnItemClick(R.id.lv_todoList)
    internal fun onTodoItemClick(position: Int, id: Long) {
        val startEditorIntent = Intent(this, EditTodoItemPopupActivity::class.java)
        startEditorIntent.putExtra("itemid", todoItemListAdapter.getItem(position)!!.id)
        startActivity(startEditorIntent)
    }

    @OnItemLongClick(R.id.lv_todoList)
    internal fun onTodoItemLongClick(position: Int): Boolean {
        Log.d(TAG, "Lpos:" + position)
        return true

        //Contextual action bar?

    }


    @OnClick(R.id.btn_float_add)
    internal fun addItem() {

        val startEditorIntent = Intent(this, EditTodoItemPopupActivity::class.java)
        startEditorIntent.putExtra("itemid", -1)
        startActivity(startEditorIntent)
    }

    private fun showLoginWarningDialog() {
        warningNoLoginDialog = AlertDialog.Builder(this)
                .setTitle("Unauthorized")
                .setMessage("You must login or register to use this application.")
                .setPositiveButton("Login") { dialog, which ->
                    val startLoginActivity = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivityForResult(startLoginActivity, LOGIN_REQ)
                }
                .setNegativeButton("Close Application") { dialog, which -> finish() }.show()
    }

    fun fetchData() {
        Log.d("Loader", "Requesting loader restart.")
        loaderManager.restartLoader(0 , null, this)
        fetchListLoader?.onContentChanged()
    }

    companion object {
        val TAG = "MainActivity"
        var LOGIN_REQ = 1
    }

}

