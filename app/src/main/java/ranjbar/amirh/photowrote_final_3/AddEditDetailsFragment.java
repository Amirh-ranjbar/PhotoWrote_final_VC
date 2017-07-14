package ranjbar.amirh.photowrote_final_3;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 28/06/17.
 */


public class AddEditDetailsFragment extends Fragment {

    private FloatingActionMenu menu;
    private FloatingActionButton saveChangesButton;
    private FloatingActionButton deleteNoteButton;
    private FloatingActionButton AddItemButton;
    private FloatingActionButton backButton;
    private FloatingActionButton colorButton;
    private EditText titleEditText;
    private EditText infoEditText;

    private String noteTitle;
    private String noteInfo;
    private Uri photoUri;

    private String colorNew = "#f50057";

    public interface AddEditDetailFragmentListener{
        void onSaveChanges(String title , String info, String color);

        void onNoteDeleted();

        void onBackToMainMenu();

        void onNoteUpdated();

        void onSetColorFragment();
    }

    private AddEditDetailFragmentListener detailFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        detailFragmentListener = (AddEditDetailFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detailFragmentListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(
                R.layout.fragment_detail,container ,false);

        menu =(FloatingActionMenu) view.findViewById(R.id.fabDetailFragment) ;
        menu.setClosedOnTouchOutside(true);

        saveChangesButton = (FloatingActionButton) view.findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(saveChangesListener);
        ButtonChangeIcon(saveChangesButton , R.drawable.ic_save_black_24);

        deleteNoteButton = (FloatingActionButton) view.findViewById(R.id.deleteNoteButton);
        deleteNoteButton.setOnClickListener(deleteNoteListener);
        ButtonChangeIcon(deleteNoteButton , R.drawable.ic_clear_black_24);

        AddItemButton = (FloatingActionButton) view.findViewById(R.id.addItemButton);
//        AddItemButton.setEnabled(false);
        ButtonChangeIcon(AddItemButton , R.drawable.ic_playlist_add_black_24);
        AddItemButton.setOnClickListener(AddItemListener);

        backButton = (FloatingActionButton) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(backListener);
        ButtonChangeIcon(backButton , R.drawable.ic_arrow_back_black_24);

        titleEditText = (EditText)view.findViewById(R.id.titleEditText);
        infoEditText = (EditText) view.findViewById(R.id.infoEditText);

        colorButton= (FloatingActionButton) view.findViewById(R.id.fab_color);
        colorButton.setOnClickListener(colorChangeListener);
        ButtonChangeIcon(colorButton, R.drawable.color_36);

        Bundle arguments = getArguments();
        if (arguments != null) {
            //Enter in Update Note Mode
            //
            noteTitle = arguments.getString(EditorActivity.NOTE_TITLE);
            if(noteTitle != null)
                titleEditText.setText(noteTitle);
            noteInfo = arguments.getString(EditorActivity.NOTE_INFO);
            if (noteInfo !=null)
                infoEditText.setText(noteInfo);

            //delete note
            //via clearing drawingView last Path
            //OR
            //delete note
            //via deleting from DataBase
            photoUri = arguments.getParcelable(EditorActivity.NOTE_URI);

        }
        return view;
    }


    public View.OnClickListener saveChangesListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(noteTitle == null && noteInfo == null) {
                noteTitle = titleEditText.getText().toString();
                noteInfo = infoEditText.getText().toString();
                Log.d(TAG, "saveChangesListener : title :" + noteTitle);
                Log.d(TAG, "saveChangesListener : info :" + noteInfo);
                Log.d(TAG, "saveChangesListener : color :" + colorNew);

                detailFragmentListener.onSaveChanges(noteTitle, noteInfo , colorNew);
            }
            else {
                String title = titleEditText.getText().toString();
                String info = infoEditText.getText().toString();

                if(!noteTitle.equals(title) || !noteInfo.equals(info)){
                    //not the same
                    //update database by this note
                    Log.d(TAG, "saveChangesListener : need update :" + noteTitle);

                }
                else{
                    //the same note
                    //do not need updating DataBase
                    Log.d(TAG, "saveChangesListener : do not need update :" + noteTitle);

                    detailFragmentListener.onNoteUpdated();
                }
            }
        }
    };

    public View.OnClickListener deleteNoteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.d(TAG , "on DetailFragment : delete note :" );

            if(noteTitle == null && noteInfo == null) {
                detailFragmentListener.onNoteDeleted(); // notify listener

            }
            else {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("Delete Note")
                        .setMessage("Sorry this feature is Disable by some Technical Problem " +
                                "with App DataBase")
//                    .setPositiveButton( "Delete", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // continue with delete
//                            Log.d(TAG , " kiriiiiiiiiiii ,  : " + photoUri);
//                            Log.d(TAG , " kiriiiiiiiiiii ,  : " + photoUri.getLastPathSegment());
//
////                            //if All_Note most deleted
////                            getActivity().getContentResolver().delete(
////                                    DataBaseDescription.Note.CONTENT_URI,
////                                    photoUri.getLastPathSegment(),
////                                    null);
//
//                            detailFragmentListener.onNoteDeleted(); // notify listener
//                        }
//                    })
                        .setNegativeButton("Ok", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    };

    public View.OnClickListener backListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            detailFragmentListener.onBackToMainMenu();

        }
    };

    public View.OnClickListener AddItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            builder.setTitle("Add extra Item for Note")
                    .setMessage("By this feature You can Add additional Item such Location Or " +
                            "attach files to this Note")
                    .setNegativeButton("Ok", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    };

    private View.OnClickListener colorChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            detailFragmentListener.onSetColorFragment();
        }
    };

    public void colorHasChanged(String color){
        colorNew = color;
    }

    private void ButtonChangeIcon(FloatingActionButton button, int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setImageDrawable(getResources().getDrawable(drawable, getContext().getTheme()));
        }
        else {
            button.setImageDrawable(getResources().getDrawable(drawable));
        }
    }

}
