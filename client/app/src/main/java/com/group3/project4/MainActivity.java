package com.group3.project4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project4.R;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
    LoginFragment.IListener, SignupFragment.IListener, UserProfileFragment.IListener {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .commit();
    }

    @Override
    public void signup() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new SignupFragment(), "SignupFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void registerCancelled() {
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("LoginFragment");
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void loginSuccess(String email, String password) {
        setUser(email, password);
    }

    private void setUser(String email, String password) {
        RetrofitInterface retrofitInterface;
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);

        Call<LoginResult> call = retrofitInterface.login(data);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    LoginResult result = response.body();
                    user = new User(result.getId(), result.getEmail(), result.getFirstName(),
                            result.getLastName(), result.getCity(),
                            result.getGender(), "", result.getToken(), result.getAge(), result.getWeight(),
                            result.getAddress());

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.containerview, UserProfileFragment.newInstance(user), "UserProfileFragment")
                            .addToBackStack(null)
                            .commit();
                } else {
                    Toast.makeText(getApplicationContext(), "you were not found   ", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void chooseProfileImage() {

    }

    @Override
    public void signOut() {
        finishAffinity();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerview, new LoginFragment(), "LoginFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void updateUserProfile() {

    }
}