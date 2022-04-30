package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Button button_capture, button_copy;
    TextView textview_data;
    Bitmap bitmap;
    private static final int REQUEST_CAMERA_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_capture = findViewById(R.id.button_capture);
        button_copy = findViewById(R.id.button_copy);
        textview_data = findViewById(R.id.text_data);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }
        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }

        });

        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scannedText = textview_data.getText().toString();
                copyToClipBoard(scannedText);

            }
        });

    }
        @Override
        protected void onActivityResult(int requestCode,int resultCode, @Nullable Intent data)
        {
            super.onActivityResult(requestCode,resultCode,data);
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                        getTextFromImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    private void getTextFromImage(Bitmap bitmap)
    {
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()){
            Toast.makeText(MainActivity.this,"Error Occurred!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < textBlockSparseArray.size(); i++)
            {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }

            textview_data.setText(stringBuilder.toString());
            button_capture.setText("Retake");
            button_copy.setVisibility(View.VISIBLE);
            String date = processText(stringBuilder);
            String location = getLocation(stringBuilder);
            Log.d("Date : ", date);
            Intent intent = new Intent(MainActivity.this, event_form.class);
            String title = getEventTitle(stringBuilder);
            String[] time = getEventTime(stringBuilder);
            intent.putExtra("sDate",date);
            intent.putExtra("Title",title);
            intent.putExtra("startTime",time[0]);
            intent.putExtra("endTime",time[1]);
            intent.putExtra("loc", location);
            startActivity(intent);
            //To add location
        }
    }

    private String[] getEventTime(StringBuilder s) {
        String str = s.toString();
        String[] list = str.split("[, \n]+");
        String[] times = new String[2];
        times[0] = "00:00AM";
        times[1] = "11:59PM";
        boolean start = true, end = false;
        for(int i = 0; i < list.length; i++)
        {
            if((start || end) && (list[i].matches("[0-9]{2}:[0-9]{2}") || list[i].matches("[0-9]:[0-9]{2}") || list[i].matches("[0-9]{2}:[0-9]{2}[A-Za-z]{2}") || list[i].matches("[0-9]:[0-9]{2}[A-Za-z]{2}"))) {
                if (start) {
                    if (list[i].length() > 5) {
                        times[0] = list[i];
                    } else {
                        if (i + 1 < list.length) {
                            if (list[i + 1].length() == 2 && (list[i + 1].toLowerCase().equals("pm") || list[i + 1].toLowerCase().equals("am")))
                                times[0] = list[i] + list[i + 1];
                            else
                                times[0] = list[i];
                        } else {
                            times[0] = list[i];
                        }
                    }
                    start = false;
                    end = true;
                }
                else if(end)
                {
                    if (list[i].length() > 5) {
                        times[1] = list[i];
                    } else {
                        if (i + 1 < list.length) {
                            if (list[i + 1].length() == 2 && (list[i + 1].toLowerCase().equals("pm") || list[i + 1].toLowerCase().equals("am")))
                                times[1] = list[i] + list[i + 1];
                            else
                                times[1] = list[i];
                        } else {
                            times[1] = list[i];
                        }
                    }
                    start = false;
                    end = false;
                }
            }
        }
        return times;
    }

    private String getEventTitle(StringBuilder s) {
        String str = s.toString();
        String[] list = str.split("[, \n]+");
        if(list.length >= 2)
            return list[0] + " " + list[1];
        else
            return list[0];
    }

    private void copyToClipBoard(String text)
    {
        ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Data", text);
        clipBoard.setPrimaryClip(clip);
        Toast.makeText(MainActivity.this,"Copied to ClipBoard!",Toast.LENGTH_SHORT).show();
    }

    private String getLocation(StringBuilder s){
        String str = s.toString();
        String[] list = str.split("[, \n]+");
        List<String> locations = new ArrayList<>();
        Collections.addAll(locations, "delhi", "detroit","mumbai", "kolkata", "bangalore", "chennai", "hyderabad", "pune", "nagpur", "nashik", "aurangabad", "new york", "london", "chicago", "washington", "north america", "usa", "uk", "india", "bengluru");
        for(String val : list){
            if(locations.contains(val.toLowerCase())){
                return val;
            }
        }
        return "";
    }

    private String processText(StringBuilder s)
    {
        String str = s.toString();
        String[] list = str.split("[, \n]+");
        String day = "",month = "",year = "";
        boolean d = true, m = true, y = true;
        HashMap<String,Integer> mp = new HashMap<>();
        String [] temp = {"january","february","march","april","may","june","july","august","september","october","november","december","jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
        for (int i = 0; i < temp.length; i++)
        {
            mp.put(temp[i],i%12 + 1);
        }
//        System.out.println(mp.toString());
        ArrayList<String> Months = new ArrayList<>(Arrays.asList(temp));

        for(String value : list)
        {
            Log.d(" ", value);
            if(d && (value.toLowerCase().matches("[0-9]{1}[a-z]{2}") || value.toLowerCase().matches("[0-9]{2}[a-z]{2}") || value.matches("[0-9]{2}")))
            {
                if(value.length() <= 2) {
                    day = value;
                    Log.d("Day - ", day);
                }
                else {
                    if(value.length() == 3)
                        day = value.substring(0, 1);
                    else
                        day = value.substring(0, 2);
                    Log.d("Day - ", day);
                }

                m = true;
                d = false;

            }
            else if(m && value.matches("[A-Za-z]*") && Months.contains(value.toLowerCase()))
            {
                month = value;
                y = true;
                m = false;
            }
            else if(y && value.matches("[0-9]{4}"))
            {
                year = value;
                y = false;
            }
        }
        String mm = (mp.get(month.toLowerCase()) >= 10 ? String.valueOf(mp.get(month.toLowerCase())) :"0" + mp.get(month.toLowerCase()));
        return (mm + "/" + (day.length() == 2  ? day : "0" + day) + "/" + year);

    }
}
