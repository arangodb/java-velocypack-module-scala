package com.arangodb.velocypack.module.scala

import com.arangodb.velocypack.VPackModule
import com.arangodb.velocypack.VPackSetupContext
import com.arangodb.velocypack.module.scala.internal.VPackScalaSerializers
import com.arangodb.velocypack.module.scala.internal.VPackScalaDeserializers

import scala.collection.mutable
import scala.collection.concurrent.TrieMap
import scala.collection.immutable.{HashMap, ListMap, TreeMap, TreeSeqMap, VectorMap}

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

    Set[Class[_ <: Seq[_]]](
      classOf[List[Any]],
      classOf[Vector[Any]],
      classOf[Seq[Any]],
      Seq(()).getClass,
      Seq.empty[Any].getClass,
      Nil.getClass
    ).foreach(context.registerSerializer(_, VPackScalaSerializers.SEQ))

    Set[Class[_ <: Map[_, _]]](
      classOf[Map.Map1[_, _]],
      classOf[Map.Map2[_, _]],
      classOf[Map.Map3[_, _]],
      classOf[Map.Map4[_, _]],
      classOf[HashMap[_, _]],
      classOf[VectorMap[_, _]],
      classOf[TreeSeqMap[_, _]],
      classOf[TreeMap[_, _]],
      Map("" -> "").withDefault(null).getClass, // inner Map.WithDefault
      ListMap("" -> "").getClass // inner ListMap.Node
    ).foreach(context.registerSerializer(_, VPackScalaSerializers.MAP_IMMUTABLE))

    Set[Class[_ <: mutable.Map[_, _]]](
      classOf[TrieMap[Any, Any]]
    ).foreach(context.registerSerializer(_, VPackScalaSerializers.MAP_MUTABLE))

    context.registerEnclosingSerializer(classOf[Map[Any, Any]], VPackScalaSerializers.MAP_IMMUTABLE)
    context.registerEnclosingSerializer(classOf[mutable.Map[Any, Any]], VPackScalaSerializers.MAP_MUTABLE)

    context.registerSerializer(classOf[BigInt], VPackScalaSerializers.BIG_INT)
    context.registerSerializer(classOf[BigDecimal], VPackScalaSerializers.BIG_DECIMAL)
    context.registerSerializer(classOf[Option[Any]], VPackScalaSerializers.OPTION)
  }

}
