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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clickcounterv6.R;
import com.example.clickcounterv6.dbl.CmsDBO;
import com.example.clickcounterv6.dbl.TopicDBO;
import com.example.clickcounterv6.vm.MyApplication;
import com.example.clickcounterv6.vm.MyListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TopicList extends Fragment {
    ListView topiclistView;
    TextView txtSelectedTopic,txtCount;
    EditText txtTopic;
    TopicDBO topicDBHelper;
    View rootView;

    int userId,n;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String profId = "idKey";
    CmsDBO cmsDBHelper = new CmsDBO(MyApplication.getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_topic_list, container, false);

        final FloatingActionButton btnAddTopic=rootView.findViewById(R.id.btnAddTopic);
       // final Button btnExit=rootView.findViewById(R.id.btnExitApp);


        final Button btnSave=rootView.findViewById(R.id.btnSaveTopicTP);
        final Button btnIncrCounter=rootView.findViewById(R.id.btnIncreCounterTP);
        final Button btnResetCounter=rootView.findViewById(R.id.btnResetCounterTP);
        final Button btnGoToListCT =rootView.findViewById(R.id.btnGoToListCTTP);
        final Button btnGoToListAT =rootView.findViewById(R.id.btnGoToListATTP);
        txtTopic = (EditText) rootView.findViewById(R.id.txtTopicTP);
        txtCount=(TextView)rootView.findViewById(R.id.txtCountTP);
        txtSelectedTopic=(TextView)rootView.findViewById(R.id.txtSelectedTopicTP);

        HashMap<String, String> cmshmap = cmsDBHelper.getAllCMSPageData("topicpage");

        btnSave.setText(cmshmap.get("key_btnSaveTopicTP"));
        btnIncrCounter.setText(cmshmap.get("key_btnIncreCounterTP"));
        btnResetCounter.setText(cmshmap.get("key_btnResetTP"));
        btnGoToListCT.setText(cmshmap.get("key_btnGoToListTP"));
        btnGoToListAT.setText(cmshmap.get("key_btnGoToListTP"));
        txtTopic.setHint(cmshmap.get("key_txtTopicTP"));

        sharedpreferences = MyApplication.getContext().getSharedPreferences(MyPREFERENCES, MyApplication.getContext().MODE_PRIVATE);
        userId = Integer. parseInt((sharedpreferences.getString(profId, "0")));

        topicDBHelper = new TopicDBO(MyApplication.getContext());
        getData();

        //Redirect from List Topic to Add Topic
        //final Button btnAddTopic=rootView.findViewById(R.id.btnAddTopic);
        btnAddTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutVisibility(2);
            }
        });

        //Save Topic logic
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTopic = txtTopic.getText().toString();

                if(TextUtils.isEmpty(strTopic)) {
                    txtTopic.setError("Enter Topic");
                    txtTopic.requestFocus();
                    return;
                }

                boolean output = saveTopic(strTopic);
                txtTopic.setText("");
                txtTopic.setError(null);

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


        btnIncrCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n = Integer.parseInt(txtCount.getText().toString());
                String currentTopic = (txtSelectedTopic.getText().toString());

                topicDBHelper.updateTopic(currentTopic,n+1,userId);
                txtCount.setText(String.valueOf(n+1));
            }
        });

        btnResetCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCount.setText("0");

                txtSelectedTopic=(TextView)rootView.findViewById(R.id.txtSelectedTopicTP);
                String currentTopic = (txtSelectedTopic.getText().toString());

                topicDBHelper.updateTopic(currentTopic,0,userId);
            }
        });

        btnGoToListCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                layoutVisibility(1);
            }
        });

        btnGoToListAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTopic.setText("");
                txtTopic.setError(null);
                getData();
                layoutVisibility(1);
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

        Map<String, String> map = new TreeMap<String, String>(topicCountMap);
        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext()) {
            Map.Entry me2 = (Map.Entry)iterator2.next();

            String key = (String)me2.getKey();
            String value = (String)me2.getValue();

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

                txtSelectedTopic=(TextView)rootView.findViewById(R.id.txtSelectedTopicTP);
                txtSelectedTopic.setText(value);

                txtCount=(TextView)rootView.findViewById(R.id.txtCountTP);
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

    public boolean saveTopic(String strTopic) {
        boolean output = false;
        try {
            output = topicDBHelper.insertTopic(strTopic.toUpperCase(),0 ,userId);

        } catch (Exception ex) {
        }
        return output;
    }
}
