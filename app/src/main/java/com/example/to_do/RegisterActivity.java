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
import android.view.View;
import android.view.Window;
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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean EditTextEmptyCheck;
    ProgressDialog progressDialog;
    EditText edEmail, edPassword, edcnpassword;
    Button btnSigin;
    TextView signin;
    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private FirebaseAuth firebaseAuth;
    private String memail, mpass, mcnfpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_register);

        initialize();
        clickableTextview();
        setOnClickListener();

    }

    /**
     * for Clickable Sign in TextView
     */
    private void clickableTextview() {

        String mLoginText = "already have an account? SIGN IN";
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
        signin.setText(ss);
        signin.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * for Initialize Variable
     */
    private void initialize() {
        signin = findViewById(R.id.already_account);
        edEmail = findViewById(R.id.edemail);
        edPassword = findViewById(R.id.edpassword);
        edcnpassword = findViewById(R.id.confmpassword);
        btnSigin = findViewById(R.id.btnSignup);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            finish();
            navigateToMainScreen();
        }

        progressDialog = new ProgressDialog(RegisterActivity.this);

    }

    /**
     * for Navigate to Main Screen
     */
    private void navigateToMainScreen() {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }


    private void setOnClickListener() {
        btnSigin.setOnClickListener(this);
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

        CheckEditTextIsEmptyOrNot();
        if (EditTextEmptyCheck) {
            progressDialog.setMessage("Please Wait, We are Registering Your Data on Server");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(memail, mpass)
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
                            progressDialog.dismiss();

                        }
                    });
        }

    }

    /**
     * For Checking Validation of TextBox
     */
    public void CheckEditTextIsEmptyOrNot() {

        memail = edEmail.getText().toString().trim();
        mpass = edPassword.getText().toString().trim();
        mcnfpass = edcnpassword.getText().toString().trim();


        if (TextUtils.isEmpty(memail) && TextUtils.isEmpty(mpass) && TextUtils.isEmpty(mcnfpass)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.enter_field, Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(memail)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(memail)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.proper_email, Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(mpass)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
        }
        else if (mpass.length() < 8 || mpass.length() > 16) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.password_range, Toast.LENGTH_SHORT).show();
        }
        else if (!mpass.equals(mcnfpass)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.passwordnotmatch, Toast.LENGTH_SHORT).show();
        }
        else {
            EditTextEmptyCheck = true;
        }
        // if(password.getText().toString().equals(confirmpassword.getText().toString()))

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
    private boolean isValidEmail(String memail) {


        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(memail);
        return matcher.matches();
    }

}
