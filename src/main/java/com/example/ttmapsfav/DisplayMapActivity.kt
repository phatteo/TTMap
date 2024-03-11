package com.example.ttmapsfav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ttmapsfav.databinding.ActivityDisplayMapBinding
import com.example.ttmapsfav.models.UserMap
import com.google.android.gms.maps.model.LatLngBounds

private const val TAG = "DisplayMapActivity"

class DisplayMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayMapBinding
    private lateinit var userMap: UserMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userMap = intent.getSerializableExtra(Utils.EXTRA_USER_MAP) as UserMap
        supportActionBar?.title = userMap.title

// Lấy SupportMapFragment và nhận thông báo khi bản đồ sẵn sàng được sử dụng.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Thao tác bản đồ một khi có sẵn.
     * Lệnh gọi lại này được kích hoạt khi bản đồ đã sẵn sàng để sử dụng.
     * Đây là nơi chúng ta có thể thêm điểm đánh dấu hoặc dòng, thêm người nghe hoặc di chuyển camera. Trong trường hợp này,
     * chúng tôi vừa thêm điểm đánh dấu gần Sydney, Australia.
     * Nếu dịch vụ Google Play chưa được cài đặt trên thiết bị, người dùng sẽ được nhắc cài đặt
     * nó bên trong SupportMapFragment. Phương pháp này sẽ chỉ được kích hoạt khi người dùng đã
     * đã cài đặt dịch vụ Google Play và quay lại ứng dụng.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.i(TAG, "map: ${userMap.title}")
        val boundsBuilder = LatLngBounds.builder()
        for(place in userMap.places){
            val latLng = LatLng(place.latitude, place.longitude)
            boundsBuilder.include(latLng)
            mMap.addMarker(MarkerOptions().position(latLng).title(place.title).snippet(place.description))
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),
            1000, 1000, 0))

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),
            1000, 1000, 0))
    }
}