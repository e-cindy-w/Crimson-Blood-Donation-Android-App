package com.example.crimson;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter {
    private Activity mContext;
    List<donorUser> donorUserList;

    public ListAdapter(Activity mContext, List<donorUser> donorUserList){
        super(mContext, R.layout.list_item, donorUserList);
        this.mContext = mContext;
        this.donorUserList = donorUserList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.list_item, null, true);

        TextView donorName = listItemView.findViewById(R.id.donorName);
//        TextView gender = listItemView.findViewById(R.id.gender);
        TextView phoneNo = listItemView.findViewById(R.id.phoneNo);
        TextView lastDonationDate = listItemView.findViewById(R.id.lastDonationDate);

        donorUser donorUser = donorUserList.get(position);
        donorName.setText(donorUser.getFirstName() + " " + donorUser.getLastName());
//        gender.setText(donorUser.getNationalID());
        phoneNo.setText(donorUser.getPhoneNo());
        lastDonationDate.setText(donorUser.getLastDonationDate());

        return listItemView;
    }
}
