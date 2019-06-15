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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";

    private Vector<HashMap> mDataset;
    private String mSection;

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

    MyAdapter(String section) {
        mSection = section;
        mDataset = new Vector<>();
    }

    void setDataset(HashMap myDataset){
        if(myDataset == null)
            return;

        mDataset = new Vector<>();

        for (Object o : myDataset.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            mDataset.add((HashMap) pair.getValue());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // returns the index where the new data has been added
    void addData(HashMap newEntry){
        mDataset.add(0, newEntry);
        this.notifyItemInserted(0);
    }

    // everything else could have changed but not the document id so we use that as reference
    void changeData(HashMap editedEntry){
        String documentID = editedEntry.get(Database.DB_KEY_DOCID).toString();

        for(int i = 0; i < mDataset.size(); i++){
            if(documentID.equals(mDataset.elementAt(i).get(Database.DB_KEY_DOCID).toString())) {
                mDataset.set(i, editedEntry);
                this.notifyItemChanged(i);
                return;
            }
        }
    }

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
