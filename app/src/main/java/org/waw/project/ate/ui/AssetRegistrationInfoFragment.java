package org.waw.project.ate.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.waw.project.ate.AssetRegTakeDataActivity;
import org.waw.project.ate.R;
import org.waw.project.ate.model.AssetRegistrationInfoPage;
import org.waw.project.ate.utils.RecyclerItemClickListener;

public class AssetRegistrationInfoFragment extends Fragment {

    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AssetRegistrationInfoPage mPage;
    private TextView mAssetID;
    private AutoCompleteTextView mProjectCode;
    private TextView mRemark;

    private Button mTakePicture;
    static final int REQUEST_PICTURE_CAPTURE = 1;
    private String pictureFilePath;
    Bitmap bitmap;

    ImageView mImgView;


    public static AssetRegistrationInfoFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AssetRegistrationInfoFragment fragment = new AssetRegistrationInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public AssetRegistrationInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AssetRegistrationInfoPage) mCallbacks.onGetPage(mKey);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAssetID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AssetRegistrationInfoPage.ASSET_ID_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
        /*
        mProjectCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(),
                        "Clicked item "
                                + parent.getItemAtPosition(position)
                        , Toast.LENGTH_SHORT).show();
            }
        });
    */
        mProjectCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AssetRegistrationInfoPage.PROJECT_CODE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AssetRegistrationInfoPage.REMARK_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page_asset_registration_info, container, false);

        ((TextView) view.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mAssetID = ((TextView) view.findViewById(R.id.assetID));
        mAssetID.setText(mPage.getData().getString(AssetRegistrationInfoPage.ASSET_ID_DATA_KEY));

        mProjectCode = ((AutoCompleteTextView) view.findViewById(R.id.projectCode));
        mProjectCode.setText(mPage.getData().getString(AssetRegistrationInfoPage.PROJECT_CODE_DATA_KEY));

        mRemark =  ((TextView) view.findViewById(R.id.remark));
        mRemark.setText(mPage.getData().getString(AssetRegistrationInfoPage.REMARK_DATA_KEY));

        mTakePicture = (Button) view.findViewById(R.id.takePicture);

        mImgView = (ImageView) view.findViewById(R.id.imgView);
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
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mAssetID != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

}
