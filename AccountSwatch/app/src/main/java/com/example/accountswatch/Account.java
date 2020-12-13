package com.example.accountswatch;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity implements Parcelable {

        // region variables

        private String username;
        private String password;
        private String url;
        private String email;

        //endregion

        //region constructors

        // Default Constructor
        public Account(){}

        // Variable Constructor
        public Account(String Username, String Password, String Url, String Email) {
            this.username = Username;
            this.password = Password;
            this.url = Url;
            this.email = Email;
        }

        // Parcel Constructor
        public Account(Parcel in){
            this.username = in.readString();
            this.password = in.readString();
            this.url = in.readString();
            this.email = in.readString();
        }

        //endregion

        //region getMethods

        public String getUsername() { return this.username; }

        public String getPassword() { return this.password; }

        public String getUrl(){ return this.url; }

        public String getEmail() { return this.email; }

        public String getAll() {
            return this.username+"\n"+this.password+"\n"+this.url+"\n"+this.email;
        }

        //endregion

        //region parcel methods

        // Used to save/load
        public static final Creator<Account> CREATOR = new Creator<Account>() {
            @Override
            public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

            @Override
            public Account[] newArray(int size) {
            return new Account[size];
        }
        };

        // Unused
        @Override
        public int describeContents() {
            return 0;
        }

        // Save Parcel Object
        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(this.username);
            parcel.writeString(this.password);
            parcel.writeString(this.url);
            parcel.writeString(this.email);
        }

        //endregion

}
