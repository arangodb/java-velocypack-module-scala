package com.arangodb.velocypack.module.scala

import org.scalatest.Matchers
import org.scalatest.FunSuite
import scala.beans.BeanProperty
import com.arangodb.velocypack.VPack
import com.arangodb.velocypack.VPackBuilder
import com.arangodb.velocypack.ValueType

case class ListTestEntity(@BeanProperty var s: List[String] = List(), @BeanProperty var i: List[Int] = List(), @BeanProperty var o: List[ListTestEntity] = List()) {
  def this() = this(s = List())
}

class VPackListTest extends FunSuite with Matchers {

  test("serialize list") {
    val vp = new VPack.Builder().registerModule(new VPackScalaModule).build()
    val vpack = vp.serialize(ListTestEntity(List("hello world"), List(69), List(new ListTestEntity)))
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

  test("deserialize list") {
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
    val entity: ListTestEntity = vp.deserialize(builder.slice, classOf[ListTestEntity])
    entity should not be null
    entity.s.size should be(1)
    entity.s(0) should be("hello world")
    entity.i.size should be(1)
    entity.i(0) should be(69)
    entity.o.size should be(1)
    entity.o(0) should not be null
    entity.o(0) shouldBe a [ListTestEntity]
  }

}
