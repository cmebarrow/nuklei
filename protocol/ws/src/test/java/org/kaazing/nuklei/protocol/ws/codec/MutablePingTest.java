/*
 * Copyright 2015 Kaazing Corporation, All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.nuklei.protocol.ws.codec;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.experimental.theories.Theory;
import org.kaazing.nuklei.FlyweightBE;
import org.kaazing.nuklei.protocol.ws.codec.Frame.Payload;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

public class MutablePingTest extends FrameTest
{
    private MutablePing ping = new MutablePing();

    @Theory
    public void wrapShouldSetMaskBitAndLength0(int offset, boolean masked) throws Exception
    {
        long originalMaskBytes = FlyweightBE.uint32Get(buffer, offset + 2);
        ping.wrap(masked, buffer, offset);
        assertEquals(masked,  ping.isMasked());
        assertEquals(0, ping.getLength());
        assertEquals(offset, ping.offset());
        assertEquals(offset + (masked ? 6 : 2), ping.limit());
        if (masked)
        {
            long mask = FlyweightBE.uint32Get(buffer, offset + 2);
            assertNotEquals(originalMaskBytes, mask);
        }
    }

    @Theory
    public void wrapShouldFailIfBufferNotBigEnough(int offset, boolean masked) throws Exception
    {
        MutableDirectBuffer buffer = new UnsafeBuffer(new byte[offset + (masked ? 5 : 1)]);
        try
        {
            ping.wrap(masked, buffer, offset);
        }
        catch(IndexOutOfBoundsException expected)
        {
            return;
        }
        fail("Expected exception not caught");
    }

    @Theory
    public void setPayloadWithNonZeroOffsetShouldWork(int offset, boolean masked) throws Exception
    {
        long originalMaskBytes = FlyweightBE.uint32Get(buffer, offset + 2);
        ping.wrap(masked, buffer, offset);
        DirectBuffer payloadInput = new UnsafeBuffer(new byte[]{12, 13, 14, (byte) 0xff});
        int length = 3;
        ping.setPayload(payloadInput, 1, length);

        Payload payload = ping.getPayload();
        assertEquals(13, payload.buffer().getByte(payload.offset()));
        assertEquals(14, payload.buffer().getByte(payload.offset()+1));
        assertEquals((byte)0xff, payload.buffer().getByte(payload.offset()+2));

        assertEquals(masked,  ping.isMasked());
        assertEquals(length, ping.getLength());
        assertEquals(offset, ping.offset());
        assertEquals(offset + (masked ? 6 : 2) + length, ping.limit());
        if (masked)
        {
            long mask = FlyweightBE.uint32Get(buffer, offset + 2);
            assertNotEquals(originalMaskBytes, mask);
        }
    }

    @Theory
    public void setPayloadWithZeroOffsetShouldWork(int offset, boolean masked) throws Exception
    {
        long originalMaskBytes = FlyweightBE.uint32Get(buffer, offset + 2);
        ping.wrap(masked, buffer, offset);
        byte[] payloadInputBytes = new byte[]{12, 13, 14, (byte) 0xff};
        DirectBuffer payloadInput = new UnsafeBuffer(payloadInputBytes);
        int length = 4;
        ping.setPayload(payloadInput, 0, length);

        Payload payload = ping.getPayload();
        byte[] payloadBytes = new byte[ping.getLength()];
        payload.buffer().getBytes(payload.offset(), payloadBytes, 0, ping.getLength());
        assertArrayEquals(payloadInputBytes, payloadBytes);

        assertEquals(masked,  ping.isMasked());
        assertEquals(length, ping.getLength());
        assertEquals(offset, ping.offset());
        assertEquals(offset + (masked ? 6 : 2) + length, ping.limit());
        if (masked)
        {
            long mask = FlyweightBE.uint32Get(buffer, offset + 2);
            assertNotEquals(originalMaskBytes, mask);
        }
    }

    // TODO:
    // setPayloadWithOverlongLengthShouldFail, etc


    @Theory
    public void setPayloadWithOverlongLengthShouldFail(int offset, boolean masked) throws Exception
    {
        ping.wrap(masked, buffer, offset);
        int length = 126;
        byte[] payloadInputBytes = new byte[length + offset];
        DirectBuffer payloadInput = new UnsafeBuffer(payloadInputBytes);
        try
        {
            ping.setPayload(payloadInput, 0, length);
        }
        catch (IllegalArgumentException expected)
        {
            assertTrue(expected.getMessage().contains("length" ));
            return;
        }
        fail("Expected exception not thrown");
    }

}
