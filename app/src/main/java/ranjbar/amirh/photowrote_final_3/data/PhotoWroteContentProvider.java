
// ContentProvider subclass for manipulating the app's database
package ranjbar.amirh.photowrote_final_3.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import ranjbar.amirh.photowrote_final_3.R;
import ranjbar.amirh.photowrote_final_3.data.DataBaseDescription.Note;

import static android.content.ContentValues.TAG;

public class PhotoWroteContentProvider extends ContentProvider {
    // used to access the database
    private PhotoWroteDataBaseHelper dbHelper;

    // UriMatcher helps ContentProvider determine operation to perform
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    // constants used with UriMatcher to determine operation to perform
    private static final int ONE_CONTACT = 1; // manipulate one contact
    private static final int CONTACTS = 2; // manipulate contacts table

    // static block to configure this ContentProvider's UriMatcher
    static {
        // Uri for Contact with the specified id (#)
        uriMatcher.addURI(DataBaseDescription.AUTHORITY,
                Note.TABLE_NAME + "/#", ONE_CONTACT);

        // Uri for Contacts table
        uriMatcher.addURI(DataBaseDescription.AUTHORITY,
                Note.TABLE_NAME, CONTACTS);
    }


    @Override
    public boolean onCreate() {

        dbHelper = new PhotoWroteDataBaseHelper(getContext());
        return true; // ContentProvider successfully created
    }

    // required method: Not used in this app, so we return null
    @Override
    public String getType(Uri uri) {
        return null;
    }

    // query the database
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        Log.d(TAG , " ContentProvider  Query , Uri : " + uri);

        // create SQLiteQueryBuilder for querying contacts table
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Note.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT: // Note with specified id will be selected
                queryBuilder.appendWhere(
//                        Note._ID + "=" + uri.getLastPathSegment());
                    Note.COLUMN_NAME + "=" + uri.getLastPathSegment());

                Log.d(TAG , "UriMatcher  :  " + uri);
                Log.d(TAG , "UriMatcher  :  " + uri.getLastPathSegment());

                break;
            case CONTACTS: // all Notes will be selected
                Log.d(TAG , "UriMatcher  : all Notes will be selected " );
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        // execute the query to select one or all notes
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);

        // configure to watch for content changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // insert a new Note in the database
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newContactUri = null;

        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                // insert the new Note--success yields new Note's row id
                long rowId = dbHelper.getWritableDatabase().insert(
                        Note.TABLE_NAME, null, values);


                // if the Note was inserted, create an appropriate Uri;
                // otherwise, throw an exception
                if (rowId > 0) { // SQLite row IDs start at 1
                    newContactUri = Note.buildContactUri(rowId);

                    Log.d(TAG , "note was inserted , rowID : " + rowId);

                    // notify observers that the database changed
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else {
                    Log.d(TAG , "Exception throw  :  Insert faild , rowID : " + rowId);

                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                }
                break;
            default:
                Log.d(TAG , "Exception throw :  Invalid insert Uri");

                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_insert_uri) + uri);
        }

        return newContactUri;
    }

    // update an existing Note in the database
    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int numberOfRowsUpdated; // 1 if update successful; 0 otherwise

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                // get from the uri the id of contact to update
                String id = uri.getLastPathSegment();

                // update the Note
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        Note.TABLE_NAME, values, Note._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }
        // if changes were made, notify observers that the database changed
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsUpdated;
    }

    // delete an existing Note from the database
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted =0;

        Log.d(TAG , " delete Note , selection  : " + selection);


        int pointX1Index = selection.indexOf(",");
        String pointX1 = selection.substring(0 , pointX1Index);

        selection=  selection.replaceFirst(pointX1 + "," , "");

        Log.d(TAG , " delete Note , pointX1Index  :: " + pointX1Index);
        Log.d(TAG , " delete Note , pointX1  :: " + pointX1);
        Log.d(TAG , " delete Note , selection  :: " + selection);

        int pointY1Index = selection.indexOf(",");
        String pointY1 = selection.substring(0 , pointY1Index);

        selection= selection.replaceFirst(pointY1 + "," , "");

        Log.d(TAG , " delete Note , pointY1Index  ::: " + pointY1Index);
        Log.d(TAG , " delete Note , pointY1  ::: " + pointY1);
        Log.d(TAG , " delete Note , selection  ::: " + selection);

        int pointX2Index = selection.indexOf(",");
        String pointX2 = selection.substring(0 , pointX2Index);

        selection= selection.replaceFirst(pointX2 + "," , "");

        Log.d(TAG , " delete Note , pointX2Index  :::: " + pointX2Index);
        Log.d(TAG , " delete Note , pointX2  :::: " + pointX2);
        Log.d(TAG , " delete Note , selection  :::: " + selection);

        int pointY2Index = selection.indexOf(",");
        String pointY2 = selection.substring(0 , pointY2Index);

        selection= selection.replaceFirst(pointY2 + "," , "");

        Log.d(TAG , " delete Note , pointY2Index  ::::: " + pointY2Index);
        Log.d(TAG , " delete Note , pointY2  ::::: " + pointY2);
        Log.d(TAG , " delete Note , selection  ::::: " + selection);

//        int noteNameIndex = selection.indexOf(",");
//        String noteName = selection.substring(0 , noteNameIndex);
//
//        selection = selection.replaceFirst(noteName + "," ,"");

//        Log.d(TAG , " delete Note , noteNameIndex  : " + noteNameIndex);

        String noteName = selection ;

        Log.d(TAG , " delete Note , noteName  : " + noteName);
        Log.d(TAG , " delete Note , selection  : " + selection);

        switch (uriMatcher.match(uri)) {
            case ONE_CONTACT:
                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , ONE_CONTACT  :: " + uri);
                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , selection ::: " + selection);

                // get from the Selection the id of Note to update
//                String noteName = uri.getLastPathSegment();


                // delete the Note
//                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
//                        Note.TABLE_NAME, Note.COLUMN_NAME + "=" + selection, selectionArgs);

                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        Note.TABLE_NAME ,
                        Note.COLUMN_POINTX1 + "=" + String.valueOf(pointX1) , null );


                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , numberOfRowsDeleted ::: " + numberOfRowsDeleted);

                break;
            case CONTACTS:
                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , CONTACTS ::: " + uri);
                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , selection ::: " + selection);


//                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
//                        Note.TABLE_NAME , Note.COLUMN_NAME + "=? and " + Note.COLUMN_POINTX1 + "=?" ,
//                        new String[]{noteName , pointX1});

//                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
//                        Note.TABLE_NAME ,
//                        Note.COLUMN_NAME + "=" + noteName , null );

                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        Note.TABLE_NAME ,
                        Note.COLUMN_NAME + "=555"  , null );

                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , point ::: " + pointX1);
                Log.d(TAG , " on Delete Note , PhotoWroteContentProvider , numberOfRowsDeleted ::: " + numberOfRowsDeleted);

                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // notify observers that the database changed
        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }
}