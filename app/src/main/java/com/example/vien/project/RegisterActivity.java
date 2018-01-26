package com.example.vien.project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private AutoCompleteTextView etEmail, etNama, etPassword, etRepasword;
    private EditText etTelepone;
    private Button btnregis, btngoogle;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private final int RC_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("mohon  menunggu...");
        progressDialog.setCanceledOnTouchOutside(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");


        etEmail = (AutoCompleteTextView) findViewById(R.id.email);
        etTelepone = (EditText) findViewById(R.id.telephone);
        etNama = (AutoCompleteTextView) findViewById(R.id.nama);
        etPassword = (AutoCompleteTextView) findViewById(R.id.password);
        etRepasword = (AutoCompleteTextView) findViewById(R.id.repassword);
        btnregis = (Button) findViewById(R.id.buttonregis);
        btngoogle = (Button) findViewById(R.id.btnsubmitgoogle);





        btnregis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();

            }
        });

        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("551116281068-katf9t4f6gl54jcdlaji1b2u8os3cval.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);


        btngoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent,RC_SIGN_IN);


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(RegisterActivity.this, "Register berhasil", Toast.LENGTH_LONG).show();
            }
            catch (ApiException e){

                Log.w("Firebase Message:", "Google sign in failed", e);
                Toast.makeText(RegisterActivity.this, "gagal login", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void doRegister() {

        Boolean validation = validation();
        if (!validation) {
            Toast.makeText(this,"Inputan Tidak sesuai",Toast.LENGTH_LONG).show();

        } else {
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Firebase Message", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                               Toast.makeText(RegisterActivity.this,"Authentification success",Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase Message", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }

    private boolean validation() {
        boolean isValid = true;



        if (isValid) {
            if (etNama.getText().toString().isEmpty()) {

                isValid = false;
                etNama.setError("wajib diisi");
            }
        }

        if (isValid) {
            if (etTelepone.getText().toString().isEmpty()) {

                isValid = false;
                etTelepone.setError("wajib diisi");
            }
        }
        if (isValid) {
            if (etEmail.getText().toString().isEmpty()) {

                isValid = false;
                etEmail.setError("wajib diisi");
            }
        }

        if (isValid) {
            if (!isValidEmail(etEmail.getText().toString())) {
                isValid = false;
                etEmail.setError("wajib diisi");
            }

        }


        if (isValid) {
            if (etPassword.getText().toString().isEmpty()) {

                isValid = false;
                etPassword.setError("wajib diisi");
            }

        }

        if (isValid) {
            if (etPassword.getText().toString().length()<=6) {
                isValid = false;
                etPassword.setError("Password Minimal 6 karakter");
            }

        }


        if (isValid) {
            if (etRepasword.getText().toString().isEmpty()) {

                isValid = false;
                etRepasword.setError("wajib diisi");
            }
        }

        if (isValid) {
            if (!etPassword.getText().toString().equals(etRepasword.getText().toString())) {

                isValid = false;
                etRepasword.setError("wajib diisi");
            }
        }
        return isValid;
    }


    public boolean isValidEmail(String email){
        if(email.trim().matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(.[a-zA-Z]{2,})$")) return true;
    return false;

    }
}



