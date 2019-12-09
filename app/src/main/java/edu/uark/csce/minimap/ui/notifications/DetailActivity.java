package edu.uark.csce.minimap.ui.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import edu.uark.csce.minimap.R;

import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_FILE;
import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_IMAGE;
import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_IMAGE_URL;
import static edu.uark.csce.minimap.ui.notifications.NotificationsFragment.EXTRA_TEXT_CONTENT;

public class DetailActivity extends AppCompatActivity {

    private String imageName;
    private Bitmap my_image;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        imageName = intent.getStringExtra("ImageName");

        storageReference = FirebaseStorage.getInstance().getReference("Images");

        final ImageView imageView = findViewById(R.id.image_view_detail);

        try {
            StorageReference ref = storageReference.child(imageName + ".jpg");
            final File localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(my_image);
//                    Toast.makeText(getApplicationContext(), "Image downloaded", Toast.LENGTH_LONG).show();
                }
            });
        }catch(IOException e){
//            Toast.makeText(getApplicationContext(), "File Failed", Toast.LENGTH_LONG).show();
        }

//        TextView textView = findViewById(R.id.text_view_detail);

        //Picasso.get().load(imageUrl).into(imageView);

       // Picasso.get().load(image).into(imageView);
//        textView.setText(textContent);




    }
}
