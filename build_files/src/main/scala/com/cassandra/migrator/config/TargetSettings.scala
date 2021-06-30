package com.cassandra.migrator.config

import cats.implicits._
import io.circe.{ Decoder, DecodingFailure, Encoder, Json }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.syntax._

sealed trait TargetSettings
object TargetSettings {
  case class Cassandra(host: String,
                       port: Int,
                       localDC: Option[String],
                       credentials: Option[Credentials],
                       sslOptions: Option[SSLOptions],
                       keyspace: String,
                       table: String,
                       connections: Option[Int],
                       stripTrailingZerosForDecimals: Boolean)
      extends TargetSettings

  implicit val decoder: Decoder[TargetSettings] =
    Decoder.instance { cursor =>
      cursor.get[String]("type").flatMap {
        case "cassandra" =>
          deriveDecoder[Cassandra].apply(cursor)
        case otherwise =>
          Left(DecodingFailure(s"Invalid target type: ${otherwise}", cursor.history))
      }
    }

  implicit val encoder: Encoder[TargetSettings] =
    Encoder.instance {
      case t: Cassandra =>
        deriveEncoder[Cassandra].encodeObject(t).add("type", Json.fromString("cassandra")).asJson
    }
}
