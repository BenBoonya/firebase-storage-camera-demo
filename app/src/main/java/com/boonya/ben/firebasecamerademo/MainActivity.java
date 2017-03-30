package com.boonya.ben.firebasecamerademo;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.boonya.ben.firebasecamerademo.adapter.recycler.ImagePreviewAdapter;
import com.boonya.ben.firebasecamerademo.adapter.recycler.ViewHolder.ImagePreviewViewHolder;
import com.boonya.ben.firebasecamerademo.helper.PhotoSelectionHelper;
import com.boonya.ben.firebasecamerademo.model.ImageInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private PhotoSelectionHelper mPhotoSelectionHelper;
    private FirebaseAuth mAuth;
    private DatabaseReference mImageInfoRef;
    private FirebaseRecyclerAdapter mAdapter;

    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private FragmentManager mFragmentManager;
    private RecyclerView mRecyclerView;

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final String FRAGMENT_NAME = "Upload Dialog";
    private static final String TAG = "FirebaseAuth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mFragmentManager = getFragmentManager();
        mImageInfoRef = FirebaseDatabase.getInstance().getReference().child(Const.Firebase.FIREBASE_IMAGE_INFO_REF);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mPhotoSelectionHelper = new PhotoSelectionHelper(this, new PhotoSelectionHelper.CallBack() {
            @Override
            public void onCroppedSuccess(final Uri croppedUri) {
                if (croppedUri != null) {
                    UploadImageDialogFragment uploadImageDialogFragment = UploadImageDialogFragment.newInstance(croppedUri.toString());
                    uploadImageDialogFragment.show(mFragmentManager, FRAGMENT_NAME);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInAnonymously();
        }

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

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImagePreviewAdapter(ImageInfo.class, R.layout.photo_preview_viewholder
                , ImagePreviewViewHolder.class, mImageInfoRef) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                if (mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "signInAnonymously:FAILURE", exception);
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
