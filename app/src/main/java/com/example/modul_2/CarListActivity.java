package com.example.modul_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CarListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarListAdapter carListAdapter;
    private List<Car> carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_list_item);
        recyclerView = findViewById(R.id.recyclerViewCars);
        CarDataSource carDataSource = new CarDataSource(this);
        carList = carDataSource.loadCarList();
        carListAdapter = new CarListAdapter(this, carList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carListAdapter);
        EditText filterbrand=findViewById(R.id.filtertextBrand);
        //EditText filterManif=findViewById(R.id.filtertextManifacturer);
        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortCarsByPrice();
            }
        });
        Button filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button active");
                //filterCarsByManufacturer(filterManif.getText().toString());
                filterCarsByBrand(filterbrand.getText().toString());
            }
        });
        carListAdapter.setOnItemClickListener(new CarListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Car car) {
                // Start a new activity and pass the selected car details
                Intent intent = new Intent(CarListActivity.this, DetailedCarActivity.class);
                intent.putExtra("selectedCar", car);
                Log.d( "id by car in onItemClick: ",car.getId());
                startActivity(intent);
            }
        });
    }

    // Метод обновления данных в адаптере после сортировки или фильтрации
    private void updateAdapter(List<Car> newList) {
        carListAdapter = new CarListAdapter(this, newList);
        recyclerView.setAdapter(carListAdapter);
    }

// Методы для сортировки и фильтрации (предполагается, что они уже написаны)

    private void sortCarsByPrice() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car car1, Car car2) {
                return Integer.compare(car1.getPrice(), car2.getPrice());
            }
        });
        updateAdapter(carList);
        carListAdapter.notifyDataSetChanged();
        carListAdapter.setOnItemClickListener(new CarListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Car car) {
                // Start a new activity and pass the selected car details
                Intent intent = new Intent(CarListActivity.this, DetailedCarActivity.class);
                intent.putExtra("selectedCar", car);
                Log.d( "id by car in onItemClick: ",car.getId());
                startActivity(intent);
            }
        });
    }

    private void filterCarsByManufacturer(String manufacturer) {
        List<Car> filteredList = new ArrayList<>();
        for (Car car : carList) {
            if (car.getManufacturer().equalsIgnoreCase(manufacturer)) {
                filteredList.add(car);
            }
        }
        // Создаем новый Intent для перезагрузки активности
        updateAdapter(filteredList);
        carListAdapter.notifyDataSetChanged();
    }

    private void filterCarsByBrand(String brand) {
        List<Car> filteredList = new ArrayList<>();
        for (Car car : carList) {
            if (car.getBrand().equalsIgnoreCase(brand)) {
                filteredList.add(car);
            }
        }
        updateAdapter(filteredList);
        carListAdapter.notifyDataSetChanged();
        carListAdapter.setOnItemClickListener(new CarListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Car car) {
                // Start a new activity and pass the selected car details
                Intent intent = new Intent(CarListActivity.this, DetailedCarActivity.class);
                intent.putExtra("selectedCar", car);
                Log.d( "id by car in onItemClick: ",car.getId());
                startActivity(intent);
            }
        });
    }


    // Метод для загрузки списка автомобилей из JSON

}

