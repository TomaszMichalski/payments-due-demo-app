# Payments Due Demo Application

This is a mini-project application to serve the functionality of recording payments 
that are due, recording doing these payments and retrieving a list of payments that 
are due in a specific amount of time.

The application consists only of backend, which provides a REST API containing 4 endpoints:

* record payment that is due
* record payment is done
* get payments due in given days
* get payments due by given date

For storage, AWS DynamoDB is used.

## API docs

API docs are also available as [OpenAPI definition](http://localhost:8080/payments-due-demo-app/swagger-ui/index.html) when application is running.

### Record payment that is due

```
PUT /payments-due-demo-app/v1/payments
{
  "paymentId": "string",
  "name": "string",
  "vendor": "string",
  "amount": 0,
  "currency": "string",
  "due": "2023-09-20T12:37:33.990Z"
}
```

Returns:
* 201 Created
* 409 Conflict

### Record payment is done

```
DELETE /payments-due-demo-app/v1/payments/{paymentId}
```

Returns:
* 204 No Content
* 404 Not Found

### Get payments due in given days

```
GET /payments-due-demo-app/v1/payments?days={number}
```

Returns:

```
{
  "paymentsDue": [
    {
      "paymentId": "string",
      "name": "string",
      "vendor": "string",
      "amount": 0,
      "currency": "string",
      "due": "2023-09-20T12:40:22.192Z"
    }
  ]
}
```

### Get payments due by given date

```
GET /payments-due-demo-app/v1/payments?until={localdatetime}
```

Returns:

```
{
  "paymentsDue": [
    {
      "paymentId": "string",
      "name": "string",
      "vendor": "string",
      "amount": 0,
      "currency": "string",
      "due": "2023-09-20T12:40:22.192Z"
    }
  ]
}
```

# Calling the service

You can import a request collection to Postman that is present in the application 
main directory: `Payments Due Demo.postman_collection.json`.

The service has really basic authorization mechanism supporting 3 users:
```
U_001
U_002
U_003
```

Password is `password` in all cases.

You can change current user in the Postman pre-request script.

Authenticated user is used to record and retrieve payment data - one user cannot retrieve data for another.

# Local setup

Since local DynamoDB does not require real data for configuration, setup a `local-dev` 
profile with mock configuration:

```
% aws configure --profile local-dev
AWS Access Key ID [None]: demo
AWS Secret Access Key [None]: demo
Default region name [None]: us-east-1
Default output format [None]: 
```

Run local instance of AWS DynamoDB (docker required):

```
% docker run -p 8000:8000 amazon/dynamodb-local
```

You should see similar output:

```
Initializing DynamoDB Local with the following configuration:
Port:	8000
InMemory:	true
DbPath:	null
SharedDb:	false
shouldDelayTransientStatuses:	false
CorsParams:	*
```

Your local DynamoDB should be running.

Go to application main directory and run:

```
aws dynamodb create-table --cli-input-json file://create-table.json --endpoint-url http://localhost:8000 --profile local-dev
```

This will create required DynamoDB table based on the template. You can verify if the table was created:

```
aws dynamodb list-tables --endpoint-url http://localhost:8000 --profile local-dev
```

Run the Java application.

You're all set up!
