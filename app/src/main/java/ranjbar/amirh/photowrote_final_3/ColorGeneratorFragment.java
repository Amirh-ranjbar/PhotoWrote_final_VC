package ranjbar.amirh.photowrote_final_3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 14/07/17.
 */

public class ColorGeneratorFragment extends DialogFragment {

    private EditText colorEditText;
    private View colorView;
    private int color = Color.CYAN;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // create dialog
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_color, null);
        builder.setView(colorDialogView); // add GUI to dialog

        // set the AlertDialog's message
        builder.setTitle(R.string.title_color_dialog);

        colorEditText = (EditText) colorDialogView.findViewById(R.id.coloredittext);
        colorEditText.addTextChangedListener(colorTextListener);
        colorEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


        colorEditText.setFilters(new InputFilter[]{HexinputFilter, new InputFilter.LengthFilter(6)});

        colorView = (View) colorDialogView.findViewById(R.id.colorview);
        colorView.setBackgroundColor(color);

        final AddEditDetailsFragment detailsFragment = getDetailFragment();


        builder.setPositiveButton(R.string.button_set_color,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d(TAG, "on Color send to detail fragment , color  : #" + colorEditText.getText().toString());

                        detailsFragment.colorHasChanged("#" + colorEditText.getText().toString());
                    }
                }
        );


        Log.d(TAG, "on Color fragment , default color : " + color);

        return builder.create(); // return dialog
    }

    private AddEditDetailsFragment getDetailFragment() {
        return (AddEditDetailsFragment) getFragmentManager().findFragmentById(
                R.id.editorFrame);
    }


    private TextWatcher colorTextListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(charSequence.length() == 6) {
                color = Color.parseColor("#" + charSequence.toString());
                colorView.setBackgroundColor(color);

                Log.d(TAG, "on Color fragment , text changed  : " + charSequence);
                Log.d(TAG, "on Color fragment , new  color : " + color);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    // here comes the filter to control input on that component
    InputFilter HexinputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned spanned, int i2, int i3) {

            if (source.length() > 44) return "";// max 44chars
//
// Here you can add more controls, e.g. allow only hex chars etc

            for (int i = start; i < end; i++) {
                if (
                     source.charAt(i) != 'a'
                        && source.charAt(i) != 'A'
                        && source.charAt(i) != 'b'
                        && source.charAt(i) != 'B'
                        && source.charAt(i) != 'c'
                        && source.charAt(i) != 'C'
                        && source.charAt(i) != 'd'
                        && source.charAt(i) != 'D'
                        && source.charAt(i) != 'e'
                        && source.charAt(i) != 'E'
                        && source.charAt(i) != 'f'
                        && source.charAt(i) != 'F'
                             && source.charAt(i) != '0'
                             && source.charAt(i) != '1'
                             && source.charAt(i) != '2'
                             && source.charAt(i) != '3'
                             && source.charAt(i) != '4'
                             && source.charAt(i) != '5'
                             && source.charAt(i) != '6'
                             && source.charAt(i) != '7'
                             && source.charAt(i) != '8'
                             && source.charAt(i) != '9'
                        ) {
                    Log.d(TAG, "on Color fragment , char bayad hazf shavad : " +  source.charAt(i) );
                    return "";

                }
                Log.d(TAG, "on Color fragment , char : " +  source.charAt(i));
            }
            return null;
        }
    };
}
