package apps.osias.contactsonly;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import apps.osias.contactsonly.models.Contact;

/**
 * Created by osias on 3/6/2018.
 */

public class ContactArrayAdapter extends ArrayAdapter<Contact>{
    ArrayList<Contact> contactArrayList;
    int selectedItem = -1;
    int count = 0;
    int idGen = 0;
    ArrayList<DataSetObserver> dataSetObservers;
    public ContactArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        contactArrayList = new ArrayList<Contact>();
        dataSetObservers = new ArrayList<>();
    }

    public ContactArrayAdapter(@NonNull Context context, int resource, @NonNull Contact[] contacts) {
        super(context, resource, contacts);
        contactArrayList = new ArrayList<Contact>();
        for(Contact contact: contacts) {
            contactArrayList.add(contact);
            count++;
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return i == selectedItem;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        if(dataSetObservers != null)
        dataSetObservers.add(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        if(dataSetObservers != null)
        dataSetObservers.remove(dataSetObserver);
    }
}
