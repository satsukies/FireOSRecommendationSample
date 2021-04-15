package dev.satsukies.samples.fireosrecommendationsample

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.recommendation.app.ContentRecommendation
import kotlin.random.Random

class LegacyRecommendationNotifier(
  private val context: Context
) : RecommendationNotifier {

  private val manager = NotificationManagerCompat.from(context)

  override fun setup() {
    // no-op
  }

  override fun send() {
    val id = Random(System.currentTimeMillis()).nextInt(0, 10)
    val notification = createNotification(id)

    manager.notify("notif-id-$id".hashCode(), notification)
    Toast.makeText(context, "id: $id notified", Toast.LENGTH_SHORT).show()
  }

  override fun clear() {
    manager.cancelAll()
  }

  private fun createNotification(id: Int): Notification {
    // RecommendationExtender didn't support NotificationCompat.
    // cf. https://developer.amazon.com/ja/docs/fire-tv/recommendations-send-recommendations.html#androidrecommendations
    val recommendation = ContentRecommendation.Builder()
      // required
      .setText("Legacy Recommend text: $id")
      .setTitle("Legacy Recommend title: $id")
      .setContentImage(BitmapFactory.decodeResource(context.resources, R.drawable.banner_w640))
      .setContentIntentData(
        ContentRecommendation.INTENT_TYPE_ACTIVITY,
        Intent(context, MainActivity::class.java),
        100,
        null
      )
      .setBadgeIcon(R.drawable.icon_240)
      // optional
      .setGenres(arrayOf("genre", "is", "open", "end"))
      .setContentTypes(arrayOf(ContentRecommendation.CONTENT_TYPE_VIDEO))
      .setProgress(id * 100, id * 50)
      .build()

    val notification: Notification = recommendation.getNotificationObject(context)

    // set extras for amazon
    // cf. https://developer.amazon.com/ja/docs/fire-tv/recommendations-send-recommendations.html#codeexamplerecommendation
    with(notification.extras) {
      putString("com.amazon.extra.DISPLAY_NAME", "OS7RecomSample")
      putString("com.amazon.extra.PREVIEW_URL", "https://placehold.jp/1920x1080.png")
      putInt("com.amazon.extra.CONTENT_CUSTOMER_RATING_COUNT", 10)
    }

    return notification
  }
}