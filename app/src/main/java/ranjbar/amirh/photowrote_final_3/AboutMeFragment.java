package ranjbar.amirh.photowrote_final_3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 13/07/17.
 */

public class AboutMeFragment extends Fragment {

    private ImageView instagramIcon;
    private ImageView telegramIcon;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(
                R.layout.fragment_about_me,container ,false);

        instagramIcon = (ImageView)view.findViewById(R.id.instagramIcon);
        instagramIcon.setOnTouchListener(instaIconListener);

        telegramIcon = (ImageView)view.findViewById(R.id.telegramIcon);
        telegramIcon.setOnTouchListener(telegramIconListener);

        backButton = (Button) view.findViewById(R.id.aboutMeButton);
        backButton.setOnClickListener(backListener);
        return view;
    }

    private View.OnTouchListener instaIconListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            try {
                Uri uri = Uri.parse("https://instagram.com/_u/Amirh.ranjbar");
                 Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
                return true;
            }
            catch (ActivityNotFoundException e) {
             Log.e(TAG, e.getMessage());
            }

            return false;
        }
    };

    private View.OnTouchListener telegramIconListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            try {
                Uri uri = Uri.parse("https://telegram.me/amirh_ranjbar");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
                return true;
            }
            catch (ActivityNotFoundException e) {
                Log.e(TAG, e.getMessage());
            }
            return false;
        }
    };

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getFragmentManager().popBackStack();
        }
    };
}
