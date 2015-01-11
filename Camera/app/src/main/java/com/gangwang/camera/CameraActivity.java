package com.gangwang.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends Activity {

    private static final String TAG = "CameraActivity";
    private Camera mCamera = null;
    private CameraPreview mPreview;
    private List<Points> mSamplePoints_1 = null;
    private List<Points> mSamplePoints_2 = null;
    private List<Points> mSamplePoints_3 = null;
    private List<Points> mSamplePoints_4 = null;
    private List<Points> mSamplePoints_5 = null;
    private List<Points> mSamplePoints_6 = null;
    private List<Points> mSamplePoints_7 = null;
    private List<Points> mSamplePoints_8 = null;
    private List<Points> mSamplePoints_9 = null;
    private TextView mTextView = null;
    private EditText mAddress;
    private Socket mSocket = null;
    private CircularProgressButton mButtonConnect;
    //private Bitmap mCapturedBitmap;

    private void initSamplePoints() {
        int i, j;
        int init_x, init_y;

        mSamplePoints_1 = new ArrayList<Points>();
        mSamplePoints_2 = new ArrayList<Points>();
        mSamplePoints_3 = new ArrayList<Points>();
        mSamplePoints_4 = new ArrayList<Points>();
        mSamplePoints_5 = new ArrayList<Points>();
        mSamplePoints_6 = new ArrayList<Points>();
        mSamplePoints_7 = new ArrayList<Points>();
        mSamplePoints_8 = new ArrayList<Points>();
        mSamplePoints_9 = new ArrayList<Points>();

        init_x = 98;
        init_y = 25;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_1.add(point);
            }
        }

        init_x = 98;
        init_y = 65;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_2.add(point);
            }
        }

        init_x = 98;
        init_y = 105;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_3.add(point);
            }
        }

        init_x = 58;
        init_y = 25;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_4.add(point);
            }
        }

        init_x = 58;
        init_y = 65;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_5.add(point);
            }
        }

        init_x = 58;
        init_y = 105;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_6.add(point);
            }
        }

        init_x = 18;
        init_y = 25;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_7.add(point);
            }
        }

        init_x = 18;
        init_y = 65;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_8.add(point);
            }
        }

        init_x = 18;
        init_y = 105;

        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                Points point = new Points();
                point.pos_x = init_x + i;
                point.pos_y = init_y + j;

                mSamplePoints_9.add(point);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initSamplePoints();

        mTextView = (TextView) findViewById(R.id.response);

        mCamera = getCameraInstance();
        //mCamera.setDisplayOrientation(90);

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        mCamera.setParameters(parameters);

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        preview.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent motionEvent) {
                        Log.i(TAG, "Position = x: " + motionEvent.getX() + " y: " + motionEvent.getY());
                        //Log.i(TAG, "Preview size: " + mPreview.getWidth() + " " + mPreview.getHeight());
                        //int rgbPixel = mCapturedBitmap.getPixel(Math.round(motionEvent.getX()),
                        //                                        Math.round(motionEvent.getX()));
                        //mTextView.setBackgroundColor(rgbPixel);
                        return true;
                    }
                }
        );
        /*
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPicture_JPG);
                    }
                }
        );
        */

        mAddress = (EditText)findViewById(R.id.address);
        mButtonConnect = (CircularProgressButton) findViewById(R.id.connect);
        mButtonConnect.setIndeterminateProgressMode(true);
        mButtonConnect.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((mButtonConnect.getProgress() == 0) |
                                (mButtonConnect.getProgress() == -1)) {
                            mButtonConnect.setProgress(0);
                            mButtonConnect.setProgress(50);
                            InitSocketTask connectTask = new InitSocketTask(
                                    mAddress.getText().toString(), 50007);

                            connectTask.execute();
                        }
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
            if (mCamera != null) {
                mCamera.stopPreview();
            }
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
            Camera.CameraInfo info = new Camera.CameraInfo();
            int degree = 0;
            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    degree = 270;
                    //mCamera.setDisplayOrientation(90);
                    break;

                case Surface.ROTATION_90:
                    degree = 180;
                    //mCamera.setDisplayOrientation(0);
                    break;

                case Surface.ROTATION_180:
                    degree = 0;
                    //mCamera.setDisplayOrientation(270);
                    break;

                case Surface.ROTATION_270:
                    degree = 90;
                    //mCamera.setDisplayOrientation(180);
                    break;
            }
            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degree) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degree + 360) % 360;
            }
            mCamera.setDisplayOrientation(result);

            int Width = this.getWidth();
            int Height = this.getHeight();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            //List<Camera.Size> pic_sizes = parameters.getSupportedPictureSizes();
            //Camera.Size optimalSize = getOptimalPreviewSize(sizes, Width, Height);
            parameters.setPreviewSize(960, 720);
            //parameters.setPictureSize(800, 600);

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

    private Camera.Size getOptimalPreviewSize(List <Camera.Size>sizes, int w, int h) {
        final double aspect_tolerance = 0.1;
        final double max_downsize = 1.5;

        Log.i(TAG, "getOptimalPreviewSize w:" + w + " h:" + h);

        double targetRatio = (double) (h/w);
        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = w;

        for (Camera.Size size : sizes) {
            Log.i(TAG, "getOptimalPreviewSize size.width:" + size.width + " size.height:" + size.height);
            double ratio = (double)size.width / size.height;
            double downsize = (double)size.width / h;
            if (downsize > max_downsize) {
                continue;
            }

            if (Math.abs(ratio - targetRatio) > aspect_tolerance)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                double downsize = (double)size.width / h;

                if (downsize > max_downsize) {
                    continue;
                }

                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mCamera) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        if (null != mSocket) {
            try {
                mSocket.close();
            } catch (IOException e) {
                //
            }
            mSocket = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mCamera) {
            mCamera = getCameraInstance();
            mButtonConnect.setProgress(0);
        }
    }

    private Camera.PictureCallback mPicture_JPG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            /*
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
            */

            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, op);
            getCubeColor(bm);

            camera.startPreview();
        }
    };

    private void getCubeColor(Bitmap bitmap) {
        int rgbPixel;
        int r = 0;
        int g = 0;
        int b = 0;
        String output;
        byte data[];
        OutputStream outputStream = null;

        try {
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            Log.i(TAG, "Couldn't send out data: " + e.getMessage());
        }

        Log.i(TAG, "Sample1");
        for (Points point : mSamplePoints_1) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output = "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample2");
        for (Points point : mSamplePoints_2) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample3");
        for (Points point : mSamplePoints_3) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample4");
        for (Points point : mSamplePoints_4) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample5");
        for (Points point : mSamplePoints_5) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample6");
        for (Points point : mSamplePoints_6) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample7");
        for (Points point : mSamplePoints_7) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample8");
        for (Points point : mSamplePoints_8) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;

        r = g = b = 0;
        Log.i(TAG, "Sample9");
        for (Points point : mSamplePoints_9) {
            rgbPixel = bitmap.getPixel(point.pos_x, point.pos_y);
            r += Color.red(rgbPixel);
            g += Color.green(rgbPixel);
            b += Color.blue(rgbPixel);
            Log.i(TAG, "Color of " + Integer.toString(point.pos_x) +
                    "x" + Integer.toString(point.pos_y) +
                    " pixel: " + Integer.toHexString(rgbPixel) +
                    " r:" + Color.red(rgbPixel) + " g:" + Color.green(rgbPixel) + " b:" + Color.blue(rgbPixel));
        }

        Log.i(TAG, "Red: " + r + " Green: " + g + " Blue: " + b);

        r = r / 16;
        g = g / 16;
        b = b / 16;

        Log.i(TAG, "Red/16: " + r + " Green/16: " + g + " Blue/16: " + b);
        output += "R" + r + "G" + g + "B" + b;
        data = output.getBytes();

        try {
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            //outputStream.close();
        } catch (IOException e) {
            Log.i(TAG, "write error: " + e.getMessage());
        } catch (NullPointerException e) {
            //
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CubeScan");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CameraTest", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public class InitSocketTask extends AsyncTask<Void, String, Void>
    {
        String dstAddress;
        int dstPort;
        String response = "Android";

        InitSocketTask(String addr, int port)
        {
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            try
            {
                //int bytesRead;
                mSocket = new Socket(dstAddress, dstPort);

                OutputStream outputStream = mSocket.getOutputStream();
                String myself = "Android";
                byte data[] = myself.getBytes();
                outputStream.write(data, 0, data.length);
                outputStream.flush();

                publishProgress("Connected");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(mSocket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    publishProgress(line);
                }
            }
            catch (UnknownHostException e)
            {
                response = "UnknownHostException: " + e.toString();
                publishProgress("Fail");
            }
            catch (IOException e)
            {
                response = "IOException: " + e.toString();
                publishProgress("Fail");
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            if (progress[0].equals("Connected")) {
                mButtonConnect.setProgress(100);
            }
            else if (progress[0].equals("Fail")) {
                mButtonConnect.setProgress(-1);
            }
            else {
                mCamera.takePicture(null, null, mPicture_JPG);
                mTextView.setText(progress[0]);
            }
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //super.onPostExecute(result);
            //mTextView.setText(response);
        }
    }
}
