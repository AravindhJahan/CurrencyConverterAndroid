package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuyPlanActivity extends Activity {

    RelativeLayout relativeLayout;
    Button close;
    String to;
    TextView support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buy_plan);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        relativeLayout = (RelativeLayout) findViewById(R.id.supportdesk);
        close = (Button) findViewById(R.id.close_byplan);
        support = (TextView) findViewById(R.id.text2);

        to = support.getText().toString();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPlanActivity.this.finish();
            }
        });


    }


}
