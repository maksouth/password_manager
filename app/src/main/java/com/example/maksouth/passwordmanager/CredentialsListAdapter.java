package com.example.maksouth.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maksouth.passwordmanager.entities.ItemCredentials;

import java.util.List;

/**
 * Created by maksouth on 12.02.17.
 */

public class CredentialsListAdapter extends RecyclerView.Adapter<CredentialsListAdapter.ViewHolder> {

    public static final String LOG_TAG = "MY_TAG";

    private List<ItemCredentials> credentialsList;
    private Context context;

    public CredentialsListAdapter(List<ItemCredentials> list, Context context){
        credentialsList = list;
        this.context = context;
    }

    public void setCredentialsList(List<ItemCredentials> newList){
        credentialsList = newList;
        notifyDataSetChanged();
    }

    @Override
    public CredentialsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.credentials_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CredentialsListAdapter.ViewHolder holder, final int position) {
        holder.nameTV.setText(credentialsList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Recycler view item clicked " + position);
                Intent intent = new Intent(context, OneItemActivity.class);
                intent.putExtra(OneItemActivity.ITEM_CREDENTIALS_ID_KEY, credentialsList.get(position).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return credentialsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView nameTV;
        public ViewHolder(View v) {
            super(v);
            nameTV = (TextView)v.findViewById(R.id.bottom_horizontal_divider);
        }
    }

}
