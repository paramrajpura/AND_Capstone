package app.com.example.android.arxivreader;//package com.sam_chordas.android.schematicplanets.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;


@Database(version = EPaperDatabase.VERSION)
public final class EPaperDatabase {
    private EPaperDatabase(){}

    public static final int VERSION = 1;


        @Table(EPaperColumns.class) public static final String FAVS = "favs";

}