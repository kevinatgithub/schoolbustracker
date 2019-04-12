package app.kevin.dev.schoolservicetrackerparent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import app.kevin.dev.schoolservicetrackerparent.lib.ConfirmDialogHelper;
import app.kevin.dev.schoolservicetrackerparent.lib.Session;
import app.kevin.dev.schoolservicetrackerparent.models.Parent;

import static com.google.android.gms.vision.CameraSource.CAMERA_FACING_BACK;

public class ScanQRActivity extends AppCompatActivity {

    SurfaceView cameraPreview;
    TextView txtPreview;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        cameraPreview = findViewById(R.id.cameraPreview);
        txtPreview = findViewById(R.id.txtResult);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setFacing(CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScanQRActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, RequestCameraPermissionID
                    );
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0){

                    txtPreview.post(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.release();
                            assignQRtoSession(qrcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });

        // TODO: 04/04/2019 Remove in production
        findViewById(R.id.btnDummy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialogHelper.prompt(ScanQRActivity.this, "QR Value", "Enter Dummy QR Value", new ConfirmDialogHelper.OnPromptCallback() {
                    @Override
                    public void execute(String value) {
                        assignQRtoSession(value);
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    protected void assignQRtoSession(String qrcode){
        String json = Session.get(this,"parent",null);
        Parent parent = gson.fromJson(json,Parent.class);
        if(parent.getVehicles() == null){
            parent.setVehicles(new ArrayList<String>());
        }
        parent.getVehicles().add(qrcode);
        Session.set(this,"parent",gson.toJson(parent));
        finish();
    }
}
