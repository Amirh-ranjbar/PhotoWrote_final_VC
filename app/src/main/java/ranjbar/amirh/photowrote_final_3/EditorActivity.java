package ranjbar.amirh.photowrote_final_3;

/**
 * Created by amirh on 28/06/17.
 */

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.IOException;

import ranjbar.amirh.photowrote_final_3.Views.DrawingView.OnNoteTouchedListener;

import static android.content.ContentValues.TAG;

public class  EditorActivity extends AppCompatActivity
       implements AddEditDetailsFragment.AddEditDetailFragmentListener

{
    //com.github.clans.fab.FloatingActionMenu
    private FloatingActionMenu fabMenu;
    //com.github.clans.fab.FloatingActionButton
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fabEdit;


    private Uri photoUri;// for loading notes as contactUri

    int showHideFabs = 0;
    int fabsState = 1;
    int openPicType=0;
    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;
    private static final int REQUEST_TAKE_PICTURE = 1;
    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".provider";
    public static final String NOTE_URI = "note_uri";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_INFO = "note_info";

    private DrawingViewFragment drawingViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        isStoragePermissionGranted();

        fabMenu = (FloatingActionMenu)findViewById(R.id.fab);
        fabMenu.setOnClickListener(FABclickListener);
        fabMenu.setClosedOnTouchOutside(true);

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_save);
        fabEdit.setVisibility(View.INVISIBLE);
        fabEdit.setOnClickListener(fabEdit_changeListener);

        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab1.setOnClickListener(fab1_ChangeListener);

        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab2.setOnClickListener(fab2_ChangeListener);

        drawingViewFragment= new DrawingViewFragment();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.drawingViewFrame, drawingViewFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
    }

    public View.OnClickListener FABclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//                //change mian Fab to save button for saving new Note
//                // running detailFragment
//                //then saveNote b drawingVIew
//                setAddEditDetailsFragment(null);

        }
    };

    public View.OnClickListener fab1_ChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            if(fabsState==1){
                //enter in loading image with existing note by image name
                fabMenu.close(true);
                fab1.setLabelText("Edit");
                fabsState = 3;
                openPicType = 0;

                onPickPhoto(view);

                setDrawingViewPhotoUri();// set Photo Uri that was Loaded for loading notes
                drawingViewFragment.setLoadEditState(true);
                drawingViewFragment.setDrawingViewlistener(changeListener);
            }
            else if(fabsState==2){
                //Activating drawingView TouchEvent for drawing line (Adding new Note)
                fabMenu.close(true);
                //Extra work for sure
                //in this state the icons not change
                fabsState = 2;
                drawingViewFragment.setLoadEditState(false);

                //creating new FloatingActionButton for saving new note
                fabEdit.setVisibility(View.VISIBLE);
                fabMenu.setVisibility(View.INVISIBLE);


            }
            else if (fabsState == 3){
                //Just loading previous notes for specific image by image name
//                setFabsPosition(false);//hide mini fabs
                fabMenu.close(true);
                fab1.setLabelText("Add Note");
                fabsState = 2;

//                drawingViewFragment.setLoadEditState(false);
            }
        }
    };
    public View.OnClickListener fab2_ChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Listen for Opening New Image
            //Fixed in every State of App
            fabsState=2;
            fabMenu.close(false);
            fab1.setLabelText("Add Note");

            onPickPhoto(view);
            openPicType = 0;
            //
            drawingViewFragment.setLoadEditState(true);
            drawingViewFragment.setDrawingViewlistener(changeListener);
            //Ask question for loading previous notes   ????????????

            // drawingViewFragment.setLoaderManager();// load previous note by the opened image

        }
    };

    public View.OnClickListener fabEdit_changeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            fabEdit.setVisibility(View.INVISIBLE);
            fabMenu.setVisibility(View.VISIBLE);

            //open AddEdit Fragment for saving details

        }
    };

    private void setAddEditDetailsFragment(Bundle arguments){

        AddEditDetailsFragment detailFragment = new AddEditDetailsFragment();

        if (arguments != null)
            detailFragment.setArguments(arguments);

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.editorFrame, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display

    }

    public void setDrawingViewPhotoUri(){
//        Bundle arguments = new Bundle();
//        arguments.putParcelable(NOTE_URI, photoUri);
//        drawingViewFragment.setArguments(arguments);
        drawingViewFragment.setPhotoUri(photoUri);

        //call it for Loading info by photoUri
        drawingViewFragment.getLoaderManager()
                .restartLoader(DrawingViewFragment.NOTE_LOADER
                        , null,
                        drawingViewFragment.getLoaderCallBack());
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_PICTURE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(openPicType == 0) {
            //pic photo from gallery
            if (data != null) {
                photoUri = data.getData();
                Log.d(TAG , " PhotoUri from pickPhoto , Uri : " + photoUri);
                Log.d(TAG , " PhotoUri from pickPhoto , LastSeg : " + photoUri.getLastPathSegment());

                // Do something with the photo based on Uri
                Bitmap selectedImage = null;
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                    //setting photoUri for drawingFragment
                    setDrawingViewPhotoUri();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.imageView);
                ivPreview.setImageBitmap(selectedImage);
            }
        }
        else {
            //taking photo by camera
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri selectedImage = photoUri;
                        getContentResolver().notifyChange(selectedImage, null);
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        ContentResolver cr = getContentResolver();
                        Bitmap bitmap;
                        try {
                            bitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(cr, selectedImage);

                            imageView.setImageBitmap(bitmap);
                            Toast.makeText(this, selectedImage.toString(),
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Camera", e.toString());
                        }
                    }
            }
        }
    }

    @Override
    public void onSaveChanges(String title, String info) {

        getSupportFragmentManager().popBackStack();

        drawingViewFragment.saveNote(title , info);


        drawingViewFragment.setLoadEditState(true);
       drawingViewFragment.onCreateLoader(DrawingViewFragment.NOTE_LOADER,null);
//        drawingViewFragment.setLoaderManager();
        drawingViewFragment.setDrawingViewlistener(changeListener);
        showHideFabs = 0;
        fabsState = 2;
    }

    private OnNoteTouchedListener changeListener = new OnNoteTouchedListener()
    {
        @Override
        public void onNoteTouched(String title, String info) {

            Log.d(TAG , " onNoteTouched event ,  title : " + title);
            Log.d(TAG , " onNoteTouched event ,  info : " + info);
            //initial argm for editing last notes

            Bundle arguments = new Bundle();
            //if(title == null && info == null)

            arguments.putString(NOTE_TITLE,title);
            arguments.putString(NOTE_INFO , info);

            setAddEditDetailsFragment(arguments);
        }

    };
}