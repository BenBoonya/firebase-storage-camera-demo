package com.boonya.ben.firebasecamerademo;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.boonya.ben.firebasecamerademo.model.ImageInfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by Boonya Kitpitak on 3/29/17.
 */

public class UploadImageDialogFragment extends DialogFragment {
    private ImageView mImageView;
    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private ProgressBar mProgressBar;

    private String mImageUri;
    private String mImageName;
    private String mImageDescription;
    private StorageReference mStorageRef;
    private DatabaseReference mImageInfoReference;
    private Context mContext;

    public static UploadImageDialogFragment newInstance(String imageUri) {
        UploadImageDialogFragment fragment = new UploadImageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Const.Extra.IMAGE_URI_EXTRA, imageUri);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mStorageRef = FirebaseStorage.getInstance().getReference().child(Const.Firebase.FIREBASE_FOOD_DIRECTORY);
        mImageInfoReference = FirebaseDatabase.getInstance().getReference().child(Const.Firebase.FIREBASE_IMAGE_INFO_REF);

        Bundle bundle = getArguments();
        mImageUri = bundle.getString(Const.Extra.IMAGE_URI_EXTRA);
        mContext = getActivity();

        return inflater.inflate(R.layout.upload_image_dialog_fragment, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImageView = (ImageView) view.findViewById(R.id.image_view);
        mNameEditText = (EditText) view.findViewById(R.id.image_name);
        mDescriptionEditText = (EditText) view.findViewById(R.id.image_description);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        Button mUploadBtn = (Button) view.findViewById(R.id.upload_button);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageName = mNameEditText.getText().toString();
                mImageDescription = mDescriptionEditText.getText().toString();
                if (mImageView.getDrawable() != null || mImageName == null || mImageDescription == null) {
                    uploadImage();
                } else {
                    Toast.makeText(getActivity(), "Please fill up information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mImageUri != null) {
            Glide.with(mImageView.getContext())
                    .load(mImageUri)
                    .override((int) getResources().getDimension(R.dimen.image_size_big), (int) getResources().getDimension(R.dimen.image_size_big))
                    .into(mImageView);
        }
    }

    @SuppressWarnings("VisibleForTests")
    private void uploadImage() {

        mProgressBar.setVisibility(View.VISIBLE);
        UploadTask uploadTask = mStorageRef.child(String.valueOf(System.currentTimeMillis()) + ".jpg").putFile(Uri.parse(mImageUri));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressBar.setVisibility(View.GONE);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Upload fail", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setImageUrl(downloadUrl.toString());
                    imageInfo.setImageDecription(mImageDescription);
                    imageInfo.setImageName(mImageName);
                    mImageInfoReference.push().setValue(imageInfo);
                }
                mProgressBar.setVisibility(View.GONE);
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Upload success", Toast.LENGTH_SHORT).show();
                }
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        });
    }
}
