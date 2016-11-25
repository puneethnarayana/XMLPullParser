package com.example.puneeth.xmlparsing;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Puneeth on 25-11-2016.
 */

public class Parsing {

    private static final String ns = null;

    // We don't use namespaces

    public RecordsforA parse(InputStream in, String ecgRecordingTag) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.v("Parse", "In Parse");
            return readFeed(parser, ecgRecordingTag);
        } finally {
            in.close();
        }
    }

    private RecordsforA readFeed(XmlPullParser parser, String recordingTag) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "A");
        String nameRoot = parser.getName();
        String attrValRoot = parser.getAttributeValue(0);
        String attrNameRoot = parser.getAttributeName(0);
        Log.v("Parse", "Name--" + nameRoot + "--aN--" + attrNameRoot + "--aV--" + attrValRoot);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
//            if (name.equals(recordingTag)) {
//                Log.v("Parse", "In the loop for : " + name);
//                RecordsForD dRecord = readRecordsForD(parser, recordingTag);
//                return dRecord;
//            } else {
//                skip(parser);
//            }

            if (name.equals(recordingTag)) {
                Log.v("Parse","In the loop for : "+name);
                RecordsforA a = readRecordsForA(parser, recordingTag);
                return a;
            }
            else {
                skip(parser);
            }

        }
        return null;
    }


    public static class RecordsForD {
        public final String D1;
        public final String D2;
        public final String D3;
        public final String D4;

        private RecordsForD(String D1, String D2, String D3, String D4) {
            this.D1 = D1;
            this.D2 = D2;
            this.D3 = D3;
            this.D4 = D4;
        }
    }

    public static class RecordsforC4 {
        public final RecordsForD D;

        private RecordsforC4(RecordsForD D) {
            this.D = D;
        }
    }

    public static class RecordsForC {
        public final String C1;
        public final String C2;
        public final String C3;
        public final RecordsforC4 C4;

        private RecordsForC(String C1, String C2, String C3, RecordsforC4 C4) {
            this.C1 = C1;
            this.C2 = C2;
            this.C3 = C3;
            this.C4 = C4;
        }
    }

        public static class RecordsforA {
            public final RecordsForC C;

            private RecordsforA(RecordsForC C) {
                this.C = C;
            }
        }

        private RecordsForD readRecordsForD(XmlPullParser parser, String startTag)
                throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, startTag);
            String D1 = null;
            String D2 = null;
            String D3 = null;
            String D4 = null;
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("D1")) {
                    D1 = readTextFromNode(parser, "D1");
                } else if (name.equals("D2")) {
                    D2 = readTextFromNode(parser, "D2");
                } else if (name.equals("D3")) {
                    D3 = readTextFromNode(parser, "D3");
                } else if (name.equals("D4")) {
                    D4 = readTextFromNode(parser, "D4");
                } else {
                    skip(parser);
                }
            }
            return new RecordsForD(D1, D2, D3, D4);
        }

    private RecordsForC readRecordsForC(XmlPullParser parser,String startTag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, startTag);
        String C1 = null;
        String C2 = null;
        String C3= null;
        RecordsforC4 C4 = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("C1")) {
                C1 = readTextFromNode(parser,"C1");
            } else if (name.equals("C2")) {
                C2 = readTextFromNode(parser,"C2");
            } else if (name.equals("C3")) {
                C3 = readTextFromNode(parser,"C3");
            } else if (name.equals("C4")) {

                Log.v("Parse","In the loop for : "+name);

                C4 = new RecordsforC4(readRecordsForD(parser, "C4"));
            } else {
                skip(parser);
            }
        }
        return new RecordsForC(C1,C2,C3,C4);
    }

    private RecordsforA readRecordsForA(XmlPullParser parser,String startTag) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, startTag);
        RecordsForC C = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.v("YoYO",name);
            if (name.equals("C")) {
                C = readRecordsForC(parser, "C");
            }
            else {
                skip(parser);
            }
        }
        return new RecordsforA(C);
    }

        private String readTextFromNode(XmlPullParser parser, String tagName)
                throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, tagName);
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tagName);
            return title;
        }

        // For the tags extracts their text values.
        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }


        // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
        // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
        // finds the matching END_TAG (as indicated by the value of "depth" being 0).
        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {

                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
}
