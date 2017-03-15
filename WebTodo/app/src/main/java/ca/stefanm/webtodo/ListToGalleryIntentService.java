package ca.stefanm.webtodo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Rectangle;
import android.text.StaticLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.stefanm.webtodo.localstorage.Session;
import ca.stefanm.webtodo.models.TodoItem;


//TODO IntentService to take todolist -> bitmap and save in Gallery.



/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ListToGalleryIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "ca.stefanm.webtodo.action.FOO";
    private static final String ACTION_BAZ = "ca.stefanm.webtodo.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "ca.stefanm.webtodo.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "ca.stefanm.webtodo.extra.PARAM2";

    public ListToGalleryIntentService() {
        super("ListToGalleryIntentService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {

        //Get input text.


        List<TodoItem> itemList = new Session(this).getTodoList().getItems();

        String imageText = "";

        for (TodoItem item : itemList){
            imageText += (item.getContents() + "\n");
        }

        if (imageText.length() != 0) {
            convertToImage(imageText, 20, 80);
        }

    }







    private void convertToImage(String inputText, int fontSizePt, int maxLineLength){


        //We want to take the input text

        //http://stackoverflow.com/questions/9973048/convert-text-to-image-file-on-android



        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(fontSizePt);
        textPaint.setAntiAlias(true);

        List<String> textLines = new ArrayList<>();

        Collections.addAll(textLines, inputText.split("\n"));

        Rect bounds = new Rect();

        textPaint.getTextBounds(textLines.get(0), 0, textLines.get(0).length(), bounds);

        Bitmap b = Bitmap.createBitmap(maxLineLength, textLines.size() * fontSizePt, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(b);

        for (int i = 0; i < textLines.size(); i++) {
            canvas.drawText(textLines.get(i), 0, i * bounds.height(), textPaint);
        }

        FileOutputStream fos;

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), "todo_" + new Date().getTime());

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        b.compress(Bitmap.CompressFormat.PNG, 80, fos);

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

    }
}
