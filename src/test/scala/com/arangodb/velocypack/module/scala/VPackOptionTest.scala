package com.arangodb.velocypack.module.scala

import scala.beans.BeanProperty

import org.scalatest.funsuite._
import org.scalatest.matchers._

import com.arangodb.velocypack.VPack
import com.arangodb.velocypack.VPackBuilder
import com.arangodb.velocypack.ValueType

case class OptionTestEntity(@BeanProperty var s: Option[String] = Option.empty,
                            @BeanProperty var i: Option[Int] = Option.empty,
                            @BeanProperty var o: Option[OptionTestEntity] = Option.empty) {
  def this() = this(s = Option.empty)
}

class VPackOptionTest extends AnyFunSuite with should.Matchers {

  test("serialize Option") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(OptionTestEntity(Some("hello world"), Some(69), Some(new OptionTestEntity)))
    vpack should not be null
    vpack.isObject should be(true)
    vpack.get("s").isString should be(true)
    vpack.get("s").getAsString should be("hello world")
    vpack.get("i").isInteger should be(true)
    vpack.get("i").getAsInt should be(69)
    vpack.get("o").isObject() should be(true)
  }

  test("deserialize Option") {
    val builder = new VPackBuilder()
    builder add ValueType.OBJECT
    builder add ("s", "hello world")
    builder add ("i", new Integer(69))
    builder add ("o", ValueType.OBJECT)
    builder.close
    builder.close

    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val entity: OptionTestEntity = vp.deserialize(builder.slice, classOf[OptionTestEntity])
    entity should not be null
    entity.s.isDefined should be(true)
    entity.s.get should be("hello world")
    entity.i.isDefined should be(true)
    entity.i.get should be(69)
    entity.o.isDefined should be(true)
  }

  test("deserialize null") {
    val builder = new VPackBuilder()
    builder.add(ValueType.OBJECT)
    builder.add("s", ValueType.NULL)
    builder.add("i", ValueType.NULL)
    builder.add("o", ValueType.NULL)
    builder.close

    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val entity: OptionTestEntity = vp.deserialize(builder.slice, classOf[OptionTestEntity])
    entity should not be null
    entity.s should not be null
    entity.s.isDefined should be(false)
    entity.i should not be null
    entity.i.isDefined should be(false)
    entity.o should not be null
    entity.o.isDefined should be(false)
  }
}