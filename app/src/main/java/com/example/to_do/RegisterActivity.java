package com.example.to_do;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog pbRegisterDialog;
    private EditText edEmail, edPassword, edCnfmPassword;
    private Button btnSigIn;
    private TextView tvSignIn;
    private FirebaseAuth firebaseAuth;
    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        initialize();
        clickableTextview();
        setOnClickListener();

    }

    /**
     * for Clickable Sign in TextView
     */
    private void clickableTextview() {

        String mLoginText = getString(R.string.already_account_message);
        SpannableString ss = new SpannableString(mLoginText);
        ClickableSpan clickableSpanSignin = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                navigateToLoginScreen();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpanSignin, 24, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignIn.setText(ss);
        tvSignIn.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * for Initialize Variable
     */
    private void initialize() {
        tvSignIn = findViewById(R.id.tvAlreadyAccountMessage);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edCnfmPassword = findViewById(R.id.edCnfmPassword);
        btnSigIn = findViewById(R.id.btnSignUp);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            finish();
            navigateToMainScreen();
        }

        pbRegisterDialog = new ProgressDialog(RegisterActivity.this);

    }

    /**
     * for Navigate to Main Screen
     */
    private void navigateToMainScreen() {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }


    private void setOnClickListener() {
        btnSigIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        registeruser();


    }


    /**
     * for Navigate to login Screen
     */
    private void navigateToLoginScreen() {
        finish();
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
    }


    /**
     * for user Registration
     */
    private void registeruser() {


        if (CheckEditTextIsEmptyOrNot()) {
            pbRegisterDialog.setMessage("Please Wait, We are Registering Your Data on Server");
            pbRegisterDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {

                                finish();
                                // firebaseAuth.signOut();
                                navigateToLoginScreen();
                                //Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.valid_email, Toast.LENGTH_SHORT).show();
                            }
                            pbRegisterDialog.dismiss();

                        }
                    });
        }

    }

    /**
     * For Checking Validation of TextBox
     */
    public Boolean CheckEditTextIsEmptyOrNot() {

        mEmail = edEmail.getText().toString().trim();
        mPassword = edPassword.getText().toString().trim();
        String mCnfmPassword = edCnfmPassword.getText().toString().trim();


        if (TextUtils.isEmpty(mEmail) && TextUtils.isEmpty(mPassword) && TextUtils.isEmpty(mCnfmPassword)) {
            Toast.makeText(getApplicationContext(), R.string.enter_field, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mEmail)) {
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(mEmail)) {
            Toast.makeText(getApplicationContext(), R.string.proper_email, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mPassword)) {
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPassword.length() < 8 || mPassword.length() > 16) {
            Toast.makeText(getApplicationContext(), R.string.password_range, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!mPassword.equals(mCnfmPassword)) {
            Toast.makeText(getApplicationContext(), R.string.passwordNotMatch, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /**
     * for Press Back Press
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToLoginScreen();
    }

    /**
     * For Email Validation Function
     */
    private boolean isValidEmail(String mEmail) {
        return (!TextUtils.isEmpty(mEmail) && Patterns.EMAIL_ADDRESS.matcher(mEmail).matches());
    }

}
