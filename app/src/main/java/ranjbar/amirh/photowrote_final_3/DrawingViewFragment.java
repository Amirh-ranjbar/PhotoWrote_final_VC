package ranjbar.amirh.photowrote_final_3;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ranjbar.amirh.photowrote_final_3.Views.DrawingView;
import ranjbar.amirh.photowrote_final_3.data.DataBaseDescription;
import ranjbar.amirh.photowrote_final_3.data.DataBaseDescription.Note;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 28/06/17.
 */

public class DrawingViewFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int NOTE_LOADER = 0; // identifies the Loader
    private DrawingView drawingView;
    private Uri photoUri; // Uri of selected Image

    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(
                R.layout.fragment_drawingview, container, false);

        drawingView = (DrawingView) view.findViewById(R.id.drawingViewFragment);

        getLoaderManager().initLoader(NOTE_LOADER, null, this);

        return view;
    }

    public LoaderManager.LoaderCallbacks<Cursor> getLoaderCallBack(){
        return this;
    }

    public void setPhotoUri(Uri uri){
        photoUri = uri;
    }

    // saves Note information to the database
    public void saveNote(String title , String info , String color) {
        drawingView.setPhotoUri(photoUri);
        Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde , saveNote: title :" + title);
        Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde , saveNote : color :" +color);
        drawingView.SavingNote(title, info , color);
    }

    public  void setDrawingViewState(boolean state){
        //set DrawingView Enable or disable
        drawingView.setEnabled(state);
    }

    public void setLoadEditState(boolean state){
        drawingView.setLoadEditType(state);// true for loading && false for editing
    }

    public void setDrawingViewlistener(DrawingView.DrawingViewListener l) {
        drawingView.setDrawingViewListener(l);
    }

    public void clearDrawingView(){
        drawingView.clear();
    }

    public void setDrawingViewParameters(int h , int  w){

        Log.d(TAG , " DrawingView   , height : " + drawingView.getHeight());
        Log.d(TAG , " DrawingView   , width : " + drawingView.getHeight());
        Log.d(TAG , " DrawingView   , parameters : " + drawingView.getLayoutParams());

//        drawingView.measure(h , w);
        drawingView.setDesiredParameters(w , h);
//        drawingView.measure(drawingView.getMeasuredWidthAndState(),drawingView.getMeasuredHeightAndState());

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader;
        Log.d(TAG, " onCreateLoaderrrrrrrrrrrr omad , id : " + id);
        Log.d(TAG, " onCreateLoaderrrrrrrrrrrr omad , photouri : " + photoUri);

        switch (id) {
            case NOTE_LOADER:
                cursorLoader = new CursorLoader(getActivity(), Note.CONTENT_URI, null, null, null, null);
                break;
            default:
                cursorLoader = null;
                break;
        }
        Log.d(TAG, " onCreateLoaderrrrrrrrrrrr akharesh , cursorLoader : " + cursorLoader);
        
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, " onLoadFinished  shod  , data  : " + data);

        // if the contact exists in the database, display its data
        if (data != null && data.moveToFirst() && photoUri != null) {
            Log.d(TAG, " onLoadFinished  jolotarrrrrrrr  , data  : " + data);

            //Toast.makeText(getContext(), "Previous Notes has Successfully Loaded", Toast.LENGTH_LONG).show();


            do {
                int nameIndex = data.getColumnIndex(Note.COLUMN_NAME);
                String name = data.getString(nameIndex);
                Log.d(TAG, " onLoadFinished  shod  , name  : " + name);
                Log.d(TAG, " onLoadFinished  shod  , PhotoUri , last  : " + photoUri.getLastPathSegment());

                if (name != null && !name.isEmpty()) {
                    if (name.equals(photoUri.getLastPathSegment())) {
                        // get the column index for each data item
                        int pointX1Index = data.getColumnIndex(Note.COLUMN_POINTX1);
                        int pointY1Index = data.getColumnIndex(Note.COLUMN_POINTY1);
                        int pointX2Index = data.getColumnIndex(Note.COLUMN_POINTX2);
                        int pointY2Index = data.getColumnIndex(Note.COLUMN_POINTY2);
                        int titleIndex = data.getColumnIndex(Note.COLUMN_TITLE);
                        int infoIndex = data.getColumnIndex(Note.COLUMN_INFO);
                        int colorIndex = data.getColumnIndex(Note.COLUMN_COLOR);

                        float pointX1 = Float.valueOf(data.getString(pointX1Index));
                        float pointY1 = Float.valueOf(data.getString(pointY1Index));
                        float pointX2 = Float.valueOf(data.getString(pointX2Index));
                        float pointY2 = Float.valueOf(data.getString(pointY2Index));
                        String title = data.getString(titleIndex);
                        String info = data.getString(infoIndex);
                        String color = data.getString(colorIndex);

                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointX1);
                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointY1);
                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointX2);
                        Log.d(TAG, " onLoadFinished  shod  , point  : " + pointY2);
                        Log.d(TAG, " onLoadFinished  shod  , title  : " + title);
                        Log.d(TAG, " onLoadFinished  shod  , info  : " + info);
                        Log.d(TAG, " onLoadFinished  shod  , color  : " + color);

                        float p[] = {pointX1, pointY1, pointX2, pointY2};

                        drawingView.setArrayOfPoints(p, title, info , color );
                        drawingView.postInvalidate();
//                        Canvas canvas = new Canvas();
//                        Paint paint = drawingView.getDrawPaint();
//                        paint.setColor(Color.RED);
//                        canvas.drawLine(pointX1, pointY1, pointX2, pointY2, paint);
//                        drawingView.draw(canvas);

                    }
                }
            } while (data.moveToNext());

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public void onExitApp(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Good Buy")
                .setMessage("Thanks for your valuable Time \n\n  Feel free to contact me :)")
                .setPositiveButton( "Exit" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        System.exit(0);
                        return;

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void deleteAllNotesByPhotoUri(){

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle("Delete Notes")
                .setMessage("Are you sure you want to delete All Notes of this photo?")
                .setPositiveButton( "Delete All", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Log.d(TAG , " kiriiiiiiiiiii ,  : " + photoUri);
                        Log.d(TAG , " kiriiiiiiiiiii ,  : " + photoUri.getLastPathSegment());

                        //if All_Note most deleted
                        getActivity().getContentResolver().delete(
                                DataBaseDescription.Note.CONTENT_URI,
                                photoUri.getLastPathSegment(),
                                null);

                        drawingView.clear();
                        // notify listener
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
