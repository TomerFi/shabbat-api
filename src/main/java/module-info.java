module info.tomfi.shabbat {
  requires java.net.http;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;

  opens info.tomfi.shabbat to
      com.fasterxml.jackson.databind;

  exports info.tomfi.shabbat;
}
