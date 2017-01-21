package app.com.example.android.arxivreader;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.app.SearchManager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.transition.Fade;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class SearchActivity extends AppCompatActivity {

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static List<YourObject.Entry> mEntryList=new ArrayList<>();
    static RecyclerView mRecycler;
    static String mQuery;
    SearchView mSearchView;
    static EntryAdapter mAdapter;
    static boolean mUpdateFlag = true;
    private boolean mExpandSearchView = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService
                (Context.SEARCH_SERVICE);
        if(mExpandSearchView){
            menu.findItem(R.id.menu_search).expandActionView();
            mExpandSearchView =false;
        }
        mSearchView = (SearchView) menu.findItem
                (R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo
                (getComponentName()));
        mSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = (Fade) TransitionInflater.from(this).
                    inflateTransition(R.transition.main_exit);
            getWindow().setExitTransition(fade);
        }


        ((MyApplication) getApplication() ).startTracking();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }

        viewPager.setCurrentItem(1);
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        handleIntent(intent);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchPreferencesFragment(),getString(R.string.pref_tab_name) );
        adapter.addFragment(new SearchFragment(),getString(R.string.search_tab_name) );
        adapter.addFragment(new FavoritesFragment(),getString(R.string.favs_tab_name) );
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
            //Toast.makeText(this,mQuery,Toast.LENGTH_LONG).show();
            mEntryList.clear();
            SharedPreferences prefSearchFor = getSharedPreferences(
                    SearchPreferencesFragment.SEARCH_FOR_KEY, MODE_PRIVATE);
            String searchPref = prefSearchFor.getString(
                    SearchPreferencesFragment.PREF_KEY_SEARCH_FOR,
                    SearchPreferencesFragment.DEF_SEARCH_VALUE);
            mQuery = searchPref + mQuery;
            SharedPreferences prefCat = getSharedPreferences(
                    SearchPreferencesFragment.SEARCH_CAT_KEY, MODE_PRIVATE);
            String searchCat = prefCat.getString(
                    SearchPreferencesFragment.PREF_KEY_CAT, SearchPreferencesFragment.DEF_CAT_VALUE);
            if(!Objects.equals(searchCat, SearchPreferencesFragment.DEF_CAT_VALUE)){
                mQuery = mQuery + searchCat;
            }


            getDataSetAdapter(mQuery,"0",getSharedPreferences(
                    SearchPreferencesFragment.SORT_BY_KEY,MODE_PRIVATE).
                    getString(SearchPreferencesFragment.PREF_KEY_SORT,
                            SearchPreferencesFragment.DEF_SORT_VALUE));
            mSearchView.clearFocus();
        }

        else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            mExpandSearchView = true;
        }
    }

    private void getDataSetAdapter(String query, final String startPage, String sortBy){
        String maxResults = "10";
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://export.arxiv.org/")
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<YourObject> call = apiService.getUser(query,startPage,maxResults,sortBy);
        call.enqueue(new Callback<YourObject>() {
            @Override
            public void onResponse(Call<YourObject> call, Response<YourObject>
                    response) {
                if (response.isSuccessful()) {
                    // tasks available
                    //Log.v("TEST","in response "+startPage);
                    YourObject resultObj = response.body();
                    DataUtils.getAppendedList(resultObj,mEntryList);
                    mAdapter.notifyDataSetChanged();
                    mUpdateFlag = true;
                } else {
                    // error response, no access to resource?
                    //Log.v("TEST","error");
                    Toast.makeText(getBaseContext(),getString(R.string.error_arxiv),
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<YourObject> call, Throwable t) {
                // something went completely south (like no internet connection)
                Toast.makeText(getBaseContext(),getString(R.string.error_internet),
                        Toast.LENGTH_LONG).show();
                //Log.d("Error", t.getMessage());
            }
        });
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class SearchFragment extends Fragment {

        int pastVisiblesItems, visibleItemCount, totalItemCount;

        public SearchFragment() {
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
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);

            mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            mAdapter = new EntryAdapter(getActivity(), mEntryList);

            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecycler.setLayoutManager(mLayoutManager);
            //mRecycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            //mRecycler.setItemAnimator(new DefaultItemAnimator());
            mRecycler.setAdapter(mAdapter);


            mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findLastVisibleItemPosition();

                        if (mUpdateFlag) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                mUpdateFlag = false;
                                //Log.v("...", "Last Item Wow !");
                                ((SearchActivity) getActivity()).getDataSetAdapter(mQuery,
                                        String.valueOf(totalItemCount),
                                        getActivity().getSharedPreferences(
                                                SearchPreferencesFragment.SORT_BY_KEY, MODE_PRIVATE).
                                                getString(SearchPreferencesFragment.PREF_KEY_SORT,
                                                        SearchPreferencesFragment.DEF_SORT_VALUE));
                                //Do pagination.. i.e. fetch new data
                            }
                        }
                    }
                }
            });
            return rootView;
        }
    }
}

