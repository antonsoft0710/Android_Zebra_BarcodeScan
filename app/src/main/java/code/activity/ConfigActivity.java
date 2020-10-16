package code.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alien.inventory.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dd.processbutton.FlatButton;

import org.json.JSONException;
import org.json.JSONObject;

import code.common.AppSettings;
import code.common.BaseActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;

public class ConfigActivity extends BaseActivity implements View.OnClickListener {

    //EditText
    EditText etServerNameOrIP, etDbName, etPort, etUsername, etPasswrod;

    //FlatButton for clicks
    FlatButton fbTest,fbExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //EditText
        etServerNameOrIP =  findViewById(R.id.etServerNameOrIP);
        etDbName =  findViewById(R.id.etDbName);
        etPort =  findViewById(R.id.etPort);
        etUsername =  findViewById(R.id.etUsername);
        etPasswrod =  findViewById(R.id.etPasswrod);

        //FlatButton
        fbTest=  findViewById(R.id.fbTest);
        fbExit=  findViewById(R.id.fbExit);

        setValues();

        //CLickListner
        fbTest.setOnClickListener(this);
        fbExit.setOnClickListener(this);

        //hideSoftKeyboard
        AppUtils.hideSoftKeyboard(mActivity);
    }

    //Setting Values from SharedPreferences
    private void setValues() {

        etServerNameOrIP.setText(AppSettings.getString(AppSettings.servernameOrIP));
        etDbName.setText(AppSettings.getString(AppSettings.dbName));
        etPort.setText(AppSettings.getString(AppSettings.port));
        etUsername.setText(AppSettings.getString(AppSettings.username));
        etPasswrod.setText(AppSettings.getString(AppSettings.password));

        /*etServerNameOrIP.setText("localhost");
        etDbName.setText("qanvuste_print");
        etPort.setText(AppSettings.getString(AppSettings.port));
        etUsername.setText("qanvuste_print");
        etPasswrod.setText("lc3YaDgT");*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fbExit:

                finish();

                break;

            case R.id.fbTest:

                if(etServerNameOrIP.getText().toString().isEmpty())
                {
                    AppUtils.showToastSort(mActivity,getString(R.string.errorServerIp));
                }
                else if(etDbName.getText().toString().isEmpty())
                {
                    AppUtils.showToastSort(mActivity,getString(R.string.errorDbName));
                }
                else if(etPort.getText().toString().isEmpty())
                {
                    AppUtils.showToastSort(mActivity,getString(R.string.errorPort));
                }
                else if(etUsername.getText().toString().isEmpty())
                {
                    AppUtils.showToastSort(mActivity,getString(R.string.errorUsername));
                }
                else if(etPasswrod.getText().toString().isEmpty())
                {
                    AppUtils.showToastSort(mActivity,getString(R.string.errorPassword));
                }
                else if (AppUtils.isNetworkAvailable(mActivity)) {
                    connectDatabaseApi();
                } else {
                    AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                }

                break;
        }
    }

    //Webservice call for checking database access
    private void connectDatabaseApi() {

        AppSettings.putString(AppSettings.servernameOrIP, "");
        AppSettings.putString(AppSettings.dbName, "");
        AppSettings.putString(AppSettings.port, "");
        AppSettings.putString(AppSettings.username, "");
        AppSettings.putString(AppSettings.password, "");

        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.connect;
        Log.v("connectDatabaseApi-URL", url);

        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {

            jsonData.put("username",        etUsername.getText().toString().trim());
            jsonData.put("password",        etPasswrod.getText().toString().trim());
            jsonData.put("databaseName",    etDbName.getText().toString().trim());
            jsonData.put("hostname",        etServerNameOrIP.getText().toString().trim());

            json.put(AppConstants.projectName, jsonData);

            Log.v("connectDatabaseApi", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .setTag("connectDatabaseApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSON(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();

                        AppUtils.showToastSort(mActivity,"Connection Unsuccessfull");

                        // handle error
                        if (error.getErrorCode() != 0) {
                            //AppUtils.showToastSort(mActivity,String.valueOf(error.getErrorCode()));
                            Log.d("onError errorCode ", "onError errorCode : " + error.getErrorCode());
                            Log.d("onError errorBody", "onError errorBody : " + error.getErrorBody());
                            Log.d("onError errorDetail", "onError errorDetail : " + error.getErrorDetail());
                        } else {
                            //AppUtils.showToastSort(mActivity, String.valueOf(error.getErrorDetail()));
                        }
                    }
                });
    }

    //Parse Data coming from server in respinse
    private void parseJSON(JSONObject response){

        AppUtils.hideDialog();

        Log.d("response ", response.toString());

        try {
            JSONObject jsonObject= response.getJSONObject(AppConstants.projectName);

            if(jsonObject.getString("resCode").equals("1"))
            {
                AppSettings.putString(AppSettings.servernameOrIP, etServerNameOrIP.getText().toString());
                AppSettings.putString(AppSettings.dbName, etDbName.getText().toString());
                AppSettings.putString(AppSettings.port, etPort.getText().toString());
                AppSettings.putString(AppSettings.username, etUsername.getText().toString());
                AppSettings.putString(AppSettings.password, etPasswrod.getText().toString());

                AppUtils.showToastSort(mActivity,"Connection has been Established");

                finish();
            }
            else
            {
                AppUtils.showToastSort(mActivity, jsonObject.getString("resMsg"));
            }

        } catch (JSONException e) {
            e.printStackTrace();

            AppUtils.showToastSort(mActivity, getString(R.string.invalidCredentials));
        }
    }
}
