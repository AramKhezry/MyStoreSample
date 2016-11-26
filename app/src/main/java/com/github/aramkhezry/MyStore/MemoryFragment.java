package com.github.aramkhezry.MyStore;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.aramkhezry.MyStore.Dao.Memory;
import com.mikepenz.materialdrawer.AccountHeader;
import com.nguyenhoanglam.imagepicker.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bkhezry on 11/22/2016.
 */

public class MemoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private MemorysAdapter adapter;
    private List<Memory> memoryList;
    private AccountHeader headerResult = null;
    private static final String ARG_POSITION = "position";

    TextView textView;

    private int position;

    public static MemoryFragment newInstance(int position) {
        MemoryFragment f = new MemoryFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);

        final FloatingActionButton addmemory = (FloatingActionButton)rootView.findViewById(R.id.add_memory);
        addmemory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNewMemory.class);
                startActivityForResult(intent, 1001);

            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        memoryList = new ArrayList<>();
        adapter = new MemorysAdapter(memoryList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(MainActivity.this, "Position: "+categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), ShowMemoryActivity.class);
                        intent.putExtra("memory", memoryList.get(position));
                        startActivityForResult(intent, 1002);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Scroll Down
                    if (addmemory.isShown()) {
                        addmemory.hide();
                    }

                } else if (dy < 0) {
                    // Scroll Up
                    if (!addmemory.isShown()) {
                        addmemory.show();
                    }
                }
            }

        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareMemorys();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            prepareMemorys();

        } else if (requestCode == 1002) {
            prepareMemorys();

        }
    }






    private void prepareMemorys() {

        DataBaseHandler dataBaseHandler = new DataBaseHandler(getActivity());
        List<Memory> memoryListDb = dataBaseHandler.getAllMemory();
        memoryList.clear();
        memoryList.addAll(memoryListDb);
        adapter.notifyDataSetChanged();
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
