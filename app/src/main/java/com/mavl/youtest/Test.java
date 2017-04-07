package com.mavl.youtest;

/**
 * Created by mavl on 27.03.2017.
 */

public class Test {
    private int ID;
    private String shortName;
    private int authorID;
    private String description;
    private int time;
    private boolean random;
    private int mode;
    private String pass;

    Test() {}

    public Test(/*int ID, */String shortName, int authorID, String description, int time, boolean random, int mode, String pass) {
        //this.ID = ID;
        this.shortName = shortName;
        this.authorID = authorID;
        this.description = description;
        this.time = time;
        this.random = random;
        this.mode = mode;
        this.pass = pass;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String sqlThisInsertString(String tableName) {
        return "INSERT INTO " + tableName + " VALUES (" + this.shortName + ", "+ this.authorID + ", "
                + this.description + ", "+ this.time + ", "+ ((this.random)?1:0) + ", "
                + this.mode + ", "+ this.pass +")";
    }

    public static String sqlInsertString(String tableName, String shortName, int authorID, String description, int time, boolean random, int mode, String pass) {
        return "INSERT INTO " + tableName + "(shortName, description, time, random, mode, pass) VALUES (\"" + shortName + "\", \"" + description + "\", "+ time + ", "+ ((random)?1:0) + ", "
                + mode + ", \""+ pass +"\");";
    }
}
