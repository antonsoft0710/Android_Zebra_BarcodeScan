package code.common;

import android.app.Activity;

public final class AppSettings extends OSettings {

    public static final String PREFS_MAIN_FILE      = "PREFS_ALIEN_FILE";
    public static final String servernameOrIP       = "servernameOrIP";
    public static final String dbName               = "dbName";
    public static final String port                 = "port";
    public static final String username             = "username";
    public static final String password             = "password";
    public static final String completed            = "completed";

    public AppSettings(Activity mActivity) {
        super(mActivity);
    }
}
