package edu.sfsu.news.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import edu.sfsu.news.R;
import edu.sfsu.news.code.model.NewsModel;

public class ContentActivity extends AppCompatActivity {

    ArrayList<NewsModel> newsModel = new ArrayList<>();
    public static final String EXTRA_ID = "articleId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();

        ImageView imgView = (ImageView) findViewById(R.id.article_image_view);
        TextView param_content = (TextView) findViewById(R.id.tv_article_content);

        String str_2 = intent.getStringExtra("image");
        Picasso.get().load(str_2).into(imgView);

        String str_1 = intent.getStringExtra("content");
        param_content.setText(str_1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}