package bola.encrypted_message_app.crypted_message;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;
import bola.encrypted_message_app.R;

public class TelephoneGuide extends Activity {

    SearchView searchView;
    List<String> persons;
    List<String> phones;
    ListView contactList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.guide, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type something..");
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);

        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.DKGRAY);
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText != null) {
                searchText.setTextColor(Color.WHITE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText))
                    loadContacts("");
                else
                    loadContacts(newText.toString());
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telephone_guide);
        contactList = (ListView) findViewById(R.id.listView1);
        loadContacts("");

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    Bundle phoneData = new Bundle();
                    phoneData.putString("phoneNumber", phones.get(position));
                    phoneData.putString("personName", persons.get(position));
                    Intent intentObject = new Intent();
                    intentObject.setClass(TelephoneGuide.this, Conversation.class);
                    intentObject.putExtras(phoneData);
                    startActivity(intentObject);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void loadContacts(String match) {

        persons = new ArrayList<String>();
        phones = new ArrayList<String>();

        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + "=" + ("1") + "";
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null , selection, null, sortOrder);

        while(contacts.moveToNext()) {
            String name = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (name!=null||number!=null) {
                if (match.toLowerCase().contains(match.toLowerCase())) {
                    phones.add(number);
                    persons.add(name);
                }
            }
        }

        contacts.close();

        contactList.setAdapter(new ArrayAdapter<String>(TelephoneGuide.this, R.layout.contactlist, R.id.name, persons));
        contactList.setFastScrollEnabled(true);
        }
    }


