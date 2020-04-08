package com.example.consumiendodatosapirest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.Build.VERSION_CODES.M;


public class MainActivity extends AppCompatActivity {

    ImageView imgComic;
    TextView txtTitle;
    Button btnNewComic;
    XkcdService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ENLAZAMOS LAS VARIABLES CON EL LAYOUT
        imgComic = (ImageView) findViewById(R.id.imageViewCommit);
        txtTitle = (TextView) findViewById(R.id.textViewTitel);
        btnNewComic = (Button) findViewById(R.id.buttonCommit);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://xkcd.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(XkcdService.class);

        btnNewComic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Comic> call = service.getComic(new Random().nextInt(1000));//COGEMOS EL COMIC ALEATORIAMENTE
                call.enqueue(new Callback<Comic>() {

                    //SI TODO HA IDO BIEN, SE EJECUTARA ESTE METODO
                    @Override
                    public void onResponse(Call<Comic> call, Response<Comic> response) {
                        Comic comic = response.body();//recogemos los datos
                        try {
                            if (comic != null) {
                                txtTitle.setText(comic.getTitle());
                                Picasso.get()
                                        .load(comic.getImg())
                                        .into(imgComic);
                            }
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "Ha habido un error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //SI ALGO HA IDO MAL MANDAREMOS UN TOAST MOSTRANDOLO
                    @Override
                    public void onFailure(Call<Comic> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Ocurrio un error con la API", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
