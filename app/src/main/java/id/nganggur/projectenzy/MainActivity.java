package id.nganggur.projectenzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText inputnya;
    private Brain brain;
    private TextView result;
    private Switch debugSwitch, swLang;
//    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brain = new Brain(getApplicationContext());

        String languageToLoad;
        if (brain.isBahasa()){
            languageToLoad = "ID"; // your language
        }else{
            languageToLoad = "en";
        }
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);


        inputnya = findViewById(R.id.edInput);
        result = findViewById(R.id.txResult);
        debugSwitch = findViewById(R.id.swDebug);
        swLang = findViewById(R.id.swLang);
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                brain.setDebugMode(isChecked);
            }
        });
        swLang.setChecked(brain.isBahasa());
        if (brain.isBahasa()){
            swLang.setText(R.string.bahasa);
        }else{
            swLang.setText(R.string.english);
        }
        swLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                brain.setBahasa(isChecked);
                restart();
            }
        });

        debugSwitch.setChecked(brain.isDebugMode());
    }

    public void restart(){
            recreate();
    }

    public void think (View view){
        String resultnya = brain.find(inputnya.getText().toString());
        if (inputnya.getText().toString().equals("show()")){
            result.setGravity(Gravity.CENTER_VERTICAL| Gravity.LEFT);
        }else{
            result.setGravity(Gravity.CENTER);
        }
        if(resultnya.contains(getString(R.string.toogledebug))){
            debugSwitch.setChecked(brain.isDebugMode());
        }
        if (brain.isLearnMode()){
            inputnya.setHint(R.string.should_answer);
        }else{
            inputnya.setHint(getString(R.string.ask_me));
        }
            result.setText(resultnya);
            inputnya.setText("");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!result.getText().toString().equals(getString(R.string.hello))){
            outState.putString("result",result.getText().toString());
        }
        if (!inputnya.getText().toString().equals("")){
            outState.putString("input",inputnya.getText().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String res = savedInstanceState.getString("result",getString(R.string.hello));
        String in = savedInstanceState.getString("input");

        result.setText(res);
//        inputnya.setText(in);
    }
}
