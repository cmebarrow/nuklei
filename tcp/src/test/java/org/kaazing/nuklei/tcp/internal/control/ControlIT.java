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
package org.kaazing.nuklei.tcp.internal.control;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;
import static uk.co.real_logic.agrona.IoUtil.createEmptyFile;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.nuklei.test.NukleusRule;

import uk.co.real_logic.agrona.concurrent.ringbuffer.RingBufferDescriptor;

public class ControlIT
{
    private final K3poRule k3po = new K3poRule()
            .setScriptRoot("org/kaazing/nuklei/specification/tcp/control");

    private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

    private final NukleusRule nukleus = new NukleusRule("tcp")
            .setDirectory("target/nukleus-itests")
            .setCommandBufferCapacity(1024)
            .setResponseBufferCapacity(1024)
            .setCounterValuesBufferCapacity(1024);

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout).around(nukleus);

    @Before
    public void setupStreamFiles() throws Exception
    {
        int streamCapacity = 1024 * 1024;

        File handler = new File("target/nukleus-itests/handler/streams/tcp");
        createEmptyFile(handler.getAbsoluteFile(), streamCapacity + RingBufferDescriptor.TRAILER_LENGTH);
    }

    @Test
    @Specification({
        "bind.address.and.port/controller"
    })
    public void shouldBindAddressAndPort() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unbind.address.and.port/controller",
    })
    public void shouldUnbindAddressAndPort() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "prepare.address.and.port/controller",
    })
    public void shouldPrepareAddressAndPort() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unprepare.address.and.port/controller",
    })
    public void shouldUnprepareAddressAndPort() throws Exception
    {
        k3po.finish();
    }

}