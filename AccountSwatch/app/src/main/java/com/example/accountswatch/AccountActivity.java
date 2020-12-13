package com.example.accountswatch;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class AccountActivity extends AppCompatActivity implements SaveLoad {

    //region username and password settings

    // Username settings
    private int usernameLength = 7;
    private boolean usernameIncludeNumbers = true;
    private boolean usernameIncludesSpecialChars = true;

    // Password settings
    private int passwordLength = 7;
    private boolean passwordIncludeNumbers = true;
    private boolean passwordIncludesSpecialChars = true;

    //endregion

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

    private static final String EMAIL = "EMAIL";

    //endregion

    //region account information fields

    private EditText username_field;
    private EditText password_field;
    private EditText url_field;

    // Create account button
    private Button createAccount;

    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        //region load username and password settings

        // Load Password Settings
        passwordLength = Integer.parseInt(loadData(INFO_ACTIVITY, PASSWORD_LENGTH));
        passwordIncludeNumbers = Boolean.parseBoolean(loadData(INFO_ACTIVITY,PASSWORD_INCLUDES_NUMBERS));
        passwordIncludesSpecialChars = Boolean.parseBoolean(loadData(INFO_ACTIVITY, PASSWORD_INCLUDE_SPCHARS));

        // Load Username Settings
        usernameLength = Integer.parseInt(loadData(INFO_ACTIVITY, USERNAME_LENGTH));
        usernameIncludeNumbers = Boolean.parseBoolean(loadData(INFO_ACTIVITY, USERNAME_INCLUDES_NUMBERS));
        usernameIncludesSpecialChars = Boolean.parseBoolean(loadData(INFO_ACTIVITY, USERNAME_INCLUDE_SPCHARS));

        //endregion

        //region initialize account activity fields
        username_field = findViewById(R.id.enter_username);
        password_field = findViewById(R.id.enter_password);
        url_field = findViewById(R.id.website_url);

        // Loads page with generated account info
        username_field.setText(random(usernameIncludeNumbers,usernameIncludesSpecialChars,usernameLength));
        password_field.setText(random(passwordIncludeNumbers,passwordIncludesSpecialChars,passwordLength));

        // Sets create account button
        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get data from account information fields
                String username = username_field.getText().toString();
                String password = password_field.getText().toString();
                String url = url_field.getText().toString();

                // Check to see if the data is valid (more or less)
                if (username.length() < usernameLength || password.length() < passwordLength || url.length() < 7){
                    // failed
                }else {
                    // succeeded, a new account is sent to the main activity
                    Account newAccount = new Account(username_field.getText().toString(), password_field.getText().toString(), url_field.getText().toString(),loadData(INFO_ACTIVITY,EMAIL));
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("account",
                            newAccount.getUsername()+"\n"+
                                    newAccount.getPassword()+"\n"+
                                    newAccount.getUrl()+"\n"+
                                    newAccount.getEmail()+"\n");
                    clipboard.setPrimaryClip(clip);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("result", newAccount);
                    setResult(RESULT_OK, resultIntent);

                    finish();

                }
            }
        });

        //endregion

    }

    //region save/load methods

    @Override
    public void saveData(String pref, String key, String data) {
        SharedPreferences sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, data);
        editor.apply();
    }

    @Override
    public String loadData(String pref, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        return sharedPreferences.getString(key,"0");
    }

    //endregion

    //region create random string

    public static String random(boolean containsNumbers, boolean containsSpecialCharacters, int length) {

        int stringLength = length - 1;
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;

        // Adds a number to the string
        if (containsNumbers){
            randomStringBuilder.append(generator.nextInt(9));
            stringLength--;
        }

        // Adds a special character to the string
        if (containsSpecialCharacters){
            randomStringBuilder.append((char) (generator.nextInt(5) + 33));
            stringLength--;
        }

        // Adds at least one char
        randomStringBuilder.append((char) (generator.nextInt(25) + 97));

        // Adds the rest of the characters to the string
        for (int i = 0; i < stringLength; i++){
            tempChar = (char) (generator.nextInt(25) + 65);
            randomStringBuilder.append(tempChar);
        }

        return randomStringBuilder.toString();
    }

    //endregion

}
