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
package org.kaazing.nuklei.ws.internal;

import java.util.function.Consumer;

import org.kaazing.nuklei.CompositeNukleus;
import org.kaazing.nuklei.Nukleus;

public final class WsNukleus extends CompositeNukleus
{
    private final Context context;

    WsNukleus(Context context)
    {
        this.context = context;
    }

    @Override
    public int process() throws Exception
    {
        int weight = 0;

        return weight;
    }

    @Override
    public String name()
    {
        return "ws";
    }

    @Override
    public void close() throws Exception
    {
        context.close();
    }

    @Override
    public void forEach(Consumer<? super Nukleus> action)
    {
    }
}
