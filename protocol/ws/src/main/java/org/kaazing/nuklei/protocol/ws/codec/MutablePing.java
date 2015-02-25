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

import static java.lang.String.format;

import java.util.concurrent.ThreadLocalRandom;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.MutableDirectBuffer;

public final class MutablePing extends Ping
{
    public MutablePing()
    {

    }

    public MutablePing wrap(boolean masked, MutableDirectBuffer buffer, int offset)
    {
        byte first = (byte) 0x8a;
        buffer.setMemory(offset, 1, first);
        super.wrap(buffer, offset, true);
        setLengthAndMaskBit(0, masked);
        if (masked)
        {
            int dataOffset = getDataOffset();
            int maskOffset = dataOffset - 4;
            long mask = ThreadLocalRandom.current().nextInt();
            uint32Put(buffer, maskOffset, mask);
        }
        return this;
    }

    public void setPayload(DirectBuffer payload, int offset, int length)
    {
        if (length > getMaxPayloadLength())
        {
            throw new IllegalArgumentException(format("Payload length cannot exceed %d", getMaxPayloadLength()));
        }
        MutableDirectBuffer buffer = mutableBuffer();
        if (buffer == null)
        {
            throw new IllegalStateException("wrap must be called before setPayload");
        }
        if (!isMasked())
        {
            setLengthAndMaskBit(length, false);
            buffer.putBytes(getDataOffset(), payload, offset, length);
        }
        else
        {
            long mask = uint32Get(buffer, getDataOffset() - 4);
            setLengthAndMaskBit(length, true);
            int dataOffset = getDataOffset();
            int maskOffset = dataOffset - 4;
            uint32Put(buffer, maskOffset, mask);
            for (int i = 0; i < length; i++)
            {
                byte masked = (byte) (payload.getByte(offset + i) ^ buffer().getByte(maskOffset + i % 4) & 0xFF);
                buffer.setMemory(dataOffset + i, 1, masked);
            }
        }
    }

}
