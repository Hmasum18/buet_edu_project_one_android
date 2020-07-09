package com.example.buet_edu_project_one_vmasum.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.buet_edu_project_one_vmasum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity:";
    private EditText userName,userPhoneNumber,userOrg,userPass,userConfPass;
    private Button submitButton;
    private String name,phoneNumber,org,password,confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = userPhoneNumber.getText().toString();
                String dummyEmail = phoneNumber+"haha@dummy.com";
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(dummyEmail)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                  boolean newUser =task.getResult().getSignInMethods().isEmpty();
                                    if(newUser)
                                    {
                                        if(validateUserInput())
                                        {
                                            Log.w(TAG,"new phone number");
                                            sendOTP();
                                            Intent intent = new Intent(getApplicationContext(),VerificationActivity.class);
                                            intent.putExtra("Name",name);
                                            intent.putExtra("Phone",phoneNumber);
                                            intent.putExtra("Org",org);
                                            intent.putExtra("Password",password);
                                            startActivity(intent);
                                        }
                                    }else {
                                        Log.w(TAG,"Phone number is already used");
                                        userPhoneNumber.setError("Phone number is already used");
                                        userPhoneNumber.requestFocus();
                                    }
                            }
                        });
            }
        });



    }

    public void findViews()
    {
        userName = findViewById(R.id.regActvNameTextId);
        userPhoneNumber= findViewById(R.id.regActvPhoneTextId);
        userOrg  = findViewById(R.id.regActvOrgTextId);
        userPass = findViewById(R.id.regActvPassTextId);
        userConfPass =  findViewById(R.id.regActvConfPassId);
        submitButton = findViewById(R.id.regActvSubmitButtonId);
    }

    public boolean validateUserInput()
    {
        name = userName.getText().toString().trim();
        phoneNumber = userPhoneNumber.getText().toString().trim();
        org = userOrg.getText().toString().trim();
        password = userPass.getText().toString().trim();
        confPassword = userConfPass.getText().toString().trim();

        if(name.equals(""))
        {
            userName.setError("Name field can't be empty");
            userName.requestFocus();
            return false;
        }
        if(phoneNumber.equals(""))
        {
            userPhoneNumber.setError("Phone number field can't be empty");
            userPhoneNumber.requestFocus();
            return false;
        }

        if(org.equals(""))
        {
            userOrg.setError("Organization can't be empty");
            userOrg.requestFocus();
            return false;
        }

        if(password.equals(""))
        {
            userPass.setError("Password field can't be empty");
            userPass.requestFocus();
            return false;
        }

        if(password.length()<6)
        {
            userPass.setError("Password length should be greater than 6");
            userPass.requestFocus();
            return false;
        }

        if(confPassword.equals(""))
        {
            userConfPass.setError("Confrim password field can't be empty");
            userConfPass.requestFocus();
            return false;
        }

        if(!password.equals(confPassword))
        {
            userConfPass.setError("Password doesn't match");
            userConfPass.requestFocus();
            return false;
        }

        return true;
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