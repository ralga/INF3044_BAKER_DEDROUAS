package org.esiea.baker_dedrouas.projet_dedrouas_baker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    RecyclerView rv = null;
    JSONArray list = null;
    AlertDialog.Builder builder = null;
    Adapter adt;

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
        for(int i = 0; i <50 ;i++){
            try {
                JSONObject list1 = new JSONObject();
                list1.put("Name", "Contact "+ i);
                list1.put("Num",""+i);
                list.put(list1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            save(list);
        }

    }

    public void chosen(View v){
        Toast.makeText(this,"Meh !",Toast.LENGTH_SHORT);
    }

    private void setAdapter(){
        ArrayAdapter<String> items = new ArrayAdapter<>(
                rv.getContext(), R.layout.ligne, R.id.monTexte);
        for(int i = 0; i<list.length();++i){
            try {
                JSONObject obj = (JSONObject)list.get(i);
                items.add(obj.get("Name").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adt = new Adapter(list);
        rv.setAdapter(adt);
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
        adt.setNewContact(getFromFile());
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

    private class Adapter extends RecyclerView.Adapter<Adapter.Holder>{
        JSONArray contact;

        public Adapter(JSONArray array){
            this.contact = array;
        }

        @Override
        public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new Holder(inflater.inflate(R.layout.ligne, parent, false));
        }

        @Override
        public void onBindViewHolder(Adapter.Holder holder, int position) {
            try {
                holder.name.setText(list.getJSONObject(position).get("Name").toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list.length();
        }

        public void setNewContact(JSONArray update){
            list = update;
            notifyDataSetChanged();
        }

        public class Holder extends RecyclerView.ViewHolder{
            public TextView name;

            public Holder(View itemView) {
                super(itemView);
                name = (TextView)itemView.findViewById(R.id.monTexte);
            }
        }
    }
}

