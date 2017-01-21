package app.com.example.android.arxivreader;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

public interface EPaperColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID ="_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String LINK_ID = "link_id";

    @DataType(DataType.Type.TEXT) @NotNull
    @Unique
    public static final String TITLE = "title";
    
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String AUTHORS = "authors";
    
    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SUMMARY ="summary";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String PUBLISHED_DATE ="published_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String CATEGORY ="category";
}