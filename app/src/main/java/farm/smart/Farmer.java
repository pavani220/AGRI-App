package farm.smart;

public class Farmer {
    public String name;
    public String mobile;
    public String email;
    public int acres;
    public String aadhar;
    public String surveyNumber;
    public String address;

    public Farmer() {}

    public Farmer(String name, String mobile, String email, int acres, String aadhar, String surveyNumber, String address) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.acres = acres;
        this.aadhar = aadhar;
        this.surveyNumber = surveyNumber;
        this.address = address;
    }
}
