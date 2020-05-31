package com.paril.mlaclientapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.util.Attributes;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAGroupDetails1;
import com.paril.mlaclientapp.ui.activity.MLAHomeActivity;
import com.paril.mlaclientapp.ui.activity.MLAViewGroupActivity;
import com.paril.mlaclientapp.ui.adapter.MLAGroupAdapter;
import com.paril.mlaclientapp.ui.adapter.OnItemClickListener;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.util.PrefsManager;
import com.paril.mlaclientapp.util.UserTypeData;
import com.paril.mlaclientapp.util.VerticalSpaceItemDecoration;
import com.paril.mlaclientapp.webservice.Api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by paril on 7/14/2017.
 */
public class MLAGroupViewFragment extends Fragment {

    com.paril.mlaclientapp.ui.view.EmptyRecyclerView recyclerViewGroups;
    MLAGroupAdapter groupDisplayAdapter;
    private PrefsManager manager;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mla_grouplist, container, false);
        recyclerViewGroups = (com.paril.mlaclientapp.ui.view.EmptyRecyclerView) view.findViewById(R.id.mla_display_group_recyyclerView);
        recyclerViewGroups.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewGroups.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.divider_list)));
        manager = new PrefsManager(getActivity());
        FloatingActionButton btnAddStudent = (FloatingActionButton) view.findViewById(R.id.fragment_display_group_fabAddUser);

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), MLAViewGroupActivity.class);
                intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, true);
                intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, false);
                startActivity(intent);
            }
        });

        if (manager.getStringData("userType").equals(UserTypeData.STUDENT)) {
            btnAddStudent.setVisibility(View.GONE);
        } else {
            btnAddStudent.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonUtils.checkInternetConnection(getActivity())) {
            MLAGetAllGroupDetailsAPI getSubjectDetails = new MLAGetAllGroupDetailsAPI(this.getActivity());
            getSubjectDetails.execute();
        } else {
            ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.check_connection), view.findViewById(R.id.fragment_display_group_coordinatorLayout));
        }

    }

    class MLAGetAllGroupDetailsAPI extends AsyncTask<Void, Void, List<MLAGroupDetails1>> {
        Context context;

        public MLAGetAllGroupDetailsAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            ((MLAHomeActivity) getActivity()).showProgressDialog("Getting Group Data...");
        }

        @Override
        protected void onPostExecute(List<MLAGroupDetails1> listGroupDetail) {

            ((MLAHomeActivity) getActivity()).hideProgressDialog();
            List<MLAGroupDetails1> listGroupDetails = new ArrayList<MLAGroupDetails1>();
            if (listGroupDetail != null) {
                listGroupDetails = listGroupDetail;
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.server_error), getView().findViewById(R.id.fragment_display_group_coordinatorLayout));
            }

            groupDisplayAdapter = new MLAGroupAdapter(context, listGroupDetails, manager.getStringData("userType").equals(UserTypeData.ADMIN), new OnItemClickListener<MLAGroupDetails1>() {
                @Override
                public void onItemClick(MLAGroupDetails1 item, int resourceId) {
                    Log.d("OnItemClick", "resource:" + resourceId);
                    if (resourceId == R.id.group_item_display_layout_imgEditUser) {
                        final Intent intent = new Intent(getActivity(), MLAViewGroupActivity.class);
                        intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, false);
                        intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, true);
                        intent.putExtra(CommonUtils.EXTRA_USER_ADMIN_DATA, item);
                        startActivity(intent);
                    }
//                    else if (resourceId == R.id.subject_item_display_layout_imgDeleteUser) {
//                        if (CommonUtils.checkInternetConnection(getActivity())) {
//                            MLADeleteGroupAPI deleteSubjectTask = new MLADeleteGroupAPI(MLAGroupViewFragment
//                                    .this.getActivity());
//                            deleteSubjectTask.execute(item.getIdSubject());
//                        } else {
//                            ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.check_connection), view.findViewById(R.id.fragment_display_subject_coordinatorLayout));
//                        }
//
//                    }
                    else if (resourceId == R.id.group_item_display_layout_swipeParent) {
                        final Intent intent = new Intent(getActivity(), MLAViewGroupActivity.class);
                        intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, false);
                        intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, false);
                        intent.putExtra(CommonUtils.EXTRA_USER_ADMIN_DATA, item);
                        startActivity(intent);
                    }
                }
            });
            groupDisplayAdapter.setMode(Attributes.Mode.Single);
            recyclerViewGroups.setAdapter(groupDisplayAdapter);
            recyclerViewGroups.setEmptyView(getView().findViewById(R.id.fragment_display_group_relEmptyView));
        }

        @Override
        protected List<MLAGroupDetails1> doInBackground(Void... params) {

            try {
                Call<List<MLAGroupDetails1>> callSubjectData = Api.getClient().getAllGroup();
                Response<List<MLAGroupDetails1>> responseSubjectData = callSubjectData.execute();
                if (responseSubjectData.isSuccessful() && responseSubjectData.body() != null) {
                    return responseSubjectData.body();
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



}
