// Complete
package com.example.accountswatch;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailActivity extends AppCompatActivity implements SaveLoad {

    // Email field and enter email button
    private Button save_email;
    private EditText email;

    // Save tag
    private static final String EMAIL_ACTIVITY = "email_prefs";

    // Load tag
    private static final String EMAIL = "EMAIL";

    // Message maker
    private Toast message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        // Initialize button and EditText
        save_email = findViewById(R.id.save_email);
        email = findViewById(R.id.enter_email);

        // Load email from file
        email.setText(loadData(EMAIL_ACTIVITY, EMAIL));

        // Save email onClick
        save_email.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {

                if (isEmailValid(email.getText().toString())) {
                    // If email is valid (more or less) save email
                    saveData(EMAIL_ACTIVITY, EMAIL, email.getText().toString());
                    finish();
                }else{
                    // if email is not valid
                    showToast("Invalid Email");
                }
            }
        });


    }

    // Checks to see if email is in email format
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void saveData(String pref, String key, String data){
        SharedPreferences sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, data);

        editor.apply();

        Toast.makeText(this,data+" saved!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public String loadData(String pref, String key){
        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        return sharedPreferences.getString(key,"error");

    }

    // showToasts
    private void showToast(String text) {
        if (message!=null)
            message.cancel();
        message = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        message.show();
    }
}
