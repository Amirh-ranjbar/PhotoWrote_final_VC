package ranjbar.amirh.photowrote_final_3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;

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
    private int noteId;
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

            noteId = arguments.getInt(EditorActivity.NOTE_ID);
            photoUri = arguments.getParcelable(EditorActivity.NOTE_URI);

//            if(photoUri == null)
                //image cannot be accessed
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

            confirmDelete.show(getActivity().getSupportFragmentManager() , "confirm delete" );

        }
    };

    // DialogFragment to confirm deletion of Note
    public final DialogFragment confirmDelete = new DialogFragment() {
        // create an AlertDialog and return it
        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            // create a new AlertDialog Builder
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message);
            // provide an OK button that simply dismisses the dialog
            builder.setPositiveButton(R.string.button_delete,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface dialog, int button) {
                            // use Activity's ContentResolver to invoke
                            // delete on the AddressBookContentProvider
                            getActivity().getContentResolver().delete(
                                    photoUri, String.valueOf(noteId) , null);
                            detailFragmentListener.onNoteDeleted(); // notify listener
                        }
                    }
            );
            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create(); // return the AlertDialog
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
