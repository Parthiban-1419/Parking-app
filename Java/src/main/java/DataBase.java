import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Date;

public class DataBase {
    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));

    public static void main(String[] args) throws IOException, ParseException {
        DataBase db = new DataBase();

        Date d = new Date();
        System.out.println(d.getTime());

//        db.updateData();
//        db.getData("parking-app", "vehicleType", "bike");
/*        db.putData("{\n" +
                "    \"vehicleType\" : \"bike\",\n" +
                "    \"floors\" : [{\n" +
                "      \"floorNumber\" : -2,\n" +
                "      \"blocks\" : [\n" +
                "        {\n" +
                "          \"blockName\" : \"A\",\n" +
                "          \"places\" : [\n" +
                "            {\n" +
                "              \"place\" : 1,\n" +
                "              \"status\": \"Available\",\n" +
                "              \"vehicleNumber\" : \"\",\n" +
                "              \"parkedTime\" : 0\n" +
                "            }," +
                        "{"+
                            "\"place\" : 2," +
                            "\"status\": \"Available\"," +
                            "\"vehicleNumber\" : \"\"," +
                            "\"parkedTime\" : 0 " +
                        "}" +
                "]\n" +
                "        }]\n" +
                "    }]\n" +
//                "  }\n" ;
//                "  {\n" +
//                "    \"vehicleType\" : \"car\",\n" +
//                "    \"floors\" : [{\n" +
//                "      \"floorNumber\" : 1,\n" +
//                "      \"blocks\" : [\n" +
//                "        {\n" +
//                "          \"blockNumber\" : \"A\",\n" +
//                "          \"places\" : [\n" +
//                "            {\n" +
//                "              \"place\" : 1,\n" +
//                "              \"status\": \"Available\",\n" +
//                "              \"vehicleNumber\" : null,\n" +
//                "              \"parkedTime\" : 0\n" +
//                "            }]\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"blockNumber\" : \"B\",\n" +
//                "          \"places\" : [\n" +
//                "            {\n" +
//                "              \"place\" : 1,\n" +
//                "              \"status\": \"Available\",\n" +
//                "              \"vehicleNumber\" : null,\n" +
//                "              \"parkedTime\" : 0\n" +
//                "            }]\n" +
//                "        }]\n" +
//                "    }]\n" +
//                "  }]\n" +
                "}","parking-app");
*/
    }

    public void updateData() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("parking-app", "1").doc(
                "{"+
                "\"vehicleType\" : \"bike\"," +
                "\"floors\" : [" +
                        "{\"floorNumber\" : -2," +
                        "\"blocks\" : [" +
                            "{\"blockName\" : \"A\"," +
                            "\"places\" : [" +
                                "{"+
                                    "\"place\" : 1," +
                                    "\"status\": \"Available\"," +
                                    "\"vehicleNumber\" : \"\"," +
                                    "\"parkedTime\" : 0 " +
                                "}]" +
                            "}]" +
                        "}]" +
                "}", XContentType.JSON);

        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.getResult().name());
    }

    public void putData(String json, String index) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id("1");
        indexRequest.source(json, XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println(indexResponse.getResult().name());
    }

    public JSONArray getData(String index, String key, String value) throws IOException, ParseException {
        JSONArray jArray = new JSONArray();
        JSONParser parser = new JSONParser();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchQuery(key, value)));

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println("hits : " + searchResponse.getHits().getTotalHits().value);

        if (searchResponse.getHits().getTotalHits().value > 0) {
            SearchHit[] searchHit = searchResponse.getHits().getHits();
            for (SearchHit hit : searchHit) {
                System.out.println(hit.getId());
                jArray.add(parser.parse(hit.getSourceAsString()));
            }
        }
        return jArray;
    }

}
