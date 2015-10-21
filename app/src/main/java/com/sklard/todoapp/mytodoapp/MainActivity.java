package com.sklard.todoapp.mytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView listviewItems;
    EditText editorNewItem;

    static final int EDIT_ITEM_REQUEST = 101;

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_ITEM_REQUEST) {
                String newValue = data.getExtras().getString("result");
                int pos = data.getExtras().getInt("position");
                items.set(pos, newValue);
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editorNewItem = (EditText) findViewById(R.id.edittextNewItemName);

        readItems();

        listviewItems = (ListView) findViewById(R.id.listView);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listviewItems.setAdapter(itemsAdapter);

        setupListInteraction();
    }


    private void setupListInteraction() {

        // LONG PRESS means "Remove this item."
        listviewItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        // REGULAR CLICK means "Let me edit this item"
        listviewItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                        intent.putExtra("text", itemsAdapter.getItem(position));
                        intent.putExtra("position", position);
                        intent.putExtra("itemID", id);
                        startActivityForResult(intent, EDIT_ITEM_REQUEST);
                    }
                }
        );
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

    public void onClickToAddItem(View view) {
        String strNewItem = editorNewItem.getText().toString();
        itemsAdapter.add(strNewItem);
        editorNewItem.setText("");
        writeItems();
    }
}
