package farm.smart;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;

public class QrCodeDisplayActivity extends AppCompatActivity {

    ImageView qrImageView;
    Button downloadBtn;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_display);

        qrImageView = findViewById(R.id.qrImageView);
        downloadBtn = findViewById(R.id.downloadBtn);

        userId = getIntent().getStringExtra("USER_ID");

        if (userId != null) {
            generateQRCode(userId);
        }

        downloadBtn.setOnClickListener(v -> saveQRCode());
    }

    Bitmap qrBitmap;

    private void generateQRCode(String data) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrBitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400);
            qrImageView.setImageBitmap(qrBitmap);
        } catch (WriterException e) {
            Toast.makeText(this, "Error generating QR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQRCode() {
        if (qrBitmap == null) return;

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path, "qr_" + userId + ".png");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            Toast.makeText(this, "QR Code saved to Pictures!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
