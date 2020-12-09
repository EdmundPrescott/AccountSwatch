// Complete
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface, SharedPreferences.OnSharedPreferenceChangeListener, SaveLoad {

    // Main buttons
    private Button set_email;
    private Button generate_info;

    // Recyclerview fields
    private RecyclerView accounts_view;
    private AccountListAdapter mAdapter;
    private LinkedList<Account> accounts = new LinkedList<>();

    // onclick Popup
    private Dialog myDialog;

    // Receives account from account activity
    private final int REQUEST_CODE = 1;

    // Save tags
    private static final String ACCOUNT_USERNAME = "USERNAME";
    private static final String ACCOUNT_PASSWORD = "PASSWORD";
    private static final String ACCOUNT_URL = "URL";
    private static final String ACCOUNT_EMAIL = "EMAIL";

    // Popup
    private TextView username;
    private TextView password;
    private TextView url;
    private TextView email;

    private ImageButton copy_username;
    private ImageButton copy_password;
    private ImageButton copy_url;
    private ImageButton copy_email;

    private Button delete;
    private Button sendEmail;
    private Account popupAccount;
    private int accountPosition;


    // Used to store text in clipboard
    private ClipboardManager clipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize buttons
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

        // Initializes recyclerview
        accounts_view = findViewById(R.id.accounts_view);
        mAdapter = new AccountListAdapter(accounts, this, this);
        accounts_view.setAdapter(mAdapter);
        accounts_view.setLayoutManager(new LinearLayoutManager(this));
        readFileAndFill();

        // Initializes popup
        myDialog = new Dialog(this);

        // Initialize clipboard
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

    }

    // Get account from account activity and save it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){

            // Gets returned account
            Account newAccount = data.getExtras().getParcelable("result");

            // Saves account in preferences
            saveData(newAccount.getUrl(),ACCOUNT_USERNAME,newAccount.getUsername());
            saveData(newAccount.getUrl(),ACCOUNT_PASSWORD,newAccount.getPassword());
            saveData(newAccount.getUrl(),ACCOUNT_URL,newAccount.getUrl());
            saveData(newAccount.getUrl(),ACCOUNT_EMAIL,newAccount.getEmail());

            // Save account website in a txt
            storeAccount(newAccount.getUrl());

            // Add account to recyclerview
            accounts.addLast(newAccount);
            accounts_view.getAdapter().notifyItemChanged(accounts.size());


        }
    }

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

    // recyclerview onClick
    @Override
    public void onItemClick(final int position) {

        // Initialize the popup with everything
        myDialog.setContentView(R.layout.custompopup);

        // Initialize image buttons
        copy_username = myDialog.findViewById(R.id.copy_username);
        copy_password = myDialog.findViewById(R.id.copy_password);
        copy_url = myDialog.findViewById(R.id.copy_url);
        copy_email = myDialog.findViewById(R.id.copy_email);

        // Initialize textviews
        username = myDialog.findViewById(R.id.username_popup);
        password = myDialog.findViewById(R.id.password_popup);
        url = myDialog.findViewById(R.id.url_popup);
        email = myDialog.findViewById(R.id.email_popup);

        // Initialize buttons
        delete = myDialog.findViewById(R.id.delete_button);
        sendEmail = myDialog.findViewById(R.id.send_email_button);

        // Set textview text
        username.setText("Username: "+accounts.get(position).getUsername());
        password.setText("Password: "+accounts.get(position).getPassword());
        url.setText("Website: "+accounts.get(position).getUrl());
        email.setText("Email: "+accounts.get(position).getEmail());

        // Save the account and account position for when the buttons get clicked
        popupAccount = accounts.get(position);
        accountPosition = position;


        // Initialize onclick functions for buttons
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeData(popupAccount.getUrl(), accountPosition);

            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail(popupAccount.getUrl(),popupAccount.getAll());
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

        copy_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("account", accounts.get(position).getUrl());
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

        myDialog.show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    // Setting menu methods
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

    // Adds account url to accounts.txt
    public void storeAccount(String name){
        String accounts = readFile();
        accounts += name;
        writeFile(accounts);
    }

    // Write string to accounts.txt
    public void writeFile(String text) {
        String textToSave = text;

        try {
            FileOutputStream fileOutputStream = openFileOutput("accounts.txt", MODE_PRIVATE);
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
            FileInputStream fileInputStream = openFileInput("accounts.txt");
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
            FileInputStream fileInputStream = openFileInput("accounts.txt");
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
            FileInputStream fileInputStream = openFileInput("accounts.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lines;
            while ((lines = bufferedReader.readLine()) != null) {
                try {
                    Account newAccount = new Account(loadData(lines,ACCOUNT_USERNAME),loadData(lines,ACCOUNT_PASSWORD),loadData(lines,ACCOUNT_URL),loadData(lines,ACCOUNT_EMAIL));
                    if (!newAccount.getUrl().equals("0")) {
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
        writeFile(readFileExcluding(pref));

        SharedPreferences sharedPreferences = getSharedPreferences(pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCOUNT_USERNAME);
        editor.remove(ACCOUNT_PASSWORD);
        editor.remove(ACCOUNT_URL);
        editor.remove(ACCOUNT_EMAIL);
        editor.remove(pref);
        editor.apply();

        accounts.clear();
        mAdapter = new AccountListAdapter(accounts, this, this);
        accounts_view.setAdapter(mAdapter);
        accounts_view.setLayoutManager(new LinearLayoutManager(this));
        readFileAndFill();

    }

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
}