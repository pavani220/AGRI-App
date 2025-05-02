package farm.smart;

public class AgriData {
    public String inputName;
    public String companyName;
    public String inputType; // Organic / Semi Organic / Non Organic
    public String dosage;
    public int usagePerMonth;
    public String beforeUsage;
    public String afterUsage;
    public String userId;

    // Default constructor for Firebase
    public AgriData() {}

    public AgriData(String inputName, String companyName, String inputType, String dosage, int usagePerMonth, String beforeUsage, String afterUsage, String userId) {
        this.inputName = inputName;
        this.companyName = companyName;
        this.inputType = inputType;
        this.dosage = dosage;
        this.usagePerMonth = usagePerMonth;
        this.beforeUsage = beforeUsage;
        this.afterUsage = afterUsage;
        this.userId = userId;
    }

    // Getters and setters (only showing userId as an example)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


