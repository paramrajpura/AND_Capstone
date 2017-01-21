package app.com.example.android.arxivreader;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import static android.content.Context.MODE_PRIVATE;


public class SearchPreferencesFragment extends Fragment{

    public static final String SEARCH_FOR_KEY = "SearchFor";
    public static final String SEARCH_CAT_KEY = "Category";
    public static final String SORT_BY_KEY = "SortBy";
    public static final String PREF_KEY_SEARCH_FOR = "PrefSearchFor";
    public static final String PREF_KEY_CAT = "PrefCategory";
    public static final String PREF_KEY_SORT = "PrefSortBy";

    public static final String DEF_SEARCH_VALUE = "all:";
    public static final String DEF_CAT_VALUE = "";
    public static final String DEF_SORT_VALUE = "relevance";
    public SearchPreferencesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_search_preferences, container, false);

        RadioGroup rgFor = (RadioGroup) rootView.findViewById(R.id.search_for_rg);

        final SharedPreferences.Editor searchForEditor = getActivity().
                getSharedPreferences(SEARCH_FOR_KEY, MODE_PRIVATE).edit();
        searchForEditor.putString(PREF_KEY_SEARCH_FOR,DEF_SEARCH_VALUE);
        searchForEditor.apply();

        rgFor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.v("In rg","In");
                switch(checkedId){
                    case R.id.all_search_radio_button:
                        // do operations specific to this selection
                        searchForEditor.putString(PREF_KEY_SEARCH_FOR,DEF_SEARCH_VALUE);
                        //Log.v("In rg","all");
                        break;
                    case R.id.author_radio_button:
                        // do operations specific to this selection
                        searchForEditor.putString(PREF_KEY_SEARCH_FOR, "au:");
                        //Log.v("In rg","author");
                        break;
                    case R.id.title_radio_button:
                        // do operations specific to this selection
                        searchForEditor.putString(PREF_KEY_SEARCH_FOR, "ti:");
                        //Log.v("In rg","title");
                        break;
                    case R.id.abstract_radio_button:
                        // do operations specific to this selection
                        searchForEditor.putString(PREF_KEY_SEARCH_FOR, "abs:");
                        //Log.v("In rg","abs");
                        break;
                }
                searchForEditor.apply();
            }
        });

        RadioGroup rgCat = (RadioGroup) rootView.findViewById(R.id.search_cat_rg);

        final SharedPreferences.Editor searchCatEditor = getActivity().
                getSharedPreferences(SEARCH_CAT_KEY, MODE_PRIVATE).edit();
        searchCatEditor.putString(PREF_KEY_CAT, DEF_CAT_VALUE);
        searchCatEditor.apply();

        rgCat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.v("In rg","In");
                switch(checkedId){
                    case R.id.stat_radio_button:
                        // do operations specific to this selection
                        searchCatEditor.putString(PREF_KEY_CAT, "+AND+cat:stat.*");
                        //Log.v("In rg","all");
                        break;
                    case R.id.qbio_radio_button:
                        // do operations specific to this selection
                        searchCatEditor.putString(PREF_KEY_CAT, "+AND+cat:q-bio.*");
                        //Log.v("In rg","author");
                        break;
                    case R.id.cs_radio_button:
                        // do operations specific to this selection
                        searchCatEditor.putString(PREF_KEY_CAT, "+AND+cat:cs.*");
                        //Log.v("In rg","title");
                        break;
                    case R.id.phy_radio_button:
                        // do operations specific to this selection
                        searchCatEditor.putString(PREF_KEY_CAT, "+AND+cat:physics.*");
                        //Log.v("In rg","abs");
                        break;
                    case R.id.math_radio_button:
                        // do operations specific to this selection
                        searchCatEditor.putString(PREF_KEY_CAT, "+AND+cat:math.*");
                        //Log.v("In rg","abs");
                        break;
                    case R.id.all_cat_radio_button:
                        // do operations specific to this selection
                        searchCatEditor.putString(PREF_KEY_CAT, DEF_CAT_VALUE);
                        //Log.v("In rg","abs");
                        break;
                }
                searchCatEditor.apply();
            }
        });

        RadioGroup rgSort = (RadioGroup) rootView.findViewById(R.id.sort_by_rg);

        final SharedPreferences.Editor sortByEditor = getActivity().
                getSharedPreferences(SORT_BY_KEY, MODE_PRIVATE).edit();
        sortByEditor.putString(PREF_KEY_SORT, DEF_SORT_VALUE);
        sortByEditor.apply();

        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Log.v("In rg","In");
                switch(checkedId){
                    case R.id.date_radio_button:
                        // do operations specific to this selection
                        sortByEditor.putString(PREF_KEY_SORT, "lastUpdatedDate");
                        //Log.v("In rg","all");
                        break;
                    case R.id.relevance_radio_button:
                        // do operations specific to this selection
                        sortByEditor.putString(PREF_KEY_SORT, DEF_SORT_VALUE);
                        //Log.v("In rg","author");
                        break;
                }
                sortByEditor.apply();
            }
        });
        return rootView;
    }

}
