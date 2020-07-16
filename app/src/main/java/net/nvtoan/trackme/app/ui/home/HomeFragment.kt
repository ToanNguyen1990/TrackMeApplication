package net.nvtoan.trackme.app.ui.home

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import net.nvtoan.trackme.R
import net.nvtoan.trackme.databinding.FragmentHomeBinding
import net.toannt.hacore.ui.fragment.HMBindingFragment

class HomeFragment: HMBindingFragment<FragmentHomeBinding>(R.layout.fragment_home),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    override fun executeOnBackPressed() {
        super.executeOnBackPressed()
        onExecuteFinishActivity()
    }

    override fun initUI() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun registerObservables() {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}