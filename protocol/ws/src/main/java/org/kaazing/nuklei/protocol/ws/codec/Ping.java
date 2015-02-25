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

import java.net.ProtocolException;

import uk.co.real_logic.agrona.DirectBuffer;

public class Ping extends ControlFrame
{

    Ping()
    {

    }

    public Ping wrap(DirectBuffer buffer, int offset) throws ProtocolException
    {
        super.wrap(buffer, offset, false);
        return this;
    }

}
