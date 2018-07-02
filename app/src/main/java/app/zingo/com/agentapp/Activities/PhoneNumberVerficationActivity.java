package app.zingo.com.agentapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.zingo.com.agentapp.CustomViews.CustomEditextTTF;
import app.zingo.com.agentapp.CustomViews.CustomFontTextView;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.LoginApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumberVerficationActivity extends AppCompatActivity {

    CustomEditextTTF mCountryCode,mPhoneNumber,mOtp;
    CustomFontTextView mSubmit,mResend;
    ImageView mNext;
    LinearLayout mOtpLayout;
    boolean response_str ;
    ProgressDialog dialog = null;
    //FireBase OTP
    private static final String TAG = "PhoneNumberVerfication";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    // [START declare_auth]
    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String type  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_verfication);

        mCountryCode = (CustomEditextTTF)findViewById(R.id.country_code);
        mPhoneNumber = (CustomEditextTTF)findViewById(R.id.mobile_number);
        mOtp = (CustomEditextTTF)findViewById(R.id.otp_text);
        mSubmit = (CustomFontTextView)findViewById(R.id.send_otp);
        mResend = (CustomFontTextView)findViewById(R.id.resend_otp);
        mNext = (ImageView)findViewById(R.id.next);
        mOtpLayout = (LinearLayout) findViewById(R.id.otp_layout);


        type = getIntent().getStringExtra("Type");
        System.out.println("type = "+type);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Util.hideKeyboard(PhoneNumberVerficationActivity.this);
                if(type != null)
                {
                    String mobile = mPhoneNumber.getText().toString();
                    String country = mCountryCode.getText().toString();

                    if(mobile==null||mobile.isEmpty()){
                        mPhoneNumber.setError("Should not be Empty");
                        mPhoneNumber.requestFocus();
                    }else if(country==null||country.isEmpty()){
                        mCountryCode.setError("Should not be Empty");
                        mCountryCode.requestFocus();
                    }else{
                        checkPhoneAvailablity(country,mobile,"send");
                   /* if(response_str){
                        startPhoneNumberVerification(country+mobile);
                    }*/
                    }
                }
                else
                {
                    System.out.println("type = "+type);
                }
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mOtp.getText().toString();
                if(mPhoneNumber.getText().toString().isEmpty()){
                    mPhoneNumber.setError("Enter Phone number.");
                }
                else if (TextUtils.isEmpty(code)) {
                    mOtp.setError("Cannot be empty.");
                    return;
                }
                else if(mVerificationId==null||mVerificationId.isEmpty()){
                    Toast.makeText(PhoneNumberVerficationActivity.this, "OTP is not matching", Toast.LENGTH_SHORT).show();
                }else{
                    dialog = new ProgressDialog(PhoneNumberVerficationActivity.this);
                    dialog.setMessage(getResources().getString(R.string.loader_message));
                    dialog.setCancelable(false);
                    dialog.show();
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });

        mResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = mCountryCode.getText().toString();
                String mobile = mPhoneNumber.getText().toString();
                if(mPhoneNumber.getText().toString()==null||mPhoneNumber.getText().toString().isEmpty()){
                    mPhoneNumber.setError("Should not empty");
                }else if(country==null||country.isEmpty()){
                    mCountryCode.setError("Should not be Empty");
                    mCountryCode.requestFocus();
                }else{
                   checkPhoneAvailablity(country,mobile,"resend");
                    /*if(response_str){
                        resendVerificationCode(country+mobile, mResendToken);
                    }*/
                }
            }
        });

        mPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(mPhoneNumber.getText().toString().equalsIgnoreCase(""))
                {
                    mOtpLayout.setVisibility(View.GONE);
                    mSubmit.setVisibility(View.VISIBLE);
                    mResend.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
               
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                
                String code = credential.getSmsCode();
                mOtp.setText(code);
                dialog = new ProgressDialog(PhoneNumberVerficationActivity.this);
                dialog.setMessage(getResources().getString(R.string.loader_message));
                dialog.setCancelable(false);
                dialog.show();
               
                signInWithPhoneAuthCredential(credential);//<---hard code---->

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
              
                
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    
                    mPhoneNumber.setError("Invalid phone number.");
                  
                } else if (e instanceof FirebaseTooManyRequestsException) {
                   
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                   
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
               
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(PhoneNumberVerficationActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

            
            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

      
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                75,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
        mOtpLayout.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.GONE);
        mResend.setVisibility(View.VISIBLE);


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            // [START_EXCLUDE]

                           
                               if(!mPhoneNumber.getText().toString().equalsIgnoreCase(""))
                               {
                                   Toast.makeText(PhoneNumberVerficationActivity.this, "Sucess verification", Toast.LENGTH_SHORT).show();
                                   Intent next = new Intent(PhoneNumberVerficationActivity.this, SignUpActivity.class);
                                   next.putExtra("PhoneNumber", mPhoneNumber.getText().toString());
                                   next.putExtra("Type",type);
                                   startActivity(next);
                               }
                               else
                               {
                                   Toast.makeText(PhoneNumberVerficationActivity.this,"Please enter phone number",Toast.LENGTH_SHORT).show();
                               }
                                //PhoneNumberVerficationActivity.this.finish();
                           


                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                              
                                mOtp.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                         
                        }
                    }
                });
    }


    private boolean checkPhoneAvailablity(final String country,final String mobile,final String type){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final TravellerAgentProfiles p1 = new TravellerAgentProfiles();

        p1.setPhoneNumber(mPhoneNumber.getText().toString());


        LoginApi apiService =
                Util.getClient().create(LoginApi.class);
        Call<ArrayList<TravellerAgentProfiles>> call = apiService.getProfileByPhone(Constants.auth_string,p1);

        call.enqueue(new Callback<ArrayList<TravellerAgentProfiles>>() {
            @Override
            public void onResponse(Call<ArrayList<TravellerAgentProfiles>> call, Response<ArrayList<TravellerAgentProfiles>> response) {
                //                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                int statusCode = response.code();
                if (statusCode == 200) {
                    //String  s = response.body();

                    if (response.body()!=null){
                        //if (s.equals("Profile Exist")){
                        mPhoneNumber.requestFocus();
                        mPhoneNumber.setError("Phone Number Already exist..!!");
                        response_str = true;
                    }

                    if (progressDialog!=null)
                        progressDialog.dismiss();


                }else if (statusCode == 404){
                    response_str = false;
                    if(type.equalsIgnoreCase("resend")){
                        resendVerificationCode("+"+country+mobile, mResendToken);
                    }else{
                        startPhoneNumberVerification("+"+country+mobile);
                    }


                    if (progressDialog!=null)
                        progressDialog.dismiss();
                } else{
                    if (progressDialog!=null)
                        progressDialog.dismiss();
                    Toast.makeText(PhoneNumberVerficationActivity.this, "checking failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                }
                //                callGetStartEnd();
            }

            @Override
            public void onFailure(Call<ArrayList<TravellerAgentProfiles>> call, Throwable t) {
                // Log error here since request failed
                if (progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("TAG", t.toString());
                Toast.makeText(PhoneNumberVerficationActivity.this, "Pleae check your data connection", Toast.LENGTH_SHORT).show();
            }
        });
        //            }


        //        });


        return response_str;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent(PhoneNumberVerficationActivity.this,LoginActivity.class);
        startActivity(intent);
        PhoneNumberVerficationActivity.this.finish();
    }
}
