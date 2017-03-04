package com.example.maksouth.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.maksouth.passwordmanager.case_processed_activities.PasswordManagerActivity;

public class ItemListActivity extends PasswordManagerActivity {

    private RecyclerView mRecyclerView;
    private CredentialsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar menuToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setCredentialsList(facade.getAllCredentials());
    }

    void initialize(){
        menuToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(menuToolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CredentialsListAdapter(facade.getAllCredentials(), getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_item:
                startActivity(new Intent(this, AddItemActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
