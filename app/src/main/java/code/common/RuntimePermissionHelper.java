package code.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alien.inventory.R;

import java.util.ArrayList;

public final class RuntimePermissionHelper {

    private static RuntimePermissionHelper runtimePermissionHelper;
    public static final int PERMISSION_REQUEST_CODE = 1;
    private static Activity activity;

    private static ArrayList<String> requiredPermissions;
    private static ArrayList<String> ungrantedPermissions = new ArrayList<String>();

    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private RuntimePermissionHelper(Activity activity)  {
        this.activity = activity;
    }

    public static synchronized RuntimePermissionHelper getInstance(Activity activity){
        if(runtimePermissionHelper == null) {
            runtimePermissionHelper = new RuntimePermissionHelper(activity);
        }
        return runtimePermissionHelper;
    }


    private static void initPermissions() {
        requiredPermissions = new ArrayList<String>();
        requiredPermissions.add(READ_EXTERNAL_STORAGE);
        requiredPermissions.add(WRITE_EXTERNAL_STORAGE);
        requiredPermissions.add(CAMERA);
        requiredPermissions.add(ACCESS_COARSE_LOCATION);
        requiredPermissions.add(ACCESS_FINE_LOCATION);
        //Add all the required permission in the list
    }

    public static void requestPermissionsIfDenied(){
        ungrantedPermissions = getUnGrantedPermissionsList();
        if(canShowPermissionRationaleDialog()){
            showMessageOKCancel(activity.getResources().getString(R.string.permissionMessage),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermissions();
                        }
                    });
            return;
        }
        askPermissions();
    }

    public void requestPermissionsIfDenied(final String permission){
        if(canShowPermissionRationaleDialog(permission)){
            showMessageOKCancel(activity.getResources().getString(R.string.permissionMessage),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askPermission(permission);
                        }
                    });
            return;
        }
        askPermission(permission);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public static boolean canShowPermissionRationaleDialog() {
        boolean shouldShowRationale = false;
        for(String permission: ungrantedPermissions) {
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if(shouldShow) {
                shouldShowRationale = true;
            }
        }
        return shouldShowRationale;
    }

    public boolean canShowPermissionRationaleDialog(String permission) {
        boolean shouldShowRationale = false;
            boolean shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
            if(shouldShow) {
                shouldShowRationale = true;
        }
        return shouldShowRationale;
    }

    public static void askPermissions() {
        if(ungrantedPermissions.size()>0) {
            ActivityCompat.requestPermissions(activity,
                    ungrantedPermissions.toArray(new String[ungrantedPermissions.size()]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void askPermission(String permission) {
            ActivityCompat.requestPermissions(activity, new String[] {permission}, PERMISSION_REQUEST_CODE);
    }

    private static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       /* Intent intent = new Intent(activity,ErrorActivity.class);
                        intent.putExtra("permissions_denied",true);
                        activity.startActivity(intent);
                        activity.finish();*/
                    }
                })
                .create()
                .show();
    }


    public static boolean isAllPermissionAvailable() {
        boolean isAllPermissionAvailable = true;
        initPermissions();
        for(String permission : requiredPermissions){
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                isAllPermissionAvailable = false;
                break;
            }
        }
        return isAllPermissionAvailable;
    }

    public static ArrayList<String> getUnGrantedPermissionsList() {
        ArrayList<String> list = new ArrayList<String>();
        for(String permission: requiredPermissions) {
            int result = ActivityCompat.checkSelfPermission(activity, permission);
            if(result != PackageManager.PERMISSION_GRANTED) {
                list.add(permission);
            }
        }
        return list;
    }

    public static boolean isPermissionAvailable(String permission) {
        boolean isPermissionAvailable = true;
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
                isPermissionAvailable = false;
            }
        return isPermissionAvailable;
    }

    public static void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (isAllPermissionAvailable()) {
                // All permissions available. Go with the flow
            } else {
                requestPermissionsIfDenied();
            }
        }
    }
}
