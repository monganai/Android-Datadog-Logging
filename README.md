# Android---Datadog-Logging
Send logs directly to Datadog from your Android application 

Usage
------------------------

*Add DD_LOG.java as a class to your application*

*Define the logger in an activity:*

`public DD_LOG mLogger;`

........................................................................................................

initialize the logger:

`mLogger = new DD_LOG();`

.........................................................................................................

Add key value pairs of attributes which will automatically be parsed in Datadog:

`String[] payload = {"message:sent from Android","level:info","customer.name:John","customer.email:johnDoe@email.com","customer.Country:Ireland"};`

Logs will be added to a queue and flushed to Datadog when the queue is full

`mLogger.Log(payload);`

...........................................................................................................

Logs can also be sent directly with no queueing by using the sendLog method:

`mLogger.sendLog(payload);`

