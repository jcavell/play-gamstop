package v1.person;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "person")
public class PersonData {

    public PersonData() {
    }

    public PersonData(String firstName, String lastName, String email, String postcode, String dateOfBirth) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email= email;
        this.postcode=postcode;

    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String firstName;
    public String lastName;
    public String dateOfBirth;
    public String email;
    public String postcode;
}
