// Complete
package com.example.accountswatch;

public interface SaveLoad {
    // This was for my convenience
    void saveData(String pref, String key, String data);
    String loadData(String pref, String key);

}
