package com.joern.flywayexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Button bt_deleteTable = (Button) findViewById(R.id.bt_deleteTable);
        Button bt_addRow = (Button) findViewById(R.id.bt_addRow);
        bt_deleteTable.setOnClickListener(this);
        bt_addRow.setOnClickListener(this);

        updateTableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_deleteTable:
                bt_deleteTable();
                break;

            case R.id.bt_addRow:
                bt_addRow();
                break;
        }

    }

    private void bt_deleteTable() {

        dbHelper.deleteDogs();
        updateTableView();
    }

    private void bt_addRow(){

        EditText et_name = (EditText) findViewById(R.id.et_name);
        EditText et_color = (EditText) findViewById(R.id.et_color);
        EditText et_race = (EditText) findViewById(R.id.et_race);

        String name = et_name.getText().toString();
        String color = et_color.getText().toString();
        String race = et_race.getText().toString();

        if(name == null || name.isEmpty()){

            Toast.makeText(this, "please enter name", Toast.LENGTH_SHORT).show();

        }else{

            boolean dogCreated = dbHelper.createDog(name, color, race);

            if (dogCreated) {

                updateTableView();
                et_name.setText("");
                et_color.setText("");
                et_race.setText("");

            } else {
                Toast.makeText(this, "failed to create dog", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateTableView() {

        LinearLayout ll_tableBody = (LinearLayout) findViewById(R.id.ll_tableBody);
        ll_tableBody.removeAllViews();

        List<Dog> dogList = dbHelper.readDogs();
        if (dogList.isEmpty()) {

            LinearLayout ll_row = (LinearLayout) getLayoutInflater().inflate(R.layout.row, null);
            ((TextView) ll_row.findViewById(R.id.tv_id)).setText("...");
            ((TextView) ll_row.findViewById(R.id.tv_name)).setText("...");
            ((TextView) ll_row.findViewById(R.id.tv_color)).setText("...");
            ((TextView) ll_row.findViewById(R.id.tv_race)).setText("...");

            ll_tableBody.addView(ll_row);

        } else {

            for (Dog dog : dogList) {

                LinearLayout ll_row = (LinearLayout) getLayoutInflater().inflate(R.layout.row, null);
                ((TextView) ll_row.findViewById(R.id.tv_id)).setText(dog.getId());
                ((TextView) ll_row.findViewById(R.id.tv_name)).setText(dog.getName());
                ((TextView) ll_row.findViewById(R.id.tv_color)).setText(dog.getColor());
                ((TextView) ll_row.findViewById(R.id.tv_race)).setText(dog.getRace());

                ll_tableBody.addView(ll_row);
            }
        }
    }
}