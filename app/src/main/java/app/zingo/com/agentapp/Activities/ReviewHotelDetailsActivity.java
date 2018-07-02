package app.zingo.com.agentapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.TextView;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.widget.LinearLayout;

import app.zingo.com.agentapp.Adapter.AutocompleteCustomArrayAdapter;
import app.zingo.com.agentapp.Adapter.ImageAdapter;
import app.zingo.com.agentapp.Adapter.ProfileAdapter;
import app.zingo.com.agentapp.Adapter.ViewPagerAdapter;
import app.zingo.com.agentapp.CustomViews.CustomAutoCompleteView;
import app.zingo.com.agentapp.MainActivity;
import app.zingo.com.agentapp.Model.AgentHotel;
import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.Model.NotificationManager;
import app.zingo.com.agentapp.Model.Payment;
import app.zingo.com.agentapp.Model.Profile1;
import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.Model.TravellerAgentProfiles;
import app.zingo.com.agentapp.R;
import app.zingo.com.agentapp.Utils.Constants;
import app.zingo.com.agentapp.Utils.PreferenceHandler;
import app.zingo.com.agentapp.Utils.ThreadExecuter;
import app.zingo.com.agentapp.Utils.Util;
import app.zingo.com.agentapp.WebApi.BookingApi;
import app.zingo.com.agentapp.WebApi.HotelOperations;
import app.zingo.com.agentapp.WebApi.LoginApi;
import app.zingo.com.agentapp.WebApi.NotificationApi;
import app.zingo.com.agentapp.WebApi.PaymentApi;
import app.zingo.com.agentapp.WebApi.TravellerApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewHotelDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    Button mPayLater,mPayNow;
    //TextInputEditText mGuestMobile,mGuestEmail,mGuestCompany,mGuestGST,mOTP;
    TextView mHotelTotalCharges,mHotelName,mHotelAdd,mCID,mCOD,mCIT,mCOT,mHotelDisplayRate,
                mNoAdult,mNoChild,mRoomType,mNoRooms,mHotelRate,mHotelGst,mAgentErnings,mNoOfNights,mZingoWallet,mVerifyMobile,
            mResendOTP,mSubmitOtp;
    private RadioButton mMale,mFemale,mTransgender,mBussiness,mPersonal;
    LinearLayout mCompany,mGST,mGSTCompanyParent,mEarningParent,mCouponCodeParent,mOTPParent,mOtherParent;
    CheckBox mZingoCash;
    CustomAutoCompleteView mGuestName;
    EditText mGuestMobile,mGuestEmail,mGuestCompany,mGuestGST,mOTP;

    //variable
    int travellerIid;
    Traveller traveller;
    Bookings1 bookings;
    int price,hotelid,displayprice;
    long commissionAmt;
    String checkoutDate,checkinDate,room,CheckInTime,CheckOutTime;
    AgentHotel hotelDetailseResponse;

    int walletAmount,usedAmount;

    boolean book = true;
    TravellerAgentProfiles profiles;
    double amount;
    ProgressDialog dialog;

    ArrayList<Traveller> tlist;
    ArrayAdapter<Traveller> searchAdapter;


    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    // [START declare_auth]
    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String type  = null;


    //PaymentGateway
    private static final String TAG = ReviewHotelDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_review_hotel_details);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);



            mPayLater = (Button)findViewById(R.id.pay_later_btn);
            mPayNow = (Button)findViewById(R.id.pay_now_btn);

            mGuestName = (CustomAutoCompleteView)findViewById(R.id.full_name_guest);
            mGuestName.setThreshold(1);
            mGuestName.setAdapter(searchAdapter);

           // mGuestMobile = (TextInputEditText)findViewById(R.id.mobile_guest);
            mGuestMobile = (EditText) findViewById(R.id.mobile_guest);
           // mGuestEmail = (TextInputEditText)findViewById(R.id.email_guest);
            mGuestEmail = (EditText)findViewById(R.id.email_guest);
           // mGuestGST = (TextInputEditText)findViewById(R.id.gst_guest);
            mGuestGST = (EditText)findViewById(R.id.gst_guest);
            //mGuestCompany = (TextInputEditText)findViewById(R.id.company_guest);
            mGuestCompany = (EditText)findViewById(R.id.company_guest);
           // mOTP = (TextInputEditText)findViewById(R.id.mobile_otp);
            mOTP = (EditText)findViewById(R.id.mobile_otp);
            mCompany = (LinearLayout) findViewById(R.id.company_layout);
            mGST = (LinearLayout)findViewById(R.id.gst_layout);
            mEarningParent = (LinearLayout)findViewById(R.id.your_earning_parent);
            mEarningParent.setVisibility(View.GONE);
            mGSTCompanyParent = (LinearLayout)findViewById(R.id.gst_company_parent);
            mCouponCodeParent = (LinearLayout)findViewById(R.id.coupon_code_parent);
            mOTPParent = (LinearLayout)findViewById(R.id.otp_parent);
            mOtherParent = (LinearLayout)findViewById(R.id.others_parent);
            mGSTCompanyParent.setVisibility(View.GONE);
            mCouponCodeParent.setVisibility(View.GONE);
            mOTPParent.setVisibility(View.GONE);

            mHotelTotalCharges = (TextView)findViewById(R.id.hotel_total_charges);
            mHotelRate = (TextView)findViewById(R.id.hotel_charges);
            mHotelDisplayRate = (TextView)findViewById(R.id.hotel_display_charges);
            mHotelGst = (TextView)findViewById(R.id.hotel_gst_charges);
            mNoOfNights = (TextView)findViewById(R.id.txt_no_of_Nights);
            mZingoWallet = (TextView)findViewById(R.id.zingo_money);
            mZingoCash = (CheckBox) findViewById(R.id.zingo_wallet);

            mAgentErnings = (TextView)findViewById(R.id.agent_earnings);
            mHotelName = (TextView)findViewById(R.id.txt_hotel_name);
            mHotelAdd = (TextView)findViewById(R.id.txt_hotel_address);
            mCID = (TextView)findViewById(R.id.txt_hotel_check_in_date);
            mCOD = (TextView)findViewById(R.id.txt_hotel_check_out_date);
            mCIT = (TextView)findViewById(R.id.txt_hotel_check_in_time);
            mCOT = (TextView)findViewById(R.id.txt_hotel_check_out_time);
            mRoomType = (TextView)findViewById(R.id.txt_room_type);
            mNoRooms = (TextView)findViewById(R.id.txt_no_of_rooms);
            mNoAdult = (TextView)findViewById(R.id.txt_no_of_adult);
            mNoChild = (TextView)findViewById(R.id.txt_no_of_child);
            mVerifyMobile = (TextView)findViewById(R.id.verify_mobile_number);
            mResendOTP = (TextView)findViewById(R.id.resend_otp);
            mSubmitOtp = (TextView)findViewById(R.id.submit_otp);


            mMale = (RadioButton) findViewById(R.id.booking_male);
            mFemale = (RadioButton) findViewById(R.id.booking_female);
            mPersonal = (RadioButton) findViewById(R.id.booking_personal);
            mBussiness = (RadioButton) findViewById(R.id.booking_company);
            mPersonal.setChecked(true);
            //mTransgender = (RadioButton) findViewById(R.id.booking_transgender);
            System.out.println("Duration="+dateCal());

            price = getIntent().getIntExtra("Price",0);
            displayprice = getIntent().getIntExtra("displayprice",0);
            hotelid = getIntent().getIntExtra("HotelId",0);
            checkoutDate = getIntent().getStringExtra("CheckoutDate");
            checkinDate = getIntent().getStringExtra("CheckinDate");
            CheckInTime = getIntent().getStringExtra("CheckInTime");
            CheckOutTime = getIntent().getStringExtra("CheckoutTime");
            System.out.println("check in = "+CheckInTime);
            System.out.println("CheckInTime"+CheckInTime+" "+"CheckOutTime"+CheckOutTime);
            room = getIntent().getStringExtra("Room");

            if(hotelid != 0)
            {
                getHotelDetails(hotelid);
                getProfile();

            }

            mGuestName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Traveller ci = (Traveller)parent.getItemAtPosition(position);
                    mGuestName.setText(ci.getFirstName());
                    if(ci.getGender().equalsIgnoreCase("Male"))
                    {
                        mMale.setChecked(true);
                        mMale.setEnabled(false);
                        mFemale.setEnabled(false);

                    }
                    else if(ci.getGender().equalsIgnoreCase("Female"))
                    {
                        mFemale.setChecked(true);
                        mMale.setEnabled(false);
                        mFemale.setEnabled(false);
                    }
                    //mGuestMobile.setEnabled(false);
                    mGuestEmail.setText(ci.getEmail());
                    //mGuestEmail.setEnabled(false);
                    mGuestCompany.setText(ci.getCompany());
                    //mGuestCompany.setEnabled(false);
                    mGuestGST.setText(ci.getCustomerGST());
                    //mGuestGST.setEnabled(false);

                    travellerIid = ci.getTravellerId();
                }
            });

            if(room != null)
            {
                String[] s = room.split(",");
                mNoRooms.setText(s[0]+" Rooms");
                mNoAdult.setText(s[1]+" Adults");
                mNoChild.setText(s[2]+" child(ren)");

            }
            if(CheckInTime != null && CheckOutTime != null)
            {
                mCOT.setText(CheckOutTime);
                mCIT.setText(CheckInTime);
            }

            mResendOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobile = mGuestMobile.getText().toString();
                    if(mobile == null || mobile.isEmpty())
                    {
                        mGuestMobile.setError("Please enter valid mobile number");
                    }
                    else
                    {
                        dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
                        dialog.setMessage(getResources().getString(R.string.loader_message));
                        dialog.setCancelable(false);
                        dialog.show();
                        //verifyPhoneNumberWithCode(mVerificationId, code);
                    /*if(type.equalsIgnoreCase("resend")){
                        resendVerificationCode(country+mobile, mResendToken);
                    }else{*/
                        //startPhoneNumberVerification(country+mobile);
                        //startPhoneNumberVerification(mobile);
                        resendVerificationCode(mobile, mResendToken);
                    }
                }
            });

            mVerifyMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mobile = mGuestMobile.getText().toString();
                    if(mobile == null || mobile.isEmpty())
                    {
                        mGuestMobile.setError("Please enter valid mobile number");
                    }
                    else
                    {
                        dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
                        dialog.setMessage(getResources().getString(R.string.loader_message));
                        dialog.setCancelable(false);
                        dialog.show();
                        //verifyPhoneNumberWithCode(mVerificationId, code);
                    /*if(type.equalsIgnoreCase("resend")){
                        resendVerificationCode(country+mobile, mResendToken);
                    }else{*/
                        //startPhoneNumberVerification(country+mobile);
                        startPhoneNumberVerification(mobile);
                    }
                    //}
                }
            });


            mSubmitOtp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = mOTP.getText().toString();
                    if (TextUtils.isEmpty(code)) {
                        mOTP.setError("Cannot be empty.");
                        return;
                    }
                    else if(mVerificationId==null||mVerificationId.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "OTP is not matching", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
                        dialog.setMessage(getResources().getString(R.string.loader_message));
                        dialog.setCancelable(false);
                        dialog.show();
                        verifyPhoneNumberWithCode(mVerificationId, code);
                    }
                }
            });

            mZingoCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    String userRoleId = PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserRoleUniqueID();

                    if(userRoleId.equalsIgnoreCase("Luci-Agent")){
                        double percent  = walletAmount * 40;
                        amount = percent/100;
                        if(b){
                            calculateAmount(-amount);
                        }else{
                            calculateAmount(0);
                        }

                    }else{
                        double percent  = walletAmount * 20;
                        amount = percent/100;
                        if(b){
                            calculateAmount(-amount);
                        }else{
                            calculateAmount(0);
                        }
                    }

                }
            });

            if(mZingoCash.isChecked()){

                String userRoleId = PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserRoleUniqueID();

                if(userRoleId.equalsIgnoreCase("Luci-Agent")){
                    double percent  = walletAmount * 40;
                    amount = percent/100;
                    calculateAmount(-amount);
                }else{
                    double percent  = walletAmount * 20;
                    amount = percent/100;
                    calculateAmount(-amount);
                }


            }else{
                calculateAmount(0);
            }


            mPersonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGST.setVisibility(View.GONE);
                    mCompany.setVisibility(View.GONE);
                }
            });

            mCompany.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGST.setVisibility(View.VISIBLE);
                    mCompany.setVisibility(View.VISIBLE);
                }
            });


            mPersonal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPersonal.isChecked())
                    {
                        mPersonal.setChecked(true);
                        mBussiness.setChecked(false);
                        mGSTCompanyParent.setVisibility(View.GONE);
                    }
                    else
                    {
                        mPersonal.setChecked(true);
                        mBussiness.setChecked(false);
                        mGSTCompanyParent.setVisibility(View.GONE);
                    }
                }
            });

            mBussiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBussiness.isChecked())
                    {
                        mBussiness.setChecked(true);
                        mPersonal.setChecked(false);
                        mGSTCompanyParent.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mBussiness.setChecked(true);
                        mPersonal.setChecked(false);
                        mGSTCompanyParent.setVisibility(View.VISIBLE);
                    }
                }
            });

            mGuestMobile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                isValidEmail();

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (start == 9){

                        getTravelerByPhone(mGuestMobile.getText().toString());

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });




            mPayLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String guestName = mGuestName.getText().toString();
                    String guestMobile = mGuestMobile.getText().toString();
                    String guestEmail = mGuestEmail.getText().toString();
                    String total = mHotelTotalCharges.getText().toString();
                    String hotelName = mHotelName.getText().toString();
                    String gst = mGuestGST.getText().toString();
                    String company = mGuestCompany.getText().toString();

                    if(guestName==null||guestName.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestMobile==null||guestMobile.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestEmail==null||guestEmail.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(total==null||total.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(hotelName==null||hotelName.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    } else if(!mMale.isChecked() && !mFemale.isChecked() )//&& !mTransgender.isChecked())
                    {
                        Toast.makeText(ReviewHotelDetailsActivity.this,"Please select gender",Toast.LENGTH_SHORT).show();
                    }else if(mBussiness.isChecked()  )//&& !mTransgender.isChecked())
                    {
                        if(gst==null||gst.isEmpty()){
                            mGuestGST.setError("Field should not be empty");
                            mGuestGST.requestFocus();
                        }
                        else if(gst.length() != 15)
                        {
                            mGuestGST.setError("Please enter valid gst number");
                            mGuestGST.requestFocus();
                        }
                        else if(company==null||company.isEmpty()){
                            mGuestCompany.setError("Field should not be empty");
                            mGuestCompany.requestFocus();
                        }else{
                            if(travellerIid==0){
                                addTraveler("paylater");
                                //System.out.println("Not exist");
                            }else if(tlist != null && !isexist(tlist)){
                                addTraveler("paylater");
                                //updateTraveller("paylater");
                                //System.out.println("Not exist "+isexist(tlist));
                            }
                            else
                            {
                                booking("paylater");
                            }
                        }
                    }else{
                        if(travellerIid==0){
                            addTraveler("paylater");
                            //System.out.println("Not exist "+isexist(tlist));
                        }else if(tlist != null && !isexist(tlist)){
                            addTraveler("paylater");
                            //updateTraveller("paylater");
                            //System.out.println("Not exist "+isexist(tlist));
                        }
                        else
                        {
                            booking("paylater");
                        }
                    /*Intent book = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                    startActivity(book);*/
                    }


                    //booking("paylater");
                }
            });

            mPayNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String guestName = mGuestName.getText().toString();
                    String guestMobile = mGuestMobile.getText().toString();
                    String guestEmail = mGuestEmail.getText().toString();
                    String total = mHotelTotalCharges.getText().toString();
                    String hotelName = mHotelName.getText().toString();

                    if(guestName==null||guestName.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestMobile==null||guestMobile.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(guestEmail==null||guestEmail.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(total==null||total.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    }else if(hotelName==null||hotelName.isEmpty()){
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Field should not be empty", Toast.LENGTH_SHORT).show();
                    } else if(!mMale.isChecked() && !mFemale.isChecked() )//&& !mTransgender.isChecked())
                    {
                        Toast.makeText(ReviewHotelDetailsActivity.this,"Please select gender",Toast.LENGTH_SHORT).show();
                    }else{
                        startPayment();
                    /*Intent book = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                    startActivity(book);*/
                    }

                }
            });

            setTitle("Review Hotel Details");



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
                    mOTP.setText(code);
                    dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
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

                        mGuestMobile.setError("Invalid phone number.");

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


                }
            };

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean isexist(ArrayList<Traveller> travellers) {

        if(travellers.size() != 0)
        {
            String name = mGuestName.getText().toString().toLowerCase().trim();
            String mobile = mGuestMobile.getText().toString().trim();
            String email = mGuestEmail.getText().toString().trim();
            for (int i=0;i<travellers.size();i++)
            {
                Traveller traveller = travellers.get(i);
                if(traveller.getFirstName() != null && traveller.getPhoneNumber() != null && traveller.getEmail() != null)
                {

                    if(traveller.getFirstName().equalsIgnoreCase(name) && traveller.getPhoneNumber().equalsIgnoreCase(mobile)
                            && traveller.getEmail().equalsIgnoreCase(email))
                    {
                        travellerIid = traveller.getTravellerId();

                        return true;
                        //break;
                    }
                }
            }
        }
        return false;
    }



    /*private void filterHotels(String s) {

        ArrayList<Profile1> filteredList = new ArrayList<>();
        for(int i=0;i<hotelDetailseResponse.size();i++)
        {
            String text = hotelDetailseResponse.get(i).getHotelName().toLowerCase();
            if(text.contains(s))
            {
                filteredList.add(hotelDetailseResponse.get(i));
            }

        }

        ProfileAdapter hotelListAdapter = new ProfileAdapter(ReviewHotelDetailsActivity.this,filteredList);
        hotel_list.setAdapter(hotelListAdapter);
        hotelListAdapter.notifyDataSetChanged();

    }*/


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
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
        /*mOtpLayout.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.GONE);
        mResend.setVisibility(View.VISIBLE);*/
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
        Toast.makeText(ReviewHotelDetailsActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();

    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);
        // ForceResendingToken from callbacks
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }

        Toast.makeText(ReviewHotelDetailsActivity.this, "OTP resent successfully", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ReviewHotelDetailsActivity.this, "Sucess verification", Toast.LENGTH_SHORT).show();
                            // [START_EXCLUDE]



                            //PhoneNumberVerficationActivity.this.finish();
                            mGuestName.setText("");
                            mGuestEmail.setText("");
                            mGuestCompany.setText("");
                            mGuestGST.setText("");

                            mOtherParent.setVisibility(View.VISIBLE);
                            mPayLater.setEnabled(true);
                            mPayNow.setEnabled(true);



                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                mOTP.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }

                        }
                    }
                });
    }














    private void getHotelDetails(final int hotelId) {

        final ProgressDialog dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                HotelOperations hotelOperation = Util.getClient().create(HotelOperations.class);
                Call<AgentHotel> response = hotelOperation.getAgentHotelByHotelId(auth_string,hotelId);

                response.enqueue(new Callback<AgentHotel>() {
                    @Override
                    public void onResponse(Call<AgentHotel> call, Response<AgentHotel> response) {
                        System.out.println("GetHotelByProfileId = "+response.code());
                        hotelDetailseResponse = response.body();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        if(response.code() == 200)
                        {
                            if(hotelDetailseResponse != null )
                            {
                                mHotelName.setText(hotelDetailseResponse.getHotelDisplayName());

                                if(hotelDetailseResponse.getMaps()==null||hotelDetailseResponse.getMaps().size()==0){
                                    mHotelAdd.setText("No Location found for this hotel");
                                }else{
                                    mHotelAdd.setText(hotelDetailseResponse.getMaps().get(0).getLocation());
                                }



                            }
                            else
                            {
                            }
                        }
                        else
                        {
                            Toast.makeText(ReviewHotelDetailsActivity.this,"Check your internet connection or please try after some time",
                                    Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<AgentHotel> call, Throwable t) {
                        System.out.println("Failed");

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(ReviewHotelDetailsActivity.this,"Check your internet connection or please try after some time",
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }


    public void calculateAmount(final double wallet)
    {
        //int totalamount =

        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EE, dd MMM");

            Date fdate = simpleDateFormat.parse(checkinDate);
            Date tdate = simpleDateFormat.parse(checkoutDate);
            mCID.setText(simpleDateFormat1.format(fdate));
            mCOD.setText(simpleDateFormat1.format(tdate));


            int days = getDays();
            mNoOfNights.setText(days+" Nights");

            int totalbaseprice = 0;
            int totalprice = 0;
            int GST =0,GST1 = 0,roomPrice = 0;
            String[] s = room.split(",");

            if(s[0] != null && !s[0].isEmpty()) {
                totalbaseprice = price*days;
                GST = getGstPrice(price)*Integer.parseInt(s[0])*days;
                roomPrice = price*Integer.parseInt(s[0])*days;
                GST1 = getGstPrice(price);
                totalprice = (totalbaseprice+GST1)*Integer.parseInt(s[0]);
            }
            else
            {
                totalbaseprice = price*days;
                roomPrice = price*days;
                GST = getGstPrice(price);
                totalprice = totalbaseprice+GST;
            }
            //int totalbaseprice = price*days*rooms;




            mHotelGst.setText("₹ "+GST);
            mHotelRate.setText("₹ "+price);
            mHotelDisplayRate.setText("₹ "+displayprice);
            mHotelTotalCharges.setText("₹ "+(totalprice+wallet));

            /*agentcommission = (int)((totalprice *25)/100);
            mAgentErnings.setText("₹ "+agentcommission);*/

           if(PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
           {
               mEarningParent.setVisibility(View.VISIBLE);
               if(PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getCommissionAmount() != 0)
               {
                   //
                   commissionAmt = PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getCommissionAmount();
                   mAgentErnings.setText("₹ "+PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getCommissionAmount());
               }
               else if(PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getCommissionPercentage() != 0)
               {
                   double agentcommission = ((price *PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getCommissionPercentage())/100);

                   commissionAmt = (long)agentcommission;
                   mAgentErnings.setText("₹ "+agentcommission);
               }
               else
               {
                   mAgentErnings.setText("₹ "+0);
               }
           }
           else
           {
               mEarningParent.setVisibility(View.GONE);
           }

        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
    }


    public int getGstPrice(int sellRate)
    {
        double gstamount = 0;
        if(sellRate <= 999.99)
        {
            //System.out.println("0%");

            gstamount = (sellRate * 0)/100;

        }
        else if(sellRate >= 1000 && sellRate <= 2499.99)
        {

            gstamount = (sellRate * 12)/100;

            //System.out.println("12%");
        }
        else if(sellRate >= 2500 && sellRate <= 7499.99)
        {

             gstamount = (sellRate * 18)/100;

            //System.out.println("18%");
        }
        else if(sellRate >= 7500)
        {

             gstamount = (sellRate * 28)/100;

            //System.out.println("28%");
        }

        return (int)Math.round(gstamount);
    }

    private int getDays() {
        //System.out.println("Text=="+checkinDate+" Date"+checkoutDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date fd = null,td = null;
        try {
            fd = sdf.parse(checkinDate);
            //System.out.println("Text=="+book_from_date.getText().toString()+" Date"+fd);
             td = sdf.parse(checkoutDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("From==="+fd+" To===="+td);
        try {
            long diff = td.getTime() - fd.getTime();
            int diffDays = (int) diff / (24 * 60 * 60 * 1000);
            System.out.println("Diff===" + diffDays);
            return diffDays;

        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        double amount=0;

        try {
            JSONObject options = new JSONObject();
            if(mHotelTotalCharges.getText().toString().contains(" ")){
                String total[] = mHotelTotalCharges.getText().toString().split(" ");
                amount = Double.parseDouble(total[1]);
            }else{
                amount = Double.parseDouble(mHotelTotalCharges.getText().toString());
            }
            //int amount = Integer.parseInt(mHotelTotalCharges.getText().toString());
            options.put("name", mHotelName.getText().toString() );
            options.put("description", "For  "+mGuestName.getText().toString());
            //You can omit the image option to fetch the image from dashboard
            //options.put("image", R.drawable.app_logo);
            //options.put("currency", "INR");
            options.put("amount",amount * 100);
            //options.put("amount",1 * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", mGuestEmail.getText().toString());
            preFill.put("contact",mGuestMobile.getText().toString() );

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }



    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

            //addPayment();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            /*if(travellerIid==0){
                addTraveler("paynow");
            }else{
                updateTraveller("paynow");
            }*/

            if(travellerIid==0){
                addTraveler("paynow");
                //System.out.println("Not exist "+isexist(tlist));
            }else if(tlist != null && !isexist(tlist)){
                //updateTraveller("paynow");
                //System.out.println("Not exist "+isexist(tlist));
                addTraveler("paylater");
            }
            else
            {
                booking("paynow");
            }

            //addTraveler();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    private void addTraveler(final String type){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Traveller dto = new Traveller();
        dto.setFirstName(mGuestName.getText().toString());
        dto.setPhoneNumber(mGuestMobile.getText().toString());
        dto.setEmail(mGuestEmail.getText().toString());

        if(mGuestCompany.getText().toString()==null||mGuestCompany.getText().toString().isEmpty()){

            dto.setCompany("");

        }else{
            dto.setCompany(""+mGuestCompany.getText().toString());
        }

        if(mGuestGST.getText().toString()==null||mGuestGST.getText().toString().isEmpty()){
            dto.setCustomerGST("");

        }else{
            dto.setCustomerGST(""+mGuestGST.getText().toString());
        }


        if(mMale.isChecked())
        {
            dto.setGender("Male");
        }
        else if(mFemale.isChecked())
        {
            dto.setGender("Female");
        }
        /*else if(mTransgender.isChecked())
        {
            dto.setGender("Transgender");
        }*/
        dto.setUserRoleId(6);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);
                TravellerApi apiService = Util.getClient().create(TravellerApi.class);
                Call<Traveller> call = apiService.addTraveler(auth_string,dto);

                call.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();

                            Traveller dto = response.body();
                            System.out.println("Response Traveller==="+response.body());

                            if (dto != null) {

                                travellerIid = dto.getTravellerId();
                                booking(type);

                            }



                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(ReviewHotelDetailsActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private void updateTraveller(final String type){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Traveller dto = traveller;

        dto.setFirstName(mGuestName.getText().toString());
        dto.setPhoneNumber(mGuestMobile.getText().toString());
        dto.setEmail(mGuestEmail.getText().toString());

        if(mGuestCompany.getText().toString()==null||mGuestCompany.getText().toString().isEmpty()){

            dto.setCompany("");

        }else{
            dto.setCompany(""+mGuestCompany.getText().toString());
        }

        if(mGuestGST.getText().toString()==null||mGuestGST.getText().toString().isEmpty()){
            dto.setCustomerGST("");

        }else{
            dto.setCustomerGST(""+mGuestGST.getText().toString());
        }
        if(mMale.isChecked())
        {
            dto.setGender("Male");
        }
        else if(mFemale.isChecked())
        {
            dto.setGender("Female");
        }
        /*else if(mTransgender.isChecked())
        {
            dto.setGender("Transgender");
        }*/
        dto.setUserRoleId(6);

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                TravellerApi apiService =
                        Util.getClient().create(TravellerApi.class);
                Call<Traveller> call = apiService.updateTravellerDetails(Util.getToken(ReviewHotelDetailsActivity.this),
                        travellerIid,dto);

                call.enqueue(new Callback<Traveller>() {
                    @Override
                    public void onResponse(Call<Traveller> call, Response<Traveller> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog!=null)
                                progressDialog.dismiss();

                            Traveller dto = response.body();
                            System.out.println("Response Traveller==="+response.body());

                            if (dto != null) {

                                booking(type);
                            }

                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(ReviewHotelDetailsActivity.this, " failed due to : "+response.message(), Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<Traveller> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }

    private void getTravelerByPhone(final String dto){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                TravellerApi apiService = Util.getClient().create(TravellerApi.class);
                Call<ArrayList<Traveller>> call = apiService.fetchTravelerByPhone(auth_string,dto);

                call.enqueue(new Callback<ArrayList<Traveller>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Traveller>> call, Response<ArrayList<Traveller>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (statusCode == 200 || statusCode == 201 || statusCode == 203 || statusCode == 204) {

                            if (progressDialog != null)
                                progressDialog.dismiss();


                            if (response.body().size() != 0) {
                                tlist = response.body();

                                AutocompleteCustomArrayAdapter autocompleteCustomArrayAdapter =
                                        new AutocompleteCustomArrayAdapter(ReviewHotelDetailsActivity.this,R.layout.hotels_row,tlist);
                                mGuestName.setThreshold(1);
                                mGuestName.setAdapter(autocompleteCustomArrayAdapter);
                                //setAdapter(tlist);
                                /*traveller = list.get(list.size() -1);

                                if (traveller != null) {
                                    travellerIid = traveller.getTravellerId();


                                    if(traveller.getCompany()!=null&&!traveller.getCompany().isEmpty()){
                                        mGuestCompany.setText(""+traveller.getCompany());
                                    }
                                    if(traveller.getCustomerGST()!=null&&!traveller.getCustomerGST().isEmpty()){
                                        mGuestGST.setText(""+traveller.getCustomerGST());
                                    }

                                    if(traveller.getFirstName() != null){
                                        mGuestName.setText(traveller.getFirstName());
                                    }
                                    if(traveller.getEmail() != null){
                                        mGuestEmail.setText(traveller.getEmail());
                                    }

                                    if(traveller.getGender() != null && !traveller.getGender().isEmpty())
                                    {
                                        if(traveller.getGender().equals("Male"))
                                        {
                                            mMale.setChecked(true);
                                        }
                                        else if(traveller.getGender().equals("Female"))
                                        {
                                            mFemale.setChecked(true);
                                        }
                                        *//*else if(traveller.getGender().equals("Transgender"))
                                        {
                                            mTransgender.setChecked(true);
                                        }*//*
                                    }
                                    mVerifyMobile.setVisibility(View.GONE);
                                    mOTPParent.setVisibility(View.GONE);
                                    mOtherParent.setVisibility(View.VISIBLE);
                                    mPayNow.setEnabled(true);
                                    mPayLater.setEnabled(true);

                                } else {
                                    travellerIid = 0;
                                    mGuestName.setText("");
                                    mGuestEmail.setText("");
                                    mGuestGST.setText("");
                                    mGuestCompany.setText("");
                                    mVerifyMobile.setVisibility(View.VISIBLE);
                                    mOTPParent.setVisibility(View.VISIBLE);
                                    mOtherParent.setVisibility(View.GONE);
                                    mPayNow.setEnabled(false);
                                    mPayLater.setEnabled(false);

                                }
*/                          /*ProfileAdapter adapter = new ProfileAdapter(ReviewHotelDetailsActivity.this,list);
                            mGuestName.setAdapter(adapter);*/

                            }else{
                                if(tlist != null)
                                {
                                    tlist.clear();
                                }
                                travellerIid = 0;
                                mGuestName.setEnabled(true);
                                mGuestMobile.setEnabled(true);
                                mGuestEmail.setEnabled(true);
                                mGuestCompany.setEnabled(true);
                                mGuestGST.setEnabled(true);

                                mVerifyMobile.setVisibility(View.VISIBLE);
                                mOTPParent.setVisibility(View.VISIBLE);
                                mOtherParent.setVisibility(View.GONE);
                                mPayNow.setEnabled(false);
                                mPayLater.setEnabled(false);
                            }
                        }else {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                Toast.makeText(ReviewHotelDetailsActivity.this, " failed due to : " + response.message(), Toast.LENGTH_SHORT).show();
                            }
//                callGetStartEnd();
                        }

                    @Override
                    public void onFailure(Call<ArrayList<Traveller>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }


        });
    }





    public void booking(String type){
        bookings = new Bookings1();

        try {
            String bookingnumber = randomByDate();
            bookings.setBookingNumber(bookingnumber);

            SimpleDateFormat sdfs = new SimpleDateFormat("E,d MMMM yyyy");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            bookings.setTravellerId(travellerIid);
            bookings.setCheckInDate(checkinDate);
            bookings.setOptCheckInDate(checkinDate);
            bookings.setCheckOutDate(checkoutDate);
            bookings.setOptCheckOutDate(checkoutDate);
            bookings.setCheckInTime(mCIT.getText().toString());
            bookings.setCheckOutTime(mCOT.getText().toString());
            bookings.setHotelId(hotelid);
            if(PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserRoleUniqueID().equalsIgnoreCase("Luci-Agent"))
            {
                bookings.setCommissionAmount((int)commissionAmt);
            }
            else
            {
                bookings.setCommissionAmount(0);
            }

            double diff = displayprice - price;
            double div = diff/displayprice;
            double disc = div*100;

            DecimalFormat numberFormat = new DecimalFormat("#.##");
            System.out.println("disc = "+disc);
            System.out.println("disc = "+(int)disc);
            System.out.println("disc = "+(displayprice ));
            System.out.println("disc = "+(price ));
            System.out.println("disc = "+(displayprice - price));
            bookings.setDiscount((int)disc);
            bookings.setDiscountAmount(displayprice - price);

            if(mNoAdult.getText().toString()!=null&&!mNoAdult.getText().toString().isEmpty()){
                if(mNoAdult.getText().toString().contains(" ")){
                    String adult[] = mNoAdult.getText().toString().split(" ");
                    bookings.setNoOfAdults(Integer.parseInt(adult[0]));
                }
            }

            if(mNoChild.getText().toString()!=null&&!mNoChild.getText().toString().isEmpty()){
                if(mNoChild.getText().toString().contains(" ")){
                    String child[] = mNoChild.getText().toString().split(" ");
                    bookings.setNoOfChild(Integer.parseInt(child[0]));
                }
            }

            if(mNoRooms.getText().toString()!=null&&!mNoRooms.getText().toString().isEmpty()){
                if(mNoRooms.getText().toString().contains(" ")){
                    String rooms[] = mNoRooms.getText().toString().split(" ");
                    bookings.setNoOfRooms(Integer.parseInt(rooms[0]));
                }
            }else{
                bookings.setNoOfRooms(1);
            }

            /*if(mRoomType.getText().toString()!=null&&!mRoomType.getText().toString().isEmpty()){
                bookings.setRoomCategory(mRoomType.getText().toString());
            }*/

            bookings.setRoomCategory("Deluxe");
            bookings.setDurationOfStay(getDays());
            bookings.setBookingStatus("Quick");
            bookings.setDeclaredRate(displayprice);

            if(price <= 999.99)
            {
                //System.out.println("0%");
                bookings.setGst(0);
                //gstamount = (sellRate * 0)/100;

            }
            else if(price >= 1000 && price <= 2499.99)
            {

                //gstamount = (sellRate * 12)/100;
                bookings.setGst(12);
                //System.out.println("12%");
            }
            else if(price >= 2500 && price <= 7499.99)
            {

                //gstamount = (sellRate * 18)/100;
                bookings.setGst(18);
                //System.out.println("18%");
            }
            else if(price >= 7500) {
                bookings.setGst(28);
                //gstamount = (sellRate * 28)/100;
            }
                //


            if(mHotelRate.getText().toString().contains(" ")){
                String sell[] = mHotelRate.getText().toString().split(" ");
                bookings.setSellRate((int) Double.parseDouble(sell[1]));
               // bookings.setDeclaredRate((int) Double.parseDouble(sell[1]));
            }else{
                bookings.setSellRate((int) Double.parseDouble(mHotelRate.getText().toString()));
                //bookings.setDeclaredRate((int) Double.parseDouble(mHotelRate.getText().toString()));
            }

            if(mHotelGst.getText().toString().contains(" ")){
                String gst[] = mHotelGst.getText().toString().split(" ");
                bookings.setGstAmount((int) Double.parseDouble(gst[1]));
            }else{
                bookings.setGstAmount((int) Double.parseDouble(mHotelGst.getText().toString()));
            }


       /* String [] arrOfStr = gst.split("%", 2);
        int value = Integer.parseInt(arrOfStr[0]);
        bookings.setGst(value);*/
            if(mHotelTotalCharges.getText().toString().contains(" ")){
                String total[] = mHotelTotalCharges.getText().toString().split(" ");
                bookings.setTotalAmount((int) Double.parseDouble(total[1]));
                if(type.equalsIgnoreCase("paylater")){
                    bookings.setBalanceAmount((int) Double.parseDouble(total[1]));
                }else{
                    bookings.setBalanceAmount(0);
                }
            }else{
                bookings.setTotalAmount((int) Double.parseDouble(mHotelTotalCharges.getText().toString()));
                if(type.equalsIgnoreCase("paylater")){
                    bookings.setBalanceAmount((int) Double.parseDouble(mHotelTotalCharges.getText().toString()));
                }else{
                    bookings.setBalanceAmount(0);
                }
            }

            // bookings.setBalanceAmount((int) Double.parseDouble(total));
            //bookings.setBookingPlan(mBookingPlan.getSelectedItem().toString());

            //Current time and date for booking

            long date = System.currentTimeMillis();

            //  SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String bookingDate = sdf.format(date);
            System.out.println("Booking Date===" + bookingDate);
            bookings.setBookingDate(bookingDate);

            SimpleDateFormat sdft = new SimpleDateFormat("hh:mm a");
            Date d = new Date();
            String time = sdft.format(d);

            bookings.setBookingTime(time);
            bookings.setTravellerAgentId(PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserId());
            if(book){
                updateRoomBooking(bookings,type);
                book = false;
            }else{
                Toast.makeText(ReviewHotelDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Booking",bookings);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(" Error: "+e.getMessage());
        }
    }


    public void updateRoomBooking(final Bookings1 booking,final String type){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

//        travellersList.add(dto);
        // bookings = new Bookings1();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                BookingApi bookingApi = Util.getClient().create(BookingApi.class);
                String auth = Util.getToken(ReviewHotelDetailsActivity.this);
                Call<Bookings1> bookingresponse = bookingApi.postBooking(auth,booking);

                bookingresponse.enqueue(new Callback<Bookings1>() {
                    @Override
                    public void onResponse(Call<Bookings1> call, Response<Bookings1> response) {

                        if(response.code() == 200 || response.code() == 201)
                        {
                            if(progressDialog != null)
                            {
                                progressDialog.dismiss();
                            }

                            if(response.body() != null)
                            {
                                bookings = response.body();

                                if(type.equalsIgnoreCase("paylater")){
                                    HotelNotification notify = new HotelNotification();
                                    notify.setHotelId(bookings.getHotelId());
                                    //System.out.println("roomids = "+roomids);
                                    // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                                    notify.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                            " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                                    notify.setTitle("New Booking from Zingo Hotels");
                                    notify.setSenderId(Constants.senderId);
                                    notify.setServerId(Constants.serverId);
                                    sendfirebaseNotification(notify,bookings);

                                    HotelNotification notifyprofile = new HotelNotification();
                                    notifyprofile.setProfileId(107);
                                    //System.out.println("roomids = "+roomids);
                                    // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                                    notifyprofile.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                            " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                                    notifyprofile.setTitle("New Booking from Zingo Hotels");
                                    notifyprofile.setSenderId(Constants.bill_senderId);
                                    notifyprofile.setServerId(Constants.bill_serverID);
                                    sendNotificationByProfileId(notifyprofile,bookings);

                                }else{
                                    addPayment();
                                }






                            }
                        }
                        else
                        {
                            if(progressDialog != null)
                            {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(ReviewHotelDetailsActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Bookings1> call, Throwable throwable) {
                        if(progressDialog != null)
                        {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ReviewHotelDetailsActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



    }


    public void addPayment(){
       /* String paymentName = "Room Payment";
     
      
        String paymentPrice = mAmount.getText().toString();
        String paymentMode = "Online";
        
        */
       final Payment payment = new Payment();
        if(mHotelTotalCharges.getText().toString().contains(" ")){
            String total[] = mHotelTotalCharges.getText().toString().split(" ");
            payment.setAmount((int) Double.parseDouble(total[1]));
          
        }else{
            payment.setAmount((int) Double.parseDouble(mHotelTotalCharges.getText().toString()));
         
        }
        payment.setPaymentName("Room Payment");
        payment.setPaymentType("Online");

       
            payment.setBookingId(bookings.getBookingId());
        SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        payment.setPaymentDate(date.format(new Date()));
        final ProgressDialog dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {

                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                PaymentApi paymentApi = Util.getClient().create(PaymentApi.class);

                Call<Payment> response = paymentApi.addPayment(auth_string,payment);
                response.enqueue(new Callback<Payment>() {
                    @Override
                    public void onResponse(Call<Payment> call, Response<Payment> response) {

                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                        Payment serviceResponse = response.body();

                        if(response.code() == 200 && serviceResponse != null)
                        {

                            //Toast.makeText(ReviewHotelDetailsActivity.this,"Payment added successfully",Toast.LENGTH_LONG).show();
                           HotelNotification notify = new HotelNotification();
                            notify.setHotelId(bookings.getHotelId());
                            //System.out.println("roomids = "+roomids);
                           // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                           notify.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                    " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                            notify.setTitle("New Booking from Zingo Hotels");
                            notify.setSenderId(Constants.senderId);
                            notify.setServerId(Constants.serverId);
                            sendfirebaseNotification(notify,bookings);

                            HotelNotification notifyprofile = new HotelNotification();
                            notifyprofile.setProfileId(107);
                            //System.out.println("roomids = "+roomids);
                            // notify.setMessage(bookings.getProfileId()+","+bookings.getCheckInDate()+","+bookings.getCheckOutDate()+","+bookings.getTotalAmount()+","+room);
                            notifyprofile.setMessage("Congrats! "+hotelDetailseResponse.getHotelDisplayName()+" got one new booking for "+getDays() +" nights from "+bookings.getCheckInDate()+
                                    " to "+bookings.getCheckOutDate()+"\nBooking Number:"+bookings.getBookingNumber()+","+bookings.getProfileId());
                            notifyprofile.setTitle("New Booking from Zingo Hotels");
                            notifyprofile.setSenderId(Constants.bill_senderId);
                            notifyprofile.setServerId(Constants.bill_serverID);
                            sendNotificationByProfileId(notifyprofile,bookings);

                        }
                        else {

                            Toast.makeText(ReviewHotelDetailsActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Payment> call, Throwable t) {
                        System.out.println("onFailure");
                        Toast.makeText(ReviewHotelDetailsActivity.this,"Please try after some time",Toast.LENGTH_SHORT).show();
                        if(dialog != null && dialog.isShowing())
                        {
                            dialog.dismiss();
                        }
                    }
                });

            }
        });
        
       
       
    }

    private void sendNotificationByProfileId(final HotelNotification notification, final Bookings1 bookings1) {

        /*final ProgressDialog dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();*/

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                Call<ArrayList<String>> response = travellerApi.sendnotificationToProfile(auth_string,notification);

                response.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        System.out.println(response.code());
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Booking",bookings1);
                                intent.putExtras(bundle);
                                startActivity(intent);*/

                                /*NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(notification.getTitle());
                                nf.setNotificationFor(notification.getMessage());
                                nf.setHotelId(notification.getHotelId());
                                savenotification(nf,bookings1);*/
                                Log.d("Notification","Sent to bill");


                            }
                        }
                        else
                        {
                            Log.d("Notification","not Sent to bill");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Notification sent failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void sendfirebaseNotification(final HotelNotification notification, final Bookings1 bookings1) {

        final ProgressDialog dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);//"Basic " +  Base64.encodeToString(authentication.getBytes(), Base64.NO_WRAP);
                NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                Call<ArrayList<String>> response = travellerApi.sendnotificationToHotel(auth_string,notification);

                response.enqueue(new Callback<ArrayList<String>>() {
                    @Override
                    public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        if(response.code() == 200)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Booking",bookings1);
                                intent.putExtras(bundle);
                                startActivity(intent);*/

                                NotificationManager nf = new NotificationManager();
                                nf.setNotificationText(notification.getTitle());
                                nf.setNotificationFor(notification.getMessage());
                                nf.setHotelId(notification.getHotelId());
                                savenotification(nf,bookings1);


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(ReviewHotelDetailsActivity.this, "Notification sent failed due to "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void savenotification(final NotificationManager notification, final Bookings1 bookings1) {

        final ProgressDialog dialog = new ProgressDialog(ReviewHotelDetailsActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(false);
        dialog.show();

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hotel id = "+notification.getHotelId());
                String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);
                NotificationApi travellerApi = Util.getClient().create(NotificationApi.class);
                Call<NotificationManager> response = travellerApi.saveNotification(auth_string,notification);

                response.enqueue(new Callback<NotificationManager>() {
                    @Override
                    public void onResponse(Call<NotificationManager> call, Response<NotificationManager> response) {

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());
                        if(response.code() == 200||response.code() == 201)
                        {
                            if(response.body() != null)
                            {
                                /*Toast.makeText(ReviewHotelDetailsActivity.this,"Thank you for selecting room. Your request has been sent to hotel. " +
                                        "Please wait for there reply.",Toast.LENGTH_SHORT).show();*/
                                //SelectRoom.this.finish();

                                if(mZingoCash.isChecked()){
                                    updateProfileOther(amount,bookings1);
                                }else {
                                    Toast.makeText(ReviewHotelDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("Booking",bookings1);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }


                                //Toast.makeText(ReviewHotelDetailsActivity.this, "Save Notification", Toast.LENGTH_SHORT).show();




                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationManager> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }


    //Generate BookingNumber
    public String randomByDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date random = new Date();
        String bookNumber = dateFormat.format(random);
        System.out.println(""+bookNumber);
        return ""+bookNumber;
    }

    public int dateCal(){

        SimpleDateFormat sdf = new SimpleDateFormat("E,d MMMM yyyy");
        SimpleDateFormat sdfs = new SimpleDateFormat("MM/dd/yyyy");

        Date fd=null,td=null;
        long diffDays=0;

        try {
            fd = sdf.parse(mCID.getText().toString()+" "+ Calendar.getInstance().get(Calendar.YEAR));
            String from = sdfs.format(fd);
            String to = sdfs.format(sdf.parse(mCID.getText().toString()+" "+ Calendar.getInstance().get(Calendar.YEAR)));
            System.out.println("Ffff="+from+"tttt"+to);
            td = sdf.parse(mCOD.getText().toString()+" "+ Calendar.getInstance().get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("From==="+fd+" To===="+td);
        try {
            long diff = td.getTime() - fd.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println("Diff===" + diffDays);
        }catch(Exception e){
            e.printStackTrace();
        }

        return ((int) diffDays);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getProfile() {

       /* final ProgressDialog dialog = new ProgressDialog(ComingSoonActivity.this);
        dialog.setTitle("Loading");
        dialog.setCancelable(false);
        dialog.show();
*/
        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                LoginApi profileApi = Util.getClient().create(LoginApi.class);
                String authenticationString = Util.getToken(ReviewHotelDetailsActivity.this);
                Call<TravellerAgentProfiles> getProfile = profileApi.getProfileByID(authenticationString, PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserId());
                //System.out.println("hotelid = "+hotelid);
                System.out.println();

                getProfile.enqueue(new Callback<TravellerAgentProfiles>() {
                    @Override
                    public void onResponse(Call<TravellerAgentProfiles> call, Response<TravellerAgentProfiles> response) {
                       /* if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                        if(response.code() == 200)
                        {
                           // profiles = response.body();

                            if(response.body() != null)
                            {

                                profiles = response.body();

                                walletAmount = response.body().getWalletBalance();

                                String userRoleId = PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserRoleUniqueID();
                                if(userRoleId.equalsIgnoreCase("Luci-Agent")){
                                    double percent  = walletAmount * 40;
                                    double amount = percent/100;
                                    //calculateAmount(-amount);
                                    mZingoWallet.setText("₹ "+amount);
                                }else{
                                    double percent  = walletAmount * 20;
                                    double amount = percent/100;
                                    //calculateAmount(-amount);
                                    mZingoWallet.setText("₹ "+amount);
                                }



                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TravellerAgentProfiles> call, Throwable t) {
                        /*if(dialog != null)
                        {
                            dialog.dismiss();
                        }*/
                    }
                });
            }
        });
    }

    private void updateProfileOther(final double wallet,final Bookings1 bookings1) {

        /*final ProgressDialog dialog = new ProgressDialog(ComingSoonActivity.this);
        dialog.setMessage(getResources().getString(R.string.loader_message));
        dialog.setCancelable(true);
        dialog.show();*/

        TravellerAgentProfiles dto = profiles;

        double walletBalance = walletAmount - wallet;
        dto.setWalletBalance((int) walletBalance);
        dto.setUsedAmount((int) amount);

        String auth_string = Util.getToken(ReviewHotelDetailsActivity.this);

        LoginApi profileApi = Util.getClient().create(LoginApi.class);
        Call<String> res = profileApi.updateProfileById(auth_string,PreferenceHandler.getInstance(ReviewHotelDetailsActivity.this).getUserId(),dto);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

               /* if(dialog != null)
                {
                    dialog.dismiss();
                }*/
                if(response.code() == 204 ||response.code() == 200 ||response.code() == 201)
                {
                    Toast.makeText(ReviewHotelDetailsActivity.this,"Booking done successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReviewHotelDetailsActivity.this,BookingDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Booking",bookings1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
               /* if(dialog != null)
                {
                    dialog.dismiss();
                }*/
                //Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}
