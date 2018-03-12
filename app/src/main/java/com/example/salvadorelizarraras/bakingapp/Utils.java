package com.example.salvadorelizarraras.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.salvadorelizarraras.bakingapp.Recipe.Ingredient;
import com.example.salvadorelizarraras.bakingapp.Recipe.Recipe;
import com.example.salvadorelizarraras.bakingapp.Recipe.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Salvador Elizarraras on 05/03/2018.
 */

public class Utils {


    public static final String fileName = "recipes";

    public static final String  mUriBase = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static String TAG = Utils.class.getSimpleName();




    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public static String getResponseFromHttpUrl(URL url) throws IOException {

        Log.d(TAG, "getResponseFromHttpUrl: --->  " +url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }finally{

            connection.disconnect();
        }

    }

    public static String readFile(Context context, String fileName){

        String outCome = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                outCome+= mLine;
            }

        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG, "readFile "+e.getStackTrace());
                }
            }
        }
        return  outCome;
    }

    @Nullable
    public static ArrayList<Recipe> getDataFromFile(Context context, String data){
        ArrayList<Recipe> mRecipes= new ArrayList<>();
        try {
            JSONArray  jsonArray = new JSONArray(data);
            JSONObject jsonObject;

            for (int i = 0; i < jsonArray.length(); i++) {
                Recipe mRecipe = new Recipe();
                jsonObject = jsonArray.getJSONObject(i);

                mRecipe.setId((Integer) jsonObject.get(ID));
                mRecipe.setImage((String) jsonObject.get(IMAGE));
                mRecipe.setName((String) jsonObject.get(NAME));
                mRecipe.setServings((Integer) jsonObject.getInt(SERVINGS));

                JSONArray jsonArrayIngredients = jsonObject.getJSONArray(INGREDIENTS);
                ArrayList<Ingredient> ingredientsArrayList = new ArrayList<>();
                for (int j = 0; j < jsonArrayIngredients.length() ; j++) {
                    JSONObject jsonObjectIngredient = jsonArrayIngredients.getJSONObject(j);
                    Ingredient ingredient = new Ingredient();

                    ingredient.setQuantity(jsonObjectIngredient.getInt(QUANTITY));
                    ingredient.setMeasure(jsonObjectIngredient.getString(MEASURE));
                    ingredient.setIngredient(jsonObjectIngredient.getString(INGREDIENT));

                    ingredientsArrayList.add(ingredient);
                }
                mRecipe.setIngredients(ingredientsArrayList);




                JSONArray jsonArraySteps = jsonObject.getJSONArray(STEPS);
                ArrayList<Steps> stepsArrayList = new ArrayList<>();
                for (int j = 0; j <jsonArraySteps.length() ; j++) {
                    JSONObject jsonObjectSteps = jsonArraySteps.getJSONObject(j);
                    Steps steps = new Steps();

                    steps.setId(jsonObjectSteps.getInt(ID));
                    steps.setShortDescription(jsonObjectSteps.getString(SHORT_DESCRIPTION));
                    steps.setDescription(jsonObjectSteps.getString(DESCRIPTION));
                    steps.setVideoURL(jsonObjectSteps.getString(VIDEO_URL));
                    steps.setThumbnailURL(jsonObjectSteps.getString(THUMB_NAIL_URL));
                    stepsArrayList.add(steps);
                }
                mRecipe.setSteps(stepsArrayList);

                mRecipes.add(mRecipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mRecipes;
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);

    }


        private static String ID = "id";
        private static String SHORT_DESCRIPTION = "shortDescription";
        private static String DESCRIPTION = "description";
        private static String VIDEO_URL = "videoURL";
        private static String THUMB_NAIL_URL = "thumbnailURL";
        private static String NAME ="name";
        private static String INGREDIENTS = "ingredients";
        private static String INGREDIENT = "ingredient";
        private static String STEPS = "steps";
        private static String SERVINGS ="servings";
        private static String IMAGE ="image";
        private static String QUANTITY = "quantity";
        private static String MEASURE ="measure";


}
