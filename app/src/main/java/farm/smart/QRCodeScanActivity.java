package farm.smart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start QR code scanner
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); // Specify the format
        integrator.setPrompt("Scan QR Code");
        integrator.setCameraId(0);  // Use the rear camera
        integrator.setBeepEnabled(true); // Enable beep on scan
        integrator.setBarcodeImageEnabled(true); // Enable image capture of scanned QR
        integrator.initiateScan();
    }

    // Handle the result after QR scan
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String qrData = result.getContents(); // Get QR data
            if (qrData != null) {
                processQRCodeData(qrData);
            } else {
                // QR Code was empty or scan was cancelled
                showToast("No QR code detected");
            }
        }
    }

    // Process the QR Code data and fetch farmer data
    private void processQRCodeData(String qrData) {
        // Assuming QR data contains the farmer ID or some unique identifier
        String farmerId = qrData;

        // Assuming we fetch the session data (e.g., from a SessionManager class)
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn() && sessionManager.getEmail().equals("adminvurimi@gmail.com")) {
            // Fetch farmer data related to the farmerId
            fetchFarmerData(farmerId);
        } else {
            showToast("You are not authorized to view this data.");
        }
    }

    private void fetchFarmerData(String farmerId) {
        // Example: Fetch farmer data from server or database
        // Call your API or database logic to fetch farmer data using farmerId

        // For example, make a network request or query your local database
        // After fetching the farmer data, proceed with displaying it in your app
        showToast("Farmer data fetched for ID: " + farmerId);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
