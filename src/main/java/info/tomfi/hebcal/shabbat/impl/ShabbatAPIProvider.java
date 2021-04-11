/*
 * Copyright Tomer Figenblat.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.tomfi.hebcal.shabbat.impl;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import info.tomfi.hebcal.shabbat.ShabbatAPI;
import info.tomfi.hebcal.shabbat.request.Request;
import info.tomfi.hebcal.shabbat.response.Response;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Service provider encapsulating HebCal's Shabbat REST API. */
@AutoService(ShabbatAPI.class)
public final class ShabbatAPIProvider implements ShabbatAPI {
  private static final String DEFAULT_ENDPOINT = "https://www.hebcal.com/shabbat/";

  private final HttpClient client;
  private final ObjectMapper mapper;
  private final String endpoint;

  /**
   * Default constructor, used to instantiate the service provider with the static {@value
   * #DEFAULT_ENDPOINT}.
   *
   * @see #ShabbatAPIProvider(String)
   */
  public ShabbatAPIProvider() {
    this(DEFAULT_ENDPOINT);
  }

  /**
   * Construtor used to instantiate the initial {@link HttpClient} and {@link ObjectMapper}.
   *
   * @param setEndpoint the endpont uri to set as the HebCal's endpoint.
   * @see #ShabbatAPIProvider()
   */
  public ShabbatAPIProvider(final String setEndpoint) {
    client = HttpClient.newHttpClient();
    mapper = new ObjectMapper();
    endpoint = setEndpoint;
  }

  @Override
  public CompletableFuture<Response> sendAsync(final Request request) {
    var query =
        request.queryParams().entrySet().stream()
            .map(e -> String.join("=", e.getKey(), e.getValue()))
            .collect(joining("&"));

    var uri = URI.create(String.join("?", endpoint, query));

    var httpRequest = HttpRequest.newBuilder(uri).header("Accept", "application/json").build();

    return client
        .sendAsync(httpRequest, ofString())
        .thenApply(HttpResponse::body)
        .thenApply(
            s -> {
              try {
                return mapper.readValue(s, Response.class);
              } catch (JsonProcessingException jpe) {
                throw new CompletionException(jpe);
              }
            });
  }
}
