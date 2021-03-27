package com.example.neverforget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> tasks;
    EditText in;
    TextView out;
    Random rand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        rand = new Random();
        initViews();
        setUpOutActionListeners();
    }

    private void setUpOutActionListeners() {
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle();
            }
        });
        out.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeCurrentItem();
                vibrate();
                return true;
            }
        });
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int duration = 50;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(duration);
        }
    }

    private void removeCurrentItem() {
        String current = out.getText().toString();
        if (tasks.contains(current)) tasks.remove(current);
        shuffle();
    }

    private void initViews() {
        in = findViewById(R.id.et_in);
        out = findViewById(R.id.tv_out);
    }

    public void addNewItem(View view) {
        String newText = in.getText().toString();
        String[] items = newText.split("\n");
        for (String item: items) {
            tasks.add(item);
        }
        in.setText("");
        if (out.getText().toString().equals("Output")){
            out.setText(items[0]);
        }
        hideKeyboard();
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    public void shuffle() {
        if (tasks.size() == 0){
            out.setText("Output");
            return;
        }
        String current = out.getText().toString();
        String nextTask = current;
        while (current.equals(nextTask)){
            int i = rand.nextInt(tasks.size());
            nextTask = tasks.get(i);
        }
        out.setText(nextTask);
    }
}