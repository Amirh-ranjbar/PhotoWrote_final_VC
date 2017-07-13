package ranjbar.amirh.photowrote_final_3;

import android.content.Context;
import android.content.DialogInterface;
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

import ranjbar.amirh.photowrote_final_3.data.DataBaseDescription;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 28/06/17.
 */


public class AddEditDetailsFragment extends Fragment {

    private FloatingActionButton saveChangesButton;
    private FloatingActionButton deleteNoteButton;
    private FloatingActionButton AddItemButton;
    private EditText titleEditText;
    private EditText infoEditText;

    private String noteTitle;
    private String noteInfo;
    private float[] notePath;
    private Uri photoUri;

    public interface AddEditDetailFragmentListener{
        void onSaveChanges(String title , String info);

        void onNoteDeleted();
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

        saveChangesButton = (FloatingActionButton) view.findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(saveChangesListener);
        ButtonChangeIcon(saveChangesButton , R.drawable.ic_save_black_24);

        deleteNoteButton = (FloatingActionButton) view.findViewById(R.id.deleteNoteButton);
        deleteNoteButton.setOnClickListener(deleteNoteListener);
        ButtonChangeIcon(deleteNoteButton , R.drawable.ic_clear_black_24);

        AddItemButton = (FloatingActionButton) view.findViewById(R.id.addItemButton);
        AddItemButton.setEnabled(false);
        ButtonChangeIcon(AddItemButton , R.drawable.ic_playlist_add_black_24);

        titleEditText = (EditText)view.findViewById(R.id.titleEditText);
        infoEditText = (EditText) view.findViewById(R.id.infoEditText);

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

            notePath = arguments.getFloatArray(EditorActivity.NOTE_PATH);
            if (notePath == null){

            }
            else {

            }
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

            noteTitle = titleEditText.getText().toString();
            noteInfo = infoEditText.getText().toString();
            Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde : title :" + noteTitle);
            Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde : info :" + noteInfo);
            detailFragmentListener.onSaveChanges(noteTitle , noteInfo);
        }
    };

    public View.OnClickListener deleteNoteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde : delete note :" );

//            confirmDelete.show(getActivity().getSupportFragmentManager() , "confirm delete" );

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getContext());
            }
            builder.setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this Note?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            Log.d(TAG , " kiriiiiiiiiiii ,  : " + photoUri);
                            Log.d(TAG , " kiriiiiiiiiiii ,  : " + photoUri.getLastPathSegment());

                            //if One_Note
                            String concat = String.valueOf(notePath[0]) +
                                    "," + String.valueOf(notePath[1]) +
                                    "," + String.valueOf(notePath[2]) +
                                    "," + String.valueOf(notePath[3]) +
                                    "," + photoUri.getLastPathSegment();

                            getActivity().getContentResolver().delete(
                                    DataBaseDescription.Note.CONTENT_URI, concat , null);

                            //if All_Notes
//                            getActivity().getContentResolver().delete(
//                                    Note.CONTENT_URI, photoUri.getLastPathSegment() , null);

                            detailFragmentListener.onNoteDeleted(); // notify listener
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

//            detailFragmentListener.onNoteDeleted();
        }
    };


    private void ButtonChangeIcon(FloatingActionButton button, int drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button.setImageDrawable(getResources().getDrawable(drawable, getContext().getTheme()));
        }
        else {
            button.setImageDrawable(getResources().getDrawable(drawable));
        }
    }
}
