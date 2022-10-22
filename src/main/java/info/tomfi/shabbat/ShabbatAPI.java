package info.tomfi.shabbat;

import static java.util.stream.Collectors.joining;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/** Shabbat API Service. */
public final class ShabbatAPI {
  private static final String ENDPOINT = "https://www.hebcal.com/shabbat/";

  private final HttpClient client;
  private final ObjectMapper mapper;

  public ShabbatAPI() {
    this(HttpClient.newHttpClient());
  }

  ShabbatAPI(final HttpClient client) {
    this.client = client;
    mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * Send an asynchronous {@link APIRequest} and receive a {@link APIResponse}.
   *
   * @param request the request to send to the API.
   * @return the response wrapped in a {@link CompletableFuture}.
   */
  public CompletableFuture<APIResponse> sendAsync(final APIRequest request) {
    var query =
        request.queryParams().entrySet().stream()
            .map(e -> String.join("=", e.getKey().toString(), e.getValue().toString()))
            .collect(joining("&"));

    var uri = URI.create(String.join("?", ShabbatAPI.ENDPOINT, query));

    var httpRequest = HttpRequest.newBuilder(uri).header("Accept", "application/json").build();

    return client
        .sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .thenApply(
            s -> {
              try {
                return mapper.readValue(s, APIResponse.class);
              } catch (final JsonProcessingException jpe) {
                throw new CompletionException(jpe);
              }
            });
  }
}
