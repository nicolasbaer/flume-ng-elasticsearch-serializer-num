package ch.creativecode.logging;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import org.elasticsearch.common.jackson.core.JsonParseException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author Nicolas Baer <nicolas.baer@gmail.com>
 */
public class ContentBuilderUtil {


    private static final Charset charset = Charset.defaultCharset();

    private ContentBuilderUtil() {
    }

    public static void appendField(XContentBuilder builder, String field,
                                   byte[] data) throws IOException {
        XContentType contentType = XContentFactory.xContentType(data);
        if (contentType == null) {
            addSimpleField(builder, field, data);
        } else {
            addComplexField(builder, field, contentType, data);
        }
    }

    public static void addSimpleField(XContentBuilder builder, String fieldName,
                                      byte[] data) throws IOException {

        String dataString = new String(data, charset);
        if (NumberUtils.isNumber(dataString)) {
            if (GenericValidator.isInt(dataString)) {
                builder.field(fieldName, Integer.valueOf(dataString));
            } else if (GenericValidator.isDouble(dataString)) {
                builder.field(fieldName, Double.valueOf(dataString));
            } else if (GenericValidator.isFloat(dataString)) {
                builder.field(fieldName, Float.valueOf(dataString));
            } else {
                builder.field(fieldName, dataString);
            }
        } else {
            builder.field(fieldName, dataString);
        }
    }

    public static void addComplexField(XContentBuilder builder, String fieldName,
                                       XContentType contentType, byte[] data) throws IOException {
        XContentParser parser = null;
        try {
            XContentBuilder tmp = jsonBuilder();
            parser = XContentFactory.xContent(contentType).createParser(data);
            parser.nextToken();
            tmp.copyCurrentStructure(parser);
            builder.field(fieldName, tmp);
        } catch (JsonParseException ex) {
            // If we get an exception here the most likely cause is nested JSON that
            // can't be figured out in the body. At this point just push it through
            // as is, we have already added the field so don't do it again
            addSimpleField(builder, fieldName, data);
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }
}
