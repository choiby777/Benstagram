package com.cby.benstagram.Util;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();

        for (int i=0; i<files.length; i++){
            if (files[i].isDirectory()){
                pathArray.add(files[i].getAbsolutePath());
            }
        }

        return pathArray;
    }

    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();

        for (int i=0; i<files.length; i++){
            if (files[i].isFile()){
                pathArray.add(files[i].getAbsolutePath());
            }
        }

        return pathArray;
    }

}
