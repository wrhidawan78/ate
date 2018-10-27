package org.waw.project.ate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.waw.project.ate.model.Asset;
import org.waw.project.ate.network.ApiClient;
import org.waw.project.ate.network.ApiInterface;
import org.waw.project.ate.utils.SharedPrefManager;

import java.io.IOException;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG="";
    Button btnRegister, btnLogin;
    TextView mNIK, mEmail, mPassword, mConfirmPassword;
    private LoginActivity.UserLoginTask mAuthTask = null;
    // UI references.
    private View mProgressView;
    private View mRegisterFormView;
    SharedPrefManager sharedPrefManager;

    private String deviceId;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getSupportActionBar().setTitle("Register");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        mNIK = (TextView) findViewById(R.id.nik);
        mEmail = (TextView) findViewById(R.id.email);
        mPassword = (TextView) findViewById(R.id.password);
        mConfirmPassword = (TextView) findViewById(R.id.confirmPassword);

        progressDialog = new ProgressDialog(this);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        deviceId =  sharedPrefManager.getSPDeviceID();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private boolean isNIKValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        //TODO: Replace this with your own logic
        return (password.equals(confirmPassword));
    }

    private void attemptRegister() {
        //if (mAuthTask != null) {
        //    return;
        //}

        // Reset Form
        mNIK.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        mConfirmPassword.setError(null);

        // Store values at the time of the register
        String nik = mNIK.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        // 1. Check NIK masih aktif, Not Aktif = Cancel
        // 2. Check NIK masih sdudah teregistrasi, sudaj = Cancel

        if (!TextUtils.isEmpty(nik) && !isNIKValid(nik)) {
            mNIK.setError(getString(R.string.error_invalid_email));
            focusView = mNIK;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(confirmPassword) && !isConfirmPasswordValid(password, confirmPassword)) {
            mConfirmPassword.setError(getString(R.string.error_incorrect_confirm_password));
            focusView = mConfirmPassword;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            registrationProcess();
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           // showProgress(true);
           // mAuthTask = new LoginActivity.UserLoginTask(email, password);
           // mAuthTask.execute((Void) null);
        }
    }


    private void registrationProcess()
    {

        progressDialog.setMessage("Registering account...");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        String nik = mNIK.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        Call<ResponseBody> call = apiService.registerRequest(deviceId, nik, email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        String jsonData = response.body().string();
                        try {
                            JSONObject jsonRESULTS = new JSONObject(jsonData);
                            if (jsonRESULTS.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), "Your Account successfully registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            } else {
                                String error_message = jsonRESULTS.getString("error_message");
                                Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "error_message", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    } else {
                        progressDialog.dismiss();
                        String jsonData = response.errorBody().string();
                        try {
                            JSONObject jsonRESULTS = new JSONObject(jsonData);
                            String error_message = jsonRESULTS.getString("error_message");
                            Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
                    Log.e("debug", "Exception caught", e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "Error " + t.getMessage());
            }
        });
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
