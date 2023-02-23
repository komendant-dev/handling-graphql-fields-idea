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
