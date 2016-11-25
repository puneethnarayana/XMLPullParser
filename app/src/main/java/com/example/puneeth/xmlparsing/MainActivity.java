package com.example.puneeth.xmlparsing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.example.puneeth.xmlparsing.Parsing;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Parsing parseXML = new Parsing();
            String readingName = "B";
            Log.v("XY",readingName);
            InputStream is = getAssets().open("test.xml");
            Parsing.RecordsforA record  = parseXML.parse(is,readingName);

            Log.v("Data : D1",record.C.C1);
            Log.v("Data : D2",record.C.C2);
            Log.v("Data : D3",record.C.C3);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}
