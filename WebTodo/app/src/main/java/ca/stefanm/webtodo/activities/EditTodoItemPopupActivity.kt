package ca.stefanm.webtodo.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import butterknife.BindView

import butterknife.ButterKnife
import ca.stefanm.webtodo.R
import ca.stefanm.webtodo.models.TodoItem


///TODO COmment about how this is an activity that looks like a dialog. Who needs fragments?
class EditTodoItemPopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo_item_popup)
        ButterKnife.bind(this)
    }


    var currentlyEditedTodoItem : TodoItem? = null



    //@BindView(R.id.)







    //TODO date picker to select date for todo item.


    //TODO Set title whether edit or add.....
    
}
