package edu.ramapo.ajha.eco;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Vector;


/**/
/*
MyAdapter class

DESCRIPTION

        This non-activity class is used by the RecyclerView of the individual fragments to adapt
        the data to the model and display it accordingly onto the screen.  The benefit of using
        and adapter is it makes creating a list very easy and helps in auto updating the list with
        least effort as needed.

AUTHOR

        Abish Jha

DATE

        06/15/2019

*/
/**/
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";

    private Vector<HashMap> mDataset;
    private String mSection;


    /**/
    /*
    MyViewHolder class

    DESCRIPTION

            This non-activity class is used by the MyAdapter class to holds the content of a single
            CardView item.  This is a very simple class with some member variables whose value is
            set from the data extracted from the DB and loaded onto the card view.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    static class MyViewHolder extends RecyclerView.ViewHolder {
        View view;

        // each data item is just a string in this case
        TextView mTextViewTitle;
        TextView mTextViewAuthor;
        TextView mTextViewTime;

        MyViewHolder(View v) {
            super(v);
            this.view = v;

            mTextViewTitle = v.findViewById(R.id.text_title);
            mTextViewAuthor = v.findViewById(R.id.text_author);
            mTextViewTime = v.findViewById(R.id.text_time);
        }
    }


    /**/
    /*
    MyAdapter() MyAdapter()

    NAME

            MyAdapter - constructor

    SYNOPSIS

            MyAdapter(String section);

            section -> the section which this adapter represents

    DESCRIPTION

            Constructor for the MyAdapter class.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    MyAdapter(String section) {
        mSection = section;
        mDataset = new Vector<>();
    }


    /**/
    /*
    onCreateViewHolder() onCreateViewHolder()

    NAME

            onCreateViewHolder - constructor for the view

    SYNOPSIS

            MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

            parent -> the parent of the view being generated
            viewType -> the view type

    DESCRIPTION

            Constructor for the view holder.  Creates and inflates a view and wraps it in the
            ViewHolder and returns to the caller function for displaying.  Each view in this
            function is an item from the database shows in a CardView.

    RETURNS

            Returns an instance of MyAdapter.MyViewHolder with a CardView inside.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(v);
    }


    /**/
    /*
    onBindViewHolder() onBindViewHolder()

    NAME

            onBindViewHolder - constructor for the view

    SYNOPSIS

            void onBindViewHolder(MyViewHolder holder, final int position);

            holder -> the view holder which is the generated CardView
            position -> the index of this view in the RecyclerView holder

    DESCRIPTION

            Once the view holder is created, this function is invoked after being bound to the
            RecyclerView so that data can be populated in to the fields. Replaces the contents of a
            view (invoked by the layout manager).

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final HashMap currItem = mDataset.get(position);
        holder.mTextViewTitle.setText((String) currItem.get(Database.DB_KEY_TITLE));
        Database.appendDisplayName((String) currItem.get(Database.DB_KEY_AUTHORID), holder.mTextViewAuthor);
        holder.mTextViewTime.setText((String) currItem.get(Database.DB_KEY_TIME));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // proceed to the detail activity screen i.e. the screen for individual items
                Intent intent = new Intent(view.getContext(), DetailActivity.class);

                // include the data point unique identifier for the next activity
                intent.putExtra("section", mSection);
                intent.putExtra("docID", ((String) currItem.get(Database.DB_KEY_DOCID)));
                intent.putExtra("index", position);

                ((Activity) view.getContext()).startActivityForResult(intent, 0);
            }
        });
    }


    /**/
    /*
    getItemCount() getItemCount()

    NAME

            getItemCount - size of dataset

    SYNOPSIS

            int getItemCount();

    DESCRIPTION

            The size of the dataset can be found from this function.

    RETURNS

            Returns the size of the dataset as an integer.

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    /**/
    /*
    addData() addData()

    NAME

            addData - adds data to the dataset

    SYNOPSIS

            void addData(HashMap newEntry);

            newEntry -> the new data point to be added to the dataset.

    DESCRIPTION

            This function is called by the database action listener to update the dataset if there
            is an addition in the database.  After addition into the dataset, the layout manager
            is notified about the change.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    void addData(HashMap newEntry){
        mDataset.add(0, newEntry);
        this.notifyItemInserted(0);
    }


    /**/
    /*
    changeData() changeData()

    NAME

            changeData - changes data in the dataset

    SYNOPSIS

            void changeData(HashMap editedEntry);

            editedEntry -> the data point to be edited in the dataset.

    DESCRIPTION

            This function is called by the database action listener to update the dataset if there
            is an edit in any entry in the database.  After editing the entry, the layout manager
            is notified about the change.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    void changeData(HashMap editedEntry){
        // everything else could have changed but not the document id so we use that as reference
        String documentID = editedEntry.get(Database.DB_KEY_DOCID).toString();

        for(int i = 0; i < mDataset.size(); i++){
            if(documentID.equals(mDataset.elementAt(i).get(Database.DB_KEY_DOCID).toString())) {
                mDataset.set(i, editedEntry);
                this.notifyItemChanged(i);
                return;
            }
        }
    }


    /**/
    /*
    removeData() removeData()

    NAME

            removeData - removes data from the dataset

    SYNOPSIS

            void removeData(HashMap removeEntry);

            removeEntry -> the data point to be removed from the dataset.

    DESCRIPTION

            This function is called by the database action listener to update the dataset if there
            is a removal in any entry in the database.  After removing the entry, the layout manager
            is notified about the change.

    RETURNS

            void

    AUTHOR

            Abish Jha

    DATE

            06/15/2019

    */
    /**/
    void removeData(HashMap removeEntry){
        String documentID = removeEntry.get(Database.DB_KEY_DOCID).toString();

        for(int i = 0; i < mDataset.size(); i++){
            if(documentID.equals(mDataset.elementAt(i).get(Database.DB_KEY_DOCID).toString())) {
                mDataset.remove(i);
                this.notifyItemRemoved(i);
                return;
            }
        }
    }
}
