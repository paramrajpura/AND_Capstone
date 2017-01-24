package app.com.example.android.arxivreader;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Admin on 12-Jun-16.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        WidgetDataProvider dataProvider = new WidgetDataProvider(
                getApplicationContext(), intent);
        return dataProvider;
    }

}
