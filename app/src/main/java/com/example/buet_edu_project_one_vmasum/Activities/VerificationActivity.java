package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.buet_edu_project_one_vmasum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerficationActivity: ";
    private PinView otpPinview;
    private Button verifyButton;
    private TextView resendText;

    String name,phoneNumber, org,password;

    String userId;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = this.getIntent();
       name = intent.getStringExtra("Name");
       phoneNumber = intent.getStringExtra("Phone");
       org = intent.getStringExtra("Org");
        password = intent.getStringExtra("Password");

        otpPinview = findViewById(R.id.verifyActvPinViewId);
        verifyButton = findViewById(R.id.verifyActvVerifyButton);
        resendText = findViewById(R.id.verifyActvResendTxtId);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Completing sign up.....");

        enableVerifyButton();

        resendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
                Toast.makeText(getApplicationContext(),"Verification code sent again", Toast.LENGTH_SHORT).show();

            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpPinview.getText().toString().trim();
                if(otp.equals("1234"))
                {
                    uploadDataInfirebaseAuthAndGoToMainActivity();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Incorrect code",Toast.LENGTH_SHORT).show();
                  // otpPinview.setText("");
                }
            }
        });

    }

    private void uploadDataInfirebaseAuthAndGoToMainActivity() {
        progressDialog.show();
        String email = phoneNumber+"haha@dummy.com";
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                           FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                           if(user!=null)
                           {
                               Log.d(TAG,"sign up successful with user id : "+ user.getUid());
                               userId = user.getUid();
                               Log.d(TAG," start uploading in the fireStore");
                               ///now save the data in the fireStore by the following method
                               saveDataInFireStore();
                           }

                        }
                    }
                });
    }

    private void saveDataInFireStore() {
        Map<String,Object> userInfoMap = new HashMap<>();
        userInfoMap.put("name",name);
        userInfoMap.put("phoneNumber",phoneNumber);
        userInfoMap.put("org",org);
        userInfoMap.put("password",password);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("profile").document(userId);
        documentReference.set(userInfoMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Data successfully saved in firestore... userId = " +userId);
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),"verified",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });
    }

    public void enableVerifyButton()
    {
        otpPinview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 4)
                {
                    verifyButton.setEnabled(true);
                    verifyButton.setBackground(getDrawable(R.drawable.bg_blue));
                   // verifyButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bgBlue)));

                }
                else {
                    verifyButton.setEnabled(false);
                    verifyButton.setBackground(getDrawable(R.drawable.bg_white_blue));
                   // verifyButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")) );

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void sendOTP()
    {
        /* RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://www.google.com";
                url = "https://ptsv2.com/t/jggnk-1594058277/post";
                url = "https://us-central1-smsapi-3b6ac.cloudfunctions.net/otp";
                JSONObject jsonObject = new JSONObject();
                try {
                    // user_id, comment_id,status
                    jsonObject.put("name", name);
                    jsonObject.put("phone", phone);
                    jsonObject.put("org",org);
                    jsonObject.put("nDigits",4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                displayResultTextView.setText(response.toString());
                                //  if(response.status.equals("OK")){
                                    // Call intent from here
                                }else{
                                     errorTextView.setText("Error Sending OTP");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        displayResultTextView.setText(error.toString());
                    }
                });
                queue.add(jsonObjReq);*/
    }
}