/**
 * This interface defines methods for logging to Firebase
 */

package com.example.commutual.model.service

interface LogService {
  fun logNonFatalCrash(throwable: Throwable)
}
