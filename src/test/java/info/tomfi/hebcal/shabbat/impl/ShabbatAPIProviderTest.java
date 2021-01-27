/**
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static info.tomfi.hebcal.shabbat.assertions.BDDAssertions.then;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.BDDAssertions.assertThat;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.awaitility.Awaitility.await;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import info.tomfi.hebcal.shabbat.ShabbatAPI;
import info.tomfi.hebcal.shabbat.request.FlagState;
import info.tomfi.hebcal.shabbat.request.GeoType;
import info.tomfi.hebcal.shabbat.request.OutputType;
import info.tomfi.hebcal.shabbat.request.ParamKey;
import info.tomfi.hebcal.shabbat.request.Request;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Test cases for the Shabbat api service provider. */
@Tag("unit-tests")
final class ShabbatAPIProviderTest {
  private Faker faker;
  private int randomGeoId;
  private Map<String, StringValuePattern> quaryParams;
  private WireMockServer mockServer;
  private ShabbatAPI sut;

  @BeforeEach
  void initialize() {
    faker = new Faker();
    randomGeoId = faker.number().numberBetween(11111, 99999);
    var randomPort = faker.number().numberBetween(1111, 9999);

    quaryParams =
        Map.of(
            // the following are the default query params required by hebcal api
            ParamKey.OUTPUT_FORMAT.toString(), containing(OutputType.JSON.toString()),
            ParamKey.INCLUDE_TURAH_HAFTARAH.toString(), containing(FlagState.OFF.toString()),
            ParamKey.ASHKENAZIS_TRANSLITERATIONS.toString(), containing(FlagState.OFF.toString()),
            ParamKey.GEO_TYPE.toString(), containing(GeoType.GEO_NAME.toString()),
            ParamKey.HAVDALAH.toString(), containing("50"),
            ParamKey.CANDLE_LIGHTING.toString(), containing("18"),
            // the following derives from the request
            ParamKey.GEO_ID.toString(), containing(String.valueOf(randomGeoId)));

    mockServer = new WireMockServer(randomPort);
    mockServer.start();
    configureFor("localhost", randomPort);

    sut = new ShabbatAPIProvider(String.format("http://localhost:%d/shabbat", randomPort));
  }

  @AfterEach
  void cleanup() {
    mockServer.stop();
  }

  @Test
  void test_mock_api_by_stubbing_a_faulty_response_makes_the_future_complete_exceptionally()
      throws InterruptedException {
    givenThat(
        get(urlPathEqualTo("/shabbat"))
            .withQueryParams(quaryParams)
            .willReturn(aResponse().withStatus(200).withBody("not a json body")));

    var requst = Request.builder().forGeoId(randomGeoId).build();
    var cf = sut.sendAsync(requst);

    thenExceptionOfType(CompletionException.class)
        .isThrownBy(() -> sut.sendAsync(requst).join())
        .withCauseInstanceOf(JsonProcessingException.class);

    await()
        .atMost(Duration.ofSeconds(2))
        .untilAsserted(() -> assertThat(cf).isCompletedExceptionally());
  }

  @Test
  void test_mock_api_by_stubbing_a_minimal_json_response_and_verify_the_serialization()
      throws IOException, URISyntaxException {
    var responesText = getStringFromResource("api-responses/response_minimal.json");

    givenThat(
        get(urlPathEqualTo("/shabbat"))
            .withQueryParams(quaryParams)
            .willReturn(aResponse().withStatus(200).withBody(responesText)));

    var requst = Request.builder().forGeoId(randomGeoId).build();
    var response = sut.sendAsync(requst).join();

    verify(1, getRequestedFor(urlPathEqualTo("/shabbat")));

    then(response)
        .titleIs("response fake title")
        .linkIs("response fake link")
        .dateIs("response fake date")
        .hasNoItems();

    then(response.location())
        .admin1Is("admino uno")
        .asciinameIs("nice ascii name")
        .titleIs("location fake title")
        .tzidIs("here tzid")
        .cityIs("some city")
        .countryIs("a great country")
        .geoIs("geo who")
        .latitudeIs(25.25)
        .longitudeIs(26.26)
        .geonameidIs(2526)
        .ccEmpty();
  }

  @Test
  void test_mock_api_by_stubbing_a_full_json_response_and_verify_the_serialization()
      throws IOException, URISyntaxException {
    var responesText = getStringFromResource("api-responses/response_full.json");

    givenThat(
        get(urlPathEqualTo("/shabbat"))
            .withQueryParams(quaryParams)
            .willReturn(aResponse().withStatus(200).withBody(responesText)));

    var requst = Request.builder().forGeoId(randomGeoId).build();
    var response = sut.sendAsync(requst).join();

    verify(1, getRequestedFor(urlPathEqualTo("/shabbat")));

    then(response)
        .titleIs("response fake title")
        .linkIs("response fake link")
        .dateIs("response fake date")
        .hasItems();

    then(response.location())
        .admin1Is("admino uno")
        .asciinameIs("nice ascii name")
        .titleIs("location fake title")
        .tzidIs("here tzid")
        .cityIs("some city")
        .countryIs("a great country")
        .geoIs("geo who")
        .ccIs("AA")
        .latitudeIs(25.25)
        .longitudeIs(26.26)
        .geonameidIs(2526);

    var items = response.items().get();

    then(items.get(0))
        .titleIs("first item fake title")
        .categoryIs("first item fake category")
        .hebrewIs("first item fake hebrew")
        .dateIs("first item fake date")
        .linkIs("first item fake link")
        .memoIs("first item fake memo")
        .subcatIs("first item fake subcat")
        .titleOrigIs("fake title orig")
        .isYomtov();

    then(items.get(1))
        .titleIs("second item fake title")
        .categoryIs("second item fake category")
        .hebrewIs("second item fake hebrew")
        .dateIs("second item fake date")
        .linkIs("second item fake link")
        .memoIs("second item fake memo")
        .subcatIs("second item fake subcat")
        .titleOrigEmpty()
        .isNotYomtov();

    then(items.get(2))
        .titleIs("third item fake title")
        .categoryIs("third item fake category")
        .hebrewIs("third item fake hebrew")
        .dateIs("third item fake date")
        .linkEmpty()
        .memoEmpty()
        .subcatEmpty()
        .titleOrigEmpty()
        .isNotYomtov();
  }

  private String getStringFromResource(final String resourceName)
      throws IOException, URISyntaxException {
    try (var lines =
        Files.lines(
            Paths.get(
                Thread.currentThread()
                    .getContextClassLoader()
                    .getResource(resourceName)
                    .toURI()))) {
      return lines.collect(joining(System.lineSeparator()));
    }
  }
}
