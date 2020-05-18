package com.example.clickcounterv6.vm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import com.example.clickcounterv6.R;
import com.example.clickcounterv6.dbl.ProfileDBO;
import com.example.clickcounterv6.model.Profile;
import com.example.clickcounterv6.view.MainActivity;
import com.example.clickcounterv6.view.TabMainActivity;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
;

public class ProfileViewModel extends ViewModel {
    public MutableLiveData<String> FirstName = new MutableLiveData<>();
    public MutableLiveData<String> LastName = new MutableLiveData<>();
    private MutableLiveData<Profile> profileMutableLiveData;

    ProfileDBO profileDBHelper = new ProfileDBO(MyApplication.getContext());
    MainActivity mn = new MainActivity();

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String profId = "idKey";
    public static final String profileFname = "fnameKey";
    public static final String profileLname = "lnameKey";
    SharedPreferences sharedpreferences;

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    public MutableLiveData<Profile> getProfile() {

        if (profileMutableLiveData == null) {
            profileMutableLiveData = new MutableLiveData<>();
        }
        return profileMutableLiveData;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View view) {
        Profile profile = new Profile(FirstName.getValue(), LastName.getValue());
        profileMutableLiveData.setValue(profile);

        if (FirstName.getValue() != null && !FirstName.getValue().trim().equals("") && LastName.getValue() != null && !LastName.getValue().trim().equals("")) {
            if (view.getId() == R.id.btnNext) {
                String fName = FirstName.getValue().toUpperCase();
                String lName = LastName.getValue().toUpperCase();

                long profileId = mn.saveData(fName, lName);

                sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(profId, String.valueOf(profileId));
                editor.putString(profileFname, fName);
                editor.putString(profileLname, lName);
                editor.commit();

                Intent myIntent = new Intent(view.getContext(), TabMainActivity.class);
                view.getContext().startActivity(myIntent);
            }
        }
    }
}


