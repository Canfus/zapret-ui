package ru.zapret.logging

import java.time.Instant

data class ProcessLog(
  val timestamp: Instant = Instant.now(),
  val source: Source,
  val message: String
) {
  enum class Source {
    STDOUT,
    STDERR,
    SYSTEM
  }
}