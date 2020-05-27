package com.arangodb.velocypack.module.scala

import scala.language.existentials

import com.arangodb.velocypack.VPackModule
import com.arangodb.velocypack.VPackSetupContext
import com.arangodb.velocypack.module.scala.internal.VPackScalaSerializers
import com.arangodb.velocypack.module.scala.internal.VPackScalaDeserializers

import scala.collection.immutable.{HashMap, ListMap, TreeMap}
import scala.util.Try

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
    VPackScalaModule.sequencesSerializers.foreach(context.registerSerializer(_, VPackScalaSerializers.SEQ))
    VPackScalaModule.mapSerializers.foreach(context.registerSerializer(_, VPackScalaSerializers.MAP))

    context.registerEnclosingSerializer(classOf[Map[Any, Any]], VPackScalaSerializers.MAP)

    context.registerSerializer(classOf[BigInt], VPackScalaSerializers.BIG_INT)
    context.registerSerializer(classOf[Option[Any]], VPackScalaSerializers.OPTION)
    context.registerSerializer(classOf[BigDecimal], VPackScalaSerializers.BIG_DECIMAL)
  }

}

object VPackScalaModule {
  val sequencesSerializers = Set(
    classOf[List[Any]],
    classOf[Vector[Any]],
    classOf[Seq[Any]],
    Seq(()).getClass,
    Seq.empty.getClass,
    Nil.getClass
  )

  val mapSerializers = {
    val Array(major, minor) = scala.util.Properties.versionNumberString.split("[.]").map(_.toInt).take(2)
    if (major >= 3 || (major == 2 && minor >= 13)) {
      Set(
        classOf[Map.Map1[_,_]],
        classOf[Map.Map2[_,_]],
        classOf[Map.Map3[_,_]],
        classOf[Map.Map4[_,_]],
        classOf[HashMap[_, _]],
        classOf[TreeMap[_, _]],
        ListMap(() -> ()).getClass,
        classOf[Map[_, _]]
      )
    } else {
      def loadClass(name: String): Option[Class[_]] = Try {
        Thread.currentThread().getContextClassLoader.loadClass(name)
      }.toOption

      loadClass("scala.collection.immutable.HashMap$HashMap1") ++ // removed from scala 2.13
        loadClass("scala.collection.immutable.HashMap$HashTrieMap") ++ // removed from scala 2.13
        Set(
          classOf[Map.Map1[_,_]],
          classOf[Map.Map2[_,_]],
          classOf[Map.Map3[_,_]],
          classOf[Map.Map4[_,_]],
          classOf[TreeMap[_, _]],
          ListMap(() -> ()).getClass,
          classOf[Map[_, _]]
        )
    }
  }
}