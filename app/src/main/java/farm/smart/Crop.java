package farm.smart;

public class Crop {

    // Updated crop attributes based on the new form fields
    public String leafColor;                   // Leaf color
    public String leafSizeAndShape;            // Leaf size and shape
    public String pestAndDiseaseInspection;    // Pest and disease inspection status
    public String rootHealth;                  // Root health
    public String stemCondition;               // Stem condition
    public String flowerAndFruitDevelopment;   // Flower and fruit development
    public String plantHeightAndGrowthStage;   // Plant height and growth stage
    public String userId;                      // User ID to track which user owns the crop details

    // Default constructor for Firebase
    public Crop() {}

    // Constructor to initialize the crop-specific details along with userId
    public Crop(String leafColor, String leafSizeAndShape, String pestAndDiseaseInspection, String rootHealth,
                String stemCondition, String flowerAndFruitDevelopment, String plantHeightAndGrowthStage, String userId) {
        this.leafColor = leafColor;
        this.leafSizeAndShape = leafSizeAndShape;
        this.pestAndDiseaseInspection = pestAndDiseaseInspection;
        this.rootHealth = rootHealth;
        this.stemCondition = stemCondition;
        this.flowerAndFruitDevelopment = flowerAndFruitDevelopment;
        this.plantHeightAndGrowthStage = plantHeightAndGrowthStage;
        this.userId = userId;  // Store the userId with the crop data
    }

    // Getters and setters for each attribute

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
