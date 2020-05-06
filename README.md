# Android-Datadog-Logging
Send logs directly to Datadog from your Android application 


**Note - Depricated**

*Default required variables*
Note - Modify as required for your specific setup
More information regarding these settings can be found here:
https://docs.datadoghq.com/api/?lang=bash#send-logs-over-http

-DD_API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxx";                            //Enter your Datadog API key as a string.

-DD_ENDPOINT = "https://http-intake.logs.datadoghq.com/v1/input/";   // change to eu endpoint for european accounts

-DD_SERVICE = "DataLog";                                             // Default service Name (Application Name)

-DD_SOURCE = "Android";                                              // Source is Android

-DD_HOSTNAME = android.os.Build.MODEL;                               // Device model is grabbed as hostname


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

