package edu.uark.csce.minimap.ui.notifications;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uark.csce.minimap.Profile;
import edu.uark.csce.minimap.ProfileAdapter;
import edu.uark.csce.minimap.R;

import static android.app.Activity.RESULT_OK;
import static edu.uark.csce.minimap.ui.notifications.CreateNewPost.IMAGE_REQUEST_CODE;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    static final int CREATE_NEWPOST_REQUEST = 1;  // The request code
    static final int REQUEST_IMAGE_CAPTURE = 1;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Post> list;
    PostAdapter postAdapter;
    private ProgressDialog progressDialog;
    private FloatingActionButton floatingActionButton;
    private Button send;
    private EditText editText;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String currentPhotoPath;
//    private Uri imageUri;
    private Bitmap my_image;
    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        int num = getArguments().getInt("POS");
//        Log.e("POS", String.valueOf(num));
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        //        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);


        recyclerView = (RecyclerView) root.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        storageReference = FirebaseStorage.getInstance().getReference("Images");
        progressDialog = new ProgressDialog(getContext());

        reference = FirebaseDatabase.getInstance().getReference("Images");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Post>();

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {
                    Post p = childSnapshot.getValue(Post.class);
                    list.add(p);
                }
                postAdapter = new PostAdapter(NotificationsFragment.this.getActivity(), list);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NotificationsFragment.this.getActivity(), "something went wrong", Toast.LENGTH_SHORT);
            }
        });


        floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fabtn_addition);


        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Image"), IMAGE_REQUEST_CODE);

                try{
                    dispatchTakePicIntent();
                } catch(IOException e){

                }

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editText = (EditText) root.findViewById(R.id.editText);
        send = (Button) root.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                long currentTime = System.currentTimeMillis();
                uploadPost(currentTime);
                uploadImage(currentTime);
//                downloadImage(String.valueOf(currentTime));
            }
        });

        imageView = (ImageView) root.findViewById(R.id.image);

        return root;


    }



    //this function is used to take a new photo after creating a file for it.
    private void dispatchTakePicIntent() throws IOException {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getActivity().getPackageManager())!=null) {

            //Create a new File to store the image
            File imageFile = null;
            try{
                imageFile = createImageFile();
                Log.e("File", "Created Successfully");
            }catch(IOException e){
                Log.e("File", "Failed to create a file");
            }
            if (imageFile != null) {

                String authorities = getActivity().getApplicationContext().getPackageName()+".fileprovider";

                Uri imageURI = FileProvider.getUriForFile(getActivity(), authorities, imageFile);

                takePic.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePic, 1);
                Log.e("Intent: ", "Started Successfully");
            }
        }
    }

    //the camera intent result return function
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check if the inent ran successfully and then update the mapFragment
        if(requestCode==1 && resultCode==RESULT_OK){

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //a function to create a new image file and name it using lat_lng_yyyy-MM-dd_HH:mm:ss_.jpg format
    //the image name is used to store its location as well
    private File createImageFile() throws IOException{
        // Create an image file name
        long currentTime = System.currentTimeMillis();
        String name = String.valueOf(currentTime);
        String imageFileName = name+".jpg";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);
//        imageUri = Uri.fromFile(image);
        currentPhotoPath = image.getAbsolutePath();
        return image;

    }


    private void uploadPost(long currentTime){
        String postText = editText.getText().toString();
        Post post = new Post(postText, currentTime, String.valueOf(currentTime), "urlll", null);
        databaseReference.child("Images").child(String.valueOf(currentTime)).setValue(post);
    }
    private void uploadImage(long currentTime) {

        if(currentPhotoPath!=null) {
            currentPhotoPath = resizeAndCompressImageBeforeSend(getContext(), currentPhotoPath, currentTime + ".jpg");
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
            if (imageUri != null) {

                progressDialog.setTitle("Image is Uploading...");
                progressDialog.show();

                storageReference.child(currentTime + ".jpg").putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {

                Toast.makeText(getContext(), "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

            }
        }
    }
//    private void downloadImage(String currentTime) {
//
////            progressDialog.setTitle("Image is Downloading...");
////            progressDialog.show();
//            try {
//                StorageReference ref = storageReference.child(currentTime + ".jpg");
//                final File localFile = File.createTempFile("images", "jpg");
//                ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                        imageView.setImageBitmap(my_image);
//                        Toast.makeText(getActivity(), "Download Succeeded", Toast.LENGTH_LONG).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "Download Failed", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }catch(IOException e){
//                Toast.makeText(getActivity(), "File Failed", Toast.LENGTH_LONG).show();
//            }
//    }

    public static String resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName){
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);


        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap","cacheDir: "+context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir()+fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        //return the path of resized and compressed file
        return  context.getCacheDir()+fileName;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }

}