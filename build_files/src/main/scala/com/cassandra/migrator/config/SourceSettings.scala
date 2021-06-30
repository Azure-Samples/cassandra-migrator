package com.cassandra.migrator.config

import cats.implicits._
import io.circe.syntax._
import io.circe.{ Decoder, DecodingFailure, Encoder, Json, ObjectEncoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

sealed trait SourceSettings
object SourceSettings {
  case class Cassandra(host: String,
                       port: Int,
                       localDC: Option[String],
                       credentials: Option[Credentials],
                       sslOptions: Option[SSLOptions],
                       keyspace: String,
                       table: String,
                       splitCount: Option[Int],
                       connections: Option[Int],
                       fetchSize: Int,
                       preserveTimestamps: Boolean,
                       where: Option[String])
      extends SourceSettings

  implicit val decoder: Decoder[SourceSettings] = Decoder.instance { cursor =>
    cursor.get[String]("type").flatMap {
      case "cassandra" =>
        deriveDecoder[Cassandra].apply(cursor)
      case otherwise =>
        Left(DecodingFailure(s"Unknown source type: ${otherwise}", cursor.history))
    }
  }

  implicit val encoder: Encoder[SourceSettings] = Encoder.instance {
    case s: Cassandra =>
      deriveEncoder[Cassandra]
        .encodeObject(s)
        .add("type", Json.fromString("cassandra"))
        .asJson
  }
}
