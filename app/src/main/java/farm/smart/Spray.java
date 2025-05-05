package farm.smart;

public class Spray {
    public String sprayingType;
    public String whySprayingDone;
    public String whenDone;
    public String userId;

    public Spray() {
        // Default constructor required for Firebase
    }

    public Spray(String sprayingType, String whySprayingDone, String whenDone, String userId) {
        this.sprayingType = sprayingType;
        this.whySprayingDone = whySprayingDone;
        this.whenDone = whenDone;
        this.userId = userId;
    }


// Optional: Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}




