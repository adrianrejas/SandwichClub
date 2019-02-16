package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    /* Creating objects for the UI elements to modify */
    private ImageView ingredientsIv;
    private TextView originTv;
    private TextView alsoKnownTv;
    private TextView ingredientsTv;
    private TextView descriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* Getting references of the UI objects to modify
         * (we can get them inside populateUI() too). */
        ingredientsIv = findViewById(R.id.image_iv);
        originTv = findViewById(R.id.origin_tv);
        alsoKnownTv = findViewById(R.id.also_known_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        descriptionTv = findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        try {
            /* Create strings to be set at the UI (with alternatives in case
            * of empty info) and populate text views with this info. */
            StringBuilder aliasesBuilder = new StringBuilder();
            StringBuilder ingredientsBuilder = new StringBuilder();
            originTv.setText(sandwich.getPlaceOfOrigin().isEmpty() ?
                    getString(R.string.no_origin_available) : sandwich.getPlaceOfOrigin());
            for (int i = 0; i < sandwich.getAlsoKnownAs().size(); i++) {
                aliasesBuilder.append(sandwich.getAlsoKnownAs().get(i));
                if (i < (sandwich.getAlsoKnownAs().size() -1)) {
                    aliasesBuilder.append(", ");
                }
            }
            if (aliasesBuilder.length() == 0) {
                aliasesBuilder.append(getString(R.string.no_aliases_available));
            }
            alsoKnownTv.setText(aliasesBuilder.toString());
            for (int i = 0; i < sandwich.getIngredients().size(); i++) {
                ingredientsBuilder.append("- ");
                ingredientsBuilder.append(sandwich.getIngredients().get(i));
                if (i < (sandwich.getIngredients().size() -1)) {
                    ingredientsBuilder.append("\n");
                }
            }
            if (ingredientsBuilder.length() == 0) {
                ingredientsBuilder.append(getString(R.string.no_ingredients_available));
            }
            ingredientsTv.setText(ingredientsBuilder.toString());
            descriptionTv.setText(sandwich.getDescription().isEmpty() ?
                    getString(R.string.no_description_available) : sandwich.getDescription());
            /* I moved the image loading here because I think it fits better
             * inside the populateUI() function. */
            Picasso.with(this)
                    .load(sandwich.getImage())
                    .into(ingredientsIv);
            setTitle(sandwich.getMainName());
        } catch (Exception e) {
            // In case any error happens while populating UI.
            closeOnError();
            return;
        }
    }
}
