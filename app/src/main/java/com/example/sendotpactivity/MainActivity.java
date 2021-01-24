package com.example.sendotpactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sendotpactivity.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog=new ProgressDialog(MainActivity.this);
        binding.CountryCodePickers.registerCarrierNumberEditText(binding.PhoneNumber);
        binding.SendOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.PhoneNumber.length()!=0)
                {
                    progressDialog.setMessage("OTP Sending...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Intent intent=new Intent(MainActivity.this,VerifyOTPActivity.class);
                    intent.putExtra("Code",binding.CountryCodePickers.getFullNumberWithPlus().replace(" ",""));
                    progressDialog.dismiss();
                    startActivity(intent);
                }
                else
                {
                    binding.PhoneNumber.requestFocus();
                    binding.PhoneNumber.setText("Enter Phone Number");
                }
            }
        });

    }
}