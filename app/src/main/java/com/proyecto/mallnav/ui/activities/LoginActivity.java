package com.proyecto.mallnav.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.proyecto.mallnav.R;
import com.proyecto.mallnav.ui.fragments.RegistroFragment;


public class LoginActivity extends AppCompatActivity {
    EditText mCorreo, mPassword;
    Button mLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCorreo = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextPassword);
        mLogin = findViewById(R.id.buttonLogin);
        mAuth = FirebaseAuth.getInstance();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = mCorreo.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(correo.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Ingrese sus credenciales",Toast.LENGTH_SHORT).show();
                }else {
                    iniciarSesion(correo, password);
                }
            }
        });
    }

    public void iniciarSesion(String correo, String password){
        mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent (LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this,"Bienvenido",Toast.LENGTH_SHORT).show();
                }/*else{
                    Toast.makeText(LoginActivity.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
                }*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Credenciales incorrectas",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrar(View v){
        RegistroFragment fragment = new RegistroFragment();
        fragment.show(getSupportFragmentManager(), "Navegar a fragment");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}