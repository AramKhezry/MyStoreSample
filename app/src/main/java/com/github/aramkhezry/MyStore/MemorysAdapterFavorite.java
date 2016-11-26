package com.github.aramkhezry.MyStore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.aramkhezry.MyStore.Dao.Memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;


/**
 * Created by Raman on 11/7/2016.
 */
public class MemorysAdapterFavorite extends RecyclerView.Adapter<MemorysAdapterFavorite.MyViewHolder> {

    private DataBaseHandler dataBaseHandler;

    private List<Memory> memoreyList;
    private Context context;

    public MemorysAdapterFavorite(List<Memory> memoreyList, Context context) {
        dataBaseHandler = new DataBaseHandler(context);
        this.memoreyList = memoreyList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView titleMemorys, textMEmorys, dateMemory;
        private ImageView urlImageMemory,favorite;
        private TagContainerLayout tagContainerLayout;


        public MyViewHolder(View View) {
            super(View);

            urlImageMemory = (ImageView) View.findViewById(R.id.imageCard_favorite);
            titleMemorys = (TextView) View.findViewById(R.id.titleCard_favorite);
            textMEmorys = (TextView) View.findViewById(R.id.textCard_favorite);
            textMEmorys.setMovementMethod(new ScrollingMovementMethod());
            dateMemory = (TextView) View.findViewById(R.id.dateCard_favorite);
            favorite = (ImageView) View.findViewById(R.id.favorite_favorite);
            tagContainerLayout=(TagContainerLayout) View.findViewById(R.id.tags_card);
            tagContainerLayout.setGravity(Gravity.RIGHT);


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_card_favorite, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Memory memorey = memoreyList.get(position);
        holder.titleMemorys.setText(memorey.getTitle());
        holder.textMEmorys.setText(memorey.getText());
        holder.dateMemory.setText(memorey.getCreatTime());
        Glide.with(context).load(memorey.getImageName()).into(holder.urlImageMemory);
        List<String> strings = new ArrayList<String>(Arrays.asList(memorey.getHashtag().split(";")));
        holder.tagContainerLayout.setTags(strings);


        if(memorey.getFavorite()) {
            Glide.with(context).load(R.drawable.heart_favorit_sign).into(holder.favorite);

        }
        else {
            Glide.with(context).load(R.drawable.heart_favorit).into(holder.favorite);

        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!memorey.getFavorite()) {
                    Glide.with(context).load(R.drawable.heart_favorit_sign).into(holder.favorite);
                    memoreyList.get(position).setFavorite(true);
                    dataBaseHandler.addFavorite(memorey.getId(),true);

                }
                else {
                    Glide.with(context).load(R.drawable.heart_favorit).into(holder.favorite);
                    memoreyList.get(position).setFavorite(false);
                    dataBaseHandler.addFavorite(memorey.getId(),false);
                    memoreyList.remove(position);

                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return memoreyList.size();
    }

}
