package code.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.alien.inventory.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import code.common.RuntimePermissionHelper;


public class AppUtils {
    public static Toast mToast;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    static ProgressDialog progressDialog;

    public static float convertDpToPixel(float dp) {
        return dp * (((float) Resources.getSystem().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static float convertPixelsToDp(float px) {
        return px / (((float) Resources.getSystem().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static String print(String mString) {
        return mString;
    }

    public static String printD(String Tag, String mString) {
        return mString;
    }

    public static String printE(String Tag, String mString) {
        return mString;
    }

    public static int startPosition(String word, String sourceString) {
        int startingPosition = sourceString.indexOf(word);
        print("startingPosition" + word + " " + startingPosition);
        return startingPosition;
    }

    public static int endPosition(String word, String sourceString) {
        int endingPosition = sourceString.indexOf(word) + word.length();
        print("startingPosition" + word + " " + endingPosition);
        return endingPosition;
    }

    public static void showToastSort(Context context, String text) {
        if (mToast != null && mToast.getView().isShown()) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            try {
                @SuppressLint("WrongConstant") InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
                View view = activity.getCurrentFocus();
                if (view != null) {
                    IBinder binder = view.getWindowToken();
                    if (binder != null) {
                        inputMethodManager.hideSoftInputFromWindow(binder, 0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return (((float) getDisplayMetrics(context).densityDpi) / 160.0f) * dp;
    }

    public static int convertDpToPixelSize(float dp, Context context) {
        float pixels = convertDpToPixel(dp, context);
        int res = (int) (0.5f + pixels);
        if (res != 0) {
            return res;
        }
        if (pixels == 0.0f) {
            return 0;
        }
        if (pixels > 0.0f) {
            return 1;
        }
        return -1;
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }


    public static void setCustomFont(Activity mActivity, TextView mTextView, String asset) {
        mTextView.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), asset));
    }

    public static void showRequestDialog(Activity activity) {

        try {
            if (!((Activity) activity).isFinishing()) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(activity.getString(R.string.pleaseWait));
                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progressDialog.show();
                }
                else
                {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage(activity.getString(R.string.pleaseWait));
                    progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    progressDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void showRequestDialog(Activity activity, String message) {
        if (progressDialog == null) {
            //progressDialog = new ProgressDialog(activity, R.style.MyAlertDialogStyle);
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(message);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.show();
        }
    }

    public static void hideDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTncDate() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    /*public static void showErrorMessage(View mView, String errorMessage, Context mActivity) {
        Snackbar snackbar = Snackbar.make(mView, errorMessage, Snackbar.LENGTH_SHORT);
        TextView tv = (TextView) (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        *//*Typeface font = Typeface.createFromAsset(mActivity.getAssets(), "centurygothic.otf");
        tv.setTypeface(font);*//*

        snackbar.show();
    }*/


    public static String toCamelCaseSentence(String s) {
        if (s == null) {
            return "";
        }
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String toCamelCaseWord : words) {
            sb.append(toCamelCaseWord(toCamelCaseWord));
        }
        return sb.toString().trim();
    }

    public static String toCamelCaseWord(String word) {
        if (word == null) {
            return "";
        }
        switch (word.length()) {
            case 0:
                return "";
            case 1:
                return word.toUpperCase(Locale.getDefault()) + " ";
            default:
                return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase(Locale.getDefault()) + " ";
        }
    }

    public static String split(String str) {
        String result = "";
        if (str.contains(" ")) {
            return toCamelCaseWord(str.split("\\s+")[0]);
        }
        return toCamelCaseWord(str);
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {

        v.setVisibility(View.GONE);

        /*final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);*/
    }

    // GetDeviceId
    public static String getDeviceID(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDateCurrentTimeZone(long timestamp) {

        timestamp = timestamp*1000;

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm aa");

        //System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        //System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String getDateFromTimestamp(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("dd MMM hh:mm");

        //System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();
        if (timestamp < 1000000000000L) {
            calendar.setTimeInMillis(timestamp*1000);
        }
        //System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String getTimeLineDate(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");

        //System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();
        if (timestamp < 1000000000000L) {
            calendar.setTimeInMillis(timestamp*1000);
        }
        //System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String getTimeLineTime(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("hh:mm aa");

        //System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();
        if (timestamp < 1000000000000L) {
            calendar.setTimeInMillis(timestamp*1000);
        }
        //System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String getTimeFromTimestamp(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("hh:mm aa");

        //System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();
        if (timestamp < 1000000000000L) {
            calendar.setTimeInMillis(timestamp*1000);
        }
        //System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String getCurrentDate() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current Date => " + c);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = sdf.format(c);

        return formattedDate;
    }

    public static String getCurrentTime() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current Time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrentDateNew() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }


    public static String getCurrentDateTime() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy hh:mm aa");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrentDateYMD(int addDays) {

        Calendar mcurrentDate= Calendar.getInstance();
        mcurrentDate.add(Calendar.DAY_OF_MONTH,addDays);

        Date c = mcurrentDate.getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getCurrentDateDMY(int addDays) {

        Calendar mcurrentDate= Calendar.getInstance();
        mcurrentDate.add(Calendar.DAY_OF_MONTH,addDays);

        Date c = mcurrentDate.getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getNewDateTimeFromTimestamp(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp*1000);
        //System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String parseDateToFormat(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String parseDateToDMYFormat(String oldDate) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(oldDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDateTimeFromTimestampNew(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("dd-MM-yy hh:mm aa");

        System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();

        if (timestamp < 1000000000000L) {
            calendar.setTimeInMillis(timestamp*1000);
        }
        else
        {
            calendar.setTimeInMillis(timestamp);
        }

        System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String getDateTimeFromTimestamp(long timestamp) {

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");

        System.out.println(timestamp);

        Calendar calendar = Calendar.getInstance();

        if (timestamp < 1000000000000L) {
            calendar.setTimeInMillis(timestamp*1000);
        }
        else
        {
            calendar.setTimeInMillis(timestamp);
        }

        System.out.println(formatter.format(calendar.getTime()));

        String ret = formatter.format(calendar.getTime());

        return ret;
    }

    public static String covertTimeToText(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        Date date = null;
        date = new Date(createdAt);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = null;
        if (diffDays > 0) {
            if (diffDays == 1) {
                time = diffDays + " day ago ";
            } else {
                time = diffDays + " hours ago ";
            }
        } else {
            if (diffHours > 0) {
                if (diffHours == 1) {
                    time = diffHours + " hr ago";
                } else {
                    time = diffHours + " hrs ago";
                }
            } else {
                if (diffMinutes > 0) {
                    if (diffMinutes == 1) {
                        time = diffMinutes + " min ago";
                    } else {
                        time = diffMinutes + " mins ago";
                    }
                } else {
                    if (diffSeconds > 0) {
                        time = diffSeconds + " secs ago";
                    }
                }

            }

        }
        return time;
    }

    public static String covertTimeToHours(long createdAt) {
        DateFormat userDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat dateFormatNeeded = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        Date date = null;
        date = new Date(createdAt*1000);
        String crdate1 = dateFormatNeeded.format(date);

        // Date Calculation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        crdate1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currenttime = dateFormat.format(cal.getTime());

        Date CreatedAt = null;
        Date current = null;
        try {
            CreatedAt = dateFormat.parse(crdate1);
            current = dateFormat.parse(currenttime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = current.getTime() - CreatedAt.getTime();
        //long diffSeconds = diff / 1000;
        //long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String time = "0";
        if(diffDays>0)
        {
            diffDays = diffDays*24;
        }

        if (diffHours > 0) {
            if (diffHours == 1) {
                time = String.valueOf(diffHours+diffDays);
            } else {
                time = String.valueOf(diffHours+diffDays);
            }

        }
        return time;
    }

    public static String parseDate(String givenDateString) {
        if (givenDateString.equalsIgnoreCase("")) {
            return "";
        }

        long timeInMilliseconds=0;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");
        try {

            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String result = "0";
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:MM:SS");

        String todayDate = formatter.format(new Date());
        Calendar calendar = Calendar.getInstance();

        long dayagolong =  timeInMilliseconds;
        calendar.setTimeInMillis(dayagolong);
        String agoformater = formatter.format(calendar.getTime());

        Date CurrentDate = null;
        Date CreateDate = null;

        try {
            CurrentDate = formatter.parse(todayDate);
            CreateDate = formatter.parse(agoformater);

            long different = Math.abs(CurrentDate.getTime() - CreateDate.getTime());

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if(elapsedDays>0)
            {
                elapsedDays = elapsedDays*24;
            }

            if (elapsedHours > 0) {
                result = String.valueOf(elapsedHours+elapsedDays);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.v("result-Data", result);

        return result;
    }

    public static boolean isEmailValid(String email) {

        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String getmiliTimeStamp() {

        long LIMIT = 10000000000L;

        long t = Calendar.getInstance().getTimeInMillis();

        return String.valueOf(t).substring(0, 10);
    }

    public static String changeHrFormat(String time) {

        String input = time;
        //Format of the date defined in the input String
        DateFormat df = new SimpleDateFormat("hh:mm aa");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        String output = null;
        try{
            //Converting the input String to Date
            date= df.parse(input);
            //Changing the format of date and storing it in String
            output = outputformat.format(date);
            //Displaying the date
            System.out.println(output);
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return output;
    }

    public static String getDifference(String del, String lmp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = sdf.parse(del);
            Date now = sdf.parse(lmp);
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            if (days < 7)
                return days + " Days";
            else
                return days / 7 + " Weeks";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public static int getWeekDifference(String lmpDate, String delDate) {
        int week=0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = sdf.parse(lmpDate);
            Date now = sdf.parse(delDate);
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            if (days < 7)
                week=0;
                //return hours + " Days";
            else
                week = (int) (days / 7);
            //return hours / 7 + " Weeks";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return week;
    }


    public static String getTimeDifference(String time1,String time2) {

        String timeDiff="-1";
        if(!time1.isEmpty()&&!time2.isEmpty())
        {

            String[] morNight1 = time1.split(" ");
            String[] morNight2 = time2.split(" ");

            if(morNight1[1].equalsIgnoreCase("PM")&&morNight2[1].equalsIgnoreCase("PM"))
            {
                if(morNight1[0].length()==4)
                {
                    morNight1[0] = "0"+morNight1[0];
                }

                int b = Integer.parseInt((""+morNight1[0]).substring(0, 2));

                if(b==12)
                {
                    timeDiff="1";
                }
                else
                {
                    int newTime1 = Integer.parseInt(morNight1[0].replaceAll(":",""));
                    int newTime2 = Integer.parseInt(morNight2[0].replaceAll(":",""));

                    if(newTime2>newTime1)
                    {
                        timeDiff = "1";
                    }
                    else
                    {
                        timeDiff = "-1";
                    }
                }

            }
            else  if(morNight1[1].equalsIgnoreCase("AM")&&morNight2[1].equalsIgnoreCase("AM"))
            {
                if(morNight1[0].length()==4)
                {
                    morNight1[0] = "0"+morNight1[0];
                }

                int b = Integer.parseInt((""+morNight1[0]).substring(0, 2));

                if(b==12)
                {
                    timeDiff="1";
                }
                else
                {
                    int newTime1 = Integer.parseInt(morNight1[0].replaceAll(":",""));
                    int newTime2 = Integer.parseInt(morNight2[0].replaceAll(":",""));

                    if(newTime2>newTime1)
                    {
                        timeDiff = "1";
                    }
                    else
                    {
                        timeDiff = "-1";
                    }
                }
            }
            else  if(morNight1[1].equalsIgnoreCase("PM")&&morNight2[1].equalsIgnoreCase("AM"))
            {
                timeDiff = "-1";
            }
            else
            {
                timeDiff = "1";
            }

        }



        return timeDiff;
    }

    public static String getDateAgo(String del) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = sdf.parse(del);
            Date now = new Date(System.currentTimeMillis());
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            return days + " Days";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    public static String getDateDifference(String dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = sdf.parse(dt);
            Date now = new Date(System.currentTimeMillis());
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            long daysDiff = TimeUnit.MILLISECONDS.toDays(days);
            return String.valueOf(daysDiff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getDateDiff(String dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = sdf.parse(dt);
            Date now = new Date(System.currentTimeMillis());
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            return String.valueOf(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String getWeightDaysDiff(String dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dt);
            Date now = new Date(System.currentTimeMillis());
            long days = getDateDiff(date, now, TimeUnit.DAYS);
            return String.valueOf(days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0";
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static int getAgeFromDOB(String dobDate) {

        int age = 0;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dobDate);

            try {

                Date currDate = Calendar.getInstance().getTime();
                // Log.d("Curr year === "+currDate.getYear()+" DOB Date == "+dobDate.getYear());
                age = currDate.getYear() - date.getYear();
                // Log.d("Calculated Age == "+age);

            } catch (Exception e) {
                //Log.d(SyncStateContract.Constants.kApiExpTag, e.getMessage()+ "at Get Age From DOB mehtod.");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
        System.out.println(age); // Sat Jan 02 00:00:00 GMT 2010

        return age;

    }

    public static void saveScreenShotsAppCache(Activity context, View view) throws IOException {
        try {
            AppUtils.print("===saveScreenShotsAppCache");
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());

            view.setDrawingCacheEnabled(false);
            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imagePath = new File(context.getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(context, "com.mncu.android.provider", newFile);
            AppUtils.print("===saveScreenShotsAppCache" + contentUri);
            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.pleaseWait));
                context.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getCurrentLocale(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return activity.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return activity.getResources().getConfiguration().locale;
        }
    }

    public static long dateDifference(String dob) {
        long day=0;
        try {
            Date userDob = null;
            try {
                userDob = new SimpleDateFormat("dd-MM-yyyy HH:mm aa").parse(dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date today = new Date();
            long diff =  today.getTime() - userDob.getTime();
            day =  diff / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            e.printStackTrace();
            return  day;
        }

        return  day;
    }

    public static long getCurrentTimestamp() {

        return System.currentTimeMillis();
    }

    public static long currentTimestamp() {

        long timestamp = 0;

        Calendar mcurrentDate= Calendar.getInstance();

        // 2) get a java.util.Date from the calendar instance.
        //    this date will represent the current instant, or "now".
        Date now = mcurrentDate.getTime();

        // 3) a java current time (now) instance
        Timestamp currentTimestamp = new Timestamp(now.getTime());

        //timestamp = mcurrentDate.getTimeInMillis();
        timestamp = currentTimestamp.getTime()/1000L;

        return timestamp;
    }

    public static void enableDisable(ViewGroup layout, boolean b) {
        layout.setEnabled(b);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableDisable((ViewGroup) child,b);
            } else {
                child.setEnabled(b);
            }
        }
    }


    public static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
    }


    public static String dateMinMaxDialog(Context context, final TextView textView, int type) {

        final String[] selectedDate = {""};

        Calendar mcurrentDate = Calendar.getInstance();
        int year = mcurrentDate.get(Calendar.YEAR);
        int month = mcurrentDate.get(Calendar.MONTH);
        int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker1 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                String dob = String.valueOf(new StringBuilder().append(selectedday).append("-").append(selectedmonth + 1).append("-").append(selectedyear));
                Log.d("dob", dob);
                Log.d("dob", AppUtils.formatDate(selectedyear, selectedmonth, selectedday));

                selectedDate[0] = AppUtils.formatDate(selectedyear, selectedmonth, selectedday);

                textView.setText(selectedDate[0]);

            }

        }, year, month, day);
        //mDatePicker1.setTitle("Select Date");

        mDatePicker1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });

        // TODO Hide Future Date Here
        mDatePicker1.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

        // TODO Hide Past Date Here
        //set min todays date
        //mDatePicker1.getDatePicker().setMinDate(System.currentTimeMillis());

        mDatePicker1.show();

        return selectedDate[0];
    }

    private static String formatNewDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        return sdf.format(date);
    }


    public static String getDateInFormat() {

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }

    public static String dateToTimestamp(String time) {

        Timestamp ts=null;  //declare timestamp
        Date d=new Date(time); // Intialize date with the string date
        if(d!=null){  // simple null check
            ts=new Timestamp(d.getTime()); // convert gettime from date and assign it to your timestamp.
        }

        return ts.toString();
    }

    public static String changeDateToTimestamp(String time) {

        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy, hh:mm aa");
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;

        return String.valueOf(timestamp);
    }

    public static String changeDateToTimestamp2(String time) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, hh:mm aa");
        Date date = null;
        try {
            date = (Date)formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;

        return String.valueOf(timestamp);
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public static final String md5(String str) {
        //final String s = context.getPackageName();
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            //Log.v("md5",hexString.toString());
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    public static String getMd5(String result,String breakFrom) {

        String md5String="";

        String[] separated = result.split(breakFrom);
        String newResult = separated[1];

        String[] separated2 = newResult.split("],");

        String finalResult = separated2[0]+"]";

        Log.d(breakFrom,finalResult);
        Log.d("md5", AppUtils.md5(finalResult));

        md5String = md5(finalResult);

        return md5String;
    }


    public static float getQuantity(int weight,int age) {

        float quantity=0;

        if(weight<1500)
        {
            if(age<6)
            {
                float exp1 = (float)weight / 1000;
                float exp2 = 80+(age*15);
                quantity = exp1*exp2;
            }
            else
            {
                float exp1 = (float)weight / 1000;
                float exp2 = 150;
                quantity = exp1*exp2;
            }
        }
        else if(weight>1500)
        {
            if(age<7)
            {
                float exp1 = (float)weight / 1000;
                float exp2 = 60+(age*15);
                quantity = exp1*exp2;
            }
            else
            {
                float exp1 = (float)weight / 1000;
                float exp2 = 150;
                quantity = exp1*exp2;
            }
        }

        return quantity/2;
    }

    public static float getSSTTime(String stDnT, String endSnT) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(stDnT);
            Log.i("startDate", startDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = simpleDateFormat.parse(endSnT);
            Log.i("endDate", endDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = endDate.getTime() - startDate.getTime();
        Log.i("log_tag", "difference: " + difference);

        float days = (difference / (1000 * 60 * 60 * 24));
        float hours = ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        float min = (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);

        return hours;
    }

    public static void deleteDirectory(File path) {
        path.delete();
    }

    public static boolean isValidMobileNo(String number) {
        // The given argument to compile() method
        // is regular expression. With the help of
        // regular expression we can validate mobile
        // number.
        // 1) Begins with 0 or 91
        // 2) Then contains 6 or 7 or 8 or 9.
        // 3) Then contains 10 digits
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{10}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(number);
        return (m.find() && m.group().equals(number));
    }

    public static String getBase64FromImageURL(final String imageUrl) {

        String base64 = "";
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Do network action in this function
        try {
            // Reading a Image file from file system
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();

            BufferedInputStream imageInFile = new BufferedInputStream(url.openConnection().getInputStream());

            //    FileInputStream imageInFile = new FileInputStream(is.toString());
            byte imageData[] = new byte[2048];
            imageInFile.read(imageData);

            // Converting Image byte array into Base64 String
            base64 = Base64.encodeToString(imageData,0);

            System.out.println("imageDataString : " + base64);

            System.out.println("Image Successfully Manipulated!");

            return base64;
        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        } catch (Exception e){
            e.printStackTrace();
        }

        return base64;

    }

    /**
     * This method can be check internet connection is available or not.
     *
     * @param mActivity reference of activity.
     * @return
     */
    public static boolean isNetworkAvailable(@NonNull Context mActivity) {

        boolean available = false;
        /** Getting the system's connectivity service */
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        /** Getting active network interface to get the network's staffMobile */
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                available = true;
                AppUtils.print("====activeNetwork" + activeNetwork.getTypeName());
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                available = true;
                AppUtils.print("====activeNetwork" + activeNetwork.getTypeName());
            }
        } else {
            // not connected to the internet
            available = false;
            AppUtils.print("====not connected to the internet");
        }
        /** Returning the staffMobile of the network */
        return available;
    }

    public static void checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            RuntimePermissionHelper runtimePermissionHelper = RuntimePermissionHelper.getInstance(activity);
            if (runtimePermissionHelper.isAllPermissionAvailable()) {
                // All permissions available. Go with the flow
            } else {
                // Few permissions not granted. Ask for ungranted permissions
                runtimePermissionHelper.setActivity(activity);
                runtimePermissionHelper.requestPermissionsIfDenied();
            }
        }

    }

}
