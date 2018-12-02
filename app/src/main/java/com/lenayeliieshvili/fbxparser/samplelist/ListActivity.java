package com.lenayeliieshvili.fbxparser.samplelist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lenayeliieshvili.fbxparser.R;
import com.lenayeliieshvili.fbxparser.main.MainActivity;
import com.lenayeliieshvili.fbxparser.samplelist.adapter.SampleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity implements ItemClickListener{

    private RecyclerView mRecycler;
    private SampleAdapter mAdapter;
    private List<String> mData = new ArrayList<>();

    public static Intent getLaunchIntent(Context context) {
        Intent starter = new Intent(context, ListActivity.class);
        return starter;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mRecycler = (RecyclerView) findViewById(R.id.recycler_samples);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        try {
            String[] assets = getAssets().list("");
            for (String asset : assets) {
                if (asset.endsWith(".fbx"))
                    mData.add(asset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAdapter = new SampleAdapter(mData, this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(int pos) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("file_name", mData.get(pos));
        startActivity(intent);
    }
}
