package com.cby.benstagram.Util;

import com.cby.benstagram.Comparator.ModifiedDate;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSearch {

    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();

        if (files == null) return pathArray;

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

        if (files == null) return pathArray;

        Arrays.sort(files , new ModifiedDate());

        for (int i=0; i<files.length; i++){
            if (files[i].isFile()){
                pathArray.add(files[i].getAbsolutePath());
            }
        }

        return pathArray;
    }

}
