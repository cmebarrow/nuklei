/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
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
package org.kaazing.nuklei.tcp.internal.writer;


public final class UnrouteCommand implements WriterCommand
{
    private final long correlationId;
    private final String destination;

    public UnrouteCommand(
        long correlationId,
        String destination)
    {
        this.correlationId = correlationId;
        this.destination = destination;
    }

    @Override
    public void execute(Writer writer)
    {
        writer.doUnroute(correlationId, destination);
    }
}
