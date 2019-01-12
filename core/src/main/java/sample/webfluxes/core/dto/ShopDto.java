package sample.webfluxes.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopDto {

    private String shopNumber;

    private Boolean use;
    private Boolean block;
    private Boolean delete;
    private String categoryCode;
    private String address;
    private String region3Code;
    private Location realLocation;

    private Long favorites;
    private Long viewCount;
    private Double starAvgScore;

}
