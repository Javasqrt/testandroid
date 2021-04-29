package com.anton.testtagsoft;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.amirs.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MyTableRowAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
   static String link = "https://rickandmortyapi.com/api/character";
    @BindView(R.id.scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.prog_bar)
    ProgressBar progressBar;
    ArrayList<TableRowView> arrayList;
    ArrayList<TableRowView> arrayListSearch;
    @BindView(R.id.recycler_items)
    public RecyclerView recyclerView;
    @BindView(R.id.ed_search)
    EditText search_by_name;
    Thread thread;
    String nextPages;
    String name, image, status,species, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {    // Тут проводиться иницилизация объектов
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressBar = new ProgressBar(this);
        thread = new Thread();
        arrayList = new ArrayList<>();
    }



    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  // Полный экран

    }

    @Override
    protected void onStart() {
        super.onStart();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHTTP();                                       // okhttp работает в отдельном потоке
                runMethodForItemView(arrayList);                // вывод элементов

            }

        });
        thread.start();
        search_by_name.addTextChangedListener(new TextWatcher() {    // поиск по имени
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayListSearch = new ArrayList<>();                        // вывод элементов которые найдены по имени
                for(TableRowView item: arrayList){
                    if (item.getName().toLowerCase().contains(s.toString().toLowerCase())){    // если пользователь вводил с маленькой буквы
                        arrayListSearch.add(item);
                    } else if(item.getName().toUpperCase().contains(s.toString().toUpperCase())){  // если пользователь вводил с большой буквы
                        arrayListSearch.add(item);
                    }
                }
                adapter.getItemByName(arrayListSearch);
            }
        });
        layoutManager = new LinearLayoutManager(MainActivity.this);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {  // Обновление элементов при переходе на next page
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    progressBar.setVisibility(View.VISIBLE);
                    link = nextPages;
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHTTP();
                        }
                    });
                    thread.start();
                        runMethodForItemView(arrayList);

                }
            }
        });


    }

    private void runMethodForItemView(ArrayList<TableRowView> arrayList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setHasFixedSize(true);
                adapter = new MyTableRowAdapter(arrayList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                adapter.onItemClickListener(new ClickItemListener() {
                    @Override
                    public void onItemClick(int position, String name, Bitmap image, String status, String species, String gender) {
                        Intent intent = new Intent(MainActivity.this,PopUp_CharacterActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("image",image);
                        intent.putExtra("status",status);
                        intent.putExtra("species",species);
                        intent.putExtra("gender",gender);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                });
            }
        });
    }
    public void OkHTTP(){
                    String s1 ="";
                 progressBar.setVisibility(View.VISIBLE);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(link)
                            .build();
                    try (Response response = client.newCall(request).execute()) {
                        s1 =  response.body().string();
                        progressBar.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(s1);
                        JSONObject jsonArrayInfo = jsonObject.getJSONObject("info");
                        JSON json1 = new JSON(jsonArrayInfo);
                        nextPages = json1.key("next").stringValue();
                        JSONArray jsonArrayResults = jsonObject.getJSONArray("results");
                                  for (int i = 0; i < jsonArrayResults.length(); i++) {
                            JSONObject explrObject = jsonArrayResults.getJSONObject(i);
                            JSON json = new JSON(String.valueOf(explrObject));
                            name = json.key("name").stringValue();
                            image = json.key("image").stringValue();
                            status = json.key("status").stringValue();
                            species = json.key("species").stringValue();
                            gender = json.key("gender").stringValue();
                            try {
                                URL url = new URL(image);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                arrayList.add(new TableRowView(name,myBitmap,status,species,gender));
                                Log.d("Bitmap","returned");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("Exception",e.getMessage());
                            }


                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }


}

