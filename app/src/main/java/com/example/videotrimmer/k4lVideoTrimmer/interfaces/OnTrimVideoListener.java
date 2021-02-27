package com.example.videotrimmer.k4lVideoTrimmer.interfaces;

import android.net.Uri;

public interface OnTrimVideoListener {

    void onTrimStarted();

    void getResult(final Uri uri);

    void cancelAction();

    void onError(final String message);
}
