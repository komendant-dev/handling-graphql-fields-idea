package services.graphql

import model.ReportTab

sealed trait FieldType
case object Money extends FieldType
case object PriceRange extends FieldType
case object FullAddress extends FieldType

trait GroupField {
  val fieldKey: String
}

case class Field(fieldKey: String, graphQlQueryField: Option[GraphQlQueryField] = None) extends GroupField
case class CustomField(fieldKey: String, queryField: GraphQlQueryField) extends GroupField
case class TypedField(fieldKey: String, fieldType: FieldType) extends GroupField
case class FragmentField(fieldKey: String, interfacedObjectNames: Seq[String]) extends GroupField

case class Group(groupKey: String, parentGroupKey: String, fields: Map[String, GroupField])

trait Groups {
  val groups: List[Group]
}

object OrdersGroups extends Groups {
  val rootFields = Map(
    "totalTipReceived" -> TypedField("totalTipReceived", Money),
    "cartDiscountAmountSet_presentmentMoney" -> TypedField("cartDiscountAmountSet_presentmentMoney", Money)
  )

  val assignedLocationFields = Map(
    "address1AndAddress2" -> TypedField("address1AndAddress2", FullAddress),
  )

  val assignedLocationLocationFields = Map(
    "address1AndAddress2" -> TypedField("address1AndAddress2", FullAddress),
  )

  val valueFragments = Seq(
    "MoneyV2" -> Seq(GraphQlQueryField("discountValue: amount", Nil, withEdges = false)),
    "PricingPercentageValue" -> Seq(GraphQlQueryField("discountValue: percentage", Nil, withEdges = false))
  )
  val discountApplicationFields = Map(
    "value" -> CustomField("value", GraphQlQueryField("value", Nil, withEdges = false, valueFragments)),
    "title" -> FragmentField("title", Seq("AutomaticDiscountApplication", "ManualDiscountApplication", "ScriptDiscountApplication")),
  )

  val groups = List(
    Group("root", "", rootFields),
    Group("fulfillmentOrders", "root", Map()),
    Group("assignedLocation", "fulfillmentOrders", assignedLocationFields),
    Group("location", "assignedLocation", Map()),
    Group("address", "location", assignedLocationLocationFields),
    // ...
    Group("shippingLines", "root", Map()),
    Group("discountAllocations", "shippingLines", Map()),
    Group("discountApplication", "discountAllocations", discountApplicationFields),
    // ...
  )
}

// HERE WILL BE OUR CODE THAT WILL KNOW HOT TO BUILD CORRECT LIST WITH GraphQlQueryField
object NewQueryFieldsBuilder {

  class QueryFieldCreator {
    def create(field: GroupField): GraphQlQueryField = {
      field match {
        case Field(fieldKey, graphQlQueryField) => ???
        case TypedField(fieldKey, fieldType) => ???
        case CustomField(fieldKey, queryField) => ???
        case FragmentField(fieldKey, objectNames) => ???
      }
    }
  }
  
  def build(groups: Groups, selectedFields: Seq[model.TabField]): Seq[GraphQlQueryField] = ???
}

// OUR EXISTING SERIALIZERS
object GraphQlSerializers extends GraphQlQueryFieldsBuilder {

  // ...

  implicit val ordersSerializer: GraphQlQueryCanBuilt[ReportTab.Orders] = (tab: ReportTab.Orders) => {
    import ReportTab.Orders._

    // our old mapping
    val graphQlQueryFields = ???

    // our new mapping
    val newGraphQlQueryFields = NewQueryFieldsBuilder.build(OrdersGroups, tab.fields)

    // graphQlQueryFields + newGraphQlQueryFields
    val mergedGraphQlQueryFields = ???

  }

  // ...
}
