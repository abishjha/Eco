package edu.ramapo.ajha.eco;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";

    private Vector<HashMap> mDataset;
    private String mSection;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;

        // each data item is just a string in this case
        public TextView textViewTitle;
        public TextView textViewAuthor;
        public TextView textViewTime;

        public MyViewHolder(View v) {
            super(v);
            this.view = v;

            textViewTitle = (TextView) v.findViewById(R.id.text_title);
            textViewAuthor = (TextView) v.findViewById(R.id.text_author);
            textViewTime = (TextView) v.findViewById(R.id.text_time);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(HashMap myDataset, String section) {
        mDataset = new Vector<>();
        mSection = section;

        if(myDataset == null)
            return;

        Iterator it = myDataset.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
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
        holder.textViewTitle.setText((String) currItem.get("title"));
        holder.textViewAuthor.setText((String) currItem.get("author"));
        holder.textViewTime.setText((String) currItem.get("time"));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // proceed to the detail activity screen i.e. the screen for individual items
                Intent intent = new Intent(view.getContext(), DetailActivity.class);

                // include the data point unique identifier for the next activity
                intent.putExtra("section", mSection);
                intent.putExtra("docID", ((String) currItem.get("docID")));
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
}
