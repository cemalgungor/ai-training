package com.ai.traning.tools.countryInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountryInfoTools {

  private final RestClient restClient;
  private static final String BASE_URL = "https://restcountries.com";

  public CountryInfoTools(RestClient.Builder builder) {
      this.restClient = builder.
              baseUrl(BASE_URL)
              .defaultHeader("Accept", "application/json")
              .build();
  }

  public record Country(
          @JsonIgnoreProperties("name") Name name,
          @JsonIgnoreProperties("capital") List<String> capital,
          @JsonIgnoreProperties("region") String region,
          @JsonIgnoreProperties("subregion") String subregion,
          @JsonIgnoreProperties("population") long population,
          @JsonIgnoreProperties("currencies") Map<String, Currency> currencies,
          @JsonIgnoreProperties("flags") Flag flags
  ){
      public record Name(@JsonProperty("common") String common) {}
      public record Currency(@JsonProperty("name") String name , @JsonProperty("symbol") String symbol) {}
      public record Flag(@JsonProperty("png") String png) {}
  }

  @Tool(description = "Get country information by name (e.g. Turkey)")
  public String getCountryInfo(@ToolParam(description = "Country name, e.g. Turkey") String countryName){

      Country[] countries = restClient.get()
              .uri("/v3.1/name/{name}")
              .retrieve()
              .body(Country[].class);
      if (countries == null || countries.length == 0){
          return "Ülke bulunamadı : "+ countryName;
      }
      Country country = countries[0];
      String currencyText = country.currencies.values().stream()
              .map(currency -> currency.name() + " (" + currency.symbol() + ")")
              .collect(Collectors.joining(", "));

      return String.format("""
                🌍 *%s* Hakkında Bilgiler:
                🏙️ Başkent: %s
                📍 Bölge: %s (%s)
                👥 Nüfus: %,d
                💰 Para Birimi: %s
                🏳️ Bayrak: %s
                """,
              country.name().common(),
              country.capital() != null && !country.capital().isEmpty() ? country.capital().get(0) : "Yok",
              country.region(),
              country.subregion(),
              country.population(),
              currencyText,
              country.flags().png());
  }

}
