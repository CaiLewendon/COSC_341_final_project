package com.example.final_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private EditText etUsername;
    private Button btnLogin;
    public static final String PREFS_NAME = "MyAppPrefs";
    public static final String KEY_USERNAME = "username";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etUsername = view.findViewById(R.id.etUsername);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                if (username.isEmpty()){
                    etUsername.setError("Please enter a username");
                    return;
                }
                // Save the username in SharedPreferences
                SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                prefs.edit().putString(KEY_USERNAME, username).apply();

                Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                // Notify MainActivity that login is complete.
                // For example, you can call:
                if (getActivity() instanceof LoginListener) {
                    ((LoginListener) getActivity()).onLoginSuccess();
                }
            }
        });
    }

    public interface LoginListener {
        void onLoginSuccess();
    }
}