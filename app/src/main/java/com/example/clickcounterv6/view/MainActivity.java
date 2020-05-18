package com.example.clickcounterv6.view;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.example.clickcounterv6.dbl.ProfileDBO;
import com.example.clickcounterv6.model.Profile;
import com.example.clickcounterv6.R;
import com.example.clickcounterv6.databinding.ActivityMainBinding;
import com.example.clickcounterv6.vm.MyApplication;
import com.example.clickcounterv6.vm.ProfileViewModel;

import java.util.Objects;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {
    EditText txtFname , txtLname;

    //ProfileDBO profileDBHelper;
    private ProfileViewModel profileViewModel;
    private ActivityMainBinding activityMainBinding ;
    ProfileDBO profileDBHelper = new ProfileDBO(MyApplication.getContext());
    String strTelNo = "";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String telNo = "telNoKey";
    SharedPreferences sharedpreferences;
    Intent myIntent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Full Screen Window
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

//        if(profileDBHelper.isUserExist())
//        {
//            Intent myIntent = new Intent(this, TabMainActivity.class);
//            startActivity(myIntent);
//        }

        Boolean flag;
        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        flag = sharedpreferences.getBoolean("FirstLaunch", true);

        if(flag){
            savePreferences();
        }else {
            myIntent = new Intent(this, TabMainActivity.class);
            startActivity(myIntent);
            finish();
        }

        getPhoneNo();

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        activityMainBinding.setLifecycleOwner(this);

        activityMainBinding.setProfileViewModel(profileViewModel);

        profileViewModel.getProfile().observe(this, new Observer<Profile>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(@Nullable Profile profile) {
                if (TextUtils.isEmpty(Objects.requireNonNull(profile).getStrFname())) {
                    activityMainBinding.txtFName.setError("Enter First Name");
                    activityMainBinding.txtFName.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(profile).getStrLname())) {
                    activityMainBinding.txtLName.setError("Enter Last Name");
                    activityMainBinding.txtLName.requestFocus();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPhoneNo()
    {
        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            strTelNo = tMgr.getLine1Number();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(telNo, strTelNo);
            editor.commit();

            return;
        } else {
            requestPermission();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                strTelNo = mPhoneNumber;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(telNo, strTelNo);
                editor.commit();

                break;
        }
    }

    public long saveData(String fName,String lName)
    {
        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        String strTelNo = (sharedpreferences.getString(telNo, ""));

        long profileId = profileDBHelper.insertProfile(fName, lName,strTelNo);
        return profileId;
    }

    private void savePreferences() {
        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("FirstLaunch", false);
        editor.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        moveTaskToBack(true);
        finish();
    }
}

//https://stackoverflow.com/questions/21619702/android-how-to-skip-first-activity
//https://tausiq.wordpress.com/2014/06/06/android-multiple-fragments-stack-in-each-viewpager-tab/
//http://www.lucazanini.eu/en/2013/android/replacing-fragment-tab-layout-actionbar/
//https://www.tutorialspoint.com/how-to-get-phone-number-in-android
//https://www.tutorialspoint.com/how-to-create-a-tab-layout-in-android-app
//https://www.journaldev.com/20292/android-mvvm-design-pattern
//https://android--code.blogspot.com/2015/12/android-how-to-set-cardview-elevation.html
//https://www.tutorialspoint.com/android/android_session_management.htm
//https://stackoverflow.com/questions/8585712/how-to-delete-sqlite-database-of-my-application-in-android-mobile-manually/8585802
//https://stackoverflow.com/questions/37093723/how-to-add-an-android-studio-project-to-github