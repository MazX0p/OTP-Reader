package com.pandasdroid.otpreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class SmsListener extends BroadcastReceiver {


    private String sms = "";


    private String slot = "";

    private Context mConext;
    private SharedPreferences preferences;
    private final OkHttpClient client = new OkHttpClient();

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

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        mConext = context;
        Bundle bundle = intent.getExtras();


        Log.wtf("SetValue", "Value is set now");


        //Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {


            slot = "" + capturedSimSlot(bundle);


            for (String key : bundle.keySet()) {
                Log.wtf("myApplication", key + " is a key in the bundle");
            }

            //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                sms = "";
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];

                    SmsMessage[] msgss = new SmsMessage[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        msgss = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        SmsMessage smsMessage = msgss[0];
                        sms = "" + smsMessage.getMessageBody() + smsMessage.getDisplayMessageBody();
                    }


                    for (int i = 0; i < msgs.length; i++) {

                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        SharedPreferences prefs = context.getSharedPreferences("AppDB", MODE_PRIVATE);
                        String userid = prefs.getString("uid", "NaN");


                        int slot = bundle.getInt("slot", -1);
                        int sub = bundle.getInt("subscription", -1);


                        String simNum1 = prefs.getString("sim1", "na");
                        String simNum2 = prefs.getString("sim2", "na");
                        String num = "";

                        if (sub == -3) {

                            num = simNum1;

                        } else {
                            num = simNum2;

                        }

                        OTPSMS otp = new OTPSMS(userid, msgBody, msg_from, num, 758964);
                        whenSendPostRequest_thenCorrect(otp);

                    }
                } catch (Exception e) {
//
                }
            }
        }
    }

    public void whenSendPostRequest_thenCorrect(final OTPSMS otpObj)
            throws IOException {

        Log.wtf("SetValue", "" + otpObj.msg);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String sender = "" + otpObj.from;
                String otp = "" + otpObj.msg;

                RequestBody formBody2 = new FormBody.Builder()
                        .add("add_message","true")
                        .add("sender", sender)
                        .add("message", otp)
                        .add("user",mConext.getSharedPreferences("App",Context.MODE_PRIVATE).getString("key",""))
                        .add("msg_time",new SimpleDateFormat("y-M-d hh:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())))
                        .build();

                Request request2 = new Request.Builder()
                        .url(Api.BASE_URL + "otpreader/index.php")
                        .post(formBody2)
                        .build();

                Call call2 = client.newCall(request2);
                Response response2 = null;

                try {
                    response2 = call2.execute();
                    if (response2.body() != null){
                        Log.w("Body",response2.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();









    }

    public static String extractDigits(final String in,final int digit) {
        final Pattern p = Pattern.compile( "(\\d{" + digit + "})" );
        final Matcher m = p.matcher( in );
        if ( m.find() ) {
            return m.group( 0 );
        }
        return "";
    }






}
