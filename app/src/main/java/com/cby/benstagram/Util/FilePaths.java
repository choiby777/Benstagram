package com.cby.benstagram.Util;

import android.os.Environment;

public class FilePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    // 기기마다 경로가 다를수 있다.
    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

    // Firebase 의 image 저장 경로
    public String FIREBASE_IMAGE_STORAGE = "photos/users/";
}
