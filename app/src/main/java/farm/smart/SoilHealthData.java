package farm.smart;

public class SoilHealthData {

    public String soilType;
    public String soilColor;
    public String irrigationType;
    public int acres;
    public String soilTexture;
    public String soilMoisture;
    public String userId;  // New field to store the user ID

    // Default constructor for Firebase
    public SoilHealthData() {}

    public SoilHealthData(String soilType, String soilColor, String irrigationType, int acres, String soilTexture, String soilMoisture, String userId) {
        this.soilType = soilType;
        this.soilColor = soilColor;
        this.irrigationType = irrigationType;
        this.acres = acres;
        this.soilTexture = soilTexture;
        this.soilMoisture = soilMoisture;
        this.userId = userId;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
