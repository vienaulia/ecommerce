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
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private AutoCompleteTextView etEmail;
    EditText etpassword;
    private Button btnlogin,btngoogle;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private final int RC_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("mohon menunggu");
        progressDialog.setCanceledOnTouchOutside(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Masuk Aplikasi");


        etEmail = (AutoCompleteTextView) findViewById(R.id.email);
        etpassword = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.buttonlogin);
        btngoogle = (Button) findViewById(R.id.btnsubmitgoogle);

        btnlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dologin();
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
                Toast.makeText(LoginActivity.this, "Register berhasil", Toast.LENGTH_LONG).show();
            }
            catch (ApiException e){

                Log.w("Firebase Message:", "Google sign in failed", e);
                Toast.makeText(LoginActivity.this, "gagal login", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dologin() {

        boolean validation = validation();
        if (!validation) {
            Toast.makeText(this, "periksa kembali inputan anda", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etpassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Firebase Message", "login:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, " success", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase Message", "login:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }
    }

    private boolean validation() {
        boolean isValid = true;


        if (isValid) {
            if (etEmail.getText().toString().isEmpty()) {
                isValid = false;
                etEmail.setError("harap isi");
            }

        }

        if (isValid) {
            if (etpassword.getText().toString().isEmpty()) {
                isValid = false;
                etpassword.setError("harap isi");
            }

        }

        return isValid;
    }
}
