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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView login;
     ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    Boolean EditTextEmptyCheck;
    String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private EditText edEmail, edPassword;
    private Button btnLogin;
    private String memail, mpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        initialize();
        clickableTextview();
        setOnClickListener();
    }


    /**
     * for Clickable Sigin TextView
     */
    private void clickableTextview() {
        String mSignInText = "don't have an account? SIGN UP";
        SpannableString ss = new SpannableString(mSignInText);
        ClickableSpan clickableSpanLogin = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(i, 1);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpanLogin, 22, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login.setText(ss);
        login.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * for initialize variable
     */
    private void initialize() {
        login = findViewById(R.id.not_account);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            finish();
            navigateToMainScreen();
        }
        edEmail = findViewById(R.id.ledemail);
        edPassword = findViewById(R.id.lpassword);
        btnLogin = findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(LoginActivity.this);

    }

    /**
     * for  login on click of button
     */
    private void setOnClickListener() {
        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        loginuser();


    }


    /**
     * for Login user
     */
    private void loginuser() {

        CheckEditTextIsEmptyOrNot();
        if (EditTextEmptyCheck) {
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(memail, mpass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        navigateToMainScreen();

                        Toast.makeText(getApplicationContext(), R.string.success_login, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.email_not_registered, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    /**
     * For Email Validation Function
     */
    private boolean isValidEmail(String memail) {


        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(memail);
        return matcher.matches();
    }


    /**
     * for Navigate to Main Screen
     */
    private void navigateToMainScreen() {

        startActivity(new Intent(getBaseContext(), MainActivity.class));

    }

    /**
     * For Checking Validation of TextBox
     */
    public void CheckEditTextIsEmptyOrNot() {

        memail = edEmail.getText().toString().trim();
        mpass = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(memail) && TextUtils.isEmpty(mpass)) {

            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.enter_email_password, Toast.LENGTH_SHORT).show();

        } else if (!isValidEmail(memail)) {
            Toast.makeText(getApplicationContext(), R.string.proper_email, Toast.LENGTH_SHORT).show();
            EditTextEmptyCheck = false;
        } else if (TextUtils.isEmpty(memail)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mpass)) {
            EditTextEmptyCheck = false;
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
        } else if (mpass.length() < 8 || mpass.length() > 16) {
            Toast.makeText(getApplicationContext(), R.string.password_range, Toast.LENGTH_SHORT).show();
            EditTextEmptyCheck = false;
        } else {
            EditTextEmptyCheck = true;

        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}