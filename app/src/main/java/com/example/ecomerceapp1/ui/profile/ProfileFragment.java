package com.example.ecomerceapp1.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecomerceapp1.R;
import com.example.ecomerceapp1.databinding.FragmentProfileBinding;
import com.example.ecomerceapp1.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {



    Button updateBtn;
    CircleImageView profileImg;
    EditText name, email, phoneNumber, address;



    FirebaseStorage storage;

    FirebaseAuth auth;
    FirebaseDatabase database;
    private FragmentProfileBinding binding;

    private Uri profileUri;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImg = root.findViewById(R.id.profile_img);
        name = root.findViewById(R.id.profile_name);
        email = root.findViewById(R.id.profile_email);
        phoneNumber = root.findViewById(R.id.profile_phone_number);
        address = root.findViewById(R.id.profile_address);
        updateBtn = root.findViewById(R.id.btn_update);

        auth = FirebaseAuth.getInstance();
        //Firebase
        database = FirebaseDatabase.getInstance("https://grocery-store-e0d7c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        storage =  FirebaseStorage.getInstance();


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                email.setText(user.getEmail());
                address.setText(user.getAddress());
                phoneNumber.setText(user.getPhoneNumber());
                name.setText(user.getUsername());
                Glide.with(getActivity()).load(user.getProfileImg()).into(profileImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });


        return root;
    }


    public void updateUserProfile(){
        if(name.getText().equals("") || address.getText().equals("") || phoneNumber.getText().equals("")){
            Toast.makeText(getActivity(), "You must fill in all the information!", Toast.LENGTH_SHORT).show();
            return;
        }
        //1. Upload the image for user
        //store the URI of the image with the name of the userID
        //The child folder profile_picture -> then the URI of the image (child->child)
        if(profileUri != null){
            final StorageReference reference = storage.getReference().child("profile_picture").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                    //The uri of the image uploaded
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("profileImg").setValue(uri.toString());
                            Toast.makeText(getActivity(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        //2. Update the username, address and phonenumber
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("username").setValue(name.getText().toString());
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("address").setValue(address.getText().toString());
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("phoneNumber").setValue(phoneNumber.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Update User Profile successfuly", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Update User Profile Error", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData() != null){
            profileUri = data.getData();
            profileImg.setImageURI(profileUri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}