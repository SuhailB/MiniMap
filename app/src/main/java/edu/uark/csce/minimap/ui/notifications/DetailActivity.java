package edu.uark.csce.minimap.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import edu.uark.csce.minimap.R;

import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_FILE;
import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_IMAGE;
import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_IMAGE_URL;
import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_TEXT_CONTENT;

public class DetailActivity extends AppCompatActivity {

    Bitmap my_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
        String image = intent.getStringExtra(EXTRA_IMAGE);
        String textContent = intent.getStringExtra(EXTRA_TEXT_CONTENT);
        File file = (File)getIntent().getExtras().get(EXTRA_FILE);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textView = findViewById(R.id.text_view_detail);

        //Picasso.get().load(imageUrl).into(imageView);

       // Picasso.get().load(image).into(imageView);
        textView.setText(textContent);

        my_image = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(my_image);



    }
}
