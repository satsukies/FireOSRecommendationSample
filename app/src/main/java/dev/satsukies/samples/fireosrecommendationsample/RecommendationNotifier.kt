package dev.satsukies.samples.fireosrecommendationsample

import android.content.Context

interface RecommendationNotifier {

  fun setup()

  fun send()

  fun clear()
}