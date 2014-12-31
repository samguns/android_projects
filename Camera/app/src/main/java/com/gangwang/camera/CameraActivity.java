package com.gangwang.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;


public class CameraActivity extends Activity {

    private static final String TAG = "CameraActivity";
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(mShutter, mPicture_RAW, mPicture_JPG);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e) {
            // Camera is not available
        }

        return c;
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
            catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mHolder.getSurface() == null) {
                return;
            }

            try {
                mCamera.stopPreview();
            }
            catch (Exception e) {
                // ignore
            }

            Camera.Parameters parameters = mCamera.getParameters();
            Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    //parameters.setPreviewSize(h, w);
                    mCamera.setDisplayOrientation(90);
                    break;

                case Surface.ROTATION_90:
                    mCamera.setDisplayOrientation(0);
                    //parameters.setPreviewSize(w, h);
                    break;

                case Surface.ROTATION_180:
                    mCamera.setDisplayOrientation(270);
                    //parameters.setPreviewSize(h, w);
                    break;

                case Surface.ROTATION_270:
                    //parameters.setPreviewSize(w, h);
                    mCamera.setDisplayOrientation(180);
                    break;
            }

            try {
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            }
            catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    @Override
    public void onPause() {
        mCamera.release();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCamera = getCameraInstance();
    }

    private Camera.ShutterCallback mShutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            //
        }
    };

    private Camera.PictureCallback mPicture_RAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //
        }
    };

    private Camera.PictureCallback mPicture_JPG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, null);

            /*
            File dir_image2 = new File (Environment.getExternalStorageDirectory() +
                    File.separator + "Ultimate Entity Detector");
            dir_image2.mkdir();

            File tmpFile = new File(dir_image2, "TempGhost.jpg");

            try {
                FileOutputStream fos = new FileOutputStream(tmpFile);
                fos.write(data);
                fos.close();
            }
            catch (FileNotFoundException e) {
                Log.d(TAG, "Error : " + e.getMessage());
            }
            catch (IOException e) {
                Log.d(TAG, "Error : " + e.getMessage());
            }

            String path = (Environment.getExternalStorageState() +
                        File.separator + "Ultimate EntityDetector" +
                        File.separator + "TempGhost.jpg");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bmp1 = BitmapFactory.decodeFile(path, options);
            */
        }
    };
}
