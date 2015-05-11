package com.ims.common;

import java.io.File;
import java.io.IOException;

public class FilePath {

    private static String getFilePath(String filename){
        File f = new File( System.getProperty("user.dir") + File.separator + "data" + File.separator + filename);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }
        return f.getPath();
    }

    public static String getMeetingDataFile(){
        return getFilePath("MeetingData.xml");
    }

    public static String getProblemFile(){
        return getFilePath("problem.txt");
    }
}
