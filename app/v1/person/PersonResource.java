package v1.person;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class PersonResource {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String postcode;
    private String link;

    public PersonResource() {
    }

    public PersonResource(String id, String firstName, String lastName, String email, String postcode, String dateOfBirth, String link) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email= email;
        this.postcode=postcode;
        this.link=link;
    }

    public PersonResource(PersonData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.dateOfBirth = data.dateOfBirth;
        this.email= data.email;
        this.postcode=data.postcode;

    }


}
