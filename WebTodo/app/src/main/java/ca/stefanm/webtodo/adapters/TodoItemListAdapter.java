package ca.stefanm.webtodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import ca.stefanm.webtodo.R;
import ca.stefanm.webtodo.StorageController;
import ca.stefanm.webtodo.models.TodoItem;
import hugo.weaving.DebugLog;

/**
 * The Array Adapter is one of the ways to populate a ListView.
 *
 * A list view is a scrollable list containing items. Each item in the list view has its own
 * layout. This adapter populates that layout based on the backing data within an ArrayList (or other
 * List).
 *
 * The getView() method is responsible for taking the position (the number in which the item appears
 * in the list view, and in the array list), and returning a "convertView", a populated View which
 * Android can put into the ListView.
 */

public class TodoItemListAdapter extends ArrayAdapter<TodoItem> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public TodoItemListAdapter(Context context, int resource, List<TodoItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Get the data to
        final TodoItem todoItem = getItem(position);

        //Little bit of magic to get a layout that we can populate.
        if (convertView == null){
            //Layout inflaters take the XML layout data and make Java Objects that we can manipulate.
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_todo, parent, false);
        }

        //Now we can populate the todoitem.

        TextView tv_contents_short = (TextView) convertView.findViewById(R.id.tv_contents_short);
        TextView tv_modified = (TextView) convertView.findViewById(R.id.tv_modified);
        TextView tv_username = (TextView) convertView.findViewById(R.id.tv_userName);

        CheckBox cb_completed = (CheckBox) convertView.findViewById(R.id.cb_completed);

        //Hard-coded date formats aren't locale-aware, but it'll do.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
        tv_modified.setText(simpleDateFormat.format(todoItem.getModifiedOn()));

        tv_username.setText(todoItem.getCreator().getUsername());

        cb_completed.setChecked(todoItem.getCompleted());

        tv_contents_short.setText(todoItem.getContents());

        cb_completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todoItem.setCompleted(isChecked);
                //TODO
                //Opportunity for extension!
                //If the call fails, on the UI thread show a toast.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StorageController.INSTANCE.updateTodoItem(TodoItemListAdapter.this.getContext(), todoItem);
                    }
                }).start();
            }
        });


        //Populate views here
        return convertView;
    }

}
