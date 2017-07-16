package bola.encrypted_message_app.crypted_message;

import bola.encrypted_message_app.R;
import bola.encrypted_message_app.crypt.Crypt;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import java.util.ArrayList;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;


public class MessageList extends AppCompatActivity{

    SimpleCursorAdapter adapter;
    ListView messageList;
    List<String> persons;
    List<String> phones;
    Crypt cryptObject = new Crypt();
    ContentResolver cr;
    Cursor c;
    Uri inboxURI;
    String[] columns;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton refresh, contact, update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagelist);

        messageList = (ListView) findViewById(R.id.listView1);

        messageList.setEmptyView(findViewById(R.id.empty));

        inboxURI = Uri.parse("content://sms/");

        columns = new String[] {"_id","address","body","read"};

        cr = getContentResolver();

        adapterLoad();

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.fam_base);
        refresh = (FloatingActionButton) findViewById(R.id.fam1);
        contact = (FloatingActionButton) findViewById(R.id.fam2);
        update = (FloatingActionButton) findViewById(R.id.fam3);

        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapterLoad();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent j = new Intent();
                j.setClass(MessageList.this, TelephoneGuide.class);
                startActivity(j);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent();
                k.setClass(MessageList.this, UpdateUserInfo.class);
                startActivity(k);
            }
        });


        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    Bundle phoneData = new Bundle();
                    phoneData.putString("phoneNumber", phones.get(position) + "");
                    phoneData.putString("personName", persons.get(position) + "");
                    Intent intentObject = new Intent();
                    intentObject.setClass(MessageList.this, Conversation.class);
                    intentObject.putExtras(phoneData);
                    startActivity(intentObject);
                } catch (Exception ignored) {}
            }
        });
    }

    private void adapterLoad() {

        persons = new ArrayList<String>();
        phones = new ArrayList<String>();
        messageList.setAdapter(null);

        c = cr.query(inboxURI, columns, null, null, null, null);

        while (c.moveToNext()) {
            try {
                String body = c.getString(c.getColumnIndexOrThrow("body"));
                String message = cryptObject.decrypt(body);

                if (message.length() != 0) {
                    String senderPhone = c.getString(c.getColumnIndexOrThrow("address"));
                    String senderName = getContactName(senderPhone);

                    if (senderName.trim().length()==0&&!persons.contains(senderPhone))
                    {
                        persons.add(senderPhone);
                        phones.add(senderPhone);
                    }

                    if (senderName.trim().length()==0&&!persons.contains(senderName))
                    {
                        persons.add(senderName);
                        phones.add(senderPhone);
                    }
                }
            } catch (Exception ignored) { }
        }

        messageList.setAdapter(new ArrayAdapter<String>(MessageList.this, R.layout.list, R.id.name, persons));
        messageList.setFastScrollEnabled(true);
    }

    private String getContactName(final String phoneNumber) {
        Uri uri;
        String[] projection;
        Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
        projection = new String[] { android.provider.Contacts.People.NAME };
        try {
            Class<?> c=Class.forName("android.provider.CpntactsContacts$PhoneLookup");
            mBaseUri = (Uri) c.getField("CONTENT_FILTER_URL").get(mBaseUri);
            projection = new String[] {"display_name"};
        } catch (Exception ignored) { }
        uri = Uri.withAppendedPath(mBaseUri, Uri.encode(phoneNumber));
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);

        String contactName = "";

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(0);
        }

        cursor.close();
        cursor = null;

        return contactName;
    }

    @Override
    protected void onResume() {
        adapterLoad();
        super.onResume();
    }
}
