flume-ng-elasticsearch-serializer-num
=====================================

An elasticsearch flume serializer with awareness for numerical value.
This is a quick and dirty hack to support numerical values stored to elasticsearch in order to analyze it with Kibana.
Please be aware that this implementation might not perform the very best and only use it with caution :)

Config
------
In order to attach this serializer to flume the following tasks need to be performed:  
1. `mvn clean assembly:assembly`  
2. copy the generated jar (\*-with-dependencies-\*) into the flume lib directory  
3. define the following serializer: ch.creativecode.logging.ElasticsearchNumSerializer (e.g. `a1.sinks.k1.serializer = ch.creativecode.logging.ElasticsearchNumSerializer`)  


EqualsInterceptor
-----------------
This package includes an EqualsInterceptor to allow extracting header fields from the body with the following syntax: `field=value`
In order to use it the following interceptor has to be defined: `ch.creativecode.logging.EqualsInterceptor$Builder`.
Due to strange reasons, currently the Builder has to be specified instead of the Interceptor implementation.


KibanaTimeStampInterceptor
--------------------------
In order to use Kibana to analyze the data properly one has to deal with the timestamp. This does not come out-of-the-box with
the Flume Elasticsearch sink. The `KibanaTimeStampInterceptor` does the following:
1. It looks for a header called `@timestamp` as Long (ms)
2. It converts the value to the proper `ISODateTimeFormat`
3. It stores the milliseconds as `timestamp` in the header. Apparently the Elasticsearch sink relies on this property.
Therefore in order to use this interceptor, one has to set the `@timestamp` header to the event. Using the `EqualsInterceptor`
described above the following pattern can be used in log4j: `@timestamp=%d{UNIX_MILLIS} class=%c{1} %msg`