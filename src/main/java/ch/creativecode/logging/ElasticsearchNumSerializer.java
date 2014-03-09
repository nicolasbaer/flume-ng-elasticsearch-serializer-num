package ch.creativecode.logging;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.elasticsearch.ElasticSearchEventSerializer;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Nicolas Baer <nicolas.baer@gmail.com>
 */
public final class ElasticsearchNumSerializer implements ElasticSearchEventSerializer {

    @Override
    public XContentBuilder getContentBuilder(Event event) throws IOException {
        XContentBuilder builder = jsonBuilder().startObject();
        appendBody(builder, event);
        appendHeaders(builder, event);
        return builder;
    }

    private void appendBody(XContentBuilder builder, Event event)
            throws IOException {
        ContentBuilderUtil.appendField(builder, "body", event.getBody());
    }

    private void appendHeaders(XContentBuilder builder, Event event)
            throws IOException {
        Map<String, String> headers = event.getHeaders();
        for (String key : headers.keySet()) {
            ContentBuilderUtil.appendField(builder, key, headers.get(key).getBytes(charset));
        }
    }

    @Override
    public void configure(Context context) {
        // no-op
    }

    @Override
    public void configure(ComponentConfiguration conf) {
        // no-op
    }
}
