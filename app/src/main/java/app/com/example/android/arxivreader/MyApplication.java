package app.com.example.android.arxivreader;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Admin on 18-Jan-17.
 */

public class MyApplication extends Application {
    public Tracker mTracker;
    public void startTracking(){
        if(mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker = ga.newTracker(R.xml.track_app);
            ga.enableAutoActivityReports(this);
        }
    }

    public Tracker getTracker(){

        startTracking();
        return mTracker;
    }
}
