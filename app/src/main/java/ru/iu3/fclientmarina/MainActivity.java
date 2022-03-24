package ru.iu3.fclientmarina;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Random;
import ru.iu3.fclientmarina.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;
    ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int res = initRng();
        byte[] d_key = randomBytes(16);
        Random random = new Random();
        byte[] mas = new byte[20];
        for (int i = 0; i < mas.length; ++i) {
            mas[i] = (byte) ((byte) random.nextInt() % 255);
        }
        byte[] encrypt_mas = encrypt(d_key, mas);
        byte[] decrypt_mas = decrypt(d_key, encrypt_mas);

        activityResultLauncher =
                registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            // обработка результата
                            String pin = data.getStringExtra("pin");
                            Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void onButtonClick(View v)
    {
        Intent it = new Intent(this, PinpadActivity.class);
        //при StartActivity нет вывода сообщения. Используем  activityResultLauncher
        activityResultLauncher.launch(it);
//        startActivity(it);
    }
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}
