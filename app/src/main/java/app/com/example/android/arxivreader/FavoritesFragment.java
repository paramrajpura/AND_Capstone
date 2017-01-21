package app.com.example.android.arxivreader;


import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView mRecyclerFavs;
    private EntryAdapter mAdapterFavs;
    private List<YourObject.Entry> mEntryList=new ArrayList<>();

    static final int COL_LINKID = 1;
    static final int COL_TITLE = 2;
    static final int COL_AUTHORS = 3;
    static final int COL_SUMMARY = 4;
    static final int COL_PUBLISHEDDATE = 5;
    static final int COL_CATEGORY = 6;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        mRecyclerFavs= (RecyclerView) rootView.findViewById(R.id.recycler_view_favs);
        mAdapterFavs=new EntryAdapter(getActivity(),mEntryList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerFavs.setLayoutManager(mLayoutManager);
        mRecyclerFavs.addItemDecoration(new GridSpacingItemDecorationFavs(2, dpToPx(10), true));
        mRecyclerFavs.setItemAnimator(new DefaultItemAnimator());
        mRecyclerFavs.setAdapter(mAdapterFavs);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),EPaperProvider.EPapers.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEntryList.clear();
        int totalEntries = 0;
        if (data != null) {
            totalEntries = data.getCount();
        }
        List<YourObject.Entry> entryList = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            for (int i = 0; i < totalEntries; i++) {
                YourObject.Entry tmp = new YourObject.Entry();
                tmp.id = data.getString(COL_LINKID);
                tmp.title = data.getString(COL_TITLE);
                tmp.datePublished = data.getString(COL_PUBLISHEDDATE);
                YourObject.Author tmpAut = new YourObject.Author();
                tmpAut.name= data.getString(COL_AUTHORS);
                tmp.authorList = new ArrayList<>();
                tmp.authorList.add(tmpAut);
                tmp.cat = new YourObject.Category();
                tmp.cat.catName = data.getString(COL_CATEGORY);
                tmp.description = data.getString(COL_SUMMARY);

                entryList.add(tmp);
                data.moveToNext();
            }
        }
        mEntryList.addAll(entryList);
        mAdapterFavs.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class GridSpacingItemDecorationFavs extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecorationFavs(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    public int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
    }
}
