package com.anniemchee.honeydo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
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
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        showAddDialog(itemText);
    }

    public void showEditDialog(int pos) {
        final int index = pos;
        String itemName = (String) items.toArray()[index];
        View modDialog = LayoutInflater.from(this).inflate(R.layout.modify_dialog, null);
        final EditText textField = (EditText) modDialog.findViewById(R.id.itemToEdit);
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.addDialog))
                .setView(modDialog)
                .setTitle("Modify " + '"' + itemName + '"')
                .setPositiveButton("Done Editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String textToAdd = textField.getText().toString();
                        items.remove(index);
                        itemsAdapter.insert(textToAdd, index);
                        writeItems();
                        itemsAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Delete Forever", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(index);
                        writeItems();
                        itemsAdapter.notifyDataSetChanged();
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }

    public void showAddDialog(String text) {
        final EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        final String itemText = text;
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.addDialog))
                .setTitle("Adding item:")
                .setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemsAdapter.add(itemText);
                        etNewItem.setText("");
                        writeItems();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Nevermind", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etNewItem.setText("");
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }

    public void setupViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                showEditDialog(pos);
                return false;
            }
        });
    }

    public void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    public void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
