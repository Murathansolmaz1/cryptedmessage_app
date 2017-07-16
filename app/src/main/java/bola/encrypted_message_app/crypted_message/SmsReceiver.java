package bola.encrypted_message_app.crypted_message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import static android.telephony.SmsMessage.createFromPdu;

public class SmsReceiver extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0;i<smsm.length;i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_str += smsm[i].getMessageBody().toString();
            }

            try{
                Conversation.updateAdapter(sms_str,"1",smsm[0].getOriginatingAddress());
            } catch (Exception e) {

            }
        }

    }
}
