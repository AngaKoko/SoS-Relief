package com.example.anga_.canopener.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Utils {
    /**
     * Read Json File and return output
     * @return
     * @throws IOException
     */
    public static String readJsonFile(final Context context, int resId) throws IOException {

        InputStream inputStream = context.getResources().openRawResource(resId);
        String jsonString = "";
        if(inputStream == null)return null;

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }
        jsonString = writer.toString();

        return  jsonString;
    }
}
