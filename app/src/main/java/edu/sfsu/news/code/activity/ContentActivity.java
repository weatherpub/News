package edu.sfsu.news.code.activity;

import android.content.Intent;
import android.net.Uri;
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
import edu.sfsu.news.code.picasso.RoundedTransformation;

public class ContentActivity extends AppCompatActivity {

    ArrayList<NewsModel> newsModel = new ArrayList<>();
    public static final String EXTRA_ID = "articleId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_content);

        TextView receiver_message = (TextView) findViewById(R.id.tv_article_content);
        ImageView imgView = (ImageView) findViewById(R.id.article_image_view);

        Intent intent = getIntent();

        // String str_1 = intent.getStringExtra("message_key_1");
        // receiver_message.setText(str_1);

        String str_2 = intent.getStringExtra("image");
        Picasso.get().load(str_2).into(imgView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}