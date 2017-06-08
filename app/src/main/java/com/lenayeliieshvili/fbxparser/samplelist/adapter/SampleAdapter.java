package com.lenayeliieshvili.fbxparser.samplelist.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenayeliieshvili.fbxparser.R;
import com.lenayeliieshvili.fbxparser.samplelist.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleHolder> {

    private final ItemClickListener mListener;
    private List<String> mData = new ArrayList<>();

    public SampleAdapter(List<String> data, ItemClickListener listener) {
        mListener = listener;
        mData = data;
    }

    @Override
    public SampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sample, parent, false);
        return new SampleHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(SampleHolder holder, int position) {
        holder.bindData(mData.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SampleHolder extends RecyclerView.ViewHolder {

        private final ItemClickListener mListener;
        private TextView mFileTitle;

        public SampleHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            mListener = listener;
            mFileTitle = (TextView) itemView.findViewById(R.id.text_file_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }

        public void bindData(String item) {
            mFileTitle.setText(item);
        }

    }
}
