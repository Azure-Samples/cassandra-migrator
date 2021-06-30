package com.cassandra.migrator.config

import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

case class Rename(from: String, to: String)
object Rename {
  implicit val encoder: Encoder[Rename] = deriveEncoder[Rename]
  implicit val decoder: Decoder[Rename] = deriveDecoder[Rename]
}
