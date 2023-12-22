package com.example.modul_2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
public class CarDataSource {
    private Context context;

    public CarDataSource(Context context) {
        this.context = context;
    }

    public List<Car> loadCarList() {
        List<Car> carList = new ArrayList<>();
        try {
            InputStream inputStream = context.openFileInput("sportcars.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jsonArray = new JSONArray(builder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCar = jsonArray.getJSONObject(i);
                String id = jsonCar.getString("id");
                String brand = jsonCar.getString("brand");
                String manufacturer = jsonCar.getString("manufacturer");
                int price = jsonCar.getInt("price");
                String engineType = jsonCar.getString("engineType");
                String transmissionType = jsonCar.getString("transmissionType");
                String transmission = jsonCar.getString("transmission");
                String bodyType = jsonCar.getString("bodyType");
                String color = jsonCar.getString("color");
                String imageUrl = jsonCar.getString("imageUrl");
                Log.d("Json data brand", brand);
                Log.d("Json data image", imageUrl);
                Car car = new Car(id,brand, manufacturer, price, engineType, transmissionType, transmission, bodyType, color, imageUrl);
                carList.add(car);
                Log.d( "id by car: ",car.getId());
            }

            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return carList;
    }

    public void updateCarInJson(Car updatedCar) {
        try {
            InputStream inputStream = context.openFileInput("sportcars.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            Log.d( "id by car: ",updatedCar.getBrand());
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            JSONArray jsonArray = new JSONArray(builder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCar = jsonArray.getJSONObject(i);
                String id = jsonCar.getString("id");

                // Найдем JSON объект по полю "id"
                if (id.equals(updatedCar.getId())) {
                    // Обновим данные в JSON объекте из объекта Car
                    jsonCar.put("brand", updatedCar.getBrand());
                    jsonCar.put("manufacturer", updatedCar.getManufacturer());
                    jsonCar.put("price", updatedCar.getPrice());
                    jsonCar.put("engineType", updatedCar.getEngineType());
                    jsonCar.put("transmissionType", updatedCar.getTransmissionType());
                    jsonCar.put("transmission", updatedCar.getTransmission());
                    jsonCar.put("bodyType", updatedCar.getBodyType());
                    jsonCar.put("color", updatedCar.getColor());
                    jsonCar.put("imageUrl", updatedCar.getImageUrl());
                    Log.d( "succes  id by json: ",id);
                    Log.d( "succes  Id by car data: ",updatedCar.getId());
                    break; // После обновления завершаем цикл

                }
                Log.d( "id by json: ",id);
                Log.d( "Id by car data: ",updatedCar.getId());
            }

            // Записываем обновленные данные обратно в файл
            FileOutputStream outputStream = context.openFileOutput("sportcars.json", Context.MODE_PRIVATE);
            outputStream.write(jsonArray.toString().getBytes());
            outputStream.close();

            inputStream.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


}
