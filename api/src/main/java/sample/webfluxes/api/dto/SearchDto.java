package sample.webfluxes.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import sample.webfluxes.core.entity.Location;

@Getter
@Setter
public class SearchDto {

    @JsonProperty
    String region3Code;

    @JsonProperty
    Location location;

    @JsonProperty
    String categoryCode;
    @JsonProperty
    Page page;
}
