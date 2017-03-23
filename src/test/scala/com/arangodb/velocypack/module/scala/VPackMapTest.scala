package com.arangodb.velocypack.module.scala

import org.scalatest.FunSuite
import org.scalatest.Matchers
import scala.beans.BeanProperty
import com.arangodb.velocypack.VPack
import com.arangodb.velocypack.VPackBuilder
import com.arangodb.velocypack.ValueType

case class MapTestEntity(@BeanProperty var m: Map[String, Any] = Map()) {
  def this() = this(Map())
}

class VPackMapTest extends FunSuite with Matchers {

  test("serialize map") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(MapTestEntity(Map("s" -> "hello world", "i" -> 69, "o" -> Map("ss" -> "hello world"))))
    vpack should not be null
    vpack.isObject should be(true)
    vpack.size should be(1)
    vpack.get("m").isObject should be(true)
    vpack.get("m").size should be(3)
    vpack.get("m").get("s").isString should be(true)
    vpack.get("m").get("s").getAsString should be("hello world")
    vpack.get("m").get("i").isInteger should be(true)
    vpack.get("m").get("i").getAsInt should be(69)
    vpack.get("m").get("o").isObject should be(true)
    vpack.get("m").get("o").size should be(1)
    vpack.get("m").get("o").get("ss").isString should be(true)
    vpack.get("m").get("o").get("ss").getAsString should be("hello world")
  }

  test("serialize map direct") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(Map("s" -> "hello world", "i" -> 69, "o" -> Map("ss" -> "hello world")))
    vpack should not be null
    vpack.isObject should be(true)
    vpack.size should be(3)
    vpack.get("s").isString should be(true)
    vpack.get("s").getAsString should be("hello world")
    vpack.get("i").isInteger should be(true)
    vpack.get("i").getAsInt should be(69)
    vpack.get("o").isObject should be(true)
    vpack.get("o").size should be(1)
    vpack.get("o").get("ss").isString should be(true)
    vpack.get("o").get("ss").getAsString should be("hello world")
  }

  test("deserialize map") {
    val builder = new VPackBuilder
    builder add ValueType.OBJECT
    builder add ("m", ValueType.OBJECT)
    builder add ("s", "hello world")
    builder add ("i", new Integer(69))
    builder.close
    builder.close

    val vp = new VPack.Builder().registerModule(new VPackScalaModule()).build()
    val entity: MapTestEntity = vp.deserialize(builder.slice, classOf[MapTestEntity])
    entity should not be null
    entity.m.size should be(2)
    entity.m.get("s").isDefined should be(true)
    entity.m.get("s").get should be("hello world")
    entity.m.get("i").isDefined should be(true)
    entity.m.get("i").get should be(69)
  }

}