package com.mavl.youtest;

import android.content.Context;

/**
 * Created by student2 on 07.04.17.
 */

public class DataBaseCommunication {
    public static DB db;
    DataBaseCommunication(Context context) {
        db = new DB(context);
    }


}
