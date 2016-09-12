package com.example.rokobabic.rb_currencyconverter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Tab2 extends Fragment {
    private ListView listView;
    private ArrayList<String> itemList;
    private ArrayAdapter<String> adapter_list;

    private Button add;
    private Button add_DB;
    //private TextView tw1;
    //private TextView tw2;

    private String[] tmpValues;
    //Fragment frag;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_2,container,false);
        //View v2 = inflater.inflate(R.layout.tab_1,container,false);

        String[] valute = getResources().getStringArray(R.array.valuta_drzava);

        listView = (ListView) v.findViewById(R.id.listView);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        itemList=new ArrayList<String>(Arrays.asList(valute));
        adapter_list = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_checked,itemList);
        listView.setAdapter(adapter_list);

        add = (Button) v.findViewById(R.id.button);
        //tw1 = (TextView) v2.findViewById(R.id.textView);
        //tw2 = (TextView) v2.findViewById(R.id.textView2);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                int size = checked.size(); // number of name-value pairs in the array
                /*Toast.makeText(getActivity().getBaseContext(), "oznaceno: "+ Integer.toString(size),
                        Toast.LENGTH_LONG).show();*/


                tmpValues = new String[2];
                if(size == 2){
                    int num = 0;
                    for (int i = 0; i < size; i++) {
                        if (checked.valueAt(i)) {
                            String item = adapter_list.getItem(checked.keyAt(i)).toString();
                            tmpValues[num] = item.substring(0,3);
                            num++;
                        }
                    }

                    Global.svar1 = tmpValues[0];
                    Global.svar2 = tmpValues[1];
                    //tw1.setText(tmpValues[0].toString());
                    //tw2.setText(tmpValues[1].toString());

                    Toast.makeText(getActivity().getBaseContext(), "Tags added to converter tab",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Must be 2 selected items!",
                            Toast.LENGTH_LONG).show();
                }

                listView.clearChoices();
                adapter_list.notifyDataSetChanged();
            }
        });

        return v;
    }
}
