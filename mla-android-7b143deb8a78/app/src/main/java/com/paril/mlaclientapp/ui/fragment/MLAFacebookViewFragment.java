package com.paril.mlaclientapp.ui.fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import retrofit2.Call;
import retrofit2.Response;
import com.daimajia.swipe.util.Attributes;
import com.paril.mlaclientapp.ui.adapter.OnItemClickListener;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.ui.activity.MLAViewFacebookPostActivity;
import com.paril.mlaclientapp.ui.view.EmptyRecyclerView;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.util.VerticalSpaceItemDecoration;
import com.paril.mlaclientapp.model.MLAFacebookDetails;
import com.paril.mlaclientapp.ui.adapter.MLAFacebookAdapter;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import com.paril.mlaclientapp.ui.activity.MLAHomeActivity;
import com.paril.mlaclientapp.webservice.Api;
import java.io.IOException;
import java.net.MalformedURLException;

public class MLAFacebookViewFragment extends Fragment {
    EmptyRecyclerView recyclerViewUsers;
    ArrayAdapter<String> adapter;
    MLAFacebookAdapter userDisplayAdapter;
    View view;

    @Nullable
    @Override
    //change
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mla_facebooklist, container, false);
        recyclerViewUsers = (EmptyRecyclerView) view.findViewById(R.id.mla_facebook_display_recyyclerView);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewUsers.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.divider_list)));
        view.findViewById(R.id.fragment_display_facebook_fabAddUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getActivity(), MLAViewFacebookPostActivity.class);
                intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, true);
                intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, false);
                startActivity(intent);
            }
        });

        return view;
    }



    public void onResume() {
        super.onResume();
        if (CommonUtils.checkInternetConnection(getActivity())) {
            MLAGetAllFacebookAPI getUserDetails = new MLAGetAllFacebookAPI(this.getActivity());
            getUserDetails.execute();
        } else {
            ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.check_connection), view.findViewById(R.id.fragment_display_admin_coordinatorLayout));
        }

    }

    class MLAGetAllFacebookAPI extends AsyncTask<Void, Void, List<MLAFacebookDetails>> {
        Context context;

        public MLAGetAllFacebookAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            ((MLAHomeActivity) getActivity()).showProgressDialog("Getting dddddddddddddata...");

        }

        @Override
        protected void onPostExecute(List<MLAFacebookDetails> userDetails) {

            ((MLAHomeActivity) getActivity()).hideProgressDialog();
            List<MLAFacebookDetails> listUserDetails = new ArrayList<MLAFacebookDetails>();
            if (userDetails != null) {
                listUserDetails = userDetails;
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.server_error), getView().findViewById(R.id.fragment_display_admin_coordinatorLayout));
            }

            userDisplayAdapter = new MLAFacebookAdapter(context, listUserDetails,true, new OnItemClickListener<MLAFacebookDetails>() {
                @Override
                public void onItemClick(MLAFacebookDetails item, int resourceId) {
                    Log.d("OnItemClick", "resource:" + resourceId);
                    if (resourceId == R.id.user_item_display_layout_imgEditUser) {
                        final Intent intent = new Intent(getActivity(), MLAViewFacebookPostActivity.class);
                        intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, false);
                        intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, true);
                        intent.putExtra(CommonUtils.EXTRA_USER_ADMIN_DATA, item);
                        startActivity(intent);
                    }
//                    else if (resourceId == R.id.user_item_display_layout_imgDeleteUser) {
//                        if (CommonUtils.checkInternetConnection(getActivity())) {
//                            MLADeleteFacebookAPI deleteAdminTask = new MLADeleteFacebookAPI(MLAFacebookViewFragment.this.getActivity());
//                            deleteAdminTask.execute(item.getIdStudent());
//                        } else {
//                            ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.check_connection), view.findViewById(R.id.fragment_display_admin_coordinatorLayout));
//                        }
//
//                    }
                    else if (resourceId == R.id.user_item_display_layout_swipeParent) {
                        final Intent intent = new Intent(getActivity(), MLAViewFacebookPostActivity.class);
                        intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, false);
                        intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, false);

                        intent.putExtra(CommonUtils.EXTRA_USER_ADMIN_DATA, item);
                        startActivity(intent);
                    }

                }
            });
            userDisplayAdapter.setMode(Attributes.Mode.Single);
            recyclerViewUsers.setAdapter(userDisplayAdapter);
            recyclerViewUsers.setEmptyView(getView().findViewById(R.id.fragment_display_facebook_relEmptyView));
        }

        @Override
        protected List<MLAFacebookDetails> doInBackground(Void... params) {

            try {
                Call<List<MLAFacebookDetails>> callAdminUserData = Api.getClient().getFacebook();
                Response<List<MLAFacebookDetails>> responseAdminUser = callAdminUserData.execute();
                if (responseAdminUser.isSuccessful() && responseAdminUser.body() != null) {
                    return responseAdminUser.body();
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
