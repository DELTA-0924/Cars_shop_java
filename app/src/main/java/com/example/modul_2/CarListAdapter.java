package com.example.modul_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private Context context;
    private List<Car> carList;
    private OnItemClickListener listener;
    public CarListAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }
    public interface OnItemClickListener {
        void onItemClick(Car car);

    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);

        holder.textCarBrand.setText(car.getBrand());
        holder.textCarManufacturer.setText(car.getManufacturer());
        holder.textCarPrice.setText(String.valueOf(car.getPrice()));
        String imagePath =car.getImageUrl();
        String decodedPath = imagePath.replace("\\/", "/");
        Picasso.get()
                .load("file://" + decodedPath) // Здесь вызывайте метод, который возвращает URL изображения
                .placeholder(R.drawable.placeholder_image) // Заглушка, показываемая во время загрузки изображения
                .error(R.drawable.error_image) // Заглушка, показываемая в случае ошибки загрузки
                .into(holder.imageCar);
        Log.d("Image load",car.getImageUrl());
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(car);
            }
            Log.d( "id by car in onBindViewHolder: ",car.getId());
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCar;
        TextView textCarBrand, textCarManufacturer, textCarPrice;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCar = itemView.findViewById(R.id.imageCar);
            textCarBrand = itemView.findViewById(R.id.textCarBrand);
            textCarManufacturer = itemView.findViewById(R.id.textCarManufacturer);
            textCarPrice = itemView.findViewById(R.id.textCarPrice);
        }
    }
}
