package com.example.clickcounterv6.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clickcounterv6.R;
import com.example.clickcounterv6.dbl.TopicDBO;
import com.example.clickcounterv6.vm.MyApplication;
import com.example.clickcounterv6.vm.MyListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TopicList extends Fragment {
    ListView topiclistView;
    TextView textView,txtSelectedTopic,txtCount ,txtTopic;
    String[] topicArray;
    ArrayList<String> topicList = new ArrayList<String>();
    TopicDBO topicDBHelper;
    View rootView;
    int userId,n;
    Button btnIncCounter,btnResetCounter;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String profId = "idKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_topic_list, container, false);

        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        userId = Integer. parseInt((sharedpreferences.getString(profId, "")));

        topicDBHelper = new TopicDBO(MyApplication.getContext());
        getData();

        //Redirect from List Topic to Add Topic
        final Button btnAddTopic=rootView.findViewById(R.id.btnAddTopic);

        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutVisibility(2);
            }
        });

        //Save Topic logic
        final Button btnSave=rootView.findViewById(R.id.btnSaveTopic);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTopic = (TextView) rootView.findViewById(R.id.txtTopic);
                String strTopicName = txtTopic.getText().toString();

                if(TextUtils.isEmpty(strTopicName)) {
                    txtTopic.setError("Enter Topic");
                    txtTopic.requestFocus();
                    return;
                }

                boolean output = saveTopic();
                if(output)
                {
                    txtTopic.setHint("Enter Topic");
                    getData();
                    layoutVisibility(1);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"Topic Alread Exist",Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button btnIncrTopic=rootView.findViewById(R.id.btnIncreCounter);
        btnIncrTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCount=(TextView)rootView.findViewById(R.id.txtCount);
                n = Integer.parseInt(txtCount.getText().toString());

                txtSelectedTopic=(TextView)rootView.findViewById(R.id.txtSelectedTopic);
                String currentTopic = (txtSelectedTopic.getText().toString());

                topicDBHelper.updateTopic(currentTopic,n+1,userId);
                txtCount.setText(String.valueOf(n+1));
            }
        });

        final Button btnResetCounter=rootView.findViewById(R.id.btnResetCounter);
        btnResetCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCount.setText("0");

                txtSelectedTopic=(TextView)rootView.findViewById(R.id.txtSelectedTopic);
                String currentTopic = (txtSelectedTopic.getText().toString());

                topicDBHelper.updateTopic(currentTopic,0,userId);
            }
        });
        final Button btnGoToList=rootView.findViewById(R.id.btnGoToList);
        btnGoToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                layoutVisibility(1);
            }
        });

        final Button btnCancel=rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                layoutVisibility(1);
            }
        });


        final Button btnExit=rootView.findViewById(R.id.btnExitApp);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().moveTaskToBack(true);
                getActivity().finish();
            }
        });

        return rootView;
    }


    public void getData(){
        HashMap<String,String> topicCountMap = new HashMap<>();
        int i=0;

        topicCountMap = topicDBHelper.getAllTopics(userId);

        final String[] mainTopics = new String[topicCountMap.size()];
        final String[] topicCounts = new String[topicCountMap.size()];

        for (Map.Entry mapElement : topicCountMap.entrySet()) {
            String key = (String)mapElement.getKey();
            String value = (String)mapElement.getValue();

            mainTopics[i] = key;
            topicCounts[i] = value;
            i++;
        }

        final MyListAdapter topicAdapter=new MyListAdapter(getActivity(), mainTopics, topicCounts);
        topiclistView=(ListView) rootView.findViewById(R.id.topicListView);
        topiclistView.setAdapter(topicAdapter);

        topiclistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value=mainTopics[position];
                layoutVisibility(3);

                String topicCount = topicCounts[position];

                txtSelectedTopic=(TextView)rootView.findViewById(R.id.txtSelectedTopic);
                txtSelectedTopic.setText(value);

                txtCount=(TextView)rootView.findViewById(R.id.txtCount);
                txtCount.setText(topicCount);
            }
        });
    }

    public void layoutVisibility(int n){
        final LinearLayout addTopicLayout,listTopicLayout,countTopicLayout;
        switch (n)
        {
            case 1:
                listTopicLayout=rootView.findViewById(R.id.listTopicLayout);
                listTopicLayout.setVisibility(View.VISIBLE);
                addTopicLayout=rootView.findViewById(R.id.addTopicLayout);
                addTopicLayout.setVisibility(View.INVISIBLE);
                countTopicLayout=rootView.findViewById(R.id.countTopicLayout);
                countTopicLayout.setVisibility(View.INVISIBLE);
                break;
            case 2:
                listTopicLayout=rootView.findViewById(R.id.listTopicLayout);
                listTopicLayout.setVisibility(View.INVISIBLE);
                addTopicLayout=rootView.findViewById(R.id.addTopicLayout);
                addTopicLayout.setVisibility(View.VISIBLE);
                countTopicLayout=rootView.findViewById(R.id.countTopicLayout);
                countTopicLayout.setVisibility(View.INVISIBLE);
                break;
            case 3:
                listTopicLayout=rootView.findViewById(R.id.listTopicLayout);
                listTopicLayout.setVisibility(View.INVISIBLE);
                addTopicLayout=rootView.findViewById(R.id.addTopicLayout);
                addTopicLayout.setVisibility(View.INVISIBLE);
                countTopicLayout=rootView.findViewById(R.id.countTopicLayout);
                countTopicLayout.setVisibility(View.VISIBLE);
                break;
            default:
        }

    }

    public boolean saveTopic() {
        boolean output = false;
        try {
            txtTopic = (TextView) rootView.findViewById(R.id.txtTopic);
            String tName = txtTopic.getText().toString();

            output = topicDBHelper.insertTopic(tName.toUpperCase(),0 ,userId);
            txtTopic.setText("");

        } catch (Exception ex) {
        }
        return output;
    }
}
