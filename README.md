<!-- markdownlint-disable MD013 -->
# A Java API encapsulating HebCal's REST API</br>[![maven-central-version]][7] [![java-min-version]][11] [![javadoc-io-badge]][9]</br>[![gh-build-status]][0] [![codecov-coverage]][1]
<!-- markdownlint-enable MD013 -->

The library offers a Java Service encapsulating [HebCal][3]'s REST API.</br>
It also provides a command line script that encapsulates some of the library functionality using [JBang][12].

Using the API:

```java
// get the provider from the service loader
var shabbatApi = ServiceLoader.load(ShabbatAPI.class).findFirst().get();
// build a request (omit the withDate step to fetch the next shabbat)
var request = Request.builder().forGeoId(281184).withDate(LocalDate.of(2021, 1, 1)).build();
// get the response
var response = shabbatApi.sendAsync(request).get();

// 281184 is the geoid for jerusalem israel
assertThat(response.location()).titleIs("Jerusalem, Israel");
assertThat(getShabbatStart(response).toString()).isEqualTo("2021-01-01T16:05:00+02:00")
assertThat(getShabbatEnd(response).toString()).isEqualTo("2021-01-02T17:36:00+02:00")
```

Using [JBang][12]:

```shell
jbang shabbat_times@tomerfi -g 281184 -d 2021-01-01
```

Will print:

```text
Shabbat times for Jerusalem, Israel:
- starts on Friday, 1 January 2021, 16:06
- ends on Saturday, 2 January 2021, 17:37
```

Head on over to the [Wiki section][8] for more information and additional use cases.</br>
Take a look at the [Javadoc][9] for API documentation.</br>

## Links

- [HebCal site][3]
- [HebCal online tool][4]
- [HebCal API docs][5]
- [GeoNames][10]

## Disclaimer

This repository has no direct relations with [HebCal][3].</br>
[HebCal][3] were nice enough to allow public **free access** to their
[API via REST services][5].</br>
The artifact constructed with this repository merely wraps the publicly open REST API with a Java
API.

<!-- Real Links -->
[0]: https://github.com/TomerFi/hebcal-api/actions/workflows/pre_release.yml
[1]: https://codecov.io/gh/TomerFi/hebcal-api
[3]: https://www.hebcal.com/
[4]: https://www.hebcal.com/shabbat
[5]: https://www.hebcal.com/home/197/shabbat-times-rest-api
[6]: https://www.geonames.org/
[7]: https://search.maven.org/artifact/info.tomfi.hebcal/hebcal-api
[8]: https://github.com/TomerFi/hebcal-api/wiki
[9]: https://javadoc.io/doc/info.tomfi.hebcal/hebcal-api
[10]: https://www.geonames.org/
[11]: https://openjdk.java.net/projects/jdk/11/
[12]: https://www.jbang.dev/
<!-- Badges Links -->
[codecov-coverage]: https://codecov.io/gh/TomerFi/hebcal-api/branch/master/graph/badge.svg
[gh-build-status]: https://github.com/TomerFi/hebcal-api/actions/workflows/pre_release.yml/badge.svg
[maven-central-version]: https://badgen.net/maven/v/maven-central/info.tomfi.hebcal/hebcal-api?icon=maven&label=Maven%20Central
[javadoc-io-badge]: https://javadoc.io/badge2/info.tomfi.hebcal/hebcal-api/Javadoc.io.svg
[java-min-version]: https://badgen.net/badge/Java%20Version/11/5382a1
