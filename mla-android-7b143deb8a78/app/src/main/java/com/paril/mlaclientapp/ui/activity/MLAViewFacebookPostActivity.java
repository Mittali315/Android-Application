package com.paril.mlaclientapp.ui.activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.gson.GsonBuilder;

import android.widget.EditText;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAFacebookDetails;

import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.util.PrefsManager;
import com.paril.mlaclientapp.webservice.Api;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static com.paril.mlaclientapp.ui.activity.Wallet.blockchain;

public class MLAViewFacebookPostActivity extends BaseActivity {

private MLAFacebookDetails userDetails;

    EditText txtPostmsg;
    EditText txtPostwith;

    private boolean isToAdd = false;
    private boolean enabledEditMode = false;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_facebook_post_display);
        isToAdd = getIntent().getBooleanExtra(CommonUtils.EXTRA_IS_TO_ADD, false);
        txtPostmsg = (EditText) findViewById(R.id.mla_facebook_txtAddress);
        txtPostwith = (EditText) findViewById(R.id.mla_facebook_postwith);

      // spnrStudentuserId = (Spinner) findViewById(R.id.mla_facebook_spnrStudentId);

//        MLAGetAllGroupsAPI getUserDetails = new MLAGetAllGroupsAPI(this);
//        getUserDetails.execute();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!isToAdd) {
            userDetails = (MLAFacebookDetails) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
            enabledEditMode = getIntent().getBooleanExtra(CommonUtils.EXTRA_EDIT_MODE, false);
            enableFields(enabledEditMode);
        } else {
            setToolbarTitle("Add Facebook Post");
        }
    }
    private void setUpData() {

        txtPostmsg.setText(userDetails.getPostmsg());
        txtPostwith.setText(userDetails.getIdGroup());
       // spnrStudentuserId.setEnabled(false);
    }
    private void enableFields(boolean makeEditable) {
        txtPostmsg.setEnabled(makeEditable);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (!isToAdd) {
//            if (enabledEditMode) {
//                menu.findItem(R.id.menu_edit).setVisible(false);
//                menu.findItem(R.id.menu_cancel).setVisible(true);
//                menu.findItem(R.id.menu_save).setVisible(true);
//            } else {
//                menu.findItem(R.id.menu_edit).setVisible(true);
//                menu.findItem(R.id.menu_cancel).setVisible(false);
//                menu.findItem(R.id.menu_save).setVisible(false);
//            }
//        } else {
        if (isToAdd) {
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_cancel).setVisible(false);
            menu.findItem(R.id.menu_save).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_edit) {
            enabledEditMode = true;
            invalidateOptionsMenu();
            enableFields(enabledEditMode);
        } else if (item.getItemId() == R.id.menu_cancel) {
            enabledEditMode = false;
            invalidateOptionsMenu();
            enableFields(enabledEditMode);
            setUpData();
        } else if (item.getItemId() == R.id.menu_save) {

            hideKeyboard();
            if (isToAdd) {
                if (TextUtils.isEmpty(txtPostmsg.getText().toString())) {
                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_facebook_coordinatorLayout));

                }
                else {
                    if (CommonUtils.checkInternetConnection(MLAViewFacebookPostActivity.this)) {
                        MLAAddFacebookPostAPI addAdminTask = new MLAAddFacebookPostAPI(MLAViewFacebookPostActivity.this);
                        addAdminTask.execute();
                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_facebook_coordinatorLayout));
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
            class MLAAddFacebookPostAPI extends AsyncTask<Void, Void, String> {
                Context context;
                MLAFacebookDetails userDetails = new MLAFacebookDetails();

                public MLAAddFacebookPostAPI(Context ctx) {
                    context = ctx;
                }

                @Override
                protected void onPreExecute() {
                 Cryptography aes=new Cryptography(context);
                 String postmsg= txtPostmsg.getText().toString();
                 byte[] x=postmsg.getBytes();
                 String s = aes.encryptData(postmsg);

                 System.out.println("print s :" +s);
                 userDetails.setPostmsg(s);

                 String xd=aes.decryptData(s);
                 System.out.print("Decrypt :" +xd);
                 userDetails.setIdGroup(txtPostwith.getText().toString());

                    //this is digital key in actual
                    String Digitalmsg =RsaEncryption.generateDigitalsignature(postmsg);

                  //This is session key in actual
                  String sesionkey=RsaEncryption.getSessionkey();

                 //  userDetails.setDigitalSignature(sesionkey);//sessionkey
                 userDetails.setSessionKey(Digitalmsg);//digitalmsg

                    //BlockChanin
                    Wallet.addBlock(new Block(postmsg, "0"));
                    String blockchainJson = StringUtil.getJson(blockchain);
                    userDetails.setDigitalSignature(blockchainJson);


                 showProgressDialog("Add Facebook user Posttt  User Data...");
                }

                @Override
                protected void onPostExecute(String statusCode) {
                    hideProgressDialog();
                    if (statusCode.equals("201")) //the item is created
                    {
                        finish();
                    } else {
                        showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_facebook_coordinatorLayout));
                    }
                }

                @Override
                protected String doInBackground(Void... params) {
                    Call<MLAFacebookDetails> callAddFacebookPost = Api.getClient().addFacebook(userDetails.getPostmsg(),userDetails.getDigitalSignature(),userDetails.getSessionKey(),userDetails.getIdGroup());

                    try {
                        Response<MLAFacebookDetails> respCallAdmin = callAddFacebookPost.execute();
                        return "202" + respCallAdmin.code();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "";
                }
            }


}
