package com.github.aramkhezry.MyStore;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
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
import com.github.aramkhezry.MyStore.Dao.Memorey;
import com.mikepenz.materialdrawer.AccountHeader;
import com.nguyenhoanglam.imagepicker.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bkhezry on 11/22/2016.
 */

public class MemoryFoveritFragment extends Fragment {

    private RecyclerView  recyclerViewFavorite;
    private MemoreysAdapterFavorite adapterFavorite;
    private List<Memorey> memoryListFavorit;
    private AccountHeader headerResult = null;

    private static final String ARG_POSITION = "position";

    TextView textView;

    private int position;

    public static MemoryFoveritFragment newInstance(int position) {
        MemoryFoveritFragment f = new MemoryFoveritFragment();
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
        View rootView = inflater.inflate(R.layout.recycler_view_favorite, container, false);


        recyclerViewFavorite = (RecyclerView)rootView.findViewById(R.id.recycler_view_favorite);
        memoryListFavorit = new ArrayList<>();
        adapterFavorite = new MemoreysAdapterFavorite(memoryListFavorit, getActivity());
        RecyclerView.LayoutManager mLayoutManagerFavorite = new GridLayoutManager(getActivity(), 1);
        recyclerViewFavorite.setLayoutManager(mLayoutManagerFavorite);
        recyclerViewFavorite.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerViewFavorite.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFavorite.setAdapter(adapterFavorite);


        recyclerViewFavorite.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerViewFavorite, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Toast.makeText(MainActivity.this, "Position: "+categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), ShowMemoryActivity.class);
                        intent.putExtra("memory", memoryListFavorit.get(position));
                        startActivityForResult(intent, 1002);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareMemorysfavorite();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            new MaterialDialog.Builder(getActivity())
                    .contentColor(Color.WHITE)
                    .theme(Theme.DARK)
                    .contentGravity(GravityEnum.START)
                    .titleGravity(GravityEnum.START)
                    .buttonsGravity(GravityEnum.START)
                    .title("contact me at Twitter and Telegram and GitHub")
                    .contentColor(Color.RED)
                    .content("@aramkherzy")
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            prepareMemorysfavorite();

        } else if (requestCode == 1002) {
            prepareMemorysfavorite();

        }
    }




    private void prepareMemorysfavorite() {

        DataBaseHandler dataBaseHandler = new DataBaseHandler(getActivity());
        List<Memorey> memoreyListDb = dataBaseHandler.getAllMemoryFavorite();
        memoryListFavorit.clear();
        memoryListFavorit.addAll(memoreyListDb);
        adapterFavorite.notifyDataSetChanged();


    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
