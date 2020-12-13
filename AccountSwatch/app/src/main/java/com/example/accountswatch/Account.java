package com.example.accountswatch;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.appcompat.app.AppCompatActivity;

public class Account extends AppCompatActivity implements Parcelable {

        // region variables

        private String username;
        private String password;
        private String website;
        private String email;

        //endregion

        //region constructors

        // Default Constructor
        public Account(){}

        // Variable Constructor
        public Account(String Username, String Password, String website, String Email) {
            this.username = Username;
            this.password = Password;
            this.website = website;
            this.email = Email;
        }

        // Parcel Constructor
        public Account(Parcel in){
            this.username = in.readString();
            this.password = in.readString();
            this.website = in.readString();
            this.email = in.readString();
        }

        //endregion

        //region get/set Methods

        public String getUsername() { return this.username; }

        public String getPassword() { return this.password; }

        public String getWebsite(){ return this.website; }

        public String getEmail() { return this.email; }

        public void setUsername(String x) { this.username = x; }

        public void setPassword(String x) { this.password = x; }

        public void setWebsite(String x){ this.website = x; }

        public void setEmail(String x) { this.email = x; }

        public String getAll() {
            return this.username+"\n"+this.password+"\n"+this.website+"\n"+this.email;
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
            parcel.writeString(this.website);
            parcel.writeString(this.email);
        }

        //endregion

}
