package ranjbar.amirh.photowrote_final_3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 28/06/17.
 */


public class AddEditDetailsFragment extends Fragment {

    private Button saveChangesButton;
    private EditText titleEditText;
    private TextInputLayout infoTextInputLayout;

    private String noteTitle;
    private String noteInfo;

    private Uri photoUri;

    public interface AddEditDetailFragmentListener{
        void onSaveChanges(String title , String info);
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

        saveChangesButton = (Button) view.findViewById(R.id.saveChangesButton);
        saveChangesButton.setOnClickListener(saveChangesListener);

        titleEditText = (EditText)view.findViewById(R.id.titleEditText);
        infoTextInputLayout = (TextInputLayout) view.findViewById(R.id.infoTextInput);

        Bundle arguments = getArguments();
        if (arguments != null) {
            noteTitle = arguments.getString(EditorActivity.NOTE_TITLE);
            if(noteTitle != null)
                titleEditText.setText(noteTitle);
            noteInfo = arguments.getString(EditorActivity.NOTE_INFO);
            if (noteInfo !=null)
                infoTextInputLayout.getEditText().setText(noteInfo);
        }

        return view;
    }


    public View.OnClickListener saveChangesListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            noteTitle = titleEditText.getText().toString();
            noteInfo = infoTextInputLayout.getEditText().getText().toString();
            Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde : title :" + noteTitle);
            Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde : info :" + noteInfo);
            detailFragmentListener.onSaveChanges(noteTitle , noteInfo);
        }
    };
}
