package edu.uga.cs.fetchexercise;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //
    Button enterButton;
    TextView content;
    EditText searchBar;

    List<Item> itemList = new ArrayList<>();

    /**
     * initialize the activity, on create method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize elements
        enterButton = findViewById(R.id.enterButton);
        content = findViewById(R.id.content);
        searchBar = findViewById(R.id.searchBar);

        enterButton.setOnClickListener(e -> {
            enterClicked();
        });
    }


    /**
     * Set up the "enter" button listener, which will fetch the content from the typed-in url
     */
    private void enterClicked() {
        String str = searchBar.getText().toString();
        new FetchDataTask(content).execute(str);
    }

    /**
     * This class is used to fetch data from a URL in the background and update a TextView
     */
    public class FetchDataTask extends AsyncTask<String, Void, String> {

        TextView content;

        public FetchDataTask(TextView content) {
            this.content = content;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.e("fetchtask", "111");

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

                content.setText(result);
            } else {
                content.setText("Failed to fetch data");
            }
        }
    }

}