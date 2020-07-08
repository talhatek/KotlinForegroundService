package live.tek.service_kotlin

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            101
        )

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(BluetoothAdapter.ACTION_REQUEST_ENABLE),
                101
            )
        }
        buttonStart.setOnClickListener {
            val intent = Intent(this, ForegroundService::class.java)
            intent.action = "start"
            startService(intent)
            //  ForegroundService.startService(this, "Foreground Service is running...")
        }
        buttonStop.setOnClickListener {
            val intent = Intent(this, ForegroundService::class.java)
            intent.action = "kill"
            startService(intent)
            // ForegroundService.stopService(this)
        }
    }


}