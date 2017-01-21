package app.com.example.android.arxivreader;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(authority = EPaperProvider.AUTHORITY, database = EPaperDatabase.class)
public final class EPaperProvider {
    public static final String AUTHORITY =
            "app.com.example.android.arxivreader.EPaperProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String FAVS= "favs";
    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = EPaperDatabase.FAVS) public static class EPapers{
        @ContentUri(
                path = Path.FAVS,
                type = "vnd.android.cursor.dir/favs",
                defaultSort = EPaperColumns.CATEGORY + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.FAVS);

        @InexactContentUri(
                name = "TITLE",
                path = Path.FAVS + "/*",
                type = "vnd.android.cursor.item/favs",
                whereColumn = EPaperColumns.TITLE,
                pathSegment = 1)
        public static Uri withId(String title){
            return buildUri(Path.FAVS, title);
        }
    }
}