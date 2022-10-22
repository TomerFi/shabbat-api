package info.tomfi.shabbat;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.tomfi.shabbat.APIResponse.ShabbatItem;
import java.io.IOException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class APIResponseTest {
  @Test
  void verify_equals_and_hashcode() {
    EqualsVerifier.forClass(APIResponse.class)
        .suppress(Warning.ALL_NONFINAL_FIELDS_SHOULD_BE_USED)
        .verify();
    EqualsVerifier.forClass(APIResponse.DateRange.class).verify();
    EqualsVerifier.forClass(APIResponse.Item.class).verify();
    EqualsVerifier.forClass(APIResponse.Location.class).verify();
  }

  @Test
  void verify_serialization_of_a_real_non_rosh_chodesh_response_object()
      throws StreamReadException, DatabindException, IOException {
    var mapper = new ObjectMapper();

    var response =
        mapper.readValue(
            getClass().getModule().getResourceAsStream("api-responses/real_response.json"),
            APIResponse.class);

    // verify response
    assertThat(response.title).isEqualTo("Hebcal Jerusalem January 2021");
    assertThat(response.link).isEmpty();
    assertThat(response.date).isEqualTo("2022-10-23T06:37:49.996Z");
    // verify date range
    assertThat(response.range)
        .contains(
            new APIResponse.DateRange.Builder()
                .withStart("2021-01-01")
                .withEnd("2021-01-02")
                .build());
    // verify response location
    assertThat(response.location.admin1).contains("Jerusalem");
    assertThat(response.location.asciiname).contains("Jerusalem");
    assertThat(response.location.title).isEqualTo("Jerusalem, Israel");
    assertThat(response.location.tzid).contains("Asia/Jerusalem");
    assertThat(response.location.city).isEqualTo("Jerusalem");
    assertThat(response.location.country).isEqualTo("Israel");
    assertThat(response.location.geo).isEqualTo("geoname");
    assertThat(response.location.latitude).isEqualTo(31.76904);
    assertThat(response.location.longitude).isEqualTo(35.21633);
    assertThat(response.location.geonameid).isEqualTo(281184);
    assertThat(response.location.cc).contains("IL");
    // verify response items
    assertThat(response.items.get())
        .containsOnly(
            new APIResponse.Item.Builder()
                .withTitle("Candle lighting: 16:06")
                .withCategory("candles")
                .withHebrew("הדלקת נרות")
                .withDate("2021-01-01T16:06:00+02:00")
                .withMemo("Parashat Vayechi")
                .withTitleOrig("Candle lighting")
                .build(),
            new APIResponse.Item.Builder()
                .withTitle("Parashat Vayechi")
                .withCategory("parashat")
                .withHebrew("פרשת ויחי")
                .withDate("2021-01-02")
                .withLink("https://hebcal.com/s/vayechi-20210102?i=on&us=js&um=api")
                .withHdate("18 Tevet 5781")
                .build(),
            new APIResponse.Item.Builder()
                .withTitle("Havdalah (50 min): 17:37")
                .withCategory("havdalah")
                .withHebrew("הבדלה (50 דקות)")
                .withDate("2021-01-02T17:37:00+02:00")
                .withTitleOrig("Havdalah")
                .build());
    // verify shabbat start, end, parasha, and rosh chodesh parsing
    assertThat(response.getShabbatStart()).isEqualTo("2021-01-01T16:06:00+02:00");
    assertThat(response.getShabbatEnd()).isEqualTo("2021-01-02T17:37:00+02:00");
    assertThat(response.getShabbatParasha()).isEqualTo("Parashat Vayechi");
    // verify shabbat is NOT rosh chodesh
    assertThat(response.isRoshChodesh()).isFalse();
    // verify shabbat parashat item (start and end are used internally)
    var parashatItem = response.getShabbatItem(ShabbatItem.PARASHAT_SHABBAT).get();
    assertThat(parashatItem.hdate).contains("18 Tevet 5781");
  }

  @Test
  void verify_serialization_of_a_real_rosh_chodesh_response_object()
      throws StreamReadException, DatabindException, IOException {
    var mapper = new ObjectMapper();

    var response =
        mapper.readValue(
            getClass().getModule().getResourceAsStream("api-responses/real_response_rosh_chodesh.json"),
            APIResponse.class);

    // verify response
    assertThat(response.title).isEqualTo("Hebcal Jerusalem March 2022");
    assertThat(response.link).isEmpty();
    assertThat(response.date).isEqualTo("2022-10-23T15:36:47.984Z");
    // verify date range
    assertThat(response.range)
        .contains(
            new APIResponse.DateRange.Builder()
                .withStart("2022-03-04")
                .withEnd("2022-03-05")
                .build());
    // verify response location
    assertThat(response.location.admin1).contains("Jerusalem");
    assertThat(response.location.asciiname).contains("Jerusalem");
    assertThat(response.location.title).isEqualTo("Jerusalem, Israel");
    assertThat(response.location.tzid).contains("Asia/Jerusalem");
    assertThat(response.location.city).isEqualTo("Jerusalem");
    assertThat(response.location.country).isEqualTo("Israel");
    assertThat(response.location.geo).isEqualTo("geoname");
    assertThat(response.location.latitude).isEqualTo(31.76904);
    assertThat(response.location.longitude).isEqualTo(35.21633);
    assertThat(response.location.geonameid).isEqualTo(281184);
    assertThat(response.location.cc).contains("IL");
    // verify response items
    assertThat(response.items.get())
        .containsOnly(
            new APIResponse.Item.Builder()
                .withTitle("Rosh Chodesh Adar II")
                .withCategory("roshchodesh")
                .withHebrew("ראש חודש אדר ב׳")
                .withDate("2022-03-04")
                .withHdate("1 Adar II 5782")
                .withMemo("Start of month of Adar II (on leap years) on the Hebrew calendar. Adar II (אַדָר ב׳), sometimes \"Adar Bet\" or \"Adar Sheni\", is the 13th month of the Hebrew year, has 29 days, occurs only on leap years, and corresponds to February or March on the Gregorian calendar.  רֹאשׁ חוֹדֶשׁ, transliterated Rosh Chodesh or Rosh Hodesh, is a minor holiday that occurs at the beginning of every month in the Hebrew calendar. It is marked by the birth of a new moon")
                .withLink("https://hebcal.com/h/rosh-chodesh-adar-ii-2022?i=on&us=js&um=api")
                .build(),
            new APIResponse.Item.Builder()
                .withTitle("Candle lighting: 16:59")
                .withCategory("candles")
                .withHebrew("הדלקת נרות")
                .withDate("2022-03-04T16:59:00+02:00")
                .withMemo("Parashat Pekudei")
                .withTitleOrig("Candle lighting")
                .build(),
            new APIResponse.Item.Builder()
                .withTitle("Parashat Pekudei")
                .withCategory("parashat")
                .withHebrew("פרשת פקודי")
                .withDate("2022-03-05")
                .withLink("https://hebcal.com/s/pekudei-20220305?i=on&us=js&um=api")
                .withHdate("2 Adar II 5782")
                .build(),
            new APIResponse.Item.Builder()
                .withTitle("Havdalah (50 min): 18:30")
                .withCategory("havdalah")
                .withHebrew("הבדלה (50 דקות)")
                .withDate("2022-03-05T18:30:00+02:00")
                .withTitleOrig("Havdalah")
                .build());
    // verify shabbat start, end, parasha, and rosh chodesh parsing
    assertThat(response.getShabbatStart()).isEqualTo("2022-03-04T16:59:00+02:00");
    assertThat(response.getShabbatEnd()).isEqualTo("2022-03-05T18:30:00+02:00");
    assertThat(response.getShabbatParasha()).isEqualTo("Parashat Pekudei");
    // verify shabbat parashat item (start and end are used internally)
    var parashatItem = response.getShabbatItem(ShabbatItem.PARASHAT_SHABBAT).get();
    assertThat(parashatItem.hdate).contains("2 Adar II 5782");
    // verify shabbat is rosh chodesh
    assertThat(response.isRoshChodesh()).isTrue();
    var roshChodeshItem = response.getShabbatItem(ShabbatItem.ROSH_CHODESH).get();
    assertThat(roshChodeshItem.title).isEqualTo("Rosh Chodesh Adar II");
  }
}
