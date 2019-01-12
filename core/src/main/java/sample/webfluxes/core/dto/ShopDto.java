package sample.webfluxes.core.dto;

import lombok.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ShopDto {

    private String campaignId;
    private String shopNumber;

    private Boolean use;
    private Boolean block;
    private Boolean delete;
    private String categoryCode;
    private String address;
    private String region3Code;
    private Location realLocation;

    private Long favoriteCount;
    private Long viewCount;
    private Double starAvgScore;

    public XContentBuilder xContent() throws IOException {

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.humanReadable(true);
        builder.prettyPrint();

        builder.startObject();

        builder.field("campaignId", this.getCampaignId());
        builder.field("shopNumber", this.getShopNumber());
        builder.field("use", this.getUse());
        builder.field("block", this.getBlock());
        builder.field("delete", this.getDelete());
        builder.field("categoryCode", this.getCategoryCode());
        builder.field("address", this.getAddress());
        builder.field("region3Code", this.getRegion3Code());
        builder.latlon("realLocation", this.getRealLocation().getLat(), this.getRealLocation().getLon());
        builder.field("favoriteCount", this.getFavoriteCount());
        builder.field("viewCount", this.getViewCount());
        builder.field("starAvgScore", this.getStarAvgScore());
        builder.timeField("modificationDate", LocalDateTime.now());

        builder.endObject();

        return builder;
    }

    public static ShopDto dummy(String id) {

        ShopDto shopDto = new ShopDto();
        shopDto.setCampaignId(id);
        shopDto.setShopNumber("117722");
        shopDto.setUse(true);
        shopDto.setBlock(true);
        shopDto.setDelete(true);
        shopDto.setCategoryCode("1");
        shopDto.setAddress("강남역");
        shopDto.setRegion3Code(String.valueOf(11680640));
        shopDto.setRealLocation(new Location(37.49799082, 127.02779625));
        shopDto.setFavoriteCount(456L);
        shopDto.setViewCount(111L);
        shopDto.setStarAvgScore(4.99D);

        return shopDto;
    }
}
