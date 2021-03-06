package com.github.aramkhezry.MyStore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.bumptech.glide.Glide;
import com.github.aramkhezry.MyStore.Dao.Memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by Raman on 11/21/2016.
 */

public class ShowMemoryActivity extends AppCompatActivity {

    private TextView title ,textMemory,dateMemory;
    private ImageView imageMemory,more,fovrite;
    TagContainerLayout mTagContainerLayout;
     Memory memory;
    DataBaseHandler dataBaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showmemory_layout);
        Intent intent = getIntent();
        setTitle("");

        dataBaseHandler=new DataBaseHandler(ShowMemoryActivity.this);
        memory =(Memory) intent.getSerializableExtra("memory");

        mTagContainerLayout=(TagContainerLayout) findViewById(R.id.tags_card);
        mTagContainerLayout.setGravity(Gravity.RIGHT);
        title=(TextView)findViewById(R.id.titleCard);
        textMemory=(TextView)findViewById(R.id.textCard);
        dateMemory=(TextView)findViewById(R.id.dateCard);
        imageMemory=(ImageView)findViewById(R.id.imageCard);
        fovrite=(ImageView)findViewById(R.id.favorite);

        showData(memory);

        fovrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!memory.getFavorite()) {
                    Glide.with(ShowMemoryActivity.this).load(R.drawable.heart_favorit_sign).into(fovrite);
                    memory.setFavorite(true);
                    dataBaseHandler.addFavorite(memory.getId(),true);

                }
                else {
                    Glide.with(ShowMemoryActivity.this).load(R.drawable.heart_favorit).into(fovrite);
                    memory.setFavorite(false);
                    dataBaseHandler.addFavorite(memory.getId(),false);
                }

            }
        });

        more=(ImageView)findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(more, memory.getId());
            }
        });

    }

    private void showData(Memory memory) {
        if(memory.getFavorite()) {
            Glide.with(ShowMemoryActivity.this).load(R.drawable.heart_favorit_sign).into(fovrite);

        }
        else {
            Glide.with(ShowMemoryActivity.this).load(R.drawable.heart_favorit).into(fovrite);

        }
        Glide.with(ShowMemoryActivity.this).load(this.memory.getImageName()).into(imageMemory);
        dateMemory.setText(memory.getCreatTime());
        textMemory.setText(memory.getText());
        title.setText(memory.getTitle());
        List<String> strings = new ArrayList<String>(Arrays.asList(memory.getHashtag().split(";")));
        mTagContainerLayout.setTags(strings);

    }

    private void showPopupMenu(View view, Long id) {
        PopupMenu popup = new PopupMenu(ShowMemoryActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_option, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id));
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private Long position;


        public MyMenuItemClickListener(Long  position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.remove:
                    new MaterialDialog.Builder(ShowMemoryActivity.this)
                            .contentColor(Color.WHITE)
                            .theme(Theme.DARK)
                            .contentGravity(GravityEnum.END)
                            .titleGravity(GravityEnum.END)
                            .buttonsGravity(GravityEnum.END)
                            .title(" می خواهید این خاطره را حذف کنید؟")
                            .content("عنوان خاطره:" + memory.getTitle())
                            .positiveText("بلی")
                            .negativeText("خیر")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dataBaseHandler.removeMemory(memory.getId());
                                    finish();
                            }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();


                    return true;

                case R.id.edite:

                    Intent intent = new Intent(ShowMemoryActivity.this, EditActivity.class);
                    intent.putExtra("memory", memory);
                    startActivityForResult(intent, 1001);
                    return true;

                default:
            }
            return false;
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && requestCode==RESULT_OK) {
            memory =(Memory) data.getSerializableExtra("memory");
            showData(memory);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_option; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memory_show, menu);
        return true;
    }
}
