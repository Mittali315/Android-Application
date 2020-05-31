package com.paril.mlaclientapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAFacebookDetails;
import com.paril.mlaclientapp.ui.view.CustomSwipeLayout;

import java.util.List;

public class MLAFacebookAdapter extends RecyclerSwipeAdapter<MLAFacebookAdapter.SimpleViewHolder> {
    boolean isSwipable = false;

    public  class SimpleViewHolder extends RecyclerView.ViewHolder {
        CustomSwipeLayout swipeLayout;
        TextView txtUserName, txtName, txtEmailId;
        ImageView imgDelete, imgEdit;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (CustomSwipeLayout) itemView.findViewById(R.id.user_item_display_layout_swipeParent);
            txtName = (TextView) itemView.findViewById(R.id.user_item_display_layout_txtName);

          //  txtUserName = (TextView) itemView.findViewById(R.id.user_item_display_layout_txtUserName);

           // txtEmailId = (TextView) itemView.findViewById(R.id.user_item_display_layout_txtEmail);
            imgDelete = (ImageView) itemView.findViewById(R.id.user_item_display_layout_imgDeleteUser);

            imgEdit = (ImageView) itemView.findViewById(R.id.user_item_display_layout_imgEditUser);


        }

        public void bind(final MLAFacebookDetails item, final OnItemClickListener listener) {
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.setRightSwipeEnabled(isSwipable);

            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                    listener.onItemClick(item, R.id.user_item_display_layout_imgDeleteUser);
                }
            });


            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();

                    listener.onItemClick(item, R.id.user_item_display_layout_imgEditUser);
                }
            });
            swipeLayout.setOnClickItemListener(new CustomSwipeLayout.OnClickItemListener() {
                @Override
                public void onClick(View view) {

                    listener.onItemClick(item, R.id.user_item_display_layout_swipeParent);
                }
            });
            txtName.setText(item.getPostmsg());
//
//            txtUserName.setText(item.getIdFacebook());
//
//            txtEmailId.setText(item.getEmailId());
        }
    }

    private Context mContext;
    private List<MLAFacebookDetails> mDataset;

    OnItemClickListener<MLAFacebookDetails> listener;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public MLAFacebookAdapter(Context context, List<MLAFacebookDetails> objects, boolean isSwipable, OnItemClickListener<MLAFacebookDetails> onItemClickListenerener) {
        this.mContext = context;
        this.isSwipable = isSwipable;
        this.mDataset = objects;
        this.listener = onItemClickListenerener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_mla_display_post_facebook, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        MLAFacebookDetails item = mDataset.get(position);
        viewHolder.bind(item, listener);
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.user_item_display_layout_swipeParent;
    }
}

