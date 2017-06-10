package com.arangodb.velocypack.module.scala

import com.arangodb.velocypack.VPackModule
import com.arangodb.velocypack.VPackSetupContext
import com.arangodb.velocypack.module.scala.internal.VPackScalaSerializers
import com.arangodb.velocypack.module.scala.internal.VPackScalaDeserializers
import com.arangodb.velocypack.VPackInstanceCreator

class VPackScalaModule extends VPackModule {

  def setup[C <: VPackSetupContext[C]](context: C): Unit = {
    context.registerDeserializer(classOf[Option[Any]], VPackScalaDeserializers.OPTION)
    context.registerDeserializer(classOf[collection.List[Any]], VPackScalaDeserializers.LIST)
    context.registerDeserializer(classOf[collection.Map[Any, Any]], VPackScalaDeserializers.MAP)

    context.registerSerializer(classOf[Option[Any]], VPackScalaSerializers.OPTION)
    context.registerSerializer(classOf[collection.List[Any]], VPackScalaSerializers.LIST)
    context.registerSerializer(classOf[collection.Map[Any, Any]], VPackScalaSerializers.MAP)
    context.registerEnclosingSerializer(classOf[Map[Any, Any]], VPackScalaSerializers.MAP)
  }

}