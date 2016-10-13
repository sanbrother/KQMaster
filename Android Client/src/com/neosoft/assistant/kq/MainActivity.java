package com.neosoft.assistant.kq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.neosoft.assistant.kq.model.UserInfo;


public class MainActivity extends ActionBarActivity {
    private final List<HashMap<String, Object>> fillMaps = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter adapter;
    
    private final OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof HashMap<?, ?>) {
                HashMap<?, ?> map = (HashMap<?, ?>) item;
                UserInfo userInfo = (UserInfo) map.get("userInfo");
                
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "[Hudson][Status]");
                intent.putExtra(Intent.EXTRA_TEXT, Base64Codec.encrypt(userInfo.getUsername()) + "&" + Base64Codec.encrypt(userInfo.getPassword()));
                intent.setData(Uri.parse("mailto:gaodw@neusoft.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String[] from = new String[] { "title" };
        int[] to = new int[] { R.id.title };
        
        this.adapter = new SimpleAdapter(this, fillMaps, R.layout.simple_listview_item, from, to);
        
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setAdapter(this.adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        this.fillMaps.clear();
        
        for (UserInfo userInfo : SqliteDBHelper.getInstance().getUserInfoList()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("title", userInfo.getUsername());
            map.put("userInfo", userInfo);
            this.fillMaps.add(map);
        }
        
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        if (id == R.id.action_add_user) {
            Intent i = new Intent("com.neosoft.assistant.kq.ADD_USER_INFO");
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
