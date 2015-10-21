package com.sklard.todoapp.mytodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText et;
    int positionOfItemBeingEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String textToEdit = getIntent().getStringExtra("text");
        positionOfItemBeingEdited = getIntent().getIntExtra("position", 0);

        et = (EditText) findViewById(R.id.editText);
        et.setText(textToEdit);
        et.setSelection(textToEdit.length());
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSave(View view) {
        Intent data = new Intent();
        data.putExtra("position", positionOfItemBeingEdited);
        data.putExtra("result", et.getText().toString());
        setResult(RESULT_OK, data);
        this.finish();
    }
}
