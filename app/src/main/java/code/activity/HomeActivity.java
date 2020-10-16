package code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alien.inventory.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dd.processbutton.FlatButton;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import code.common.AppSettings;
import code.common.BaseActivity;
import code.utils.AppConstants;
import code.utils.AppUrls;
import code.utils.AppUtils;

public class HomeActivity extends BaseActivity implements View.OnClickListener {


    //ActionProcessButton
    ActionProcessButton abSave, abReadOrWrite, abValidiate;

    //EditText
    EditText etPrintMethodNumber, etJob, etRackId;

    //TextView
    TextView tvReason;

    //FlatButton for clicks
    FlatButton fbClear,fbConfig,fbExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //EditText
        etPrintMethodNumber =  findViewById(R.id.etPrintMethodNumber);
        etJob =  findViewById(R.id.etJob);
        etRackId =  findViewById(R.id.etRackId);

        //TextView
        tvReason =  findViewById(R.id.tvReason);
        tvReason.setVisibility(View.INVISIBLE);

        //ActionProcessButton
        abReadOrWrite = findViewById(R.id.abReadOrWrite);
        abSave = findViewById(R.id.abSave);
        abValidiate = findViewById(R.id.abValidiate);

        //FlatButton
        fbClear = findViewById(R.id.fbClear);
        fbConfig = findViewById(R.id.fbConfig);
        fbExit = findViewById(R.id.fbExit);

        //Set Mode for ActionProcessButton
        abSave.setMode(ActionProcessButton.Mode.ENDLESS);
        abValidiate.setMode(ActionProcessButton.Mode.ENDLESS);

        //setOnClickListener
        abReadOrWrite.setOnClickListener(this);
        abSave.setOnClickListener(this);
        abValidiate.setOnClickListener(this);
        fbClear.setOnClickListener(this);
        fbConfig.setOnClickListener(this);
        fbExit.setOnClickListener(this);

        /*etPrintMethodNumber.setText("363000438");
        etJob.setText("000010001");
        etRackId.setText("TK24796");*/

        /*etPrintMethodNumber.setText("156000322");
        etJob.setText("100000003");
        etRackId.setText("TK24796");*/

        //hideSoftKeyboard
        AppUtils.hideSoftKeyboard(mActivity);

    }


    @Override
    protected void onResume() {
        super.onResume();

        abValidiate.setProgress(0);
        abSave.setProgress(0);

        abSave.setVisibility(View.GONE);
        abValidiate.setVisibility(View.VISIBLE);

        tvReason.setVisibility(View.INVISIBLE);

        /*etPrintMethodNumber.setText("");
        etJob.setText("");
        etRackId.setText("");*/

        abReadOrWrite.setText(getString(R.string.read));
    }

    public void clear() {

        abValidiate.setProgress(0);
        abValidiate.setProgress(0);
        abSave.setProgress(0);

        abSave.setVisibility(View.GONE);
        abValidiate.setVisibility(View.VISIBLE);

        tvReason.setVisibility(View.INVISIBLE);

        etPrintMethodNumber.setText("");
        etJob.setText("");
        etRackId.setText("");

        etPrintMethodNumber.setText("");
        abReadOrWrite.setText(getString(R.string.read));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fbClear:

                clear();

                break;

            case R.id.fbExit:

                AppSettings.putString(AppSettings.servernameOrIP, "");
                AppSettings.putString(AppSettings.dbName, "");
                AppSettings.putString(AppSettings.port, "");
                AppSettings.putString(AppSettings.username, "");
                AppSettings.putString(AppSettings.password, "");

                finish();

                break;

            case R.id.fbConfig:

                startActivity(new Intent(mActivity, ConfigActivity.class));

                break;

            case R.id.abSave:

                abSave.setProgress(10);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        abSave.setProgress(0);
                        clear();
                    }
                }, 2000);

                break;

            case R.id.abValidiate:
                
                abValidiate.setProgress(10);
                tvReason.setVisibility(View.INVISIBLE);

                if (AppSettings.getString(AppSettings.dbName).isEmpty()) {
                    abValidiate.setProgress(-1);
                    tvReason.setVisibility(View.VISIBLE);
                    tvReason.setText("Config Database First");
                    return;
                }

                if (etRackId.getText().toString().equals("")) {
                    abValidiate.setProgress(-1);
                    tvReason.setVisibility(View.VISIBLE);
                    tvReason.setText(getString(R.string.rackIdRequired));
                    return;
                }

                if (etJob.getText().toString().equals("")) {
                    abValidiate.setProgress(-1);
                    tvReason.setVisibility(View.VISIBLE);
                    tvReason.setText(getString(R.string.jobRequired));
                    return;
                }


                if (etPrintMethodNumber.getText().toString().equals("")) {
                    abValidiate.setProgress(-1);
                    tvReason.setVisibility(View.VISIBLE);
                    tvReason.setText(getString(R.string.printRequired));
                    return;
                }

                if (AppUtils.isNetworkAvailable(mActivity)) {
                    searchDataApi();
                } else {
                    AppUtils.showToastSort(mActivity, getString(R.string.errorInternet));
                }


                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        abValidiate.setProgress(0);
                        abSave.setVisibility(View.VISIBLE);
                        abValidiate.setVisibility(View.GONE);
                        abReadOrWrite.setText(getString(R.string.write));
                    }
                }, 2000);*/



                break;
        }
    }

    //Webservice call for searching data to READ and WRITE
    private void searchDataApi() {

        AppUtils.hideSoftKeyboard(mActivity);
        AppUtils.showRequestDialog(mActivity);

        String url = AppUrls.searchData;
        Log.v("searchDataApi-URL", url);

        JSONObject json = new JSONObject();
        JSONObject jsonData = new JSONObject();

        try {

            jsonData.put("username",        AppSettings.getString(AppSettings.username));
            jsonData.put("password",        AppSettings.getString(AppSettings.password));
            jsonData.put("databaseName",    AppSettings.getString(AppSettings.dbName));
            jsonData.put("hostname",        AppSettings.getString(AppSettings.servernameOrIP));
            jsonData.put("rackId",          etRackId.getText().toString().trim());
            jsonData.put("job",             etJob.getText().toString().trim());
            jsonData.put("paintNumber",     etPrintMethodNumber.getText().toString().trim());

            json.put(AppConstants.projectName, jsonData);

            Log.v("searchDataApi", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(url)
                .addJSONObjectBody(json)
                .setPriority(Priority.HIGH)
                .setTag("searchDataApi")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJSON(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        AppUtils.hideDialog();

                        abValidiate.setProgress(-1);
                        tvReason.setVisibility(View.VISIBLE);
                        tvReason.setText("No Data Found");

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
                AppUtils.showToastSort(mActivity,"Connection has been Established Successfully");

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                AppUtils.showToastSort(mActivity,jsonArray.toString());

                abValidiate.setProgress(0);
                abSave.setVisibility(View.VISIBLE);
                abValidiate.setVisibility(View.GONE);
                abReadOrWrite.setText(getString(R.string.write));

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject dataJsonObject = jsonArray.getJSONObject(i);


                }
            }
            else
            {
                AppUtils.showToastSort(mActivity, jsonObject.getString("resMsg"));
                abValidiate.setProgress(-1);
                tvReason.setVisibility(View.VISIBLE);
                tvReason.setText("No Data Found");
            }

        } catch (JSONException e) {
            e.printStackTrace();

            abValidiate.setProgress(-1);
            tvReason.setVisibility(View.VISIBLE);
            tvReason.setText("No Data Found");
        }
    }
}
