package com.paril.mlaclientapp.ui.activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.util.Base64;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.reinaldoarrosi.maskededittext.MaskedEditText;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAStudentDetails;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.webservice.Api;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

import java.security.PublicKey;

public class MLAViewStudentActivity extends BaseActivity {
    private MLAStudentDetails userDetails;
    TextView txtUserId;
    EditText txtUserName;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtEmailId;
    MaskedEditText txtTelephone;
    EditText txtAliasMailId;
    EditText txtAddress;
    EditText txtHangoutId;
    EditText txtPassword;

    private boolean isToAdd = false;

    private boolean enabledEditMode = false;
    private LinearLayout linUserIdCont, linUserNameCont, linPasswordCont;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_studentdisplay);
        isToAdd = getIntent().getBooleanExtra(CommonUtils.EXTRA_IS_TO_ADD, false);

        txtPassword = (EditText) findViewById(R.id.mla_student_txtPassword);
        txtUserId = (TextView) findViewById(R.id.mla_student_txtUserId);
        txtUserName = (EditText) findViewById(R.id.mla_student_txtUserName);
        txtFirstName = (EditText) findViewById(R.id.mla_student_txtFirstName);
        txtLastName = (EditText) findViewById(R.id.mla_student_txtLastName);
        txtEmailId = (EditText) findViewById(R.id.mla_student_txtEmailId);
        txtTelephone = (MaskedEditText) findViewById(R.id.mla_student_txtTelephone);
        txtAliasMailId = (EditText) findViewById(R.id.mla_student_txtAliasMailId);
        txtAddress = (EditText) findViewById(R.id.mla_student_txtAddress);
        txtHangoutId = (EditText) findViewById(R.id.mla_student_txtHangoutId);
        linUserIdCont = (LinearLayout) findViewById(R.id.activity_view_admin_linUserId);
        linUserNameCont = (LinearLayout) findViewById(R.id.activity_view_admin_linUserName);
        linPasswordCont = (LinearLayout) findViewById(R.id.activity_view_admin_linPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (!isToAdd) {
            userDetails = (MLAStudentDetails) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
            setToolbarTitle(userDetails.getFirstName() + " " + userDetails.getLastName());
            setUpData();
            enabledEditMode = getIntent().getBooleanExtra(CommonUtils.EXTRA_EDIT_MODE, false);
            enableFields(enabledEditMode);
            linUserIdCont.setVisibility(View.VISIBLE);
            linUserNameCont.setVisibility(View.VISIBLE);
            linPasswordCont.setVisibility(View.GONE);
        } else {
            setToolbarTitle("Add Student");
            linUserIdCont.setVisibility(View.GONE);
            txtUserName.setEnabled(true);
            linUserNameCont.setVisibility(View.VISIBLE);
            linPasswordCont.setVisibility(View.VISIBLE);
        }
    }
    private void setUpData() {
        txtUserId.setText("" + userDetails.getUserId());
        txtUserName.setText(userDetails.getIdStudent());
        txtFirstName.setText(userDetails.getFirstName());
        txtLastName.setText(userDetails.getLastName());
        txtEmailId.setText(userDetails.getEmailId());
        txtTelephone.setText(userDetails.getTelephone());
        txtAliasMailId.setText(userDetails.getAliasMailId());
        txtAddress.setText(userDetails.getAddress());
        txtHangoutId.setText(userDetails.getSkypeId());
    }

    private void enableFields(boolean makeEditable) {
        txtAddress.setEnabled(makeEditable);
        txtAliasMailId.setEnabled(makeEditable);
        txtEmailId.setEnabled(makeEditable);
        txtFirstName.setEnabled(makeEditable);
        txtLastName.setEnabled(makeEditable);
        txtHangoutId.setEnabled(makeEditable);
        txtTelephone.setEnabled(makeEditable);
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
        if (isToAdd){
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
                if (TextUtils.isEmpty(txtAddress.getText().toString()) ||
                        TextUtils.isEmpty(txtUserName.getText().toString()) ||
                        TextUtils.isEmpty(txtEmailId.getText().toString()) ||
                        TextUtils.isEmpty(txtAliasMailId.getText().toString()) ||
                        TextUtils.isEmpty(txtLastName.getText().toString()) ||
                        TextUtils.isEmpty(txtTelephone.getText(true).toString()) ||
                        TextUtils.isEmpty(txtHangoutId.getText().toString()) ||
                        TextUtils.isEmpty(txtFirstName.getText().toString()) ||
                        TextUtils.isEmpty(txtPassword.getText().toString())) {
                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_student_coordinatorLayout));

                }
                else if (!CommonUtils.isValidMail(txtEmailId.getText().toString()) ||
                        !CommonUtils.isValidMail(txtAliasMailId.getText().toString()) ||
                        !CommonUtils.isValidMobile(txtTelephone.getText(true).toString())) {
                    if (!CommonUtils.isValidMail(txtEmailId.getText().toString())) {
                        txtEmailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));
                    } else if (!CommonUtils.isValidMail(txtAliasMailId.getText().toString())) {
                        txtAliasMailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_alt_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));
                    } else {
                        txtTelephone.requestFocus();
                        showSnackBar(getString(R.string.invalid_phone_no), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }
                }
                else {
                    if (CommonUtils.checkInternetConnection(MLAViewStudentActivity.this)) {
                        MLAAddStudentAPI addAdminTask = new MLAAddStudentAPI(MLAViewStudentActivity.this);
                        addAdminTask.execute();
                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }
                }

            }
            else {

                if (TextUtils.isEmpty(txtAddress.getText().toString()) ||
                        TextUtils.isEmpty(txtUserName.getText().toString()) ||
                        TextUtils.isEmpty(txtEmailId.getText().toString()) ||
                        TextUtils.isEmpty(txtAliasMailId.getText().toString()) ||
                        TextUtils.isEmpty(txtLastName.getText().toString()) ||
                        TextUtils.isEmpty(txtTelephone.getText(true).toString()) ||
                        TextUtils.isEmpty(txtHangoutId.getText().toString()) ||
                        TextUtils.isEmpty(txtFirstName.getText().toString()) ) {
                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_student_coordinatorLayout));
                } else if (!CommonUtils.isValidMail(txtEmailId.getText().toString()) ||
                        !CommonUtils.isValidMail(txtAliasMailId.getText().toString()) ||
                        !CommonUtils.isValidMobile(txtTelephone.getText(true).toString())) {
                    if (!CommonUtils.isValidMail(txtEmailId.getText().toString())) {
                        txtEmailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));

                    } else if (!CommonUtils.isValidMail(txtAliasMailId.getText().toString())) {
                        txtAliasMailId.requestFocus();
                        showSnackBar(getString(R.string.invalid_alt_email_id), findViewById(R.id.activity_view_student_coordinatorLayout));
                    } else {
                        txtTelephone.requestFocus();
                        showSnackBar(getString(R.string.invalid_phone_no), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }

                }
                else {
                    if (CommonUtils.checkInternetConnection(MLAViewStudentActivity.this)) {
                        MLAUpdateStudentAPI updateAdminTask = new MLAUpdateStudentAPI(MLAViewStudentActivity.this);
                        updateAdminTask.execute();
                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_student_coordinatorLayout));
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    class MLAUpdateStudentAPI extends AsyncTask<Void, Void, String> {
        Context context;
        private MLAStudentDetails details;

        public MLAUpdateStudentAPI(Context ctx) {
            context = ctx;
            details = new MLAStudentDetails();
            details.setAddress(txtAddress.getText().toString());
            details.setAliasMailId(txtAliasMailId.getText().toString());
            details.setEmailId(txtEmailId.getText().toString());
            details.setFirstName(txtFirstName.getText().toString());
            details.setIdStudent(txtUserName.getText().toString());
            details.setLastName(txtLastName.getText().toString());
            details.setSkypeId(txtHangoutId.getText().toString());
            details.setTelephone(txtTelephone.getText(true).toString());
            details.setUserId(Integer.parseInt(txtUserId.getText().toString()));
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("Update Studentinnnnn User Data...");
        }

        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("202")) //the item is updated
            {
                finish();
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_instructor_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Call<String> callUpdate = Api.getClient().updateStudent(details);
            //Call<String> callUpdate = Api.getClient().
            try {
                Response<String> respUpdate = callUpdate.execute();
                return "202";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    class MLAAddStudentAPI extends AsyncTask<Void, Void, String> {
        Context context;
        MLAStudentDetails userDetails = new MLAStudentDetails();

        public MLAAddStudentAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userDetails.setTelephone(txtTelephone.getText(true).toString());
            userDetails.setIdStudent(txtUserName.getText().toString());
            userDetails.setSkypeId(txtHangoutId.getText().toString());
            userDetails.setLastName(txtLastName.getText().toString());
            userDetails.setAddress(txtAddress.getText().toString());
            userDetails.setAliasMailId(txtAliasMailId.getText().toString());
            userDetails.setFirstName(txtFirstName.getText().toString());
            userDetails.setEmailId(txtEmailId.getText().toString());
            userDetails.setPassword(txtPassword.getText().toString());

            PublicKey publicKey=  RsaEncryption.loadpairKeys(MLAViewStudentActivity.this,userDetails.getIdStudent());
            byte[] publicKeyBytes = Base64.encode(publicKey.getEncoded(),0);
            String  publickey= new String(publicKeyBytes);
            //System.out.print(" publickkey" +publickey);
            userDetails.setPublicKey(publickey);

            showProgressDialog("Add Student MIttttttttttt User Data...");
        }

        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("201")) //the item is created
            {
                finish();
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_student_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Call<MLAStudentDetails> callAddStudent = Api.getClient().addStudent(userDetails.getIdStudent(), userDetails.getPassword(), userDetails.getFirstName(), userDetails.getLastName(), userDetails.getTelephone(), userDetails.getAddress(), userDetails.getAliasMailId(), userDetails.getEmailId(), userDetails.getSkypeId(), userDetails.getPublicKey());
            try {

                Response<MLAStudentDetails> respCallAdmin = callAddStudent.execute();
                return "" + respCallAdmin.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
