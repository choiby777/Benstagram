package com.cby.benstagram.EventListeners;

public interface UploadTaskEventListener {
    void onSuccessEvent();
    void onFailureEvent();
    void onProgressEvent(double progress);
}
