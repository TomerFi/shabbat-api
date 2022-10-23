package info.tomfi.shabbat;

import info.tomfi.shabbat.APIRequest.IntWrapper;
import info.tomfi.shabbat.APIRequest.PaddedIntWrapper;
import info.tomfi.shabbat.APIRequest.ParamKey;
import info.tomfi.shabbat.APIRequest.ParamValue;
import info.tomfi.shabbat.APIRequest.StringWrapper;
import java.time.LocalDate;
import java.util.EnumMap;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class APIRequestTest {
  @Test
  void verify_equals_and_hashcode() {
    EqualsVerifier.forClass(APIRequest.class).verify();
  }

  @Test
  void building_a_request_without_setting_the_geoid_throws_an_IllegalArgumentException() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> APIRequest.builder().build())
        .withMessage("geo id is mandatory for sending a request");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void building_a_request_with_a_non_positive_geoid_throws_an_IllegalArgumentException(
      final int geoId) {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> APIRequest.builder().forGeoId(geoId))
        .withMessage("geo id should be a positive integer");
  }

  @Test
  void build_a_request_with_a_negative_minutes_before_sunset_throws_an_IllegalArgumentException() {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> APIRequest.builder().withMinutesBeforeSunset(-1))
        .withMessage("minutes before sunset should be a non negative integer");
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  void build_a_request_with_a_non_positive_minutes_after_sundown_throws_an_IllegalArgumentException(
      final int minutes) {
    Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> APIRequest.builder().withMinutesAfterSundown(minutes))
        .withMessage("minutes after sundown should be a positive integer");
  }

  @Test
  void building_a_request_with_a_null_date_throws_a_NullPointerException() {
    Assertions.assertThatNullPointerException()
        .isThrownBy(() -> APIRequest.builder().withDate(null).build())
        .withMessage("Null localDate");
  }

  @Test
  void building_a_request_with_legal_arguments_does_not_throw_exceptions() {
    Assertions.assertThatNoException()
        .isThrownBy(
            () ->
                APIRequest.builder()
                    .forGeoId(12345)
                    .withDate(LocalDate.now())
                    .withMinutesBeforeSunset(0)
                    .withMinutesAfterSundown(10)
                    .build());
  }

  @Test
  void verify_a_request_built_with_the_query_params_builder() {
    // just random param keys - no meaning here
    var request =
        APIRequest.builder()
            .addParam(ParamKey.GEO_ID)
            .withValue(12345)
            .addParam(ParamKey.CITY)
            .withValue("null")
            .addParam(ParamKey.GREGORIAN_DAY)
            .withPaddedValue(2)
            .addParam(ParamKey.INCLUDE_TURAH_HAFTARAH)
            .withValue(APIRequest.FlagState.OFF)
            .addParam(ParamKey.GEO_TYPE)
            .withValue(APIRequest.GeoType.CITY)
            .addParam(ParamKey.OUTPUT_FORMAT)
            .withValue(APIRequest.OutputType.JSON)
            .build();

    var expectedParams = new EnumMap<ParamKey, ParamValue>(ParamKey.class);
    expectedParams.put(ParamKey.GEO_ID, IntWrapper.of(12345));
    expectedParams.put(ParamKey.CITY, StringWrapper.of("null"));
    expectedParams.put(ParamKey.GREGORIAN_DAY, PaddedIntWrapper.of(2));
    expectedParams.put(ParamKey.INCLUDE_TURAH_HAFTARAH, APIRequest.FlagState.OFF);
    expectedParams.put(ParamKey.GEO_TYPE, APIRequest.GeoType.CITY);
    expectedParams.put(ParamKey.OUTPUT_FORMAT, APIRequest.OutputType.JSON);

    Assertions.assertThat(request.queryParams()).containsAllEntriesOf(expectedParams);
  }
}
