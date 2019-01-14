package sample.webfluxes.api.dto;

import lombok.Getter;
import sample.webfluxes.core.entity.Location;

@Getter
public class SearchDto {

    String region3Code;

    Location location;

    String categoryCode;

    Page page;
}
