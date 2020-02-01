package com.arangodb.velocypack.module.scala

import com.arangodb.velocypack.VPackModule
import com.arangodb.velocypack.VPackSetupContext
import com.arangodb.velocypack.module.scala.internal.VPackScalaSerializers
import com.arangodb.velocypack.module.scala.internal.VPackScalaDeserializers

import scala.collection.immutable.{HashMap, ListMap, TreeMap}

class VPackScalaModule extends VPackModule {

  def setup[C <: VPackSetupContext[C]](context: C): Unit = {

    // deserializers

    context.registerDeserializer(classOf[Seq[Any]], VPackScalaDeserializers.VECTOR)
    context.registerDeserializer(classOf[Vector[_]], VPackScalaDeserializers.VECTOR)
    context.registerDeserializer(classOf[List[_]], VPackScalaDeserializers.LIST)

    context.registerDeserializer(classOf[Map[Any, Any]], VPackScalaDeserializers.MAP)

    context.registerDeserializer(classOf[Option[Any]], VPackScalaDeserializers.OPTION, true)
    context.registerDeserializer(classOf[BigInt], VPackScalaDeserializers.BIG_INT)
    context.registerDeserializer(classOf[BigDecimal], VPackScalaDeserializers.BIG_DECIMAL)

    // serializers

    Set(
      classOf[List[Any]],
      classOf[Vector[Any]],
      classOf[Seq[Any]],
      Seq(Unit).getClass,
      Seq.empty.getClass,
      Nil.getClass
    ).foreach(context.registerSerializer(_, VPackScalaSerializers.SEQ))

    Set(
      classOf[HashMap.HashMap1[_, _]],
      classOf[HashMap.HashTrieMap[_, _]],
      classOf[TreeMap[_, _]],
      ListMap(Unit -> Unit).getClass,
      classOf[Map[_, _]]
    ).foreach(context.registerSerializer(_, VPackScalaSerializers.MAP))

    context.registerEnclosingSerializer(classOf[Map[Any, Any]], VPackScalaSerializers.MAP)

    context.registerSerializer(classOf[BigInt], VPackScalaSerializers.BIG_INT)
    context.registerSerializer(classOf[Option[Any]], VPackScalaSerializers.OPTION)
    context.registerSerializer(classOf[BigDecimal], VPackScalaSerializers.BIG_DECIMAL)
  }

}
