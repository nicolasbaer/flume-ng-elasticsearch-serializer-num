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