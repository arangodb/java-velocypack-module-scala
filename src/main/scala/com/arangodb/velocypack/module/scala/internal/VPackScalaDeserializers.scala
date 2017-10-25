package com.arangodb.velocypack.module.scala.internal

import com.arangodb.velocypack.VPackDeserializer
import com.arangodb.velocypack.VPackDeserializationContext
import com.arangodb.velocypack.VPackSlice
import com.arangodb.velocypack.VPackDeserializerParameterizedType
import java.lang.reflect.ParameterizedType
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

object VPackScalaDeserializers {

  val OPTION = new VPackDeserializerParameterizedType[Option[Any]] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): Option[Any] =
      throw new UnsupportedOperationException

    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext, t: ParameterizedType): Option[Any] = {
      val value = context.deserialize(vpack, t.getActualTypeArguments()(0).asInstanceOf[Class[Any]])
      value match {
        case null => None
        case _    => Some(value)
      }
    }
  }

  val LIST = new VPackDeserializerParameterizedType[List[Any]] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): List[Any] =
      throw new UnsupportedOperationException

    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext, t: ParameterizedType): List[Any] = {
      val clazz = t.getActualTypeArguments()(0).asInstanceOf[Class[Any]]
      vpack.arrayIterator().map { slice => context.deserialize(slice, clazz) }.toList
    }
  }

  val MAP = new VPackDeserializer[Map[Any, Any]] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): Map[Any, Any] =
      context.deserialize(vpack, classOf[java.util.Map[Any, Any]]).toMap
  }

}