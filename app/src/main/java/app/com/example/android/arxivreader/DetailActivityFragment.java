package app.com.example.android.arxivreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    YourObject.Entry mData = null;
    FloatingActionButton mFavButton,mPDFButton,mShareFab;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container,
                false);
        mData = DataHolder.getInstance().getData();
        TextView titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
        titleTextView.setText(mData.title.replaceAll("\\n"," "));
        TextView autTextView = (TextView) rootView.findViewById(R.id.authorTextView);
        String authors="";
        int total = mData.authorList.size();
        for(int itr=0;itr<total;itr++){
            YourObject.Author tmp = mData.authorList.get(itr);
            if(itr==total-1){
                authors = authors + tmp.name;
            }
            else{
                authors = authors + tmp.name + ", ";
            }
        }
        autTextView.setText(authors);
        TextView catTextView = (TextView) rootView.findViewById(R.id.categoryTextView);
        YourObject.Category test = mData.cat;
        catTextView.setText(test.catName);
        TextView dateTextView = (TextView) rootView.findViewById(R.id.dateTextView);
        String[] date = mData.datePublished.split(Pattern.quote("T"));
        dateTextView.setText(date[0]);
        TextView summaryTextView = (TextView) rootView.findViewById(R.id.summaryTextView);
        summaryTextView.setText(mData.description.replaceAll("\\n"," "));


        mFavButton = (FloatingActionButton) rootView.findViewById(R.id.add_fav_button);
        mFavButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DataSaveAsync asyncTask = new DataSaveAsync();
                asyncTask.execute();
//                Cursor c = getActivity().getContentResolver().query
//                        (EPaperProvider.EPapers.CONTENT_URI, null, null, null, null);
////                int count = 0;
////                if (c != null) {
////                    count = c.getCount();
////                }
//                //Toast.makeText(getContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
//                if (c != null) {
//                    c.close();
//                }
            }
        });

        mPDFButton = (FloatingActionButton) rootView.findViewById(R.id.load_pdf_button);
        mPDFButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = mData.id;
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                    url = url.replace("abs","pdf");
                    Intent launchPDF = new Intent(Intent.ACTION_VIEW);
                    launchPDF.setData(Uri.parse(url));
                    startActivity(launchPDF);
            }
        });

        mShareFab = (FloatingActionButton) rootView.findViewById(R.id.share_fab);
        mShareFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = mData.id;
                if (!url.startsWith("https://") && !url.startsWith("http://")) {
                    url = "http://" + url;
                }
                Intent launchShare = new Intent(Intent.ACTION_SEND);
                launchShare.setType("text/plain");
                launchShare.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(launchShare);
            }
        });

        return rootView;
    }

    private class DataSaveAsync extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(getContext(),getString(R.string.add_fav_msg),
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(),getString(R.string.del_fav_msg),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean isAdded =false;
            ContentValues cv = new ContentValues();
            cv.put(EPaperColumns.TITLE, mData.title.replaceAll("\\n"," "));
            String authors="";
            int total = mData.authorList.size();
            for(int itr=0;itr<total;itr++){
                YourObject.Author tmp = mData.authorList.get(itr);
                if(itr==total-1){
                    authors = authors + tmp.name;
                }
                else{
                    authors = authors + tmp.name + ", ";
                }
            }
            cv.put(EPaperColumns.AUTHORS, authors);
            cv.put(EPaperColumns.LINK_ID,mData.id);
            YourObject.Category tmpCat = mData.cat;
            cv.put(EPaperColumns.CATEGORY, tmpCat.catName);
            cv.put(EPaperColumns.PUBLISHED_DATE,mData.datePublished);
            cv.put(EPaperColumns.SUMMARY,mData.description.replaceAll("\\n"," "));


            Cursor c = getActivity().getContentResolver().query(
                    EPaperProvider.EPapers.withId(mData.title),null, null,null,null);
            if(c!=null){
                if (c.getCount() == 0) {
                    getActivity().getContentResolver().insert(EPaperProvider.EPapers.CONTENT_URI
                            , cv);
                    c.close();
                    isAdded = true;

                }
                else{
                    getActivity().getContentResolver().delete(EPaperProvider.EPapers.withId(
                            mData.title),null,null);
                    //isAdded = false;

                }
            }
            return isAdded;
        }
    }

}
