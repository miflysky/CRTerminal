package com.tieto.crterminal.model.network;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class Utility 
{
    public static int convertByteArrayToInt(byte[] dataBytes) 
    {
        int result = ByteBuffer.wrap(dataBytes).getInt();
        return result;
    }

    public static byte[] convertIntToByteArray(int intData) 
    {
        byte[] bytes = ByteBuffer.allocate(4).putInt(intData).array();

        for (byte b : bytes) {
            Log.i("Byte", String.format("0x%x ", b));
        }
        return bytes;       
    }

    public static String getDataByAssertName(Context context, String fileName) 
    {
        BufferedReader br = null;
        try {               
            InputStream json=context.getAssets().open(fileName);
            br = new BufferedReader(new BufferedReader(new InputStreamReader(json)));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) 
            {
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
                line = br.readLine();
            }
            String dataString = sb.toString();
            return dataString;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br = null;
        }
        return null;
    }
}
