package farm.smart;

public class Farmer {

    public String name;
    public String mobile;
    public String email;
    public int acres;
    public String aadhar;
    public String surveyNumber;
    public String address;
    public String userId;  // New field to store the user ID

    // Default constructor for Firebase
    public Farmer() {}

    public Farmer(String name, String mobile, String email, int acres, String aadhar, String surveyNumber, String address, String userId) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.acres = acres;
        this.aadhar = aadhar;
        this.surveyNumber = surveyNumber;
        this.address = address;
        this.userId = userId;
    }
//to get the getter and setter for the data to get into database connected to firebase.
    // Getters and setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


