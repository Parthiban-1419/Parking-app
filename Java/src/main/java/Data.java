import com.adventnet.ds.query.*;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;

@WebServlet(urlPatterns = {"/get-data", "/book-place"})
public class Data extends HttpServlet{
    PrintWriter out;
//    Persistence persistence ;
//
//    public static void main(String[] args) {
//        JSONArray jArray = new JSONArray();
//        Column floorColumn = new Column("BuildingDetails", "FLOOR").distinct();
//        SelectQuery sQuery = new SelectQueryImpl(new Table("BuildingDetails"));
//        sQuery.addSelectColumn(floorColumn);
//
//        try {
//            RelationalAPI rAPI = RelationalAPI.getInstance();
//            Connection con = rAPI.getConnection();
//            DataSet dataSet = rAPI.executeQuery(sQuery, con);
//            System.out.print(dataSet);
//
//            while(dataSet.next()){
//                System.out.print(dataSet.getValue("floor"));
//            }
//            System.out.print(jArray);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();
        JSONObject jBuilding = new JSONObject();
        JSONArray prices = new JSONArray();

        try {
            if (request.getParameter("action").equals("getData")) {
                Criteria criteria = new Criteria(new Column("VehiclePrice", "BUILDING_NAME"), request.getParameter("buildingName"), QueryConstants.EQUAL);
                DataObject dataObject = DataAccess.get("VehiclePrice", criteria);

//                SortColumn sortPK = new SortColumn("VehiclePrice", "BUILDING_NAME", true);
//                dataObject.sortRows("VehiclePrice", sortPK);

                System.out.println(dataObject);

                Row row ;
                Iterator i = dataObject.getRows("VehiclePrice");
                while(i.hasNext()){
                    row = (Row)i.next();
                    prices.add(row.getAsJSON());
                    jBuilding.put(row.getString("VEHICLE_TYPE"), getData(request.getParameter("buildingName"), row.getString("VEHICLE_TYPE")));
                }
                jBuilding.put("buildingName", request.getParameter("buildingName"));
                jBuilding.put("vehiclePrices", prices);
                out.print(jBuilding);
            }
            else if (request.getParameter("action").equals("bookPlace")) {
                Place place = new Place();
                place.bookPlace(request.getParameter("placeId"), request.getParameter("vehicleNumber"), request.getParameter("name"));
                out.print("Place booked successfully");
            }
        }
        catch(IllegalArgumentException e){
            out.print("Enter a valid vehicle number");
        }
        catch(Exception e){
            out.print(e);
        }
    }

    public JSONArray getData(String buildingName, String vehicleType){
        Criteria criteria = new Criteria(new Column("Floor", "VEHICLE_TYPE"), vehicleType, QueryConstants.EQUAL).and(new Criteria(new Column("Floor", "BUILDING_NAME"), buildingName, QueryConstants.EQUAL));
        JSONObject floor, row, block;
        JSONArray floorTable , floorArr = new JSONArray(), rowTable, rowArr, bolckTable, blockArr;

        try {
//            RelationalAPI rAPI = RelationalAPI.getInstance();
//            Connection con = rAPI.getConnection();
//            DataSet dataSet = rAPI.executeQuery(sQuery, con);
//            out.print(dataSet);

//            while(dataSet.next()){
//                out.print(dataSet.getValue("floor"));
//            }
//
//            persistence = (Persistence) BeanUtil.lookup("Persistence");
//            dataObject = persistence.get(sQuery);
//            out.print(dataObject);

//            out.print(getFromDB("BuildingDetails", criteria));
//
//            i = dataObject.getRows("BuildingDetails");
//            while(i.hasNext()){
//                row = (Row)i.next();
//                criteria1 = new Criteria(floorColumn, row.get("FLOOR"), QueryConstants.EQUAL);
//                json = new JSONObject();
//
//                json.put("floor", row.get("FLOOR"));
//                json.put("data", getFromDB("BuildingDetails", criteria.and(criteria1)));
//
//                jArray.add(json);
//            }
//
            System.out.println("buildingName : " + buildingName + ", " + "vehicleType : " + vehicleType);
            floorTable = getFromDB("Floor", criteria);
            for(Object object : floorTable) {
                floor = new JSONObject();
                floor.put("floor_id", ((JSONObject) object).get("floor_id"));
                floor.put("floor", ((JSONObject) object).get("floor"));
                floor.put("vehicleType", ((JSONObject) object).get("vehicle_type"));
                criteria = new Criteria(new Column("FloorRow", "FLOOR_ID"), ((JSONObject) object).get("floor_id"), QueryConstants.EQUAL);
                rowTable = getFromDB("FloorRow", criteria);
                rowArr = new JSONArray();
                for(Object rowObject: rowTable){
                    row = new JSONObject();
                    row.put("row_id", ((JSONObject) rowObject).get("row_id"));
                    row.put("row", ((JSONObject) rowObject).get("row"));
                    criteria = new Criteria(new Column("Block", "ROW_ID"), ((JSONObject) rowObject).get("row_id"), QueryConstants.EQUAL);

                    bolckTable = getFromDB("Block", criteria);
                    blockArr = new JSONArray();
                    for(Object blockObject: bolckTable){
                        block = new JSONObject();
                        criteria = new Criteria(new Column("Place", "BLOCK_ID"), ((JSONObject) blockObject).get("block_id"), QueryConstants.EQUAL);
                        block.put("block_id", ((JSONObject) blockObject).get("block_id"));
                        block.put("block", ((JSONObject) blockObject).get("block"));
                        block.put("places", getFromDB("Place", criteria));
                        blockArr.add(block);
                    }
                    row.put("blocks", blockArr);
                    rowArr.add(row);
                }
                floor.put("rows", rowArr);
                floorArr.add(floor);
            }
        } catch (Exception e) {
            System.out.println(e);
            floorArr = new JSONArray();
            floorArr.add(e);
        }
        return floorArr;
    }

    JSONArray getFromDB(String tableName, Criteria criteria) throws DataAccessException {
        Row row ;
        DataObject dataObject;
        JSONArray jArray = new JSONArray();
        dataObject = DataAccess.get(tableName, criteria);
//        if(tableName.equals("Place")) {
            SortColumn sortPK = new SortColumn("Place", "PLACE_ID", true);
            dataObject.sortRows("Place", sortPK);

            sortPK = new SortColumn("Block", "BLOCK_ID", true);
            dataObject.sortRows("Block", sortPK);

            sortPK = new SortColumn("Row", "ROW", true);
            dataObject.sortRows("Row", sortPK);

            sortPK = new SortColumn("Floor", "FLOOR_ID", true);
            dataObject.sortRows("Floor", sortPK);
//        }
        Iterator i = dataObject.getRows(tableName);
        while(i.hasNext()) {
            row = (Row) i.next();
            jArray.add(row.getAsJSON());
        }
        return jArray;
    }

    public void bookPlace(String placeId, String vehicleNumber, String name) throws DataAccessException {
        UpdateQuery updateQuery = new UpdateQueryImpl("Place");
        Criteria criteria = new Criteria(new Column("Place", "PLACE_ID"), placeId, QueryConstants.EQUAL);
        updateQuery.setCriteria(criteria);
        updateQuery.setUpdateColumn("VEHICLE_NUMBER", vehicleNumber);
        updateQuery.setUpdateColumn("STATUS", "Unavailable");
        updateQuery.setUpdateColumn("ENTRY_TIME", new Date().getTime());
        updateQuery.setUpdateColumn("NAME", name);

        DataAccess.update(updateQuery);
    }

//    void demo() throws SQLException, QueryConstructionException {
//        Table tab = new Table("BuildingDetails");
//
//        SelectQuery sQuery = new SelectQueryImpl(tab);
//        Column col = Column.getColumn(tab.getTableName(), "FLOOR");
//        sQuery.addSelectColumn(col);
//        RelationalAPI rAPI = RelationalAPI.getInstance();
//        Connection con = rAPI.getConnection();
//        DataSet ds = rAPI.executeQuery(sQuery, con);
//        out.print(ds);
//        while (ds.next()){
//            out.print("Floor : " + ds.getValue("floor"));
//        }
//    }

}
