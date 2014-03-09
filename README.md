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


