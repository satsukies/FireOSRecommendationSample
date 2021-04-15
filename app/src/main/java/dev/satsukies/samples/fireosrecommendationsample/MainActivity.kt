package dev.satsukies.samples.fireosrecommendationsample

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dev.satsukies.samples.fireosrecommendationsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private val binding: ActivityMainBinding by lazy {
    DataBindingUtil.setContentView(this, R.layout.activity_main)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val notifier: RecommendationNotifier = when {
      isFireOS() && afterOreo() -> {
        // FireOS specific recommendation
        FireOs7RecommendationNotifier(this)
      }
      afterOreo() -> {
        // recommendation channel
        RecommendationChannelNotifier()
      }
      else -> {
        // legacy recommendation
        LegacyRecommendationNotifier(this)
      }
    }

    notifier.setup()

    binding.btnAdd.setOnClickListener {
      notifier.send()
    }

    binding.btnClear.setOnClickListener {
      notifier.clear()
    }
  }

  private fun isFireOS(): Boolean {
    return Build.MODEL.startsWith("AFT")
  }

  private fun afterOreo(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
  }
}