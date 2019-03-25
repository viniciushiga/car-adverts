package db

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.model._
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsync, AmazonDynamoDBAsyncClientBuilder}
import javax.inject.{Inject, Provider}
import play.api.Configuration

import scala.collection.JavaConverters.asScalaIterator

class DynamoDBClientProvider @Inject() (config: Configuration) extends Provider[AmazonDynamoDBAsync] {
  override def get(): AmazonDynamoDBAsync = {
    val client = AmazonDynamoDBAsyncClientBuilder
      .standard()
      .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(config.get[String]("dynamodb.endpoint"), config.get[String]("dynamodb.region")))
      .build()

    val tableName = config.get[String]("dynamodb.table-name")
    val tableExists = asScalaIterator(client.listTables().getTableNames().iterator()).exists(t => t.equals(tableName))

    if (!tableExists) {
      val createTableRequest = new CreateTableRequest()
        .withTableName(tableName)
        .withAttributeDefinitions(new AttributeDefinition("id", ScalarAttributeType.S))
        .withKeySchema(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH))
        .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1l).withWriteCapacityUnits(1l))

      client.createTable(createTableRequest)
    }

    client
  }
}
