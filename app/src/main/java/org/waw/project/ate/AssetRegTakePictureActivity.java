package org.waw.project.ate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.waw.project.ate.helper.UploadObject;
import org.waw.project.ate.network.ApiClient;
import org.waw.project.ate.network.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetRegTakePictureActivity extends AppCompatActivity {

    private static final String API_TOKEN = "9032f97483cad75816390817e1b5b5b5";
    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private ImageView image;
    private String pictureFilePath;
    private String deviceIdentifier;
    private String deviceId;
    TextView mAssetNo;
    Intent intent ;
    ApiInterface apiService;

    Bitmap bitmap;
    ProgressDialog progressDialog;
    Context mContext;
    Spinner spinner;

    private static final String[] paths = {"Tools", "Laptop", "Kendaraan"};

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_reg_take_picture);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //btntfab.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        /*
        LinearLayout galery = findViewById(R.id.gallery);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < 6; i++) {

            View view = inflater.inflate(R.layout.item_picture, galery, false);
            TextView textview = view.findViewById(R.id.galleryText);
            textview.setText("Picture " + i);

            ImageView imageView = view.findViewById(R.id.gallery);
            imageView.setImageResource(R.drawable.ic_camera_alt_white_24dp);
            galery.addView(view);


        }
        */

        mAssetNo = findViewById(R.id.asset_no);
        image = findViewById(R.id.picture);
        findViewById(R.id.btn_upload).setOnClickListener(saveCloud);

        mContext = this;
        apiService = ApiClient.getClient().create(ApiInterface.class);
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
      //  spinner.setOnItemSelectedListener(this);
        setupActionBar();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.asset_registration_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.takePicture:
                sendTakePictureIntent();
                return true;
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), AssetRegActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void sendTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            //startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "org.waw.project.ate",
                        pictureFile);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }

        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {


            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists()) {
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), Uri.fromFile(imgFile));
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    //save captured picture on cloud storage
    private View.OnClickListener saveCloud = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addToCloudStorage();
        }
    };

    private void addToCloudStorage() {

        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = picUri.getLastPathSegment();

        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", f.getName(), mFile);
        RequestBody assetID = RequestBody.create(MediaType.parse("text/plain"), mAssetNo.getText().toString());
        RequestBody deviceID = RequestBody.create(MediaType.parse("text/plain"), deviceId);
        RequestBody assetTypeName = RequestBody.create(MediaType.parse("text/plain"), spinner.getSelectedItem().toString());
        RequestBody fileName = RequestBody.create(MediaType.parse("text/plain"), f.getName());

        Log.d(TAG, "Device ID " + deviceId);
        Call<UploadObject> fileUpload = apiService.uploadSingleFile(assetTypeName, assetID, deviceID, fileToUpload, fileName, null, null);
        fileUpload.enqueue(new Callback<UploadObject>() {
            @Override
            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<UploadObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "Error " + t.getMessage());
            }

        });


    }
}
