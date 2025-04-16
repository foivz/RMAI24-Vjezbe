package hr.foi.rmai.memento.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TaskTimerService : Service() {
    
    override fun onBind(intent: Intent): IBinder? = null
}