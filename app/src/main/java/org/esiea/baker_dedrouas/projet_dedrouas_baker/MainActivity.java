package org.esiea.baker_dedrouas.projet_dedrouas_baker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv = null;
    JSONArray list = null;
    RecyclerSimpleViewAdapter adapter;
    AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(),getString(R.string.msg),Toast.LENGTH_SHORT).show();
                getFromFile();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });


        list = getFromFile();
        rv = (RecyclerView) findViewById((R.id.recycler));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //____________________________________

        //JSONObject list1 = new JSONObject();
        /*try {
            list1.put("Name", "Mum");
            list1.put("Num","000000000");
            list.put(list1);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //demo();
        setAdapter();
    }

    private void demo(){
        JSONObject list1 = new JSONObject();

        for(int i = 0; i <50 ;++i){
            try {
                list1.put("Name", "Contact "+ i);
                list1.put("Num",""+i);
                list.put(list1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            save(list);
        }

    }

    private void setAdapter(){
        List<String> items = new ArrayList<String>();
        for(int i = 0; i<list.length();++i){
            try {
                JSONObject obj = (JSONObject)list.get(i);
                items.add(obj.get("Name").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new RecyclerSimpleViewAdapter(items, android.R.layout.simple_list_item_1);
        rv.setAdapter(adapter);
    }

    private void save(JSONArray tab){
        try {
            Writer output = null;
            File file = new File(getCacheDir() + "/" + "contacts.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(tab.toString());
            output.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addC(View v){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        getFromFile();
        finish();
    }

    public void loadC(View v){
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private JSONArray getFromFile(){
        try{
            InputStream in = new FileInputStream(getCacheDir() + "/" + "contacts.json");
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            return new JSONArray(new String(buffer,"UTF-8"));
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(),getString(R.string.noCt),Toast.LENGTH_SHORT).show();
            return new JSONArray();
        }
        catch (JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
