package com.example.modul_2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddCarActivity extends AppCompatActivity {

    private EditText editTextBrand;
    private EditText editTextManufacturer;
    private EditText editTextTransmissionsType;
    private EditText editTextTransmissions;
    private EditText editTextColor;
    private EditText editTextEngine;
    private EditText editTextBodyType;
    private EditText editTextPrice;
    // Добавьте поля для ввода информации о машине

    private ImageView imageViewCar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String imageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        editTextBrand = findViewById(R.id.editTextBrand);
        editTextManufacturer = findViewById(R.id.editTextManufacturer);
        editTextTransmissionsType = findViewById(R.id.editTextTransmissionsType);
        editTextTransmissions = findViewById(R.id.editTextTransmissions);
        editTextColor = findViewById(R.id.editTextColor);
        editTextEngine = findViewById(R.id.editTextEngineType);
        editTextBodyType = findViewById(R.id.editTextBodyType);
        editTextPrice=findViewById(R.id.editTextPrice);

        imageViewCar = findViewById(R.id.imageViewCar);

        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCarInfoToJson();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageFilePath = getPathFromUri(imageUri);
            imageViewCar.setImageURI(imageUri);
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private void saveCarInfoToJson() {

        String brand = editTextBrand.getText().toString();
        String manufacturer = editTextManufacturer.getText().toString();
        String transmissionType = editTextTransmissionsType.getText().toString();
        String transmission = editTextTransmissions.getText().toString();
        String color = editTextColor.getText().toString();
        String engineType = editTextEngine.getText().toString();
        String bodyType = editTextBodyType.getText().toString();
        String priceString = editTextPrice.getText().toString();


        if (brand.isEmpty() || manufacturer.isEmpty() || transmissionType.isEmpty() ||
                transmission.isEmpty() || color.isEmpty() || engineType.isEmpty() ||
                bodyType.isEmpty() || priceString.isEmpty() ) {

            showToast("Please fill in all fields");
            return;
        }
        int price = 0;
        if (!priceString.isEmpty()) {
            price = Integer.parseInt(priceString);
        }
        String uniqueID = UUID.randomUUID().toString();

        JSONObject carObject = new JSONObject();
        try {
            carObject.put("id", uniqueID);
            carObject.put("brand", brand);
            carObject.put("manufacturer", manufacturer);
            carObject.put("transmissionType", transmissionType);
            carObject.put("transmission", transmission);
            carObject.put("color", color);
            carObject.put("engineType", engineType);
            carObject.put("bodyType", bodyType);
            carObject.put("price", price);
            if (imageFilePath != null) {
                String internalImagePath = copyImageToInternalStorage(imageFilePath);
                carObject.put("imageUrl", internalImagePath); // Используем путь к изображению из внутреннего хранилища
            }
            else {
                showToast("Please fill in all fields");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            File jsonFile = new File(getFilesDir(), "sportcars.json");
            JSONArray jsonArray;

            if (jsonFile.exists()) {
                // Если файл существует, считываем содержимое
                StringBuilder existingData = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        existingData.append(line);
                    }
                }

                // Если файл не пустой, парсим его как массив JSON
                if (existingData.length() > 0) {
                    jsonArray = new JSONArray(existingData.toString());
                } else {
                    jsonArray = new JSONArray();
                }
            } else {
                // Если файл не существует, создаем новый массив JSON
                jsonArray = new JSONArray();
            }

            // Добавляем новый объект в массив
            jsonArray.put(carObject);

            // Записываем массив JSON обратно в файл
            FileWriter fileWriter = new FileWriter(jsonFile);
            fileWriter.write(jsonArray.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        editTextBrand.setText("");
        editTextManufacturer.setText("");
        editTextTransmissionsType.setText("");
        editTextTransmissions.setText("");
        editTextColor.setText("");
        editTextEngine.setText("");
        editTextBodyType.setText("");
        editTextPrice.setText("");

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private String copyImageToInternalStorage(String imagePath) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "image" + timeStamp + "_";
        String internalImagePath = "";
        try {
            InputStream inputStream = new FileInputStream(new File(imagePath));
            File internalFile = new File(getFilesDir(), imageFileName);
            FileOutputStream outputStream = new FileOutputStream(internalFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            internalImagePath = internalFile.getAbsolutePath(); // Получаем путь к скопированному изображению
        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalImagePath;
    }
}
