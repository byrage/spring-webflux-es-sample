package sample.webfluxes.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Location {

    @JsonProperty
    Double lat;
    @JsonProperty
    Double lon;
}