

package com.example.commutual.model.service

interface ConfigurationService {
  suspend fun fetchConfiguration(): Boolean
  val isShowPostEditButtonConfig: Boolean
}
