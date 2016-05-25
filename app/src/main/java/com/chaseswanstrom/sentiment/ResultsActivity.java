package com.chaseswanstrom.sentiment;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();

        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(4, 0xffffffff);

        BattleActivity b = new BattleActivity();
        TextView query1 = (TextView) findViewById(R.id.textViewQuery1term);
        query1.setBackground(gd);
        query1.setText(b.getQueryWord1().toUpperCase());
        TextView results1 = (TextView) findViewById(R.id.textView1results);
        results1.setText("Positive Tweets: " + intent.getStringExtra("positive").toString() + "\n"
                + "Neutral Tweets: " + intent.getStringExtra("neutral").toString() + "\n" + "Negative Tweets: " + intent.getStringExtra("negative").toString());

        TextView query2 = (TextView) findViewById(R.id.textViewQuery2term);
        query2.setBackground(gd);
        query2.setText(b.getQueryWord2().toUpperCase());
        TextView results2 = (TextView) findViewById(R.id.textView2results);
        results2.setText("Positive Tweets: " + intent.getStringExtra("positive2").toString() + "\n"
                + "Neutral Tweets: " + intent.getStringExtra("neutral2").toString() + "\n" + "Negative Tweets: " + intent.getStringExtra("negative2").toString());


        Button topMashers = (Button) findViewById(R.id.buttonTM);
        assert topMashers != null;
        topMashers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTopIntent();
            }
        });

    }

    private void startTopIntent() {
        TwitterManager t = new TwitterManager();
        Intent myIntent = new Intent(ResultsActivity.this, TopMashersActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable("objects", t.getTweetArray());
        myIntent.putExtra("tweetArray", t.getTweetArray());
        myIntent.putExtra("unPos1", getIntent().getExtras().getSerializable("unPos1"));
        myIntent.putExtra("dnPos1", getIntent().getExtras().getSerializable("dnPos1"));
        myIntent.putExtra("imgPos1", getIntent().getParcelableExtra("imgPos1"));
        myIntent.putExtra("unPos2", getIntent().getExtras().getSerializable("unPos2"));
        myIntent.putExtra("dnPos2", getIntent().getExtras().getSerializable("dnPos2"));
        myIntent.putExtra("imgPos2", getIntent().getParcelableExtra("imgPos2"));
        myIntent.putExtra("tweetPos1", getIntent().getExtras().getSerializable("tweetPos1"));
        myIntent.putExtra("tweetPos2", getIntent().getExtras().getSerializable("tweetPos2"));
        myIntent.putExtra("unNeg1", getIntent().getExtras().getSerializable("unNeg1"));
        myIntent.putExtra("dnNeg1", getIntent().getExtras().getSerializable("dnNeg1"));
        myIntent.putExtra("imgNeg1", getIntent().getParcelableExtra("imgNeg1"));
        myIntent.putExtra("unNeg2", getIntent().getExtras().getSerializable("unNeg2"));
        myIntent.putExtra("dnNeg2", getIntent().getExtras().getSerializable("dnNeg2"));
        myIntent.putExtra("imgNeg2", getIntent().getParcelableExtra("imgNeg2"));
        myIntent.putExtra("tweetNeg1", getIntent().getExtras().getSerializable("tweetNeg1"));
        myIntent.putExtra("tweetNeg2", getIntent().getExtras().getSerializable("tweetNeg2"));
        myIntent.putExtra("query1", getIntent().getExtras().getString("query1"));
        myIntent.putExtra("query2", getIntent().getExtras().getString("query2"));
        ResultsActivity.this.startActivity(myIntent);
    }
}
