package com.javando.hrwinfo;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Debug";
    public static final String CONTENT = "content";
    private Button button;
    private TextView textView;
    private EditText editText;

    private int counter = 0;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "== onCreate: == called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        if(savedInstanceState != null && savedInstanceState.get(CONTENT) != null) {
            textView.setText(savedInstanceState.get(CONTENT).toString());
        }

        textView.setMovementMethod(new ScrollingMovementMethod());

        button.setOnClickListener(v -> {
            textView.append(
                    String.format("The button counter has been tapped %d times\n", counter++)
            );
        });
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ___started___");
        super.onStart();
    }


     @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
         Log.d(TAG, "onRestoreInstanceState: ||| called |||");
         String content = savedInstanceState.getString(CONTENT);
         if(content != null) {
             Log.d(TAG, "onRestoreInstanceState: setting content in textview!");
             textView.setText(content);
         }
         super.onRestoreInstanceState(savedInstanceState);
     }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ììì called ììì");
        super.onSaveInstanceState(outState);
        outState.putString(CONTENT, textView.getText().toString());
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ***called ***");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause:/// called ///");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ---called---");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: +++called+++");
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("Debug", "Configuration changed: "+newConfig.orientation);
        super.onConfigurationChanged(newConfig);

    }


}
