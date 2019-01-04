package ep.rest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFormActivity extends AppCompatActivity
        implements View.OnClickListener, Callback<Void> {
    private static final String TAG = ItemFormActivity.class.getCanonicalName();

    private EditText name, price, description;
    private Button button;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);

        name = (EditText) findViewById(R.id.etName);
        price = (EditText) findViewById(R.id.etPrice);
        description = (EditText) findViewById(R.id.etDescription);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        final Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("ep.rest.item");
        if (item != null) {
            name.setText(item.Item_Name);
            price.setText(String.valueOf(item.Item_Price));
            description.setText(item.Item_Description);
        }
    }

    @Override
    public void onClick(View view) {
        final String itemName = name.getText().toString().trim();
        final String itemDescription = description.getText().toString().trim();
        final double itemPrice = Double.parseDouble(price.getText().toString().trim());

        if (item == null) { // dodajanje
            ItemService.getInstance().insert(itemName, itemPrice, itemDescription).enqueue(this);
        } else { // urejanje
            ItemService.getInstance().update(item.Item_Id, itemName, itemPrice, itemDescription).enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        final Headers headers = response.headers();

        if (response.isSuccessful()) {
            final int id;
            if (item == null) {
                Log.i(TAG, "Insertion completed.");
                // Preberemo Location iz zaglavja
                final String[] parts = headers.get("Location").split("/");
                id = Integer.parseInt(parts[parts.length - 1]);
            } else {
                Log.i(TAG, "Editing saved.");
                id = item.Item_Id;
            }
            final Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("ep.rest.id", id);
            startActivity(intent);
        } else {
            String errorMessage;
            try {
                errorMessage = "An error occurred: " + response.errorBody().string();
            } catch (IOException e) {
                errorMessage = "An error occurred: error while decoding the error message.";
            }
            Log.e(TAG, errorMessage);
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Log.w(TAG, "Error: " + t.getMessage(), t);
    }
}
