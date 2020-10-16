package code.common;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mainApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);

    }
}
