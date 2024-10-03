import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.connectsit.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extract category and course info from the message data
        val category = remoteMessage.data["category"] ?: "Other"
        val courseName = remoteMessage.data["courseName"] ?: "General"

        // Display the notification
        sendNotification(category, courseName)
    }

    private fun sendNotification(category: String, courseName: String) {
        val channelId = "document_upload_channel"
        val notificationId = 1001

        // Check if notification permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        // Create notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Document Upload Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for uploaded documents by category"
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.imgi)
            .setContentTitle("New Document Uploaded")
            .setContentText("Category: $category, Course: $courseName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Show notification
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
}
