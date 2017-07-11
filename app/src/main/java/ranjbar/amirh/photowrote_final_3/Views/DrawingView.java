package ranjbar.amirh.photowrote_final_3.Views;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import ranjbar.amirh.photowrote_final_3.R;
import ranjbar.amirh.photowrote_final_3.data.DataBaseDescription.Note;

import static android.content.ContentValues.TAG;

/**
 * Created by amirh on 28/06/17.
 */

public class DrawingView extends View{

    OnNoteTouchedListener onNoteTouchedListener;
    // defines paint and canvas
    Paint drawPaint;
    Bitmap noteImageBitmap;
    int paintColor = Color.BLACK;
    Uri photoUri;// for loading notes as via link
    boolean addingNewNote =true;
    float pointX1 ;
    float pointY1 ;
    float pointX2 ;
    float pointY2 ;

    int desiredWidth =100;
    int desiredHeight = 100;
    // stores next path
    ArrayList<Path> pathArrayList = new ArrayList<>();
    ArrayList<String> titleArrayList = new ArrayList<>();
    ArrayList<String> infoArrayList = new ArrayList<>();
    boolean loadEditType= true;//Enter in Load Mode by defualt


    public DrawingView(Context context , AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();

    }

    public DrawingView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();


    }

    public interface OnNoteTouchedListener{
        void onNoteTouched(String title , String info);
    }

    public void setOnNoteTouchedListener(OnNoteTouchedListener l){
        onNoteTouchedListener = l;
    }

    public void setupPaint() {

        noteImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noteimage);
        // Setup paint with color and stroke styles
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setPhotoUri(Uri uri){
        photoUri = uri;
    }

    public void setArrayOfPoints(float p[], String title , String info) {
        pointX1 = p[0];
        pointY1 = p[1];
        pointX2 = p[2];
        pointY2 = p[3];

        Path path = new Path();
        path.moveTo(pointX1,pointY1);
        path.lineTo(pointX2,pointY2);
        pathArrayList.add(path);

        titleArrayList.add(title);

        infoArrayList.add(info);

        Log.d(TAG, "Set Array of paths , index:    " + pathArrayList.indexOf(path));
        Log.d(TAG, "Set Array of titles , index:    " + titleArrayList.indexOf(title));
        Log.d(TAG, "Set Array of infos , index:    " + infoArrayList.indexOf(info));

    }

    public void setLoadEditType(boolean type){
        loadEditType = type;
    }
    public Paint getDrawPaint(){
        return drawPaint;
    }

    public void clear(){
        pathArrayList.clear();
        titleArrayList.clear();
        infoArrayList.clear();
        pointX1 = pointY1 = pointX2 = pointY2 = 0;
        invalidate();
    }

    public void setDesiredWidthHeight(int height , int width){
        desiredHeight = height ;
        desiredWidth = width ;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        Log.d(TAG , "---- , width : " + widthMeasureSpec);
//        Log.d(TAG , "---- , height : " + heightMeasureSpec);
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        int width;
//        int height;
//
//        //Measure Width
//        if (widthMode == MeasureSpec.EXACTLY) {
//            //Must be this size
//            width = widthSize;
//            Log.d(TAG , "widthMode == MeasureSpec.EXACTLY , width : " + width);
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            //Can't be bigger than...
//            width = Math.min(desiredWidth, widthSize);
//            Log.d(TAG , "widthMode == MeasureSpec.AT_MOST , width : " + width);
//
//        } else {
//            //Be whatever you want
//            width = desiredWidth;
//            Log.d(TAG , "else  , width : " + width);
//
//        }
//
//        //Measure Height
//        if (heightMode == MeasureSpec.EXACTLY) {
//            //Must be this size
//            height = heightSize;
//            Log.d(TAG , "heightMode == MeasureSpec.EXACTLY , height : " + height);
//
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            //Can't be bigger than...
//            height = Math.min(desiredHeight, heightSize);
//            Log.d(TAG , "heightMode == MeasureSpec.AT_MOST , height : " + height);
//
//        } else {
//            //Be whatever you want
//            height = desiredHeight;
//            Log.d(TAG , " else  , height : " + height);
//        }
//
//        //MUST CALL THIS
//        setMeasuredDimension(width, height);
//
//    }
    public void setDesiredParameters(int w , int h){
        desiredHeight = h;
        desiredWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float height = noteImageBitmap.getHeight();
        float width = noteImageBitmap.getWidth();

        float startPoint[]={0f,0f};
        float endPoint[]={0f,0f};

        PathMeasure pmStart ;
        PathMeasure pmEnd ;

        if(pointX1 == pointX2 && pointY1 == pointY2  )
        {
            if(!loadEditType)
                Toast.makeText(getContext(), "Please Draw a Line", Toast.LENGTH_LONG).show();
        }
        else  {

            canvas.drawLine(pointX1, pointY1, pointX2, pointY2, drawPaint);
            if(pointX1 < pointX2)
                 canvas.drawBitmap(noteImageBitmap,pointX1 ,pointY1 - height ,drawPaint);
            else
                canvas.drawBitmap(noteImageBitmap,pointX2 ,pointY2 - height ,drawPaint);

        }
        Log.d(TAG , "konnnnnnnnnnnnnnnnnnn gonde , onDraw paths :  :" );

        for(Path p : pathArrayList ) {

            pmStart = new PathMeasure(p,false);
            pmStart.getPosTan(0 ,startPoint,null);

            pmEnd = new PathMeasure(p,false);
            pmEnd.getPosTan(pmEnd.getLength() ,endPoint,null);

            canvas.drawPath(p, drawPaint);
            if(startPoint[0] < endPoint[0])
                  canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.noteimage)
                          , startPoint[0] , startPoint[1] - height ,drawPaint);
            else
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.noteimage)
                        , endPoint[0] , endPoint[1] - height ,drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked(); // event type
        int actionIndex = event.getActionIndex(); // pointer (i.e., finger)

        float pX = event.getX(actionIndex);
        float pY = event.getY(actionIndex);

        float startPoint[]={0f,0f};
        float endPoint[]={0f,0f};

        PathMeasure pmStart ;
        PathMeasure pmEnd ;

        if(loadEditType){

            //Load mode
            // determine whether touch started, ended or is moving
            if (action == MotionEvent.ACTION_DOWN ||
                    action == MotionEvent.ACTION_POINTER_DOWN) {
                //nothing yet
            }
            else if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_POINTER_UP) {

                Path p;
                for(int cnt=0 ; cnt< pathArrayList.size() ; cnt++ ) {
                    p = pathArrayList.get(cnt);

                    Log.d(TAG, " points   : " + pX + " + " + pY);

                    pmStart = new PathMeasure(p, false);
                    pmStart.getPosTan(0, startPoint, null);

                    pmEnd = new PathMeasure(p, false);
                    pmEnd.getPosTan(pmEnd.getLength(), endPoint, null);

                    Log.d(TAG, " point  pX >  : " + (endPoint[0] - noteImageBitmap.getWidth()));
                    Log.d(TAG, " point  pX <  : " + endPoint[0]);
                    Log.d(TAG, " point  pY >  : " + (endPoint[1] - noteImageBitmap.getHeight()));
                    Log.d(TAG, " point  pY <  : " + endPoint[1]);

                    if (startPoint[0] < endPoint[0]) {
                        if (pX > startPoint[0]
                                && pX < (startPoint[0] + noteImageBitmap.getWidth())
                                && pY > (startPoint[1] - noteImageBitmap.getHeight())
                                && pY < (startPoint[1])
                                ) {
                            //call the detailFragment
                            //need DrawingView interface that implement in EditorActivity

                            Log.d(TAG, "before onNoteTouched event ,  title : " + titleArrayList.get(cnt));
                            Log.d(TAG, "before  onNoteTouched event ,  info : " + infoArrayList.get(cnt));

                            onNoteTouchedListener.onNoteTouched(titleArrayList.get(cnt), infoArrayList.get(cnt));
                        }
                    }
                    else  {
                        if (pX > endPoint[0]
                                && pX < (endPoint[0] + noteImageBitmap.getWidth())
                                && pY > (endPoint[1] - noteImageBitmap.getHeight())
                                && pY < (endPoint[1])
                                ) {
                            //call the detailFragment
                            //need DrawingView interface that implement in EditorActivity

                            Log.d(TAG, "before onNoteTouched event ,  title : " + titleArrayList.get(cnt));
                            Log.d(TAG, "before  onNoteTouched event ,  info : " + infoArrayList.get(cnt));

                            onNoteTouchedListener.onNoteTouched(titleArrayList.get(cnt), infoArrayList.get(cnt));
                        }
                    }
                }
            }
            else
                return false;

            invalidate(); // redraw
        }
        else {

            //Edit Mode
            // Checks for the event that occurs
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointX1 = pX;
                    pointY1 = pY;
                    return true;
                case MotionEvent.ACTION_UP:
                    pointX2 = pX;
                    pointY2 = pY;
                    break;
                default:
                    return false;
            }
            // Force a view to draw again
            postInvalidate();
        }
        return true;
    }

    public void SavingNote(String title , String info) {


            if (pointX1 == pointX2 && pointY1 == pointY2) {
                if(!loadEditType)
                    Toast.makeText(getContext(), "Please Draw a Line", Toast.LENGTH_LONG).show();
            } else  {

                Log.d(TAG, " points   : " + pointX1 + " + " + pointY1);
                Log.d(TAG, " points   : " + pointX2 + " + " + pointY2);
                Log.d(TAG, " title   :::::::: " + title);
                Log.d(TAG, " info   :::::::: " + info);
                Log.d(TAG, " Photo Name , lastSegment : " + photoUri.getLastPathSegment());
                // create ContentValues object containing    contact's key-value pairs
                ContentValues contentValues = new ContentValues();
                contentValues.put(Note.COLUMN_NAME, photoUri.getLastPathSegment());
                contentValues.put(Note.COLUMN_POINTX1, pointX1);
                contentValues.put(Note.COLUMN_POINTY1, pointY1);
                contentValues.put(Note.COLUMN_POINTX2, pointX2);
                contentValues.put(Note.COLUMN_POINTY2, pointY2);
                contentValues.put(Note.COLUMN_TITLE, title);
                contentValues.put(Note.COLUMN_INFO, info);


                if (addingNewNote) {
                    // when called SavingNote , add new path to paths array
                    Path path = new Path();
                    path.moveTo(pointX1, pointY1);
                    path.lineTo(pointX2, pointY2);
                    pathArrayList.add(path);

                    titleArrayList.add(title);

                    infoArrayList.add(info);

                    // use Activity's ContentResolver to invoke
                    // insert on the AddressBookContentProvider
                    Uri newContactUri = getContext().getContentResolver().insert(
                            Note.CONTENT_URI, contentValues);

                    if (newContactUri != null) {
                        Log.v(TAG, " add shod , NewContactUri : " + newContactUri);
                        Toast.makeText(getContext(), "New Note has Successfully Added", Toast.LENGTH_LONG).show();

                        //Snackbar.make(linearLayout,
                        //       R.string.note_added,Snackbar.LENGTH_LONG).show();
                        //listener.onAddEditCompleted(newContactUri);
                    } else {
                        //Snackbar.make(linearLayout,
                        //        R.string.note_not_added, Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "kirrrrrrrrrrr  2::  " + R.string.note_not_added);
                    }
                } else {
                    // use Activity's ContentResolver to invoke
                    // insert on the AddressBookContentProvider
                    int updatedRows = getContext().getContentResolver().update(
                            photoUri, contentValues, null, null);

                    if (updatedRows > 0) {
                        // listener.onAddEditCompleted(contactUri);
                        //   Snackbar.make(linearLayout,
                        //         R.string.note_updated, Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "kirrrrrrrrrrr 3::  " + R.string.note_not_added);
                    } else {
                        //  Snackbar.make(linearLayout,
                        //        R.string.note_not_updated, Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "kirrrrrrrrrrr 4::  " + R.string.note_not_updated);
                    }
                }
            }
        }
}