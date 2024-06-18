package edu.uga.cs.fetchexercise;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Initialization of teh variables
    Button enterButton;
    EditText searchBar;
    RecyclerView listView;
    RecyclerViewAdapter adapter;
    List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize elements
        enterButton = findViewById(R.id.enterButton);
        searchBar = findViewById(R.id.searchBar);
        listView = findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(this));

        // set on click listener
        enterButton.setOnClickListener(e -> {
            enterClicked();
        });
    }


    /**
     * Set up the "enter" button listener, which will fetch the content from the typed-in url
     */
    private void enterClicked() {
        String str = searchBar.getText().toString();
        new FetchDataTask(listView).execute(str);
    }

    /**
     * This class is used to fetch data from a URL in the background and update a TextView
     */
    public class FetchDataTask extends AsyncTask<String, Void, String> {

        RecyclerView recyclerView;

        public FetchDataTask(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlString = strings[0];
            try {

                // initialize the url
                URL url = new URL(urlString);

                // open a HTTP connection with the "GET" method
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");

                // Create a BufferedReader and StringBuilder to read the response
                BufferedReader inReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String inputLine;

                // while the list is not empty
                while ((inputLine = inReader.readLine()) != null) {
                    response.append(inputLine);
                }

                // close the BufferedReader
                inReader.close();

                // pass the response to onPostExecute()
                return response.toString();
            } catch (Exception e) {
                Log.e("EXCEPTION", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            itemList = new ArrayList<>();

            // if the result from background task is not null, parse them into ArrayList
            if (result != null) {
                try {
                    JSONArray jArr = new JSONArray(result);
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jObj = jArr.getJSONObject(i);
                        // safely extract the name attribute from the JSONObject
                        String name = jObj.getString("name");

                        // filter out the name with null or "" and add it to the list
                        if (!name.equals("null") && !name.isEmpty())
                            itemList.add(new Item(
                                    (Integer)jObj.get("id"),
                                    (Integer)jObj.get("listId"),
                                    name
                            ));
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", e.toString());
                }
                // call the method to sort the list and pass it to the adapter and recyclerview
                sortList(itemList);
                adapter = new RecyclerViewAdapter(itemList);
                recyclerView.setAdapter(adapter);
            } else {
                searchBar.setText("Failed to fetch data");
            }
        }
    }

    /**
     * This method is called when all the items are added to the list, sorts the items by lisstId,
     * then by the numeric part of name.
     * @param items the Item List
     */
    private void sortList(List<Item> items) {

        items.sort((item1, item2) -> {
            int idDiff = Integer.compare(item1.getListId(), item2.getListId());
            if (idDiff != 0) {
                return idDiff;
            } else {
                int num1 = extractNumber(item1.getName());
                int num2 = extractNumber(item2.getName());
                return num1 - num2;
            }

        });

    }

    /**
     * Extracts the number from the name
     * @param name item name
     * @return the integer part of the name
     */
    private int extractNumber(String name) {
        String numStr = name.replaceAll("[^0-9]", "");
        return Integer.parseInt(numStr);
    }

}