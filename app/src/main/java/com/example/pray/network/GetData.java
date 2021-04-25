package com.example.pray.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

public class GetData extends AsyncTaskLoader<ArrayList<PrayData>> {
    private String url;

    public GetData(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<PrayData> loadInBackground() {
        if (url != null)
            return HttpConnect.doInBackground(url);
        return null;
    }
}
