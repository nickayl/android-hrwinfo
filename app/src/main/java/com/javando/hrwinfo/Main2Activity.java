package com.javando.hrwinfo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.javando.android.hrwinfo.core.api.AndroidHrwInfo;
import org.javando.android.hrwinfo.core.api.CPU;
import org.javando.android.hrwinfo.core.api.Device;

public class Main2Activity extends AppCompatActivity {

    public static final String TAG = "Debug";

//    @FunctionalInterface
//    interface Response<T> {
//        void response(T response);
//    }

//    @FunctionalInterface
//    interface Error {
//        void handleError(HTTPCode httpCode, String errorDescription);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: === called ===");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        Device device = AndroidHrwInfo.getInstance().device(this);
        Log.d(TAG, "onCreate: "+device.toString());


        AndroidHrwInfo.getInstance()
                .cpu()
                .setOnFrequencyChangeListener(cores -> {
                    String str = "[";
                    for(CPU.Core core: cores) {
                        str += String.format("#%d %d ",core.getCoreNumber(), core.getCurFrequency());
                    }
                    str += "] Mhz\n";
                    Log.d(TAG, str);
                })
                .startCpuFrequencyMonitor(1000);

        Log.d(TAG, "onCreate: ciaooooo");
//           AndroidHw
//                   .cpu()
//                   .getNumCores();
//
//           AndroidHw
//                   .cpu()
//                   .monitorClockFrequency(frequency -> {
//
//                    });

//        RestWorld
//                .get("http://www.yourdomain.com/rest/v1")
//                .json()
//                .error((Error) (httpCode, errorDescription) -> {
//
//                })
//                .done((Response<String>) response -> {
//
//                });

//
//        // or
//


//        RestWorld
//                .get("http://www.yourdomain/rest/v1/user")
//                .bindJson(AndroidHrwInfo.class)
//                .go();


//        RestWorld
//                .post("http://www.yourdomain/rest/v1/user")
//                .withJson(json)
//                .go();
//
//        RestWorld
//                .post("http://www.yourdomain/rest/v1/user")
//                .withXml(xml)
//                .go();
//
//        RestWorld
//                .get("http://www.yourdomain.com/rest/v1")
//                .xml()
//                .go();

//        RestWorld
//                .get("http://www.yourdomain.com/rest/v1")
//                .binary("/path/to/save/file.ext")
//                .go();

        //FutureTask<String> f = new FutureTask<>(() -> "string");



        /*
         XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream("book.xml");
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag(); // Advance to "book" element
        streamReader.nextTag(); // Advance to "person" element

        int persons = 0;
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement()) {
                switch (streamReader.getLocalName()) {
                    case "first": {
                        SystemInfo.out.print("First Name : ");
                        SystemInfo.out.println(streamReader.getElementText());
                        break;
                    }
                    case "last": {
                        SystemInfo.out.print("Last Name : ");
                        SystemInfo.out.println(streamReader.getElementText());
                        break;
                    }
                    case "age": {
                        SystemInfo.out.print("Age : ");
                        SystemInfo.out.println(streamReader.getElementText());
                        break;
                    }
                    case "person" : {
                        persons ++;
                    }
                }
            }
            streamReader.next();
        }
        SystemInfo.out.print(persons);
        SystemInfo.out.println(" persons");

         */

    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ___started___");
        super.onStart();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ||| called |||");
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ììì called ììì");
        super.onSaveInstanceState(outState);

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
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: ");
      //  setContentView(R.layout.calculator);

    }
}
