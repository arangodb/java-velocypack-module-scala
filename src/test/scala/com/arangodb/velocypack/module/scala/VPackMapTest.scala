package com.arangodb.velocypack.module.scala

import com.arangodb.velocypack.module.scala.VPackMapTest._
import com.arangodb.velocypack.{VPack, VPackBuilder, ValueType}
import org.scalatest.{FunSuite, Matchers}

import scala.beans.BeanProperty
import scala.collection.immutable._

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


  test("serialize different kinds of nested maps") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()

    val entity = MapTestEntity(m =
      HashTrieMap("seq" -> Seq(
        HashTrieMap("foo" -> 42),
        SortedMap("foo" -> 42),
        ListMap("foo" -> 42),
        HashMap("foo" -> 42),
        Map("foo" -> 42),
        ListMap.empty,
        Map.empty
      )))

    val vpack = vp.serialize(entity)
    vpack should not be null
    vpack.isObject should be(true)
    vpack.size should be(1)
    vpack.get("m").isObject should be(true)
    vpack.get("m").size should be(1)
    vpack.get("m").get("seq").isArray should be(true)

    vpack.get("m").get("seq").get(0).isObject should be(true)
    vpack.get("m").get("seq").get(0).size should be(1)
    vpack.get("m").get("seq").get(0).get("foo").isInt should be(true)
    vpack.get("m").get("seq").get(0).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq").get(1).isObject should be(true)
    vpack.get("m").get("seq").get(1).size should be(1)
    vpack.get("m").get("seq").get(1).get("foo").isInt should be(true)
    vpack.get("m").get("seq").get(1).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq").get(2).isObject should be(true)
    vpack.get("m").get("seq").get(2).size should be(1)
    vpack.get("m").get("seq").get(2).get("foo").isInt should be(true)
    vpack.get("m").get("seq").get(2).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq").get(3).isObject should be(true)
    vpack.get("m").get("seq").get(3).size should be(1)
    vpack.get("m").get("seq").get(3).get("foo").isInt should be(true)
    vpack.get("m").get("seq").get(3).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq").get(4).isObject should be(true)
    vpack.get("m").get("seq").get(4).size should be(1)
    vpack.get("m").get("seq").get(4).get("foo").isInt should be(true)
    vpack.get("m").get("seq").get(4).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq").get(5).isObject should be(true)
    vpack.get("m").get("seq").get(5).size should be(0)

    vpack.get("m").get("seq").get(5).isObject should be(true)
    vpack.get("m").get("seq").get(5).size should be(0)
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
    entity.m.get("s") should be(Some("hello world"))
    entity.m.get("i") should be(Some(69))
  }
}

object VPackMapTest {

  object HashTrieMap {
    def apply[A, B](pairs: (A, B)*): Map[A, B] = new HashMap.HashTrieMap(0, Array(HashMap(pairs: _*)), pairs.size)
  }

}
