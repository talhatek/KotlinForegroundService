package live.tek.service_kotlin

import android.app.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder


class ForegroundService : Service() {
    private val CHANNEL_ID = "ForegroundService Kotlin"
    private val ACTIONPAUSE = "kill"
    private val ActionStart = "start"
    private lateinit var receiver: BroadcastReceiver

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ActionStart -> {
                val startKeyword = intent.getStringExtra("StartKeyword")
                startForeGroundService(startKeyword!!)
                scan()
            }
            ACTIONPAUSE -> {
                unregisterReceiver(receiver)
                stopForeground(true)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun notify(str: String) {
        val notification: Notification
        val stopSelf = Intent(this@ForegroundService, MainActivity::class.java)
        stopSelf.action = ACTIONPAUSE
        val killButton = PendingIntent.getService(
            this@ForegroundService,
            0,
            stopSelf,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val action = Notification.Action(R.drawable.ic_close_24, "Close", killButton)
            notification = Notification.Builder(this@ForegroundService, CHANNEL_ID)
                .setContentTitle("Foreground Service Kotlin Example")
                .setContentText("$str people around you")
                .setSmallIcon(R.drawable.ic_notification_24)
                .setNumber(3)
                .addAction(action)
                .build()
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, notification)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val style = Notification.BigTextStyle().bigText(str)
            notification = Notification.Builder(this@ForegroundService)
                .setContentTitle("Foreground Service Kotlin Example")
                .setContentText("$str people around you")
                .setSmallIcon(R.drawable.ic_notification_24)
                .addAction(R.drawable.ic_close_24, "Close", killButton)
                .setStyle(style)
                .build()
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, notification)
        }

    }

    private fun scan() {
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        receiver = Broadcast(bluetoothAdapter) { lis ->
            notify(makeContentText(array = lis))
        }
        registerReceiver(receiver, filter)
        bluetoothAdapter.startDiscovery()

    }

    private fun makeContentText(array: ArrayList<String>): String {
        val string = " You have ${array.size} people around you\n"
        var str = ""
        array.forEach {
            str += it + "\n"
        }
        return string + str
    }

    private fun startForeGroundService(str: String) {
        val pauseIntent = Intent(this, ForegroundService::class.java)
        pauseIntent.action = ACTIONPAUSE
        val pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                val action = Notification.Action(R.drawable.ic_close_24, "Close", pendingPrevIntent)
                val notification = Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Foreground Service Kotlin Example")
                    .setContentText(str)


                    .setSmallIcon(R.drawable.ic_notification_24)
                    .setContentIntent(pendingIntent)
                    .addAction(action)
                    .build()
                startForeground(1, notification)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> {
                val style = Notification.BigTextStyle().bigText(str)
                val notification = Notification.Builder(this)
                    .setContentTitle("Foreground Service Kotlin Example")
                    .setContentText(str)
                    .setStyle(style)

                    .setSmallIcon(R.drawable.ic_notification_24)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.ic_close_24, "Close", pendingPrevIntent)
                    .build()
                startForeground(1, notification)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 -> {
                val notification = Notification.Builder(this@ForegroundService)
                    .setContentTitle("Foreground Service Kotlin Example")
                    .setContentText(str)

                    .setSmallIcon(R.drawable.ic_notification_24)
                    .setContentIntent(pendingPrevIntent)
                    .notification
                startForeground(1, notification)
            }
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}
/*companion object {
    fun startService(context: Context, message: String) {
        val startIntent = Intent(context, ForegroundService::class.java)
        startIntent.putExtra("inputExtra", message)
        ContextCompat.startForegroundService(context, startIntent)
    }

    fun stopService(context: Context) {
        val stopIntent = Intent(context, ForegroundService::class.java)
        context.stopService(stopIntent)
    }
}*/

/*  val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Foreground Service Kotlin Example")
      .setContentText(str + " people around you")
      .setSmallIcon(R.drawable.ic_notification_24)
      .setContentIntent(pendingIntent)
      .addAction(R.drawable.ic_close_24, "Stop", pendingPrevIntent)
      .build()*/
/*  val timer = object : CountDownTimer(10000, 2000) {
    override fun onTick(millisUntilFinished: Long) {}


    override fun onFinish() {
        val notification:Notification
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val stopSelf = Intent(this@ForegroundService, MainActivity::class.java)
            stopSelf.action = ACTIONPAUSE
         //   PendingIntent pStopSelf = PendingIntent.getService(this@ForegroundService, 0, stopSelf,PendingIntent.FLAG_CANCEL_CURRENT)
            val killButton = PendingIntent.getService(this@ForegroundService,0,stopSelf,PendingIntent.FLAG_CANCEL_CURRENT)
            val action =Notification.Action(R.drawable.ic_close_24,"Close",killButton)
            notification = Notification.Builder(this@ForegroundService, CHANNEL_ID)
                .setContentTitle("Foreground Service Kotlin Example")
                .setContentText("2 people around you")
                .setSmallIcon(R.drawable.ic_notification_24)
                 .addAction(action)
                .build()
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1,notification)
        }
        else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            notification = Notification.Builder(this@ForegroundService)
                .setContentTitle("Foreground Service Kotlin Example")
                .setContentText("3 people around you")
                .setSmallIcon(R.drawable.ic_notification_24)

                .build()
            val mNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1,notification)
        }


    }
}
timer.start()*/
