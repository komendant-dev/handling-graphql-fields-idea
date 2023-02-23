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
