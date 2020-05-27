package com.arangodb.velocypack.module.scala.internal

import com.arangodb.velocypack.VPackDeserializer
import com.arangodb.velocypack.VPackDeserializationContext
import com.arangodb.velocypack.VPackSlice
import com.arangodb.velocypack.VPackDeserializerParameterizedType
import java.lang.reflect.ParameterizedType
import scala.collection.compat._
import scala.jdk.CollectionConverters._

object VPackScalaDeserializers {

  val OPTION = new VPackDeserializerParameterizedType[Option[Any]] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): Option[Any] =
      throw new UnsupportedOperationException

    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext, t: ParameterizedType): Option[Any] = {
      val value = context.deserialize[Any](vpack, t.getActualTypeArguments()(0))
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
      vpack.arrayIterator().asScala.map { slice: VPackSlice => context.deserialize[Any](slice, clazz) }.toList
    }
  }

  val VECTOR = new VPackDeserializerParameterizedType[Vector[Any]] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): Vector[Any] =
      throw new UnsupportedOperationException

    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext, t: ParameterizedType): Vector[Any] = {
      val clazz = t.getActualTypeArguments()(0).asInstanceOf[Class[Any]]
      vpack.arrayIterator().asScala.map { slice: VPackSlice => context.deserialize[Any](slice, clazz) }.toVector
    }
  }

  val MAP = new VPackDeserializer[Map[Any, Any]] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): Map[Any, Any] =
      context.deserialize[java.util.Map[Any, Any]](vpack, classOf[java.util.Map[Any, Any]]).asScala.toMap
  }

  val BIG_INT = new VPackDeserializer[BigInt] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): BigInt =
      BigInt.javaBigInteger2bigInt(context.deserialize(vpack, classOf[java.math.BigInteger]))
  }

  val BIG_DECIMAL = new VPackDeserializer[BigDecimal] {
    def deserialize(parent: VPackSlice, vpack: VPackSlice, context: VPackDeserializationContext): BigDecimal =
      BigDecimal.javaBigDecimal2bigDecimal(context.deserialize(vpack, classOf[java.math.BigDecimal]))
  }

}
