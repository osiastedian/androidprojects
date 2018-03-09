package apps.osias.contactsonly.models;

/**
 * Created by osias on 3/6/2018.
 */

public class Contact {
    private String name;
    private String phoneNumber;

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
