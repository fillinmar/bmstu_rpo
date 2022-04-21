package com.example.rpolab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.example.rpolab.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'rpolab' library on application startup.
    static {
        System.loadLibrary("rpolab");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    ActivityResultLauncher activityResultLauncher;
    private String info;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRng();

//        String s_key = "key is a 16 char";
//        String original = new String("rpo-2022");
//
//        byte[] key = s_key.getBytes(StandardCharsets.UTF_8);
//
//        byte[] encrypted = encrypt(key, original.getBytes());
//        String decrypted = new String(decrypt(key, encrypted));
//
//        Log.d("rpolab_", "ORIGINAL: " + original + " / BYTES " + original.getBytes());
//        Log.d("rpolab_", "KEY: " + s_key + " / " + Arrays.toString(key) + " / LENGTH " + key.length);
//        Log.d("rpolab_", "ENCRYPTED: " + Arrays.toString(encrypted));
//        Log.d("rpolab_", "DECRYPTED: " + decrypted);
//
//        // Example of a call to a native method
//        TextView tv = binding.sampleText;
//        tv.setText(stringFromJNI() + "\nencrypted " +
//        Arrays.toString(encrypted)+"\ndecrypted " + decrypted);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult> () {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            String pin = data.getStringExtra("pin");
                            Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }

    public void onButtonClick(View v) {
        Intent it = new Intent(this, PinpadActivity.class);
        //startActivity(it);
        activityResultLauncher.launch(it);
    }

    /**
     * A native method that is implemented by the 'rpolab' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}