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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLoginMessage;
    private ProgressDialog pbDialog;
    private FirebaseAuth firebaseAuth;
    private EditText edEmail, edPassword;
    private Button btnLogin;
    private String mEmail, mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initialize();
        manageOnClickOfSignUp();
        setOnClickListener();
    }


    /**
     * for Clickable Sign in TextView
     */
    private void manageOnClickOfSignUp() {
        String mSignInText = getString(R.string.dont_account_message);
        SpannableString ss = new SpannableString(mSignInText);
        ClickableSpan clickableSpanLogin = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpanLogin, 22, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLoginMessage.setText(ss);
        tvLoginMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * for initialize variable
     */
    private void initialize() {
        tvLoginMessage = findViewById(R.id.tvNotAccountMsg);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {

            finish();
            navigateToMainScreen();
        }
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        btnLogin = findViewById(R.id.btnLogin);

        pbDialog = new ProgressDialog(LoginActivity.this);

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


        if (CheckEditTextIsEmptyOrNot()) {
            pbDialog.setMessage("Please Wait");
            pbDialog.show();
            firebaseAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!isFinishing())
                        pbDialog.dismiss();
                    if (task.isSuccessful()) {
                        navigateToMainScreen();

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
    public boolean isValidEmail(String mEmail) {
        return (!TextUtils.isEmpty(mEmail) && Patterns.EMAIL_ADDRESS.matcher(mEmail).matches());
    }


    /**
     * for Navigate to Main Screen
     */
    private void navigateToMainScreen() {

        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();


    }

    /**
     * For Checking Validation of TextBox
     */
    public boolean CheckEditTextIsEmptyOrNot() {

        mEmail = edEmail.getText().toString().trim();
        mPassword = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mEmail) && TextUtils.isEmpty(mPassword)) {


            Toast.makeText(getApplicationContext(), R.string.enter_email_password, Toast.LENGTH_SHORT).show();
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
        }

        return true;
    }


}