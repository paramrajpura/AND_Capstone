package app.com.example.android.arxivreader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.regex.Pattern;

/**
 * Created by Admin on 12-Jun-16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext = null;
    private Cursor data = null;

    static final int COL_ID = 0;
    static final int COL_TITLE = 2;
    static final int COL_AUTHORS = 3;
    static final int COL_SUMMARY = 4;
    static final int COL_PUBLISHEDDATE = 5;
    static final int COL_CATEGORY = 6;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (data.moveToPosition(position))
            return data.getLong(COL_ID);
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.list_item);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(), R.layout.list_item);
        String title = data.getString(COL_TITLE);
        String author = data.getString(COL_AUTHORS);
        String date = data.getString(COL_PUBLISHEDDATE);
        String cat = data.getString(COL_CATEGORY);
        String[] dateArray = date.split(Pattern.quote("T"));
        remoteView.setTextColor(R.id.title_text_view,Color.BLACK);
        remoteView.setTextViewText(R.id.title_text_view, title);
        remoteView.setTextColor(R.id.author_text_view,Color.BLACK);
        remoteView.setTextViewText(R.id.author_text_view, author);
        remoteView.setTextColor(R.id.date_text_view,Color.BLACK);
        remoteView.setTextViewText(R.id.date_text_view,dateArray[0]);
        remoteView.setTextColor(R.id.cat_text_view,Color.BLACK);
        remoteView.setTextViewText(R.id.cat_text_view,cat);

//        final Intent fillInIntent = new Intent();
//        fillInIntent.putExtra(Utils.QUOTE_SYMBOL,stockSymbol);
//        remoteView.setOnClickFillInIntent(R.id.stock_symbol, fillInIntent);
        return remoteView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        data = mContext.getContentResolver().query(EPaperProvider.EPapers.CONTENT_URI,
                null,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }


    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }
}
