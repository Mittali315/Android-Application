package com.paril.mlaclientapp.ui.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAGroupDetails1;

import com.paril.mlaclientapp.model.MLARegisterUsers;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.util.PrefsManager;
import com.paril.mlaclientapp.webservice.Api;
import com.paril.mlaclientapp.model.MLAStudentDetails;
import com.paril.mlaclientapp.model.MLARegisterUsers;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MLAViewGroupActivity extends BaseActivity {

    EditText txtGroupId;
    EditText txtGroupName;
    Spinner spnrStudentId;
    MLAGroupDetails1 groupDetails1;

    boolean isToAdd = false, enabledEditMode = false;
    List<MLAStudentDetails> instDetails = new ArrayList<MLAStudentDetails>();
    List<MLARegisterUsers> instDetails1 = new ArrayList<MLARegisterUsers>();
    PrefsManager prefsManager;
    String[] instId;
    String[] instId1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_groupdisplay);
        isToAdd = getIntent().getBooleanExtra(CommonUtils.EXTRA_IS_TO_ADD, false);

        txtGroupId = (EditText) findViewById(R.id.mla_Group_txtGroupid);
        txtGroupName= (EditText) findViewById(R.id.mla_group_txtGroupversion);
        spnrStudentId = (Spinner) findViewById(R.id.mla_group_spnrStudentId);

        MLAGetAllStudentAPI getUserDetails = new MLAGetAllStudentAPI(this);
       // MLAGetAllPublickeyAPI getUserDetails1 = new MLAGetAllPublickeyAPI(this);
        getUserDetails.execute();
      //  getUserDetails1.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!isToAdd) {
            groupDetails1 = (MLAGroupDetails1) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
            enabledEditMode = getIntent().getBooleanExtra(CommonUtils.EXTRA_EDIT_MODE, false);
            enableFields(enabledEditMode);
        } else {
            setToolbarTitle("Add Group Post");
        }
    }

    private void setUpData() {

        txtGroupId.setText(groupDetails1.getIdGroup());
        txtGroupName.setText(groupDetails1.getGroupName());
       // txtStatus.setText(groupDetails1.getStatus());
        spnrStudentId.setEnabled(false);

    }
    private void enableFields(boolean makeEditable) {
        txtGroupId.setEnabled(makeEditable);
        txtGroupName.setEnabled(makeEditable);
       // txtStatus.setEnabled(makeEditable);

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

                if (txtGroupId.getText().toString().equals("")) {

                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_group_coordinatorLayout));

                  }

                else {
                    if (CommonUtils.checkInternetConnection(MLAViewGroupActivity.this)) {
                        MLAAddGroupAPI addGroupTask = new MLAAddGroupAPI(MLAViewGroupActivity.this);
                        addGroupTask.execute();
                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_group_coordinatorLayout));
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class MLAAddGroupAPI extends AsyncTask<Void, Void, String> {
        Context context;
        MLAGroupDetails1 groupDetails1 = new MLAGroupDetails1();
        public MLAAddGroupAPI(Context ctx)
        {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            groupDetails1.setIdGroup(txtGroupId.getText().toString());
            groupDetails1.setGroupName(txtGroupName.getText().toString());
            String groupkey=RsaEncryption.getRandomKey();

            groupDetails1.setGroupKey(groupkey);
            groupDetails1.setUserId(Integer.parseInt(spnrStudentId.getSelectedItem().toString()));

//            int x= Integer.parseInt(spnrStudentId.getSelectedItem().toString());
//            MLAGetAllPublickeyAPI();
            showProgressDialog("Adding Group...");



        }
        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("201")) //the item is created
            {
                finish();
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_group_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Call<MLAGroupDetails1> callAddGroup = Api.getClient().addGrouptable(groupDetails1.getIdGroup(),groupDetails1.getGroupName(),groupDetails1.getUserId(),groupDetails1.getGroupKey());
            try {
                Response<MLAGroupDetails1> respCallGroup= callAddGroup.execute();
                if (respCallGroup.isSuccessful())
                    return "202";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    class MLAGetAllStudentAPI extends AsyncTask<Void, Void, List<MLAStudentDetails>> {
        Context context;

        public MLAGetAllStudentAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("Getting Student User Data...");
        }

        @Override
        protected void onPostExecute(List<MLAStudentDetails> userDetails) {

            hideProgressDialog();
            if (userDetails != null) {
                instDetails = userDetails;
                instId = new String[instDetails.size()];

                for (int i = 0; i < instDetails.size(); i++) {
                  // instId[i] = instDetails.get(i).idStudent;
                   int x= instDetails.get(i).userId;
                    instId[i]= Integer.toString(x);

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MLAViewGroupActivity.this, android.R.layout.simple_spinner_dropdown_item, instId);

               spnrStudentId.setAdapter(adapter);
                if (!isToAdd) {
                    groupDetails1 = (MLAGroupDetails1) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
                    //setToolbarTitle(groupDetails1.getTitle());
                    setUpData();
                    enableFields(enabledEditMode);
                } else {
                    setToolbarTitle("Add Group");
                }
            } else {

                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_subject_coordinatorLayout));
            }
        }

        @Override
        protected List<MLAStudentDetails> doInBackground(Void... params) {

            try {
                Call<List<MLAStudentDetails>> callInstUserData = Api.getClient().getStudents();
                Response<List<MLAStudentDetails>> responseInstUser = callInstUserData.execute();
                if (responseInstUser.isSuccessful() && responseInstUser.body() != null) {
                    return responseInstUser.body();
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                return null;

            } catch (IOException e) {
                return null;
            }
        }
    }
    //

//    class MLAGetAllPublickeyAPI extends AsyncTask<Void, Void, List<MLARegisterUsers>> {
//        Context context;
//
//        public MLAGetAllPublickeyAPI(Context ctx) {
//            context = ctx;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            showProgressDialog("Getting Student User Data...");
//        }
//
//        @Override
//        protected void onPostExecute(List<MLARegisterUsers> userDetails1) {
//
//            hideProgressDialog();
//            if (userDetails1 != null) {
//                instDetails1 = userDetails1;
//                instId1 = new String[instDetails1.size()];
//
//                for (int i = 0; i < instDetails1.size(); i++) {
//                    instId1[i] = instDetails1.get(i).publicKey;
//
//
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MLAViewGroupActivity.this, android.R.layout.simple_spinner_dropdown_item, instId);
//
//                spnrStudentId.setAdapter(adapter);
//                if (!isToAdd) {
//                    groupDetails1 = (MLAGroupDetails1) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
//                    //setToolbarTitle(groupDetails1.getTitle());
//                    setUpData();
//                    enableFields(enabledEditMode);
//                } else {
//                    setToolbarTitle("Add Group");
//                }
//            } else {
//
//                showSnackBar(getString(R.string.server_error), findViewById(R.id.activity_view_subject_coordinatorLayout));
//            }
//        }
//
//        @Override
//        protected List<MLARegisterUsers> doInBackground(Void... params) {
//
//            try {
//                Call<List<MLARegisterUsers>> callInstUserData1 = Api.getClient().publicuser();
//                Response<List<MLARegisterUsers>> responseInstUser1 = callInstUserData1.execute();
//                if (responseInstUser1.isSuccessful() && responseInstUser1.body() != null) {
//                    return responseInstUser1.body();
//                } else {
//                    return null;
//                }
//
//            } catch (MalformedURLException e) {
//                return null;
//
//            } catch (IOException e) {
//                return null;
//            }
//        }
//    }





}
