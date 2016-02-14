package com.example.adrian.projekt2016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final EditText edittext = (EditText) findViewById(R.id.edittext_name);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this).edit().putString("userName", edittext.getText().toString()).commit();

                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final Animation mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        final Animation mAnimation2 = AnimationUtils.loadAnimation(this, R.anim.descale);

        fab.setVisibility(View.INVISIBLE);

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edittext.getText().toString().equals("")) {
                    if (fab.getVisibility() == View.VISIBLE) {} else {
                        fab.setVisibility(View.VISIBLE); //It has to be invisible before here
                        fab.startAnimation(mAnimation);
                    }
                } else {
                    fab.setVisibility(View.INVISIBLE);
                    fab.startAnimation(mAnimation2);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


    }

}
