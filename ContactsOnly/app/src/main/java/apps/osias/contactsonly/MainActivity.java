package apps.osias.contactsonly;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import apps.osias.contactsonly.models.Contact;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final int REQUEST_CODE_CALL_PHONE = 1000;
    EditText numberEditText;
    ListView contactList;

    ContactListAdapter contactArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberEditText = findViewById(R.id.numberEditText);
        contactList = findViewById(R.id.contactList);
        contactArrayAdapter = new ContactListAdapter(this);
        //contactListAdapter.add("Test me");
        contactList.setAdapter(contactArrayAdapter);
        contactList.setOnItemClickListener(this);
        CheckPermission();
        LoadContacts();

    }

    private void CheckPermission() {
        String permissionList[] = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS
        };
        for(String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(this, permissionList, 0);
            }
        }
    }

    public void CallButton_OnClick(View view){
        String number = numberEditText.getText().toString();

        if(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)) {
            Intent callIntext = new Intent(Intent.ACTION_CALL);
            callIntext.setData(Uri.parse("tel:" + number));
            startActivity(callIntext);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_CALL_PHONE);
        }
    }

    public void CallContact(Contact contact){
        numberEditText.setText(contact.getPhoneNumber());
        CallButton_OnClick(null);
    }


    private void LoadContacts(){
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);
        String name = "", phonenumber = "";
        while(cursor.moveToNext())
        {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contact contact = new Contact();
            contact.setName(name);
            contact.setPhoneNumber(phonenumber);
            contactArrayAdapter.AddContact(contact);
        }
        cursor.close();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object object = contactArrayAdapter.getItem(i);
        if(object == null)
            return ;
        if(object instanceof Contact){
            Contact contact = (Contact) object;
            CallContact(contact);
        }

    }

    class ContactViewHolder {

        View parent = null;
        TextView nameTextView;
        TextView phoneTextView;

        public ContactViewHolder(View parent) {
            this.parent = parent;
            nameTextView = parent.findViewById(R.id.nameTextView);
            phoneTextView = parent.findViewById(R.id.phoneTextView);
        }

        public void LoadContact(Contact contact){
            nameTextView.setText(contact.getName());
            phoneTextView.setText(contact.getPhoneNumber());
        }
    }

    class ContactListAdapter extends BaseAdapter{
        ArrayList<Contact> contactArrayList;
        Context context;

        public ContactListAdapter(Context context) {
            this.context = context;
            contactArrayList = new ArrayList<>();
        }

        public void AddContact(Contact contact)
        {
            contactArrayList.add(contact);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return contactArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return contactArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view  = inflater.inflate(R.layout.contact, null);
            ContactViewHolder viewHolder = new ContactViewHolder(view);
            viewHolder.LoadContact((Contact)this.getItem(i));
            return view;
        }
    }
}
