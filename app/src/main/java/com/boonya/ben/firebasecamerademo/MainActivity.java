package com.boonya.ben.firebasecamerademo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.boonya.ben.firebasecamerademo.helper.PhotoSelectionHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private PhotoSelectionHelper mPhotoSelectionHelper;
    private ImageView mImageView;
    private Button mUploadBtn;
    private FloatingActionButton mFab;
    private StorageReference mStorageRef;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.image_view);
        mUploadBtn = (Button) findViewById(R.id.upload_button);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mStorageRef = FirebaseStorage.getInstance().getReference().child(Const.Firebase.FIREBASE_FOOD_DIRECTORY);
        mPhotoSelectionHelper = new PhotoSelectionHelper(this, new PhotoSelectionHelper.CallBack() {
            @Override
            public void onCroppedSuccess(final Uri croppedUri) {
                if (croppedUri != null) {
                    Glide.with(mImageView.getContext())
                            .load(croppedUri.toString())
                            .override((int) getResources().getDimension(R.dimen.image_size_big), (int) getResources().getDimension(R.dimen.image_size_big))
                            .into(mImageView);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        mPhotoSelectionHelper.selectPhoto();
                    }
                } else {
                    mPhotoSelectionHelper.selectPhoto();
                }
            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageView.getDrawable() != null) {
                    uploadImage();
                } else {
                    Toast.makeText(MainActivity.this, "Please select the image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressWarnings("VisibleForTests")
    private void uploadImage() {
        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache();
        Bitmap bitmap = mImageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.child(String.valueOf(System.currentTimeMillis()) + ".jpg").putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(MainActivity.this, "Upload success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelectionHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPhotoSelectionHelper.selectPhoto();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
