package com.example.clickcounterv6.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.clickcounterv6.R;
import com.example.clickcounterv6.dbl.CmsDBO;
import com.example.clickcounterv6.vm.MyApplication;

import java.util.HashMap;


public class DisplayProfile  extends Fragment {
    TextView txtFnameDB,txtLnameDB,txtTelNoDB;
    TextView txtFname,txtLname,txtTelNo;
    String mPhoneNumber;
    View view;

    CmsDBO cmsDBHelper = new CmsDBO(MyApplication.getContext());
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

        //Code to SetText of Controls
        txtFname = (TextView) view.findViewById(R.id.txtFNameDP);
        txtLname = (TextView) view.findViewById(R.id.txtLNameDP);
        txtTelNo = (TextView) view.findViewById(R.id.txtTelNoDP);

        HashMap<String, String> cmshmap = cmsDBHelper.getAllCMSPageData("profilepage");

        txtFname.setHint(cmshmap.get("key_txtFNameDP"));
        txtLname.setHint(cmshmap.get("key_txtLNameDP"));
        txtTelNo.setHint(cmshmap.get("key_txtTelNoDP"));


        txtFnameDB = (TextView) view.findViewById(R.id.txtFNameDBDP);
        txtLnameDB = (TextView) view.findViewById(R.id.txtLNameDBDP);
        txtTelNoDB = (TextView) view.findViewById(R.id.txtTelNoDBDP);

        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        String firstName = (sharedpreferences.getString(profileFname, ""));
        String lastName = (sharedpreferences.getString(profileLname, ""));
        String telNo = (sharedpreferences.getString(profileTelNo, "No Sim"));

        txtFnameDB.setText(firstName);
        txtLnameDB.setText(lastName);
        txtTelNoDB.setText(telNo);

        return view;
    }
}
