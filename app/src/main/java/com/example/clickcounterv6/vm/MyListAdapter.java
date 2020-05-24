package com.example.clickcounterv6.vm;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.clickcounterv6.R;

public class MyListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] Topics;
    private final String[] Counts;
    public String newTopic;

    public MyListAdapter(Activity context, String[] topics,String[] counts) {
        super(context, R.layout.mylist, topics);

        this.context=context;
        this.Topics=topics;
        this.Counts=counts;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView topicTextInit = (TextView) rowView.findViewById(R.id.my_letter);
        TextView topicText = (TextView) rowView.findViewById(R.id.txtArrayTopic);
        TextView countText = (TextView) rowView.findViewById(R.id.txtArrayCount);

        topicTextInit.setText(String.valueOf(Topics[position].charAt(0)));

        if(Topics[position].length()>6)
        {
            newTopic =Topics[position].substring(0,6) + "..";
        }
        else
        {
            newTopic =Topics[position];
        }
        topicText.setText(newTopic);
        countText.setText(Counts[position]);

        return rowView;

    };
}
