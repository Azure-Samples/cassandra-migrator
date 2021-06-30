package com.cassandra.migrator.config

sealed trait CopyType
object CopyType {
  case object WithTimestampPreservation extends CopyType
  case object NoTimestampPreservation extends CopyType
}
