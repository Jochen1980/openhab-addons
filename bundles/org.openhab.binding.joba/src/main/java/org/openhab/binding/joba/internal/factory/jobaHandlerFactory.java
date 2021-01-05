/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.joba.internal.factory;

import static org.openhab.binding.joba.internal.jobaBindingConstants.*;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.joba.internal.handler.jobaHandler;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The {@link jobaHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Jochen Bauer - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.joba", service = ThingHandlerFactory.class)
public class jobaHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_SAMPLE);
    private final HttpClient httpClient;

    @Activate
    public jobaHandlerFactory(final @Reference HttpClientFactory httpClientFactory // ,
    // final @Reference LocaleProvider localeProvider, final @Reference LocationProvider locationProvider,
    // final @Reference TranslationProvider i18nProvider, final @Reference TimeZoneProvider timeZoneProvide
    ) {
        this.httpClient = httpClientFactory.getCommonHttpClient();
        // this.localeProvider = localeProvider;
        // this.locationProvider = locationProvider;
        // this.i18nProvider = i18nProvider;
        // this.timeZoneProvider = timeZoneProvider;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_SAMPLE.equals(thingTypeUID)) {
            // TODO decide if bridge is needed .... guess yes, see openweathermap to enrich the thing handling by adding
            // a bridge.

            return new jobaHandler(thing, httpClient); // TODO ueberladen
        }

        return null;
    }
}
