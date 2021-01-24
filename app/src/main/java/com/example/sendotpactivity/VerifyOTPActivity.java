package com.example.sendotpactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.sendotpactivity.databinding.ActivityVerifyOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD;

public class VerifyOTPActivity extends AppCompatActivity {
    ActivityVerifyOTPBinding binding;
    String VerificationID;
    FirebaseAuth firebaseAuth;
    String PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVerifyOTPBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetUpOTPInputs();
        firebaseAuth=FirebaseAuth.getInstance();
        PhoneNumber=getIntent().getStringExtra("Code");
        binding.PhoneNumberText.setText(PhoneNumber);
        SendVerificationCode(PhoneNumber);
       binding.VerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(binding.InputCodeOne.length()!=0 && binding.InputCodeTwo.length()!=0 && binding.InputCodeThree.length()!=0 && binding.InputCodeFour.length()!=0 && binding.InputCodeFive.length()!=0 && binding.InputCodeSix.length()!=0 )
               {
                   String inputs=binding.InputCodeOne.getText().toString()+binding.InputCodeTwo.getText().toString()+binding.InputCodeThree.getText().toString()+binding.InputCodeFour.getText().toString()+binding.InputCodeFive.getText().toString()+binding.InputCodeSix.getText().toString();
                   UserVerifyCode(inputs);
               }
               else
               {
                   Toast.makeText(VerifyOTPActivity.this, "Empty", Toast.LENGTH_SHORT).show();
               }
            }
        });
        binding.ResendOTPText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendVerificationCode(PhoneNumber);
            }
        });
    }
    private void SetUpOTPInputs()
    {
        binding.InputCodeOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    binding.InputCodeTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.InputCodeTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    binding.InputCodeThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.InputCodeThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    binding.InputCodeFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.InputCodeFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    binding.InputCodeFive.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.InputCodeFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty())
                {
                    binding.InputCodeSix.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void SendVerificationCode(String number) {
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(VerifyOTPActivity.this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String VerifyCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(VerifyCode, forceResendingToken);
            VerificationID=VerifyCode;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                UserVerifyCode(code);
            }
            else
            {
                Toast.makeText(VerifyOTPActivity.this, "Code Empty", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyOTPActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void UserVerifyCode(String UserCode) {
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(VerificationID,UserCode);
        SignInUserWithCredential(phoneAuthCredential);
    }

    private void SignInUserWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerifyOTPActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Intent intent=new Intent(VerifyOTPActivity.this,WelComeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(VerifyOTPActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}