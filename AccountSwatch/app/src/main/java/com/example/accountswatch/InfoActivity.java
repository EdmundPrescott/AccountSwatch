package com.example.accountswatch;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class InfoActivity extends AppCompatActivity implements SaveLoad, NumberPicker.OnValueChangeListener {

    //region save/load tags

    // Save tag
    private static final String INFO_ACTIVITY = "email_prefs";

    // Load tags
    private static final String PASSWORD_LENGTH = "PASSWORD LENGTH";
    private static final String PASSWORD_INCLUDES_NUMBERS = "PASSWORD NUMBERS";
    private static final String PASSWORD_INCLUDE_SPCHARS = "PASSWORD SPECIAL";

    private static final String USERNAME_LENGTH = "USERNAME LENGTH";
    private static final String USERNAME_INCLUDES_NUMBERS = "USERNAME NUMBERS";
    private static final String USERNAME_INCLUDE_SPCHARS = "USERNAME SPECIAL";

    //endregion

    //region switches and numberpickers etc

    // Switches for username and password character inclusions
    private SwitchCompat includeNumbers_username;
    private SwitchCompat includeSpecialChars_username;

    private SwitchCompat includeNumbers_password;
    private SwitchCompat includeSpecialChars_password;

    // Numberpickers for username and password length
    private NumberPicker numberPicker_password;
    private NumberPicker numberPicker_username;

    // Message maker
    private Toast message;

    //endregion

    //region username and password settings

    private int passwordLength = 0;
    private boolean passwordIncludeNumbers = true;
    private boolean passwordIncludesSpecialChars = true;

    private int usernameLength = 0;
    private boolean usernameIncludeNumbers = true;
    private boolean usernameIncludesSpecialChars = true;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        //region initialize numberpickers

        numberPicker_username = findViewById(R.id.numberPicker_username);
        numberPicker_username.setMinValue(0);
        numberPicker_username.setMaxValue(20);
        numberPicker_username.setOnValueChangedListener(this);

        numberPicker_password = findViewById(R.id.numberPicker_password);
        numberPicker_password.setMinValue(0);
        numberPicker_password.setMaxValue(20);
        numberPicker_password.setOnValueChangedListener(this);

        // Change number picker text to white
        numberPicker_username.setTextColor(Color.WHITE);
        numberPicker_password.setTextColor(Color.WHITE);

        //endregion

        //region load settings

        passwordLength = Integer.parseInt(loadData(INFO_ACTIVITY, PASSWORD_LENGTH));
        passwordIncludeNumbers = Boolean.parseBoolean(loadData(INFO_ACTIVITY,PASSWORD_INCLUDES_NUMBERS));
        passwordIncludesSpecialChars = Boolean.parseBoolean(loadData(INFO_ACTIVITY, PASSWORD_INCLUDE_SPCHARS));

        usernameLength = Integer.parseInt(loadData(INFO_ACTIVITY, USERNAME_LENGTH));
        usernameIncludeNumbers = Boolean.parseBoolean(loadData(INFO_ACTIVITY, USERNAME_INCLUDES_NUMBERS));
        usernameIncludesSpecialChars = Boolean.parseBoolean(loadData(INFO_ACTIVITY, USERNAME_INCLUDE_SPCHARS));

        //endregion

        //region initialize switches

        includeNumbers_username = findViewById(R.id.include_nums_username);
        includeNumbers_username.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usernameIncludeNumbers = isChecked;
                saveData(INFO_ACTIVITY, USERNAME_INCLUDES_NUMBERS,Boolean.toString(usernameIncludeNumbers));
            }
        });

        includeSpecialChars_username = findViewById(R.id.include_chars_username);
        includeSpecialChars_username.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                usernameIncludesSpecialChars = isChecked;
                saveData(INFO_ACTIVITY, USERNAME_INCLUDE_SPCHARS, Boolean.toString(usernameIncludesSpecialChars));
            }
        });

        includeNumbers_password = findViewById(R.id.include_nums_password);
        includeNumbers_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordIncludeNumbers = isChecked;
                saveData(INFO_ACTIVITY,PASSWORD_INCLUDES_NUMBERS,Boolean.toString(passwordIncludeNumbers));
            }
        });

        includeSpecialChars_password = findViewById(R.id.include_chars_password);
        includeSpecialChars_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordIncludesSpecialChars = isChecked;
                saveData(INFO_ACTIVITY,PASSWORD_INCLUDES_NUMBERS,Boolean.toString(passwordIncludesSpecialChars));
            }
        });

        //endregion

        //region initialize settings

        numberPicker_username.setValue(usernameLength);
        includeNumbers_username.setChecked(usernameIncludeNumbers);
        includeSpecialChars_username.setChecked(usernameIncludesSpecialChars);

        numberPicker_password.setValue(passwordLength);
        includeNumbers_password.setChecked(passwordIncludeNumbers);
        includeSpecialChars_password.setChecked(passwordIncludesSpecialChars);

        //endregion

    }


    //region save/load

    @Override
    public void saveData(String pref, String key, String data) {
        SharedPreferences sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, data);
        editor.apply();
    }

    @Override
    public String loadData(String pref, String key){
        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        return sharedPreferences.getString(key,"0");
    }

    //endregion

    //region info methods

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker == numberPicker_password) {
            showToast("Password length set to " + newVal);
            saveData(INFO_ACTIVITY, PASSWORD_LENGTH, Integer.toString(newVal));
        }else{
            showToast("Username length set to " + newVal);
            saveData(INFO_ACTIVITY, USERNAME_LENGTH, Integer.toString(newVal));
        }
    }

    // Display a message
    private void showToast(String text) {
        if (message!=null)
            message.cancel();
        message = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        message.show();
    }

    //endregion

}
