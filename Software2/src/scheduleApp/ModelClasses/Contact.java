package scheduleApp.ModelClasses;

/**
 * A class to model a Contact in the appointments app. The contact only holds basic information: a name, email address,
 * and id used in a database.
 * @author Jason Jackson
 */
public class Contact {

    private int id;
    private String name;
    private String email;

    /**
     * Instantiates a new Contact.
     *
     * @param id    the id
     * @param name  the name
     * @param email the email
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() { return this.name; }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets contact id.
     *
     * @param contactId the contact id
     */
    public void setContactId(int contactId) {
        this.id = contactId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
