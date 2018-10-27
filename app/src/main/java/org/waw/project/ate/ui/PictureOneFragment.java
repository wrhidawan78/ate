package org.waw.project.ate.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.waw.project.ate.R;
import org.waw.project.ate.model.AssetRegistrationInfoPage;
import org.waw.project.ate.model.PictureOnePage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureOneFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_KEY = "key";
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private PictureOnePage mPage;
    private ImageView mImgView;
    private Button mButtonTakePicture;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFilePath;
    Bitmap bitmap;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    public static PictureOneFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        PictureOneFragment fragment = new PictureOneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (PictureOnePage) mCallbacks.onGetPage(mKey);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //btntfab.setEnabled(false);
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_page_picture_one, container, false);

        ((TextView) view.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mImgView = view.findViewById(R.id.imgView);
        mButtonTakePicture = view.findViewById(R.id.btn_take_picture);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonTakePicture.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
       sendTakePictureIntent();


    }


    private void sendTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            //startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(),
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  Context applicationContext = AssetRegTakeDataActivity.contextOfApplication;
        //applicationContext.getContentResolver();

        //&& resultCode == Activity.RESULT_OK
        // if (requestCode == REQUEST_PICTURE_CAPTURE )

       // applicationContext.
        File imgFile = new  File(pictureFilePath);
        if(imgFile.exists()) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(imgFile));
                mImgView.setImageBitmap(bitmap);

                //mPage.getData().putParcelable(PictureOnePage.PICTURE_NAME + "_" + mPage.getTitle().trim(), bitmap);
                //mPage.notifyDataChanged();

                mPage.getData().putString(PictureOnePage.PICTURE_NAME + "_" + mPage.getTitle().trim(), pictureFilePath);
                mPage.notifyDataChanged();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //}


    }

}
