package edu.ramapo.ajha.eco;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DisplayListFragment extends Fragment {
    private static final String TAG = "DisplayListFragment";

    private static final String THIS_FRAGMENT_TITLE = "title";

    private String mThisFragmentTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public DisplayListFragment() {
        // Required empty public constructor
    }

    static DisplayListFragment newInstance(String fragmentTitle) {
        DisplayListFragment fragment = new DisplayListFragment();
        Bundle args = new Bundle();
        // inserting into the bundle as key-value pair so data can be retrieved using the key
        args.putString(THIS_FRAGMENT_TITLE, fragmentTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThisFragmentTitle = getArguments().getString(THIS_FRAGMENT_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_events, container, false);

        mRecyclerView = rootView.findViewById(R.id.view_events);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // gets meta data for the fragment and sets on change listeners for new data points
        Database.getMetaData(mThisFragmentTitle, mRecyclerView);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}
