package com.arangodb.velocypack.module.scala.internal

import com.arangodb.velocypack.VPackSerializer
import com.arangodb.velocypack.VPackSerializationContext
import com.arangodb.velocypack.VPackBuilder
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

object VPackScalaSerializers {

  val OPTION = new VPackSerializer[Option[Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: Option[Any], context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, value.orNull)
  }

  val LIST = new VPackSerializer[List[Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: List[Any], context: VPackSerializationContext): Unit = {
      val list: _root_.java.util.List[Any] = ListBuffer(value: _*)
      context.serialize(builder, attribute, list)
    }
  }

  val MAP = new VPackSerializer[Map[Any, Any]] {
    def serialize(builder: VPackBuilder, attribute: String, value: Map[Any, Any], context: VPackSerializationContext): Unit =
      context.serialize(builder, attribute, mapAsJavaMap(value))
  }

}