package com.arangodb.velocypack.module.scala

import com.arangodb.velocypack.{VPack, VPackBuilder, ValueType}
import org.scalatest.funsuite._
import org.scalatest.matchers._

import scala.beans.BeanProperty
import scala.collection.concurrent.TrieMap
import scala.collection.immutable._

case class MapTestEntity(@BeanProperty var m: Map[String, Any] = Map()) {
  def this() = this(Map())
}

class VPackMapTest extends AnyFunSuite with should.Matchers {

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

  test("serialize TrieMap") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(TrieMap("s" -> "hello world", "i" -> 69, "o" -> TrieMap("ss" -> "hello world")))
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

  test("serialize inner Map and MapLike maps") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()

    // dummy transformations that return inner Map classes' implementations
    val dummyMapOperations: Seq[Map[String, _] => Map[String, _]] = Seq(
      _.withDefaultValue(42),
    )

    dummyMapOperations.foreach(dummyOp => {
      val mapLike1 = dummyOp(Map("ss" -> "hello world"))
      val mapLike2 = dummyOp(Map("s" -> "hello world", "i" -> 69, "o" -> mapLike1))
      val entity = MapTestEntity(m = mapLike2)

      val vpack = vp.serialize(entity)

      vpack should not be null
      vpack.isObject should be(true)
      vpack.size should be(1)
      vpack.get("m") should not be null
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
    })
  }

  test("serialize different kinds of nested maps") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()

    val entity = MapTestEntity(m =
      HashMap(
        "seq" -> Seq(
          VectorMap("foo" -> 42),
          TreeSeqMap("foo" -> 42),
          Map("foo" -> 42), // Map.Map1
          SortedMap("foo" -> 42),
          ListMap("foo" -> 42),
          TrieMap("foo" -> 42),
          ListMap.empty,
          Map.empty
        ),
        "seq2" -> Seq(Map("foo" -> 42)), // Map.Map1
        "seq3" -> Seq(Map("foo" -> 42, "foo2" -> 42)), // Map.Map2
        "seq4" -> Seq(Map("foo" -> 42, "foo2" -> 42, "foo3" -> 42)), // Map.Map3
        "seq5" -> Seq(Map("foo" -> 42, "foo2" -> 42, "foo3" -> 42, "foo4" -> 42)), // Map.Map4
        "seq6" -> Seq(TreeMap("foo" -> 42))
      ))

    val vpack = vp.serialize(entity)
    vpack should not be null
    vpack.isObject should be(true)
    vpack.size should be(1)
    vpack.get("m").isObject should be(true)
    vpack.get("m").size should be(6)
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
    vpack.get("m").get("seq").get(5).size should be(1)
    vpack.get("m").get("seq").get(5).get("foo").isInt should be(true)
    vpack.get("m").get("seq").get(5).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq").get(6).isObject should be(true)
    vpack.get("m").get("seq").get(6).size should be(0)

    vpack.get("m").get("seq").get(7).isObject should be(true)
    vpack.get("m").get("seq").get(7).size should be(0)

    vpack.get("m").get("seq2").get(0).isObject should be(true)
    vpack.get("m").get("seq2").get(0).size should be(1)
    vpack.get("m").get("seq2").get(0).get("foo").isInt should be(true)
    vpack.get("m").get("seq2").get(0).get("foo").getAsInt should be(42)

    vpack.get("m").get("seq3").get(0).isObject should be(true)
    vpack.get("m").get("seq3").get(0).size should be(2)
    vpack.get("m").get("seq3").get(0).get("foo2").isInt should be(true)
    vpack.get("m").get("seq3").get(0).get("foo2").getAsInt should be(42)

    vpack.get("m").get("seq4").get(0).isObject should be(true)
    vpack.get("m").get("seq4").get(0).size should be(3)
    vpack.get("m").get("seq4").get(0).get("foo3").isInt should be(true)
    vpack.get("m").get("seq4").get(0).get("foo3").getAsInt should be(42)

    vpack.get("m").get("seq5").get(0).isObject should be(true)
    vpack.get("m").get("seq5").get(0).size should be(4)
    vpack.get("m").get("seq5").get(0).get("foo4").isInt should be(true)
    vpack.get("m").get("seq5").get(0).get("foo4").getAsInt should be(42)

    vpack.get("m").get("seq6").get(0).isObject should be(true)
    vpack.get("m").get("seq6").get(0).size should be(1)
    vpack.get("m").get("seq6").get(0).get("foo").isInt should be(true)
    vpack.get("m").get("seq6").get(0).get("foo").getAsInt should be(42)
  }

  test("deserialize map") {
    val builder = new VPackBuilder
    builder.add(ValueType.OBJECT)
    builder.add("m", ValueType.OBJECT)
    builder.add("s", "hello world")
    builder.add("i", Integer.valueOf(69))
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
