package io.iqube.srinath.imeddispenser.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.models.User;
import io.iqube.srinath.imeddispenser.network.API_interface;
import io.iqube.srinath.imeddispenser.network.ServiceGenerator;
import io.iqube.srinath.imeddispenser.vendor.VendorHomeActivity;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button login, forgot_password;
    EditText username, password;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            if(user.getVendor_id()==null) {
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }else{
                finish();
                startActivity(new Intent(LoginActivity.this, VendorHomeActivity.class));
            }
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username_text = username.getText().toString();
                String password_text = password.getText().toString();
                if (username_text.length() != 0 && password_text.length() != 0) {
                    API_interface client = ServiceGenerator.getClient(username_text, password_text, LoginActivity.this).create(API_interface.class);
                    client.userLogin(username_text, password_text).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(response.body());
                                realm.commitTransaction();
                                String vendor_id = response.body().getVendor_id();
                                if(vendor_id == null) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }else{
                                    startActivity(new Intent(LoginActivity.this, VendorHomeActivity.class));
                                }
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Username or Password Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}
