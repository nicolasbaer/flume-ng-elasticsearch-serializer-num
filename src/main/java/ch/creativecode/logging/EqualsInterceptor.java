package ch.creativecode.logging;

import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An interceptor for apache flume to extract patterns in the event body with
 * the following pattern: field=value
 * The extracted fields will be written to the header of the event.
 *
 * @author Nicolas Baer <nicolas.baer@gmail.com>
 */
public class EqualsInterceptor implements Interceptor {

    private static final Pattern p = Pattern.compile("(\\w+)=([^\\s]+)");

    @Override
    public void initialize() {
        // no-op
    }

    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody(), Charsets.UTF_8);
        Matcher m = p.matcher(body);

        Map<String, String> headers = event.getHeaders();
        while(m.find()) {
            String field = m.group(1);
            String value = m.group(2);

            headers.put(field, value);
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
            return new EqualsInterceptor();
        }

        @Override
        public void configure(Context context) {
            // no-op
        }

    }
}
