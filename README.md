<h1 align="left">
  Hebrew Shabbat Java API
  <br/>
  <a href="https://search.maven.org/artifact/info.tomfi.shabbat/shabbat-api">
    <img src="https://badgen.net/maven/v/maven-central/info.tomfi.shabbat/shabbat-api?icon=maven&label=Maven%20Central"/>
  </a>
  <a href="https://openjdk.java.net/projects/jdk/11/">
    <img src="https://badgen.net/badge/Java%20Version/11/5382a1"/>
  </a>
  <a href="https://javadoc.io/doc/info.tomfi.shabbat/shabbat-api">
    <img src="https://javadoc.io/badge2/info.tomfi.shabbat/shabbat-api/Javadoc.io.svg"/>
  </a>
  <a href="https://codecov.io/gh/TomerFi/shabbat-api">
    <img src="https://codecov.io/gh/TomerFi/shabbat-api/branch/master/graph/badge.svg"/>
  </a>
</h1>

<p align="left">
  <strong>Shabbat API sends requests and serialize responses to and fro <a href="https://www.hebcal.com/home/197/shabbat-times-rest-api">HebCal's Shabbat Times REST API</a></strong><br/>
</p>

<p align="left">

```xml
<dependency>
  <groupId>info.tomfi.shabbat</groupId>
  <artifactId>shabbat-api</artifactId>
  <version>2.1.9</version>
</dependency>
```

</p>

<p align="left">

```java
requires info.tomfi.shabbat; // module-info.java
```

</p>

<p align="left">

```java
import info.tomfi.shabbat.APIRequest;
import info.tomfi.shabbat.ShabbatAPI;

var api = new ShabbatAPI();
var request = APIRequest.builder()
    .forGeoId(281184)
    .withDate(LocalDate.parse("2022-03-04")) // omit the withDate step to fetch the next shabbat
    .build();
var response = api.sendAsync(request).get();
```

</p>

<p align="left">

```java
System.out.println(response.location.city); // Jerusalem
System.out.println(response.location.country); // Israel
System.out.println(response.getShabbatStart()); // 2022-03-04T16:59+02:00
System.out.println(response.getShabbatEnd()); // 2022-03-05T18:30+02:00
System.out.println(response.getShabbatParasha()); // Parashat Pekudei
System.out.println(response.isRoshChodesh()); // true
```

</p>

<p align="left">
  <table align="left">
    <tr>
      <td align="center">
        <a href="https://www.hebcal.com/home/197/shabbat-times-rest-api" target="_blank">
          HebCal Shabbat REST API
        </a>
      </td>
    </tr>
    <tr>
      <td align="center">
        <a href="https://www.geonames.org/" target="_blank">
          The GeoNames Database
        </a>
      </td>
    </tr>
  </table>
</p><br/><br/><br/><br/>

<details>
<summary><strong><a href="https://www.jbang.dev/">JBang</a> script available!</strong></summary>
<p>

```shell
$ jbang shabbat_times@tomerfi -g 281184 -d 2022-03-04

Shabbat info for Jerusalem, Israel:
- starts on Friday, 4 March 2022, 16:59
- ends on Saturday, 5 March 2022, 18:30
- the parasha is Parashat Pekudei
- shabbat is rosh chodesh
```

</p>

</details>

<details>
<summary>Snapshots access</summary>

<p align="left">

<img src="https://img.shields.io/maven-metadata/v?color=yellow&label=latest&metadataUrl=https%3A%2F%2Foss.sonatype.org%2Fcontent%2Frepositories%2Fsnapshots%2Finfo%2Ftomfi%2Fshabbat%2Fshabbat-api%2Fmaven-metadata.xml&versionSuffix=SNAPSHOT" />

Snapshots are deployed to <em>OSSRH</em>:
</p>

<p align="left">

```yaml
<repository>
  <id>snapshots-repo</id>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <releases>
    <enabled>false</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```

</p>

</details>
