# Fulfillment Jakarta EE sample

Deployable WAR for WildFly/JBoss showcasing a small fulfillment domain with JPA entities and JAX-RS resources.

## Persistence

Uses the container-managed `java:jboss/datasources/FulfillmentDS` data source. Schema generation is set to `update` for easy local bootstrapping.

## REST API

- `GET /api/orders` – list orders with line items and shipments  
- `GET /api/orders/{id}` – details of a single order  
- `POST /api/orders` – create an order with line items  
- `PUT /api/orders/{id}/status` – update order lifecycle status  
- `GET /api/shipments` – list shipments with items  
- `POST /api/shipments` – create a shipment for an order (line item quantities validated)  
- `PUT /api/shipments/{id}/handover` – mark a shipment handed to carrier (moves order to `SHIPPED`)

## Bootable JAR (WildFly embedded)

Run everything without installing WildFly:

- Dev run: `mvn -pl fulfillment-jakarta -am -DskipTests -Dmaven.repo.local=./.m2 wildfly-jar:dev-run`
- Package runnable JAR: `mvn -pl fulfillment-jakarta -am -DskipTests -Dmaven.repo.local=./.m2 clean package wildfly-jar:package`
- Run packaged server: `java -jar fulfillment-jakarta/target/fulfillment-jakarta-bootable.jar`

The bootable build:

- Assembles WildFly with `jaxrs-server`, `jpa`, and `h2-driver` layers
- Applies `src/main/wildfly/datasource.cli` to configure an in-memory H2 `FulfillmentDS` (`java:jboss/datasources/FulfillmentDS`)

Example payloads:

```json
{
  "orderNumber": "WEB-1001",
  "customerReference": "Priority customer",
  "shippingAddress": {
    "line1": "1 Fulfillment Way",
    "city": "Leipzig",
    "stateOrProvince": "SN",
    "postalCode": "04109",
    "country": "DE"
  },
  "lineItems": [
    { "sku": "SKU-123", "description": "Widget", "quantity": 2 },
    { "sku": "SKU-456", "description": "Gadget", "quantity": 1 }
  ]
}
```

```json
{
  "orderId": 1,
  "carrier": "DHL",
  "trackingNumber": "TRACK-123",
  "items": [
    { "lineItemId": 1, "quantity": 2 },
    { "lineItemId": 2, "quantity": 1 }
  ]
}
```
