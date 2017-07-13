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
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ranjbar.amirh.photowrote_final_3.Views.DrawingView;

import static android.content.ContentValues.TAG;

public class  EditorActivity extends AppCompatActivity
       implements AddEditDetailsFragment.AddEditDetailFragmentListener {

    //com.github.clans.fab.FloatingActionMenu
    private FloatingActionMenu fabMenu;
    //com.github.clans.fab.FloatingActionButton
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDeletAll;
    private FrameLayout drawingViewFrameLayout;
    private Uri photoUri;// for loading notes as contactUri
    private int showHideFabs = 0;
    private int fabsState = 1;
    private boolean AddSave = false;
    private int openPicType=0;
    private boolean detailFragmentIsOn = false;
    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;
    private static final int REQUEST_TAKE_PICTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final String AUTHORITY=
            BuildConfig.APPLICATION_ID+".provider";

    public static final String NOTE_URI = "note_uri";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_INFO = "note_info";
    public static final String NOTE_PATH = "note_path";
    public static final String NOTE_ID = "note_id";

    private String mCurrentPhotoPath;
    private DrawingViewFragment drawingViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        isStoragePermissionGranted();

        fabMenu = (FloatingActionMenu)findViewById(R.id.fab);
        fabMenu.setClosedOnTouchOutside(true);

        fabEdit = (FloatingActionButton) findViewById(R.id.fab_save);
        fabEdit.setVisibility(View.INVISIBLE);
        fabEdit.setOnClickListener(fabEdit_changeListener);

        fabDeletAll = (FloatingActionButton) findViewById(R.id.fabDeleteNotes);
        fabDeletAll.setVisibility(View.INVISIBLE);
        fabDeletAll.setOnClickListener(fabDeletAll_changeListener);
        ButtonChangeIcon(fabDeletAll , R.drawable.ic_clear_black_24);

        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab2.setOnClickListener(fab2_ChangeListener);
        ButtonChangeIcon(fab2,R.drawable.ic_note_add_black_24);

        fab3 = (FloatingActionButton) findViewById(R.id.fab_3);
        fab3.setOnClickListener(fab3_ChangeListener);
        ButtonChangeIcon(fab3 , R.drawable.ic_camera_black_24);

        drawingViewFrameLayout = (FrameLayout) findViewById(R.id.drawingViewFrame) ;

        drawingViewFragment= new DrawingViewFragment();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.drawingViewFrame, drawingViewFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
    }

    public View.OnClickListener fabDeletAll_changeListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            //delete all notes
            //that saves with with photo uri name
            if(photoUri != null) {
                drawingViewFragment.deleteAllNotesByPhotoUri();
            }
        }
    };
    public View.OnClickListener fab2_ChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Listen for Opening New Image
            fabMenu.close(false);
            openPicType = 0;
            onPickPhoto(view);
            //Ask question for loading previous notes
            // ????????????
        }
    };

    public View.OnClickListener fab3_ChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            //Take Picture via Camera and save it
            fabMenu.close(false);
            openPicType = 1;
            dispatchTakePicture();
        }
    };

    public View.OnClickListener fabEdit_changeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Creating new note and saving to database
            if(AddSave) {
                AddSave = false;
                fabMenu.showMenu(true);
                ButtonChangeIcon(fabEdit ,R.drawable.ic_mode_edit_black_24);
                //open AddEdit Fragment for saving details
                setAddEditDetailsFragment(null, photoUri);
                drawingViewFragment.setLoadEditState(true);
            }
            else {
                fabMenu.hideMenu(true);
                drawingViewFragment.setLoadEditState(false);
                //after line is create and noting is wrong
                //next step
                ButtonChangeIcon(fabEdit, R.drawable.ic_done_black_24);
                fabEdit.setEnabled(false);
                AddSave = true;
            }
        }
    };

    public void showFabEditButton(){
        if(photoUri != null)
            fabEdit.setVisibility(View.VISIBLE);
            ButtonChangeIcon(fabEdit ,R.drawable.ic_mode_edit_black_24);
    }

    private void ButtonChangeIcon(FloatingActionButton button, int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setImageDrawable(getResources().getDrawable(drawable, getBaseContext().getTheme()));
        }
        else {
            button.setImageDrawable(getResources().getDrawable(drawable));
        }
    }

    private void setAddEditDetailsFragment(Bundle arguments, Uri uri){

        AddEditDetailsFragment detailFragment = new AddEditDetailsFragment();

        //            if(photoUri != null)
        // Photo Uri in this Step
        // cannot be null for Sure

        if (arguments != null) {
            arguments.putParcelable(NOTE_URI , uri);
            detailFragment.setArguments(arguments);
        }
        else{
            arguments = new Bundle();
            arguments.putParcelable(NOTE_URI , uri);
            arguments.putString(NOTE_TITLE,null);
            arguments.putString(NOTE_INFO , null);
            detailFragment.setArguments(arguments);
        }

        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.editorFrame, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); // causes DetailFragment to display
        detailFragmentIsOn = !transaction.isEmpty();
    }

    public void setDrawingView(){
        drawingViewFragment.clearDrawingView();
        drawingViewFragment.setPhotoUri(photoUri);

        drawingViewFragment.setLoadEditState(true);
        drawingViewFragment.setDrawingViewlistener(changeListener);

//        call it for Loading info by photoUri
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

    public void setDrawingViewFrameParameters(int height , int width){
//        drawingViewFrameLayout.setLayoutParams(parameters);
        Log.d(TAG , " DrawingView FrameLayout  , height : " + drawingViewFrameLayout.getHeight());
        Log.d(TAG , " DrawingView FrameLayout  , width : " + drawingViewFrameLayout.getHeight());
        Log.d(TAG , " DrawingView FrameLayout  , parameters : " + drawingViewFrameLayout.getLayoutParams());
        Log.d(TAG , " height of imageVIew : " + height);
        Log.d(TAG , " width of imageVIew : " + width);

        drawingViewFragment.setDrawingViewParameters(height , width);

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
                    setDrawingView();
                    //show FabEdit for adding Note
                    showFabEditButton();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Load the selected image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.imageView);
                ivPreview.setImageBitmap(selectedImage);

                setDrawingViewFrameParameters(selectedImage.getHeight() ,selectedImage.getWidth() );
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
                            //setting photoUri for drawingFragment
                            setDrawingView();
                            galleryAddPic();
                            //show FabEdit for adding Note
                            showFabEditButton();
                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Camera", e.toString());
                        }
                    }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PhotoWrote_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "ranjbar.amirh.photowrote_final_3.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)  ;
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Log.d(TAG , " gallery has refreshed : contentUri" + contentUri);
    }

    @Override
    public void onSaveChanges(String title, String info) {

        getSupportFragmentManager().popBackStack();
        detailFragmentIsOn = false;

        Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde , onSaveChanges : title :" +title);
        drawingViewFragment.saveNote(title , info);

        drawingViewFragment.clearDrawingView();
        drawingViewFragment.getLoaderManager()
                .restartLoader(DrawingViewFragment.NOTE_LOADER
                        , null,
                        drawingViewFragment.getLoaderCallBack());
        drawingViewFragment.setDrawingViewlistener(changeListener);
    }

    @Override
    public void onNoteDeleted() {

        Log.d(TAG , "mameeeeeeeeee gonde , onNoteDeleted : title :" );

        getSupportFragmentManager().popBackStack();
        detailFragmentIsOn = false;

        drawingViewFragment.clearDrawingView();
        drawingViewFragment.getLoaderManager()
                .restartLoader(DrawingViewFragment.NOTE_LOADER
                        , null,
                        drawingViewFragment.getLoaderCallBack());
        drawingViewFragment.setDrawingViewlistener(changeListener);
    }

    @Override
    public void onBackToMainMenu() {

        getSupportFragmentManager().popBackStack();
        detailFragmentIsOn = false;

        drawingViewFragment.setLoadEditState(true);

        drawingViewFragment.clearDrawingView();
        drawingViewFragment.getLoaderManager()
                .restartLoader(DrawingViewFragment.NOTE_LOADER
                        , null,
                        drawingViewFragment.getLoaderCallBack());
        drawingViewFragment.setDrawingViewlistener(changeListener);
    }

    @Override
    public void onNoteUpdated() {
        getSupportFragmentManager().popBackStack();
    }

    private DrawingView.DrawingViewListener changeListener = new DrawingView.DrawingViewListener() {
        @Override
        public void onNoteTouched(String title, String info ) {

            Log.d(TAG , " onNoteTouched event ,  title : " + title);
            Log.d(TAG , " onNoteTouched event ,  info : " + info);
            //initial argm for editing last notes
            Bundle arguments = new Bundle();
            //if(title == null && info == null)
            arguments.putString(NOTE_TITLE,title);
            arguments.putString(NOTE_INFO , info);

            setAddEditDetailsFragment(arguments, photoUri);
        }

        @Override
        public void onDrawLineDetected(boolean detected){
             if(detected ) {
                fabEdit.setEnabled(true);
            }
            else{
                fabEdit.setEnabled(false);
            }
        }
    };

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
            if (detailFragmentIsOn) {
                getSupportFragmentManager().popBackStack();
                detailFragmentIsOn =false;
            }
//            else{
//                android.os.Process.killProcess(android.os.Process.myPid());
//            }
            else {
                if (!doubleBackToExitPressedOnce) {
                    this.doubleBackToExitPressedOnce = true;

                    drawingViewFragment.onExitApp();
                    fabMenu.close(true);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);

                }
            }
    }
}