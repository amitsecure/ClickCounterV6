package com.example.clickcounterv6.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.clickcounterv6.R;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;


public class CustomTitleBar extends Fragment implements View.OnClickListener {
    TextView txtPhone;
    String mPhoneNumber;
    View view;
    Button btnExit;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_custom_title_bar, container, false);

        btnExit = (Button) view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(this);

        txtPhone = (TextView) view.findViewById(R.id.phoneNumber);
        txtPhone.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        if (ActivityCompat.checkSelfPermission(getContext(), READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();

            System.out.println("tag = " + mPhoneNumber);

            if (mPhoneNumber.equals(null))
                txtPhone.setText("No SIM CARD");
            else
                txtPhone.setText("Cell : " + mPhoneNumber);
        } else {
            requestPermission();
        }
        return view;
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
                TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();

                System.out.println("tag"+mPhoneNumber);

                if (mPhoneNumber!=null && !mPhoneNumber.trim().isEmpty())
                    txtPhone.setText("Cell : " + mPhoneNumber);
                else
                    txtPhone.setText("No SIM CARD");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExit)
        {
            getActivity().moveTaskToBack(true);
            getActivity().finish();
        }
    }
}
