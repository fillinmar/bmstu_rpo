package ru.iu3.fclientmarina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;

import ru.iu3.fclientmarina.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'fclientmarina' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int res = initRng();
        //        для примера, зашифруем и расшифруем что то с помощью алгоритма 3DES
        byte[] secretKey = randomBytes(16);
//
        byte[] secret = "ILOVEBMSTU".getBytes();
        byte[] encryption = encrypt(secretKey, secret);

        byte[] decryption = decrypt(secretKey, encryption);

        String secretWord = new String(decryption, StandardCharsets.UTF_8);

        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

    }

    /**
     * A native method that is implemented by the 'fclientmarina' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}