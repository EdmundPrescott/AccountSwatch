package com.example.accountswatch;

public interface SaveLoad {
    // application save/load method template
    void saveData(String pref, String key, String data);
    String loadData(String pref, String key);

}