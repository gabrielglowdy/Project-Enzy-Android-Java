package id.nganggur.projectenzy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText inputnya;
    private Brain brain;
    private TextView result;
    private Switch debugSwitch;
//    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brain = new Brain(getApplicationContext());
        inputnya = findViewById(R.id.edInput);
        result = findViewById(R.id.txResult);
        debugSwitch = findViewById(R.id.swDebug);
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                brain.setDebugMode(isChecked);
            }
        });

        debugSwitch.setChecked(brain.isDebugMode());
    }

    public void think (View view){
        String resultnya = brain.find(inputnya.getText().toString());
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
}
