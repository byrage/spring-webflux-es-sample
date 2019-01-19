package sample.webfluxes.api.dto;

import lombok.Getter;

@Getter
public class SearchParam {

    String regionCode;

    String categoryCode;

    Page page;
}
