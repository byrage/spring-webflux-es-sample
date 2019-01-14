package sample.webfluxes.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    @JsonProperty
    Integer offset;
    @JsonProperty
    Integer size;
}
