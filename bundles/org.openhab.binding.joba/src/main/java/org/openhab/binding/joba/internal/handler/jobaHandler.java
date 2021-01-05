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
package org.openhab.binding.joba.internal.handler;

import static org.openhab.binding.joba.internal.jobaBindingConstants.*;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.http.HttpMethod;
import org.openhab.binding.joba.internal.dto.JobaResponse;
import org.openhab.binding.joba.internal.jobaConfiguration;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * The {@link jobaHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Jochen Bauer - Initial contribution
 */
@NonNullByDefault
public class jobaHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(jobaHandler.class);
    private final HttpClient httpClient; // TODO andernorts ist dieser oft als final deklariert
    private Gson gson;
    private @Nullable jobaConfiguration config;

    // public jobaHandler(Thing thing) {
    // super(thing);
    // } // TODO check ob das wegkann, denke ja - siehe DWDPollenflug-addon

    public jobaHandler(Thing thing, HttpClient client) {
        super(thing);
        this.httpClient = client;
        this.gson = new Gson();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        String idStr = channelUID.getId();
        if (CHANNEL_2.equals(idStr)) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }
            // TODO: handle command
            logger.info("-> JobaBinding (v 1.4): JobaDevSwitch was triggered.");

            // Request bestuecken
            // angelehnt am Beispiel von DWDPollenflug-Addon
            CompletableFuture<@Nullable JobaResponse> f = new CompletableFuture<>();
            String reqUrl = "https://cunds-syncapi-develop.azurewebsites.de/api/Organisation/GetOrgaList";
            Request request = httpClient.newRequest(URI.create(reqUrl));
            String bearerToken = ""; // kommt aus config
            bearerToken = config.config1;
            request.header("Authorization", "Bearer " + bearerToken);
            request.header("Accept", "application/json");
            request.header("Accept-Encoding", "gzip, deflate, br");
            request.header("Connection", "keep-alive");
            request.method(HttpMethod.GET).timeout(2000, TimeUnit.SECONDS).send(new BufferingResponseListener() {
                @NonNullByDefault({})
                @Override
                public void onComplete(Result result) {
                    final HttpResponse response = (HttpResponse) result.getResponse();
                    if (result.getFailure() != null) {
                        Throwable e = result.getFailure();
                        if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                            // f.completeExceptionally(new JobaResponse("Request timeout", e));
                        } else {
                            // f.completeExceptionally(new JobaResponse("Request failed", e));
                        }
                    } else if (response.getStatus() != 200) {
                        // f.completeExceptionally(new JobaResponse(getContentAsString()));
                    } else {
                        // here all is ok ...
                        try {
                            String dstr = getContentAsString();
                            logger.info("BP DSTR: " + dstr);
                            JobaResponse jrJson = gson.fromJson(getContentAsString(), JobaResponse.class);
                            f.complete(jrJson);
                        } catch (JsonSyntaxException ex2) {
                            // f.completeExceptionally(new JobaResponse("Parsing of response failed"));
                        }
                    }
                }
            });

            logger.info("END - handleCommand()");
            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information:
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        }
    }

    @Override
    public void initialize() {
        config = getConfigAs(jobaConfiguration.class);

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly. Also, before leaving this method a thing
        // status from one of ONLINE, OFFLINE or UNKNOWN must be set. This might already be the real thing status in
        // case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);

        // Example for background initialization:
        scheduler.execute(() -> {
            boolean thingReachable = true; // <background task with long running initialization here>
            // when done do:
            if (thingReachable) {
                updateStatus(ThingStatus.ONLINE);
            } else {
                updateStatus(ThingStatus.OFFLINE);
            }
        });

        // These logging types should be primarily used by bindings
        // logger.trace("Example trace message");
        // logger.debug("Example debug message");
        // logger.warn("Example warn message");

        // Note: When initialization can NOT be done set the status with more details for further
        // analysis. See also class ThingStatusDetail for all available status details.
        // Add a description to give user information to understand why thing does not work as expected. E.g.
        // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
        // "Can not access device as username and/or password are invalid");
    }
}
