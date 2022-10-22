package info.tomfi.shabbat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

class ShabbatAPITest {
  @Test
  @SuppressWarnings("unchecked")
  void test_invocation_of_the_api_and_verify_http_response()
      throws JsonProcessingException, IOException, InterruptedException, ExecutionException {
    // mock http response and stub with string response body
    var strResponse =
        new String(
            getClass()
                .getModule()
                .getResourceAsStream("api-responses/real_response.json")
                .readAllBytes(),
            StandardCharsets.UTF_8);
    var mockHttpResponse = mock(HttpResponse.class);
    when(mockHttpResponse.body()).thenReturn(strResponse);

    // create a dummy request and an argument matcher for stubbing the http client
    var dummyRequest = APIRequest.builder().forGeoId(1234).build();
    ArgumentMatcher<HttpRequest> matchesDummyRequest =
        r ->
            "https://www.hebcal.com/shabbat/?a=off&b=18&geonameid=1234&geo=geoname&m=50&leyning=off&cfg=json"
                .equals(r.uri().toString());

    // mock http client and stub with the mock response for the matched request
    var mockHttpClient = mock(HttpClient.class);
    when(mockHttpClient.sendAsync(argThat(matchesDummyRequest), any()))
        .thenReturn(CompletableFuture.completedFuture(mockHttpResponse));

    // instantiate the api with the mock client, send the dummy request and verify the response
    var api = new ShabbatAPI(mockHttpClient);
    var response = api.sendAsync(dummyRequest).get();
    assertThat(response.range.get().start).isEqualTo("2021-01-01");
    assertThat(response.range.get().end).isEqualTo("2021-01-02");
  }
}
