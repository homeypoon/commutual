

package com.example.commutual.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
