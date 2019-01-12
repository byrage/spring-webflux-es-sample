package sample.webfluxes.core.entity;

import lombok.*;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Shop {

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

    public static Shop dummy(String id) {

        Shop shopDto = new Shop();
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

    public XContentBuilder source() throws IOException {

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

    // TODO : JSON 파일 읽어오도록
    public static String mapping() {

        return "{\n"
                + "\"_doc\": {\n"
                + "        \"properties\": {\n"
                + "          \"address\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"block\": {\n"
                + "            \"type\": \"boolean\"\n"
                + "          },\n"
                + "          \"campaignId\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"categoryCode\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"delete\": {\n"
                + "            \"type\": \"boolean\"\n"
                + "          },\n"
                + "          \"favoriteCount\": {\n"
                + "            \"type\": \"long\"\n"
                + "          },\n"
                + "          \"id\": {\n"
                + "            \"type\": \"integer\"\n"
                + "          },\n"
                + "          \"like\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"modificationDate\": {\n"
                + "            \"type\": \"date\"\n"
                + "          },\n"
                + "          \"name\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"no_store\": {\n"
                + "            \"type\": \"keyword\"\n"
                + "          },\n"
                + "          \"query\": {\n"
                + "            \"properties\": {\n"
                + "              \"term\": {\n"
                + "                \"properties\": {\n"
                + "                  \"id\": {\n"
                + "                    \"type\": \"long\"\n"
                + "                  }\n"
                + "                }\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"realLocation\": {\n"
                + "            \"properties\": {\n"
                + "              \"lat\": {\n"
                + "                \"type\": \"float\"\n"
                + "              },\n"
                + "              \"lon\": {\n"
                + "                \"type\": \"float\"\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"region3Code\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"shopNumber\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"starAvgScore\": {\n"
                + "            \"type\": \"float\"\n"
                + "          },\n"
                + "          \"store\": {\n"
                + "            \"type\": \"keyword\",\n"
                + "            \"store\": true\n"
                + "          },\n"
                + "          \"stored_fields\": {\n"
                + "            \"type\": \"text\",\n"
                + "            \"fields\": {\n"
                + "              \"keyword\": {\n"
                + "                \"type\": \"keyword\",\n"
                + "                \"ignore_above\": 256\n"
                + "              }\n"
                + "            }\n"
                + "          },\n"
                + "          \"use\": {\n"
                + "            \"type\": \"boolean\"\n"
                + "          },\n"
                + "          \"viewCount\": {\n"
                + "            \"type\": \"long\"\n"
                + "          }\n"
                + "        }\n"
                + "      }\n"
                + "}";

    }
}
