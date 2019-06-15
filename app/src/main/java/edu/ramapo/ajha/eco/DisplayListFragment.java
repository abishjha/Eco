package edu.ramapo.ajha.eco;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**/
/*
DisplayListFragment class

DESCRIPTION

        This fragment is a template that utilizes the card view layout to display all the topics
        in a section/tab from the database on to the home screen.  Right now, all five section
        fragments utilize this class and the same layout because they have similarly formatted
        data.  RecyclerView is used with a custom adapter MyAdapter to display the list of items
        in the section.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class DisplayListFragment extends Fragment {
    private static final String TAG = "DisplayListFragment";

    private static final String THIS_FRAGMENT_TITLE = "title";

    private String mThisFragmentTitle;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public DisplayListFragment() {
        // Required empty public constructor
    }


    /**/
    /*
    newInstance() newInstance()

    NAME

            newInstance - creates a new instance of this fragment

    SYNOPSIS

            static DisplayListFragment newInstance(String fragmentTitle);

            fragmentTitle -> the title of the fragment being generated

    DESCRIPTION

            Constructs a fragment object of this class and returns it.  This is used by the
            sections pager adapter to display the fragment on the HomeActivity.

    RETURNS

            An instance of the DisplayListFragment.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static DisplayListFragment newInstance(String fragmentTitle) {
        DisplayListFragment fragment = new DisplayListFragment();
        Bundle args = new Bundle();
        // inserting into the bundle as key-value pair so data can be retrieved using the key
        args.putString(THIS_FRAGMENT_TITLE, fragmentTitle);
        fragment.setArguments(args);
        return fragment;
    }


    /**/
    /*
    onCreate() onCreate()

    NAME

            onCreate - function called on fragment creation

    SYNOPSIS

            void onCreate(Bundle savedInstanceState);

            savedInstanceState -> the saved instance state passed by the OS

    DESCRIPTION

            The function called on create of the fragment and is used to do initial setups.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThisFragmentTitle = getArguments().getString(THIS_FRAGMENT_TITLE);
        }
    }


    /**/
    /*
    onCreateView() onCreateView()

    NAME

            onCreateView - function called for fragment's view creation

    SYNOPSIS

            View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

            inflater -> the layout inflater
            container -> the container that contains the created fragment
            savedInstanceState -> the saved instance state passed by the OS

    DESCRIPTION

            The function is called when the view is created and is used to do setups and return the
            completed view to be displayed.

    RETURNS

            A View object that is to be displayed.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
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


    /**/
    /*
    onDetach() onDetach()

    NAME

            onDetach - function called on fragment detachment from parent view

    SYNOPSIS

            void onDetach();

    DESCRIPTION

            The function is called when the fragment is detached from the parent view and basically
            just detached the view from the parent view which is super.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
