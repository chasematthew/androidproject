package com.chaseswanstrom.sentiment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cwatsonbaseball on 5/2/16.
 */

public class TopMashersActivity extends AppCompatActivity implements Serializable {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topmashers);

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(4, 0xffffffff);

        Intent intent = getIntent();
        String query1 = intent.getStringExtra("query1");
        String query2 = intent.getStringExtra("query2");
        String dnPos1 = intent.getExtras().getString("dnPos1");
        String dnPos2 = intent.getExtras().getString("dnPos2");
        String dnNeg1 = intent.getExtras().getString("dnNeg1");
        String dnNeg2 = intent.getExtras().getString("dnNeg2");

        ArrayList<String> dnArray = new ArrayList<String>(Arrays.asList(dnPos1, dnPos2, dnNeg1, dnNeg2));
        for (String s : dnArray) {
            if (s == null) {
                System.out.println("Found a null string!!!!!");
                dnArray.set(dnArray.indexOf(s), "No results");
            }
        }

        //Positive tweet 1
        final TextView userName1 = (TextView) findViewById(R.id.tvUsernameT1Positive);
        userName1.setText((intent.getExtras().getString("unPos1")));
        TextView displayName1 = (TextView) findViewById(R.id.tvDisplaynameT1Positive);
        displayName1.setText(dnArray.get(0) + ": " + "\"" + query1 + "\"");
        TextView tweet1 = (TextView) findViewById(R.id.tvTweetT1Positive);
        tweet1.setText((intent.getExtras().getString("tweetPos1")));
        tweet1.setMaxLines(4);


        //Positive tweet 2
        final TextView userName2 = (TextView) findViewById(R.id.tvUsernameT2Positive);
        userName2.setText(intent.getExtras().getString("unPos2"));
        TextView displayName2 = (TextView) findViewById(R.id.tvDisplaynameT2Positive);
        displayName2.setText(dnArray.get(1) + ": " + "\"" + query2 + "\"");
        TextView tweet2 = (TextView) findViewById(R.id.tvTweetT2Positive);
        tweet2.setText((intent.getExtras().getString("tweetPos2")));
        tweet2.setMaxLines(4);


        //Negative tweet 1
        final TextView userName3 = (TextView) findViewById(R.id.tvUsernameT1Negative);
        userName3.setText((intent.getExtras().getString("unNeg1")));
        TextView displayName3 = (TextView) findViewById(R.id.tvDisplaynameT1Negative);
        displayName3.setText(dnArray.get(2) + ": " + "\"" + query1 + "\"");
        TextView tweet3 = (TextView) findViewById(R.id.tvTweetT1Negative);
        tweet3.setText((intent.getExtras().getString("tweetNeg1")));
        tweet3.setMaxLines(4);


        //Negative tweet 2
        final TextView userName4 = (TextView) findViewById(R.id.tvUsernameT2Negative);
        userName4.setText((intent.getExtras().getString("unNeg2")));
        TextView displayName4 = (TextView) findViewById(R.id.tvDisplaynameT2Negative);
        displayName4.setText(dnArray.get(3) + ": " + "\"" + query2 + "\"");
        TextView tweet4 = (TextView) findViewById(R.id.tvTweetT2Negative);
        tweet4.setText((intent.getExtras().getString("tweetNeg2")));
        tweet4.setMaxLines(4);

        //Positive Image Button Setups
        ImageButton ib1 = (ImageButton) findViewById(R.id.ibT1Positive);
        Bitmap bitmap1 = intent.getParcelableExtra("imgPos1");
        BitmapDrawable img1 = new BitmapDrawable(getResources(), bitmap1);
        ib1.setBackground(img1);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/" + userName1.getText()));
                startActivity(twitterBrowserIntent);
            }
        });
        ImageButton ib2 = (ImageButton) findViewById(R.id.ibT2Positive);
        Bitmap bitmap2 = intent.getParcelableExtra("imgPos2");
        BitmapDrawable img2 = new BitmapDrawable(getResources(), bitmap2);
        ib2.setBackground(img2);
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/" + userName2.getText()));
                startActivity(twitterBrowserIntent);
            }
        });

        //Negative Image Button Setups
        ImageButton ib3 = (ImageButton) findViewById(R.id.ibT1Negative);
        Bitmap bitmap3 = intent.getParcelableExtra("imgNeg1");
        BitmapDrawable img3 = new BitmapDrawable(getResources(), bitmap3);
        ib3.setBackground(img3);
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/" + userName3.getText()));
                startActivity(twitterBrowserIntent);
            }
        });
        ImageButton ib4 = (ImageButton) findViewById(R.id.ibT2Negative);
        Bitmap bitmap4 = intent.getParcelableExtra("imgNeg2");
        BitmapDrawable img4 = new BitmapDrawable(getResources(), bitmap4);
        ib4.setBackground(img4);
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent twitterBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/" + userName4.getText()));
                startActivity(twitterBrowserIntent);
            }
        });

        //Set the background for each tweet
        View bgT1Positive = findViewById(R.id.bgT1Positive);
        bgT1Positive.setBackground(gd);
        View bgT2Positive = findViewById(R.id.bgT2Positive);
        bgT2Positive.setBackground(gd);

        View bgT1Negative = findViewById(R.id.bgT1Negative);
        bgT1Negative.setBackground(gd);
        View bgT2Negative = findViewById(R.id.bgT2Negative);
        bgT2Negative.setBackground(gd);

        if (getIntent().getExtras().getString("isSingle") != null) {
            if (getIntent().getExtras().getString("isSingle").equalsIgnoreCase("yes")) {
                displayName2.setText("");
                displayName4.setText("");
                userName2.setText("Not available in Single Mode");
                userName4.setText("Not available in Single Mode");

            }
        }

    }

}
