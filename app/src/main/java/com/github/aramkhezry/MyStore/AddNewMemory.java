package com.github.aramkhezry.MyStore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.bumptech.glide.Glide;
import com.github.aramkhezry.MyStore.Dao.Memorey;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Raman on 10/23/2016.
 */

public class AddNewMemory extends AppCompatActivity implements DateSetListener {
    private static final int REQUEST_CODE_PICKER = 10;
    private ImageButton mStart;
    private ImageButton add_image;
    private ImageButton setMemory;
    private String imageURL;
    private EditText titleMemory;
    private EditText editTextMemory;
    private EditText editTextHashtag;
    private String setTime;
    private Button hashtag;


    TagContainerLayout mTagContainerLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmemory);
        setTitle("");

        titleMemory = (EditText) findViewById(R.id.titleMemory);
        editTextMemory = (EditText) findViewById(R.id.textMemory);
        editTextHashtag = (EditText) findViewById(R.id.edit_hashtag);
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tags);
        mTagContainerLayout.setGravity(Gravity.RIGHT);

        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                mTagContainerLayout.removeTag(position);
            }

            @Override
            public void onTagLongClick(int position, String text) {
                mTagContainerLayout.removeTag(position);
            }

            @Override
            public void onTagCrossClick(int position) {
            }
        });

        hashtag = (Button) findViewById(R.id.btn_hashtag);
        hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateHashtag()){
                    return;
                }
                mTagContainerLayout.addTag("#"+editTextHashtag.getText().toString());
                editTextHashtag.setText("");
            }
        });


        add_image = (ImageButton) findViewById(R.id.addimage);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.create(AddNewMemory.this)
                        .folderMode(true) // folder mode (false by default)
                        .folderTitle("Folder") // folder selection title
                        .imageTitle("Tap to select") // image selection title
                        .single() // single mode
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .start(REQUEST_CODE_PICKER); // start image picker activity with request code

            }
        });

        mStart = (ImageButton) findViewById(R.id.setDate);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePicker.Builder()
                        .id(5)
                        .build(AddNewMemory.this)
                        .show(getSupportFragmentManager(), "");
            }

        });


        setMemory = (ImageButton) findViewById(R.id.setMemory);
        setMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();

            }
        });


    }

    private void submitForm() {
        if (!validateTitleMemory()) {
            return;
        }

        if (!validateTextMemory()) {
            return;
        }
//        if(!validateHashtag()){
//            return;
//        }
        if(!validateHashtagSave()){
            return;
        }
        setMemoryToCircleView();
        backToMainActivity();
    }


    private void setMemoryToCircleView() {


        String title = titleMemory.getText().toString();
        String url = imageURL;
        String text = editTextMemory.getText().toString();
        String date = setTime;
        List<String> tagslist = mTagContainerLayout.getTags();
        String tags = getStringFromList(tagslist);


        addMemoryToDb(url, title, text, date, tags);

    }

    private String getStringFromList(List<String> tagslist) {

        String aram = "";
        for (int i = 0; i < tagslist.size(); i++) {
            aram = aram + tagslist.get(i) + ";";
        }
        return aram.substring(0, aram.length() - 1);
    }


    private void addMemoryToDb(String url, String title, String text, String date, String tags) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(AddNewMemory.this);
        Memorey memorey = new Memorey();
        memorey.setTitle(title);
        memorey.setText(text);
        memorey.setImageName(url);
        memorey.setCreatTime(date);
        memorey.setFavorite(false);
        memorey.setHashtag(tags);
        dataBaseHandler.addMemory(memorey);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            // do your logic ....
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Glide.with(this).load(images.get(0).getPath()).into(imageView);
            imageURL = images.get(0).getPath();
        }


    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {

        setTime = year + "/" + month + "/" + day;

//        mStart.setVisibility(View.INVISIBLE);
        TextView textdate = (TextView) findViewById(R.id.dateText);
        textdate.setText(setTime);


    }

    private void backToMainActivity() {
        Intent output = new Intent();
        setResult(RESULT_OK, output);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_option; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            return super.onOptionsItemSelected(item);
        }



    private boolean validateTitleMemory() {
        if (titleMemory.getText().toString().trim().isEmpty()) {
            titleMemory.setError(getString(R.string.err_msg_title));
            return false;
        } else {
        }
        return true;
    }
    private boolean validateHashtag() {
        if (editTextHashtag.getText().toString().trim().isEmpty()) {
            editTextHashtag.setError(getString(R.string.err_msg_hashtag));
            return false;
        } else {
        }
        return true;
    }

    private boolean validateHashtagSave() {
        if (mTagContainerLayout.getTags().size()==0) {
            editTextHashtag.setError(getString(R.string.err_msg_hashtag));
            return false;
        } else {
        }
        return true;
    }

    private boolean validateTextMemory() {
        if (editTextMemory.getText().toString().trim().isEmpty()) {
            editTextMemory.setError(getString(R.string.err_msg_name));
            return false;
        } else {
        }
        return true;
    }
}
