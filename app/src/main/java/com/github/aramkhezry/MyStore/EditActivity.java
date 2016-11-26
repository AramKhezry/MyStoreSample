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
import com.github.aramkhezry.MyStore.Dao.Memory;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;


/**
 * Created by Raman on 11/13/2016.
 */

public class EditActivity extends AppCompatActivity implements DateSetListener {


    private static final int REQUEST_CODE_PICKER = 10;
    private String setTime;
    private ImageButton mStart;
    private ImageButton setMemory;
    private String imageURL;
    private TextView editeTitle;
    private TextView editTextMemory;
    private Memory memory;
    private Button hashtag;
    private EditText  hashtagEdieText;

    TagContainerLayout mTagContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edite_main);
        setTitle("");

        Intent intent = getIntent();
        memory = (Memory) intent.getSerializableExtra("memory");


        editeTitle = (TextView) findViewById(R.id.titleMemory);
        editeTitle.setText(memory.getTitle());

        ImageView editeImage = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(memory.getImageName()).into(editeImage);

        editTextMemory = (TextView) findViewById(R.id.textMemory);
        editTextMemory.setText(memory.getText());

        TextView oldDate = (TextView) findViewById(R.id.dateText);
        oldDate.setText(memory.getCreatTime());

        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tags);
        mTagContainerLayout.setGravity(Gravity.RIGHT);

        List<String> strings = new ArrayList<String>(Arrays.asList(memory.getHashtag().split(";")));
        mTagContainerLayout.setTags(strings);

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
                mTagContainerLayout.removeTag(position);

            }
        });

        hashtag = (Button) findViewById(R.id.btn_hashtag);
        hashtagEdieText = (EditText) findViewById(R.id.edit_hashtag);
        hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateHashtag()){
                    return;
                }
                mTagContainerLayout.addTag("#"+hashtagEdieText.getText().toString());
                hashtagEdieText.setText("");
              }
        });


        ImageButton add_image = (ImageButton) findViewById(R.id.addimage);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.create(EditActivity.this)
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
                        .build(EditActivity.this)
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

    private boolean validateTitleMemory() {
        if (editeTitle.getText().toString().trim().isEmpty()) {
            editeTitle.setError(getString(R.string.err_msg_title));
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

    private boolean validateHashtagSave() {
        if (mTagContainerLayout.getTags().size()==0) {
            hashtagEdieText.setError(getString(R.string.err_msg_hashtag));
            return false;
        } else {
        }
        return true;
    }
    private boolean validateHashtag() {
        if (hashtagEdieText.getText().toString().trim().isEmpty()) {
            hashtagEdieText.setError(getString(R.string.err_msg_hashtag));
            return false;
        } else {
        }
        return true;
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

    private void setMemoryToCircleView() {
        String title = editeTitle.getText().toString();
        String url = imageURL;
        String text = editTextMemory.getText().toString();
        String date = setTime;
        List<String> tagslist = mTagContainerLayout.getTags();
        String tags = getStringFromList(tagslist);

        edidMemoryToDb(url, title, text, date,tags);


    }
    private String getStringFromList(List<String> tagslist) {

        String aram="";
        for (int i = 0; i < tagslist.size(); i++) {
            aram = aram + tagslist.get(i)+";";
        }
        return aram.substring(0, aram.length() - 1);
    }


    private void edidMemoryToDb(String url, String title, String text, String date,String tags) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(EditActivity.this);

        if (url != null) {
            memory.setImageName(url);
        }
        if (date != null) {
            memory.setCreatTime(date);
        }
        memory.setTitle(title);
        memory.setText(text);
        memory.setHashtag(tags);
        dataBaseHandler.EditMemory(memory);

    }

    private void backToMainActivity() {
        Intent output = new Intent();
        output.putExtra("memory", memory);
        setResult(RESULT_OK, output);
        finish();

    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        setTime = year + "/" + month + "/" + day;


        TextView textdateold = (TextView) findViewById(R.id.dateText);
        textdateold.setVisibility(View.INVISIBLE);
        TextView textdate = (TextView) findViewById(R.id.newdateText);
        textdate.setText(setTime);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_option; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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

}



