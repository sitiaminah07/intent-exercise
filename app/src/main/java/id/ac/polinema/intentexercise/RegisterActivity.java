package id.ac.polinema.intentexercise;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    public static final String FULLNAME_KEY = "name";
    public static final String EMAIL_KEY = "email0";
    public static final String PASSWORD_KEY = "pass";
    public static final String CONFPASS_KEY = "confPass";
    public static final String HOMEPAGE_KEY = "homeP";
    public static final String ABOUTYOU_KEY = "aboutYou0";
    public static final String AVATAR_KEY = "image";

    private static final String TAG = RegisterActivity.class.getCanonicalName();
    private static final int GALLERY_REQUEST_CODE = 1;

    private ImageView avatarImage;

    private EditText fullname;
    private EditText email;
    private EditText password;
    private EditText confirmPass;
    private EditText homePage;
    private EditText aboutYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname = findViewById(R.id.text_fullname);
        email = findViewById(R.id.text_email);
        password = findViewById(R.id.text_password);
        confirmPass = findViewById(R.id.text_confirm_password);
        homePage = findViewById(R.id.text_homepage);
        aboutYou = findViewById(R.id.text_about);
        avatarImage = findViewById(R.id.image_profile);
    }

    public void handleRegister(View view) {
        String name = fullname.getText().toString();
        String email0 = email.getText().toString();
        String pass = password.getText().toString();
        String confPass = confirmPass.getText().toString();
        String homeP = homePage.getText().toString();
        String aboutYou0 = aboutYou.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if ((name).equals("") || (email0).equals("") || (pass).equals("") || (confPass).equals("") || (homeP).equals("") || (aboutYou0).equals("")) {
            Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
        }
        else if(!pass.equals(confPass)) {
            Toast.makeText(this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
        }
        else if (!email0.matches(emailPattern)) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, ProfileActivity.class);

            avatarImage.buildDrawingCache();
            Bitmap image = avatarImage.getDrawingCache();
            Bundle extras = new Bundle();
            extras.putParcelable(AVATAR_KEY, image);
            intent.putExtras(extras);

            intent.putExtra(FULLNAME_KEY, name);
            intent.putExtra(EMAIL_KEY, email0);
            intent.putExtra(PASSWORD_KEY, pass);
            intent.putExtra(CONFPASS_KEY, confPass);
            intent.putExtra(HOMEPAGE_KEY, homeP);
            intent.putExtra(ABOUTYOU_KEY, aboutYou0);

            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                try {
                    Uri imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    avatarImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    Toast.makeText(this, "Can't load image", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public void handleChangeAvatar(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
}
