package com.cby.benstagram.Share;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cby.benstagram.Profile.AccountSettingActivity;
import com.cby.benstagram.R;
import com.cby.benstagram.Util.Permissions;
import com.google.android.gms.flags.IFlagProvider;

import java.time.Instant;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PhotoFragment";
    private static final int PHOTO_FRAGEMENT_NUM = 1;
    private static final int GALLERY_FRAGEMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 10;
    private Button btnOpenCamera;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        btnOpenCamera = view.findViewById(R.id.btn_open_camera);
        btnOpenCamera.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_open_camera){
            openCamera();
        }
    }

    private void openCamera() {
        Log.d(TAG, "openCamera: start open camera");

        ShareActivity shareActivity = (ShareActivity)getActivity();

        // 현재 탭 번호가 Photo fragment 일때만 실행
        if (shareActivity.getCurrentPageNumber() != PHOTO_FRAGEMENT_NUM) return;

        // 카메라 퍼미션이 있다면 카메라를 실행
        if (shareActivity.checkPermissions(Manifest.permission.CAMERA)){
            Log.d(TAG, "openCamera: camera permission was allowed");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent , CAMERA_REQUEST_CODE);
        }
        // 권한이 없다면 현재의 ShareActivity를 Clear 하고 새로 생성하여 시작한다.
        else{
            Log.d(TAG, "openCamera: camera permission was not allowed");

            Intent intent = new Intent(shareActivity, ShareActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean isRootTask(){
        return ((ShareActivity)getActivity()).getTask() == 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 카메라 촬영 완료시
        if (requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG, "onActivityResult:  done open camera task");

            Bitmap bitmap;
            bitmap = (Bitmap)data.getExtras().get("data");

            // 중앙의 Add 버튼클릭으로 ShareActivity가 실행된 경우
            if (isRootTask()){

                try {
                    Log.d(TAG, "onActivityResult: received new bitmap form camera : " + bitmap);

                    // AccountSettingActivity 로 촬영한 이미지를 전달하며 종료한다. (AccountSettingActivity 에서 Upload 진행)
                    Intent intent = new Intent(getActivity() , NextActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap) , bitmap);
                    startActivity(intent);

                }catch (NullPointerException e){
                    Log.e(TAG, "onActivityResult: " + e.getMessage());
                }

            }
            // ProfileEdit에서 Change Photo로 ShareActivity가 실행된 경우
            else{

                try {
                    Log.d(TAG, "onActivityResult: received new bitmap form camera : " + bitmap);

                    // AccountSettingActivity 로 촬영한 이미지를 전달하며 종료한다. (AccountSettingActivity 에서 Upload 진행)
                    Intent intent = new Intent(getActivity() , AccountSettingActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap) , bitmap);
                    intent.putExtra(getString(R.string.return_to_fragment) , getString(R.string.edit_profile_fragment));
                    startActivity(intent);

                    getActivity().finish();

                }catch (NullPointerException e){
                    Log.e(TAG, "onActivityResult: " + e.getMessage());
                }
            }
        }
    }
}
