// DatabaseDescription.java
// Describes the table name and column names for this app's database,
// and other information required by the ContentProvider
package ranjbar.amirh.photowrote_final_3.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataBaseDescription {
    // ContentProvider's name: typically the package name
    public static final String AUTHORITY =
            "ranjbar.amirh.photowrote_final_3.data";

    // base URI used to interact with the ContentProvider
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    // nested class defines contents of the contacts table
    public static final class Note implements BaseColumns {
        public static final String TABLE_NAME = "notes"; // table's name

        // Uri for the contacts table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        // column names for contacts table's columns
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POINTX1 = "x1";
        public static final String COLUMN_POINTY1 = "y1";
        public static final String COLUMN_POINTX2 = "x2";
        public static final String COLUMN_POINTY2 = "y2";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_INFO = "info";
        public static final String COLUMN_COLOR = "color";

        // creates a Uri for a specific contact
        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
