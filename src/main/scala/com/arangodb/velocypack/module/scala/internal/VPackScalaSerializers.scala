package com.arangodb.velocypack.module.scala.internal

import com.arangodb.velocypack.VPackSerializer
import com.arangodb.velocypack.VPackSerializationContext
import com.arangodb.velocypack.VPackBuilder
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._
import scala.collection.mutable

object VPackScalaSerializers {

  val OPTION = new VPackSerializer[Option[Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: Option[Any], context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, value.orNull)
  }

  val SEQ = new VPackSerializer[Seq[Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: Seq[Any], context: VPackSerializationContext): Unit = {
      val list: _root_.java.util.List[Any] = ListBuffer(value: _*).asJava
      context.serialize(builder, attribute, list)
    }
  }

  val MAP_IMMUTABLE = new VPackSerializer[Map[Any, Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: Map[Any, Any], context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, value.asJava)
  }

  val MAP_MUTABLE = new VPackSerializer[mutable.Map[Any, Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: mutable.Map[Any, Any], context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, value.asJava)
  }

  val BIG_INT = new VPackSerializer[BigInt] {
    def serialize(builder: VPackBuilder, attribute: String, value: BigInt, context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, value.bigInteger)
  }

  val BIG_DECIMAL = new VPackSerializer[BigDecimal] {
    def serialize(builder: VPackBuilder, attribute: String, value: BigDecimal, context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, value.bigDecimal)
  }
}
