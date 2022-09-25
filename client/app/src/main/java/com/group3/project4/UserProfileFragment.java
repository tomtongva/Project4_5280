package com.group3.project4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.project4.R;
import com.example.project4.databinding.FragmentUserProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileFragment extends Fragment {
    FragmentUserProfileBinding binding;
    RetrofitInterface retrofitInterface;
    Retrofit retrofit;

    private static final String USER = "USER";
   /* FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();*/
    User user;

    IListener mListener;
    public interface IListener {
        public void chooseProfileImage();
        public void signOut();
        public void updateUserProfile();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IListener) {
            mListener = (IListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    public UserProfileFragment() {
        //empty
    }

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable(USER);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);

//        if (mAuth.getCurrentUser().getEmail().equals(user.getEmail())) {
            getActivity().setTitle("My Profile");
            binding.imageUserProfile.setFocusable(true);
            binding.inputUserProfileFirstName.setFocusable(true);
            binding.inputUserProfileLastName.setFocusable(true);
            binding.radioBtnUserProfileMale.setEnabled(true);
            binding.radioBtnUserProfileFemale.setEnabled(true);
            binding.imageButtonSave.setVisibility(View.VISIBLE);
            binding.imageButtonLogout.setVisibility(View.VISIBLE);
            binding.inputUserProfileAge.setFocusable(true);
            binding.inputUserProfileWeight.setFocusable(true);
            binding.inputUserProfileAddress.setFocusable(true);
            onProfileImageClick();
//        } else {
//            getActivity().setTitle("Profile Detail");
//            binding.imageUserProfile.setFocusable(false);
//            binding.inputUserProfileFirstName.setFocusable(false);
//            binding.inputUserProfileLastName.setFocusable(false);
//            binding.radioBtnUserProfileMale.setEnabled(false);
//            binding.radioBtnUserProfileFemale.setEnabled(false);
//            binding.imageButtonSave.setVisibility(View.INVISIBLE);
//            binding.imageButtonLogout.setVisibility(View.INVISIBLE);
//        }

        binding.inputUserProfileFirstName.setText(user.getFirst_name());
        binding.inputUserProfileLastName.setText(user.getLast_name());
        binding.inputUserProfileCity.setText(user.getCity());
        binding.inputUserProfileAddress.setText(user.getAddress());

        if (user.getAge() > 0)
            binding.inputUserProfileAge.setText(user.getAge() + "");
        if (user.getWeight() > 0)
            binding.inputUserProfileWeight.setText(user.getWeight() + "");

        if (User.FEMALE.equals(user.getGender())) {
            binding.radioBtnUserProfileFemale.setChecked(true);
        } else if (User.MALE.equals(user.getGender())) {
            binding.radioBtnUserProfileMale.setChecked(true);
        }

        downloadAndSetProfileImage();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UpdateUserResult> call = retrofitInterface.updateUser("LOGGING-OUT", new HashMap());
                call.enqueue(new Callback<UpdateUserResult>() {
                    @Override
                    public void onResponse(Call<UpdateUserResult> call, Response<UpdateUserResult> response) {
                        if (response.code() == 200) {
                            UpdateUserResult result = response.body();
                            Toast.makeText(getActivity(), "Gone with the wind", Toast.LENGTH_LONG).show();
                            mListener.signOut();
                        } else {
                            Toast.makeText(getActivity(), "Cannot exit app at this time", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserResult> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        binding.imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] error = new String[1];

                String firstName = binding.inputUserProfileFirstName.getText().toString();
                String lastName = binding.inputUserProfileLastName.getText().toString();
                String city = binding.inputUserProfileCity.getText().toString();
                String address = binding.inputUserProfileAddress.getText().toString();
                int age = 0;
                int weight = 0;

                if (!binding.inputUserProfileAge.getText().toString().trim().isEmpty())
                    age = Integer.parseInt(binding.inputUserProfileAge.getText().toString());

                if (!binding.inputUserProfileWeight.getText().toString().trim().isEmpty())
                    weight = Integer.parseInt(binding.inputUserProfileWeight.getText().toString().trim());

                int selectedRadioButton = binding.radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = binding.radioGroup.findViewById(selectedRadioButton);

                String gender = "";

                if (radioButton == binding.radioBtnUserProfileMale) {
                    gender = User.MALE;
                } else if (radioButton == binding.radioBtnUserProfileFemale) {
                    gender = User.FEMALE;
                }

                if (firstName.isEmpty()) {
                    Toast.makeText(getActivity(), "First name is required", Toast.LENGTH_LONG).show();
                    error[0] = "First name is required";
                    showAlert(error[0]);
                } else if (lastName.isEmpty()) {
                    Toast.makeText(getActivity(), "Last name is required", Toast.LENGTH_LONG).show();
                    error[0] = "Last name is required";
                    showAlert(error[0]);
                } else if (city.isEmpty()) {
                    Toast.makeText(getActivity(), "City is required", Toast.LENGTH_LONG).show();
                    error[0] = "City is required";
                    showAlert(error[0]);
                } else if (gender.isEmpty()) {
                    Toast.makeText(getActivity(), "Please select a gender", Toast.LENGTH_LONG).show();
                    error[0] = "No gender selected";
                    showAlert(error[0]);
                } else {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("email", user.email);
                    data.put("firstName", firstName);
                    data.put("lastName", lastName);
                    data.put("city", city);
                    data.put("gender", gender);
                    data.put("age", age);
                    data.put("weight", weight);
                    data.put("address", address);

                    Call<UpdateUserResult> call = retrofitInterface.updateUser(user.getToken(), data);
                    call.enqueue(new Callback<UpdateUserResult>() {
                        @Override
                        public void onResponse(Call<UpdateUserResult> call, Response<UpdateUserResult> response) {
                            if (response.code() == 200) {
                                UpdateUserResult result = response.body();
                                Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_LONG).show();
                                mListener.updateUserProfile();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong. Logout and log back in.", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateUserResult> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                /*else {
                    FirebaseAuth mAuthLocal = FirebaseAuth.getInstance();
                    String finalGender = gender;
                    User newUser = new User(null, user.getEmail(), firstName, lastName, city, finalGender, user.getImage_location());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(mAuthLocal.getUid())
                            .set(newUser);
                }*/
            }
        });
    }

    private void showAlert(String error) {
        if (error != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Error")
                    .setMessage(error)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    private void onProfileImageClick() {

        ImageView profileImage = binding.imageUserProfile;

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.chooseProfileImage();
            }
        });
    }

    private void downloadAndSetProfileImage() {
        if (user.getImage_location() != null) {
            /*StorageReference imagesRef = storageRef.child(user.getImage_location());
            final long ONE_MEGABYTE = 1024 * 1024;
            imagesRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.imageUserProfile.setImageBitmap(bmp);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("myapp", "No Such file or Path found!!");
                }
            });*/
        }
    }

    public void updateProfileImage(String imageLocalPath) {
        user.setImage_location(imageLocalPath);
    }
}