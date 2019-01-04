package ep.rest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements Callback<Item> {
    private static final String TAG = ItemDetailActivity.class.getCanonicalName();

    private Item item;
    private TextView tvItemDetail;
    private CollapsingToolbarLayout toolbarLayout;
    private FloatingActionButton fabEdit, fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        tvItemDetail = (TextView) findViewById(R.id.tv_item_detail);

        /*
        fabEdit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(ItemDetailActivity.this, ItemFormActivity.class);
                intent.putExtra("ep.rest.item", item);
                startActivity(intent);
            }
        });
        fabDelete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ItemDetailActivity.this);
                dialog.setTitle("Confirm deletion");
                dialog.setMessage("Are you sure?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create().show();
            }
        });
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final int id = getIntent().getIntExtra("ep.rest.id", 0);
        if (id > 0) {
            ItemService.getInstance().get(id).enqueue(this);
        }
    }

    private void deleteItem() {
        ItemService.getInstance().delete(item.Item_Id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "Deletion succeeded");
                final Intent intent = new Intent(ItemDetailActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(ItemDetailActivity.this, "Deleted: " + item.Item_Name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, "Deletion failed: " + t.getMessage(), t);
                Toast.makeText(ItemDetailActivity.this, "Deletion failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResponse(Call<Item> call, Response<Item> response) {
        item = response.body();
        Log.i(TAG, "Got result: " + item);

        if (response.isSuccessful()) {
            tvItemDetail.setText(item.Item_Description);
            toolbarLayout.setTitle(item.Item_Name);
        } else {
            String errorMessage;
            try {
                errorMessage = "An error occurred: " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage = "An error occurred: error while decoding the error message.";
            }
            Log.e(TAG, errorMessage);
            tvItemDetail.setText(errorMessage);
        }
    }

    @Override
    public void onFailure(Call<Item> call, Throwable t) {
        Log.w(TAG, "Error: " + t.getMessage(), t);
    }
}
