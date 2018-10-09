/*
 * Copyright (c) 2018. AxonIQ
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.axonserver.connector.processor.grpc;

import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventProcessor;

import java.util.Iterator;

/**
 * Iterable class for all registered {@link EventProcessor}s.
 *
 * @author Sara Pellegrini
 * @since 4.0
 */
public class EventProcessors implements Iterable<EventProcessor> {

    private final EventHandlingConfiguration eventHandlingConfiguration;

    public EventProcessors(EventHandlingConfiguration eventHandlingConfiguration) {
        this.eventHandlingConfiguration = eventHandlingConfiguration;
    }

    @Override
    public Iterator<EventProcessor> iterator() {
        return eventHandlingConfiguration.getProcessors().iterator();
    }
}
