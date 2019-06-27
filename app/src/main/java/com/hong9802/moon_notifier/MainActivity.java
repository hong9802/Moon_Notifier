package com.hong9802.moon_notifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Notifier noti;
    GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkLocationServicesStatus())
        {
            showDialogForLocationServiceSetting();
        }
        else
        {
            checkRunTimePermission();
        }
        noti = new Notifier(this);
        gpsTracker = new GpsTracker(MainActivity.this);
    }

    public void Notifier_start(View v) throws ExecutionException, InterruptedException {
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        String address = getCurrentAddress(latitude, longitude);
        noti.createNotification(address);
    }

    public void Notifier_end(View view) {
        noti.removeNotification();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults)
    {
        if(permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length)
        {
            boolean check_result = true;
            for(int result : grandResults)
            {
                if(result != PackageManager.PERMISSION_GRANTED)
                {
                    check_result = false;
                    break;
                }
            }
            if(check_result)
            {
            }
            else
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0]) || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1]))
                {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus())
                {
                    if(checkLocationServicesStatus())
                    {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되었음");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED)
        {
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0]))
            {
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앫을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
        + "위치 설정을 허용해주세요.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    public String getCurrentAddress(double latitude, double longitude)
    {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException e) {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        }
        catch(IllegalArgumentException illegalArgumentException)
        {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if(addresses == null || addresses.size() == 0)
        {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
