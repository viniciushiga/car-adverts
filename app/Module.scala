import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import com.google.inject.{AbstractModule, Singleton}
import db.DynamoDBClientProvider

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AmazonDynamoDBAsync])
      .toProvider(classOf[DynamoDBClientProvider])
      .in(classOf[Singleton])
  }
}
