/**
 * This file traces a block with Firebase performance
 */

package com.example.commutual.model.service

import com.google.firebase.perf.ktx.trace
import com.google.firebase.perf.metrics.Trace


inline fun <T> trace(name: String, block: Trace.() -> T): T = Trace.create(name).trace(block)
