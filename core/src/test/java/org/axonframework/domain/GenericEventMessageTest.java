package org.axonframework.domain;

import org.junit.*;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Allard Buijze
 */
public class GenericEventMessageTest {

    @Test
    public void testConstructor() {
        Object payload = new Object();
        GenericEventMessage<Object> message1 = new GenericEventMessage<Object>(payload);
        Map<String, Object> metaDataMap = Collections.singletonMap("key", (Object) "value");
        MetaData metaData = MetaData.from(metaDataMap);
        GenericEventMessage<Object> message2 = new GenericEventMessage<Object>(payload, metaData);
        GenericEventMessage<Object> message3 = new GenericEventMessage<Object>(payload, metaDataMap);

        assertSame(MetaData.emptyInstance(), message1.getMetaData());
        assertEquals(Object.class, message1.getPayload().getClass());
        assertEquals(Object.class, message1.getPayloadType());

        assertSame(metaData, message2.getMetaData());
        assertEquals(Object.class, message2.getPayload().getClass());
        assertEquals(Object.class, message2.getPayloadType());

        assertNotSame(metaDataMap, message3.getMetaData());
        assertEquals(metaDataMap, message3.getMetaData());
        assertEquals(Object.class, message3.getPayload().getClass());
        assertEquals(Object.class, message3.getPayloadType());

        assertFalse(message1.getEventIdentifier().equals(message2.getEventIdentifier()));
        assertFalse(message1.getEventIdentifier().equals(message3.getEventIdentifier()));
        assertFalse(message2.getEventIdentifier().equals(message3.getEventIdentifier()));
    }

    @Test
    public void testWithMetaData() {
        Object payload = new Object();
        Map<String, Object> metaDataMap = Collections.singletonMap("key", (Object) "value");
        MetaData metaData = MetaData.from(metaDataMap);
        GenericEventMessage<Object> message = new GenericEventMessage<Object>(payload, metaData);
        GenericEventMessage<Object> message1 = message.withMetaData(MetaData.emptyInstance());
        GenericEventMessage<Object> message2 = message.withMetaData(
                MetaData.from(Collections.singletonMap("key", (Object) "otherValue")));

        assertEquals(0, message1.getMetaData().size());
        assertEquals(1, message2.getMetaData().size());
    }

    @Test
    public void testAndMetaData() {
        Object payload = new Object();
        Map<String, Object> metaDataMap = Collections.singletonMap("key", (Object) "value");
        MetaData metaData = MetaData.from(metaDataMap);
        GenericEventMessage<Object> message = new GenericEventMessage<Object>(payload, metaData);
        GenericEventMessage<Object> message1 = message.andMetaData(MetaData.emptyInstance());
        GenericEventMessage<Object> message2 = message.andMetaData(
                MetaData.from(Collections.singletonMap("key", (Object) "otherValue")));

        assertEquals(1, message1.getMetaData().size());
        assertEquals("value", message1.getMetaData().get("key"));
        assertEquals(1, message2.getMetaData().size());
        assertEquals("otherValue", message2.getMetaData().get("key"));
    }
}
