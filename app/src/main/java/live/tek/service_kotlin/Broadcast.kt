package live.tek.service_kotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED
import android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.ACTION_FOUND
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Broadcast(
    private val bluetoothAdapter: BluetoothAdapter,
    private val whatWeHave: (lis: ArrayList<String>) -> Unit
) : BroadcastReceiver() {
    private var list: ArrayList<String> = ArrayList()


    override fun onReceive(context: Context?, intent: Intent?) {
        //   Toast.makeText(context!!, "broadcast receiver", Toast.LENGTH_LONG).show()
        when (intent!!.action) {
            ACTION_FOUND -> found(intent)
            ACTION_DISCOVERY_STARTED -> started()
            ACTION_DISCOVERY_FINISHED -> finished(whatWeHave)
        }
    }

    private fun started() {
        list.clear()
    }

    private fun finished(whatWeHave: (l: ArrayList<String>) -> Unit) {
        bluetoothAdapter.cancelDiscovery()
        bluetoothAdapter.startDiscovery()
        whatWeHave(list)
    }

    private fun found(intent: Intent?) {

        val device = intent!!.getParcelableExtra<BluetoothDevice>(EXTRA_DEVICE)
        val bluetoothClass: BluetoothClass = device!!.bluetoothClass

        val deviceName = device.name
        if (!list.contains(deviceName) && bluetoothClass.deviceClass == 524)
            list.add(deviceName)
    }

}