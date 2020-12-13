package com.example.accountswatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface, SharedPreferences.OnSharedPreferenceChangeListener, SaveLoad {

    //region main buttons

    private Button set_email;
    private Button generate_info;

    //endregion

    //region recyclerview variables

    private RecyclerView accounts_view;
    private AccountListAdapter mAdapter;
    private LinkedList<Account> accounts = new LinkedList<>();

    //endregion

    //region intent request code and load tags

    // Receives account from account activity
    private final int REQUEST_CODE = 1;

    // Load tags
    private static final String ACCOUNT_USERNAME = "USERNAME";
    private static final String ACCOUNT_PASSWORD = "PASSWORD";
    private static final String ACCOUNT_website = "website";
    private static final String ACCOUNT_EMAIL = "EMAIL";

    // txt save/load file
    private static final String FILE = "Accounts.txt";

    //endregion

    //region popup variables etc

    // onclick Popup
    private Dialog myDialog;

    // dialog variables
    private EditText username;
    private EditText password;
    private EditText website;
    private EditText email;

    private ImageButton copy_username;
    private ImageButton copy_password;
    private ImageButton copy_website;
    private ImageButton copy_email;

    private Button delete;
    private Button sendEmail;
    private Account popupAccount;
    private int accountPosition;

    // Used to store text in clipboard
    private ClipboardManager clipboard;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region initialize buttons

        set_email = findViewById(R.id.enter_email_button);
        generate_info = findViewById(R.id.generate_username_password);

        // initialize button onclick methods
        set_email.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                set_email();
            }
        });
        generate_info.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                generate_info();
            }
        });

        //endregion

        //region initialize recyclerview

        accounts_view = findViewById(R.id.accounts_view);
        mAdapter = new AccountListAdapter(accounts, this, this);
        accounts_view.setAdapter(mAdapter);
        accounts_view.setLayoutManager(new LinearLayoutManager(this));
        readFileAndFill();

        //endregion

        //region initialize misc

        // Initializes popup
        myDialog = new Dialog(this);

        // Initialize clipboard
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        //endregion

    }

    //region get new account from account activity

    // Get account from account activity and save it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){

            // Gets returned account
            Account newAccount = data.getExtras().getParcelable("result");

            // Saves account in preferences
            saveData(newAccount.getWebsite(),ACCOUNT_USERNAME,newAccount.getUsername());
            saveData(newAccount.getWebsite(),ACCOUNT_PASSWORD,newAccount.getPassword());
            saveData(newAccount.getWebsite(),ACCOUNT_website,newAccount.getWebsite());
            saveData(newAccount.getWebsite(),ACCOUNT_EMAIL,newAccount.getEmail());

            // Save account website in account.txt
            storeAccount(newAccount.getWebsite());

            // Add account to recyclerview
            accounts.addLast(newAccount);
            accounts_view.getAdapter().notifyItemChanged(accounts.size());


        }
    }

    //endregion

    //region go to other activity

    // Go to email activity
    public void set_email(){
        Intent intent = new Intent(MainActivity.this, EmailActivity.class);
        startActivity(intent);
    }

    // Go to account activity
    public void generate_info(){
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //endregion

    // recyclerview onClick
    @Override
    public void onItemClick(final int position) {

        //region initialize layout and variables

        // Initialize popup layout
        myDialog.setContentView(R.layout.custompopup);

        // Save the account and account position for when the buttons get clicked
        popupAccount = accounts.get(position);
        accountPosition = position;

        //endregion

        //region initialize editTexts

        username = myDialog.findViewById(R.id.username_popup);
        password = myDialog.findViewById(R.id.password_popup);
        website = myDialog.findViewById(R.id.website_popup);
        email = myDialog.findViewById(R.id.email_popup);

        // Set editText's text
        username.setText(accounts.get(position).getUsername());
        password.setText(accounts.get(position).getPassword());
        website.setText(accounts.get(position).getWebsite());
        email.setText(accounts.get(position).getEmail());

        // Listener that makes editTexts save their changes
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData(popupAccount.getWebsite(),ACCOUNT_USERNAME,s.toString());
                accounts.get(accountPosition).setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData(popupAccount.getWebsite(),ACCOUNT_USERNAME,s.toString());
                accounts.get(accountPosition).setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        website.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData(popupAccount.getWebsite(),ACCOUNT_USERNAME,s.toString());
                accounts.get(accountPosition).setWebsite(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveData(popupAccount.getWebsite(),ACCOUNT_USERNAME,s.toString());
                accounts.get(accountPosition).setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //endregion

        //region initialize buttons

        delete = myDialog.findViewById(R.id.delete_button);
        sendEmail = myDialog.findViewById(R.id.send_email_button);

        copy_username = myDialog.findViewById(R.id.copy_username);
        copy_password = myDialog.findViewById(R.id.copy_password);
        copy_website = myDialog.findViewById(R.id.copy_website);
        copy_email = myDialog.findViewById(R.id.copy_email);

        // Initialize onclick functions for buttons
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(popupAccount.getWebsite(), accountPosition);

            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(popupAccount.getWebsite(),popupAccount.getAll());
            }
        });


        // Initialize onclick functions for image buttons
        copy_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("account", username.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        copy_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("account", accounts.get(position).getPassword());
                clipboard.setPrimaryClip(clip);
            }
        });

        copy_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("account", accounts.get(position).getWebsite());
                clipboard.setPrimaryClip(clip);
            }
        });

        copy_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("account", accounts.get(position).getEmail());
                clipboard.setPrimaryClip(clip);
            }
        });

        //endregion

        myDialog.show();
    }


    //region setting menu methods

    // Unused
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) { }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.settings_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, InfoActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region save/load methods

    // Adds account website to accounts.txt
    public void storeAccount(String name){
        String accounts = readFile();
        accounts += name;
        writeFile(accounts);
    }

    // Write string to accounts.txt
    public void writeFile(String text) {
        String textToSave = text;

        try {
            FileOutputStream fileOutputStream = openFileOutput(FILE, MODE_PRIVATE);
            fileOutputStream.write(textToSave.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Read accounts.txt and return a string
    public String readFile() {
        try {
            FileInputStream fileInputStream = openFileInput(FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                stringBuffer.append(lines + "\n");
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Read accounts.txt excluding an account
    public String readFileExcluding(String account) {
        try {
            FileInputStream fileInputStream = openFileInput(FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                if (lines.contains(account)) {
                } else{
                    stringBuffer.append(lines + "\n");
                }
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Read accounts.txt and fill the recyclerview
    public void readFileAndFill() {
        LinkedList<Account> ACCOUNTS = new LinkedList<>();
        try {
            FileInputStream fileInputStream = openFileInput(FILE);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                try {
                    Account newAccount = new Account(loadData(lines,ACCOUNT_USERNAME),loadData(lines,ACCOUNT_PASSWORD),loadData(lines,ACCOUNT_website),loadData(lines,ACCOUNT_EMAIL));
                    if (!newAccount.getWebsite().equals("0")) {
                        ACCOUNTS.add(newAccount);
                    }
                }catch (Exception e){

                }
            }
            accounts.clear();

            for (int i = 0; i < ACCOUNTS.size(); i++){
                accounts.add(ACCOUNTS.get(i));
            }
            accounts_view.getAdapter().notifyItemChanged(accounts.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        accounts_view.getAdapter().notifyItemChanged(accounts.size());
    }

    // Save data to shared prefs
    @Override
    public void saveData(String pref, String key, String data) {
        SharedPreferences sharedPreferences = getSharedPreferences(pref,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, data);

        editor.apply();


    }

    // Load data from shared prefs
    @Override
    public String loadData(String pref, String key){
        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        return sharedPreferences.getString(key,"0");

    }

    // Remove data from shared prefs and accounts.txt and recyclerview
    public void removeData(String pref, int position){
        // delete account from txt
        writeFile(readFileExcluding(pref));

        // delete account from shared prefs
        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCOUNT_USERNAME);
        editor.remove(ACCOUNT_PASSWORD);
        editor.remove(ACCOUNT_website);
        editor.remove(ACCOUNT_EMAIL);
        editor.remove(pref);
        editor.apply();

        // update recyclerview
        accounts.clear();
        mAdapter = new AccountListAdapter(accounts, this, this);
        accounts_view.setAdapter(mAdapter);
        accounts_view.setLayoutManager(new LinearLayoutManager(this));
        readFileAndFill();

    }

    //endregion

    //region email method

    // Send email of an account to users email
    public void sendMail(String name, String Message){
        Intent intent = new Intent(Intent.ACTION_SEND);
        String email = loadData("email_prefs","EMAIL");

        intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {email});
        intent.putExtra(Intent.EXTRA_SUBJECT,"New '"+name+"' account");
        intent.putExtra(Intent.EXTRA_TEXT,Message);

        intent.setType("message/rfc822");
        startActivity(intent.createChooser(intent,"Choose an email client"));
    }

    //endregion

}