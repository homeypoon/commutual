

package com.example.commutual.model.service.impl

import com.example.commutual.model.service.ConfigurationService
import com.example.commutual.BuildConfig
import com.example.commutual.R.xml as AppConfig
import com.example.commutual.model.service.trace
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class ConfigurationServiceImpl @Inject constructor() : ConfigurationService {
  private val remoteConfig
    get() = Firebase.remoteConfig

  init {
    if (BuildConfig.DEBUG) {
      val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
      remoteConfig.setConfigSettingsAsync(configSettings)
    }

    remoteConfig.setDefaultsAsync(AppConfig.remote_config_defaults)
  }

  override suspend fun fetchConfiguration(): Boolean =
    trace(FETCH_CONFIG_TRACE) { remoteConfig.fetchAndActivate().await() }

  override val isShowPostEditButtonConfig: Boolean
    get() = remoteConfig[SHOW_POST_EDIT_BUTTON_KEY].asBoolean()

  companion object {
    private const val SHOW_POST_EDIT_BUTTON_KEY = "show_post_edit_button"
    private const val FETCH_CONFIG_TRACE = "fetchConfig"
  }
}
