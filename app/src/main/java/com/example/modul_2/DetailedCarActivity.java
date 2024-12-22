package com.example.modul_2;



import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedCarActivity extends AppCompatActivity {
    private ImageView imageCar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String imageFilePath;
    private EditText editCarBrand;
    private EditText editCarManufacturer;
    private EditText editCarPrice;
    private EditText editCarTransmission;
    private EditText editCarTransmissionType;
    private EditText editCarBody;
    private EditText editCarEngine;
    private EditText editCarColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_car_layout);
        // Get the selected car from the intent
        if (getIntent() != null && getIntent().hasExtra("selectedCar")) {
            Car selectedCar = getIntent().getParcelableExtra("selectedCar");

             editCarBrand = findViewById(R.id.editCarBrand);
             editCarManufacturer = findViewById(R.id.editCarManufacturer);
             editCarPrice = findViewById(R.id.editCarPrice);
             editCarTransmission = findViewById(R.id.editCarTransmission);
             editCarTransmissionType = findViewById(R.id.editCarTransmissionType);
             editCarBody = findViewById(R.id.editCarBody);
             editCarEngine = findViewById(R.id.editCarEngine);
             editCarColor = findViewById(R.id.editCarColor);

            editCarBrand.setText(selectedCar.getBrand());
            editCarManufacturer.setText(selectedCar.getManufacturer());
            editCarPrice.setText(String.valueOf(selectedCar.getPrice()));
            editCarTransmission.setText(selectedCar.getTransmission());
            editCarTransmissionType.setText(selectedCar.getTransmissionType());
            editCarBody.setText(selectedCar.getBodyType());
            editCarEngine.setText(selectedCar.getEngineType());
            editCarColor.setText(selectedCar.getColor());

             imageCar = findViewById(R.id.imageCar);
            String imageUrl = selectedCar.getImageUrl();

            String decodedPath = imageUrl.replace("\\/", "/");
            Picasso.get()
                    .load("file://"+decodedPath)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageCar);
            Log.d( "car id on saveChanges: ",selectedCar.getId());
            Button saveButton = findViewById(R.id.buttonSaveChanges);
            saveButton.setOnClickListener(v -> saveChanges(selectedCar));
            Button buttonSelectImage = findViewById(R.id.buttonSelectImagedetail);
            buttonSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFileChooser();
                }
            });

        }
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
            imageCar.setImageURI(imageUri);
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

    void saveChanges(Car selectedCar){
        selectedCar.setBrand(editCarBrand.getText().toString());
        selectedCar.setManufacturer(editCarManufacturer.getText().toString());
        selectedCar.setPrice(Integer.parseInt(editCarPrice.getText().toString()));
        selectedCar.setTransmission(editCarTransmission.getText().toString());
        selectedCar.setTransmissionType(editCarTransmissionType.getText().toString());
        selectedCar.setBodyType(editCarBody.getText().toString());
        selectedCar.setEngineType(editCarEngine.getText().toString());
        selectedCar.setColor(editCarColor.getText().toString());
        String internalImagePath = copyImageToInternalStorage(imageFilePath);
        if(internalImagePath!=null)
            selectedCar.setImageUrl(internalImagePath);
        CarDataSource carDataSource = new CarDataSource(this);
       Log.d( "car id on saveChanges: ",selectedCar.getTransmission());
        carDataSource.updateCarInJson(selectedCar);

        Toast.makeText(DetailedCarActivity.this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
        carDataSource.loadCarList();
        finish();
        startActivity(getIntent());
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
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return internalImagePath;
    }

}
