package edu.uark.csce.minimap.ui.notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uark.csce.minimap.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context context;
    ArrayList<Post> posts;

    private Bitmap my_image;
    private StorageReference storageReference;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PostAdapter(Context c, ArrayList<Post> p)
    {
        context = c;
        posts = p;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        return new PostViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {

        holder.postText.setText(posts.get(position).getText());


        try {
            StorageReference ref = storageReference.child(posts.get(position).getTime() + ".jpg");
            final File localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.postImage.setImageBitmap(my_image);
                    Toast.makeText(context, "Image downloaded", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.postImage.getLayoutParams().width = 0;
                    Toast.makeText(context, "Image download Failed", Toast.LENGTH_LONG).show();
                }
            });
        }catch(IOException e){
            Toast.makeText(context, "File Failed", Toast.LENGTH_LONG).show();
        }

//        holder.imageURL.setText(posts.get(position).getImageURL());
//        Picasso.get().load(posts.get(position).getImage()).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder
    {
        TextView postText;
        ImageView postImage;

        public PostViewHolder(View itemView){
            super(itemView);
            postText = (TextView) itemView.findViewById(R.id.postText);
            postImage = (ImageView) itemView.findViewById(R.id.profilePic);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(mListener != null)
                     {
                         int position = getAdapterPosition();

                         if(position != RecyclerView.NO_POSITION){
                             mListener.onItemClick(position);
                         }
                     }
                 }
             });
        }
    }
}
