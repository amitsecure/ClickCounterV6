package com.example.clickcounterv6.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.clickcounterv6.R;
import com.example.clickcounterv6.vm.MyApplication;


public class DisplayProfile  extends Fragment {
    TextView txtFname,txtLname,txtTelNo;
    String mPhoneNumber;
    View view;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String profId = "idKey";
    public static final String profileFname = "fnameKey";
    public static final String profileLname = "lnameKey";
    public static final String profileTelNo = "telNoKey";
    SharedPreferences sharedpreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_display_profile, container, false);

        txtFname = (TextView) view.findViewById(R.id.txtFNameDP);
        txtLname = (TextView) view.findViewById(R.id.txtLNameDP);
        txtTelNo= (TextView) view.findViewById(R.id.txtTelNoDP);

        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        String firstName = (sharedpreferences.getString(profileFname, ""));
        String lastName = (sharedpreferences.getString(profileLname, ""));
        String telNo = (sharedpreferences.getString(profileTelNo, ""));

        txtFname.setText(firstName);
        txtLname.setText(lastName);
        txtTelNo.setText(telNo);

        return view;
    }
}
