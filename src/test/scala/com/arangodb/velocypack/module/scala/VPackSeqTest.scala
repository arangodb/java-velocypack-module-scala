package com.arangodb.velocypack.module.scala

import java.{util => ju}

import com.arangodb.velocypack.{VPack, VPackBuilder, ValueType}
import org.scalatest.funsuite._
import org.scalatest.matchers._


import scala.beans.BeanProperty

case class SeqTestEntity(@BeanProperty var s: Seq[String] = Seq(), @BeanProperty var i: Seq[Int] = Seq(), @BeanProperty var o: Seq[SeqTestEntity] = Seq()) {
  def this() = this(s = Seq())
}

case class WrappedSeqTestEntity(map: Map[String, Any], seq: Seq[Any], opt: Option[Seq[Any]]) {
  def this() = this(Map.empty, Nil, None)
}

class VPackSeqTest extends AnyFunSuite with should.Matchers {

  test("serialize seq") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(SeqTestEntity(Seq("hello world"), Seq(69), Seq(new SeqTestEntity)))
    vpack should not be null
    vpack.isObject should be(true)
    vpack.get("s").isArray should be(true)
    vpack.get("s").size should be(1)
    vpack.get("s").get(0).isString should be(true)
    vpack.get("s").get(0).getAsString should be("hello world")
    vpack.get("i").isArray should be(true)
    vpack.get("i").size should be(1)
    vpack.get("i").get(0).isInteger should be(true)
    vpack.get("i").get(0).getAsInt should be(69)
    vpack.get("o").isArray should be(true)
    vpack.get("o").size should be(1)
    vpack.get("o").get(0).isObject should be(true)
  }

  test("serialize wrapped sequence") {
    val testEntity = WrappedSeqTestEntity(
      map = Map("foo" -> Seq("a", "b")),
      seq = Seq(List(1, 2, 3)),
      opt = Some(Nil)
    )
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(testEntity)
    vpack should not be null
    vpack.isObject should be(true)
    vpack.get("map").isObject should be(true)
    vpack.get("map").get("foo").isArray should be(true)
    vpack.get("map").get("foo").size should be(2)
    vpack.get("map").get("foo").get(0).getAsString should be("a")
    vpack.get("map").get("foo").get(1).getAsString should be("b")
    vpack.get("seq").isArray should be(true)
    vpack.get("seq").get(0).isArray should be(true)
    vpack.get("seq").get(0).size() should be(3)
    vpack.get("seq").get(0).get(0).getAsInt should be(1)
    vpack.get("seq").get(0).get(1).getAsInt should be(2)
    vpack.get("seq").get(0).get(2).getAsInt should be(3)
    vpack.get("opt").isArray should be(true)
    vpack.get("opt").size() should be(0)
  }

  test("deserialize seq") {
    val builder = new VPackBuilder()
    builder add ValueType.OBJECT
    builder add ("s", ValueType.ARRAY)
    builder add "hello world"
    builder.close
    builder add ("i", ValueType.ARRAY)
    builder add new Integer(69)
    builder.close
    builder add ("o", ValueType.ARRAY)
    builder add ValueType.OBJECT
    builder.close
    builder.close
    builder.close

    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val entity: SeqTestEntity = vp.deserialize(builder.slice, classOf[SeqTestEntity])
    entity should not be null
    entity.s.size should be(1)
    entity.s(0) should be("hello world")
    entity.i.size should be(1)
    entity.i(0) should be(69)
    entity.o.size should be(1)
    entity.o(0) should not be null
    entity.o(0) shouldBe a [SeqTestEntity]
  }

  test("deserialize wrapped sequence") {
    val builder = new VPackBuilder()
    builder add ValueType.OBJECT
    builder add ("map", ValueType.OBJECT)
    builder add ("foo", ValueType.ARRAY)
    builder add "a"
    builder add "b"
    builder.close
    builder.close
    builder add ("seq", ValueType.ARRAY)
    builder add ValueType.ARRAY
    builder add new Integer(1)
    builder add new Integer(2)
    builder add new Integer(3)
    builder.close
    builder.close
    builder add ("opt", ValueType.ARRAY)
    builder.close
    builder.close

    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val entity: WrappedSeqTestEntity = vp.deserialize(builder.slice, classOf[WrappedSeqTestEntity])

    entity shouldEqual WrappedSeqTestEntity(
      map = Map("foo" -> ju.Arrays.asList("a", "b")),
      seq = Seq(ju.Arrays.asList[Long](1, 2, 3)),
      opt = Some(Nil)
    )
  }
}
