package ch.creativecode.logging;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * An interceptor for apache flume to extract patterns in the event body with
 * the following pattern: field=value
 * The extracted fields will be written to the header of the event.
 *
 * @author Nicolas Baer <nicolas.baer@gmail.com>
 */
public class KibanaTimeStampInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(KibanaTimeStampInterceptor.class);

    private final String timestampStr = "@timestamp";

    @Override
    public void initialize() {
        // no-op
    }

    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();

        if(headers.containsKey(timestampStr)) {
            String timestamp = headers.get(timestampStr);
            Long millis = Long.parseLong(timestamp);
            DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
            headers.put(timestampStr, fmt.print(millis));

            // elasticsearch sink will look for a timestamp in milliseconds
            headers.put("timestamp", timestamp);
        }

        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        for(Event event : events) {
            this.intercept(event);
        }
        return events;
    }

    @Override
    public void close() {
        // no-op
    }


    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new KibanaTimeStampInterceptor();
        }

        @Override
        public void configure(Context context) {
            // no-op
        }

    }
}
