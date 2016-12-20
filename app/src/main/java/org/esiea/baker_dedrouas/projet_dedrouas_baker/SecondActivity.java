package org.esiea.baker_dedrouas.projet_dedrouas_baker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class SecondActivity extends AppCompatActivity {

    EditText name;
    EditText num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        name = (EditText)findViewById(R.id.editText);
        num = (EditText)findViewById(R.id.num);
    }

    public void validation(View v){
        if(name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty), Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        else{
            JSONArray tab = getFromFile();
            JSONObject list1 = new JSONObject();
            try {
                list1.put("Name", name.getText().toString());
                list1.put("Num",num.getText().toString());
                tab.put(list1);
                save(tab);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

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
        Intent intent=new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
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
            return new JSONArray();
        }
        catch (JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

}
