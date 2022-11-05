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
        try {
            if (request.getParameter("action").equals("getData"))
                getData(request.getParameter("buildingName"), request.getParameter("vehicleType"));
            else if (request.getParameter("action").equals("bookPlace")) {
                bookPlace(request.getParameter("placeId"), request.getParameter("vehicleNumber"), request.getParameter("name"));
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

    public void getData(String buildingName, String vehicleType){

        JSONObject json, jBlock;
        JSONArray jArray = new JSONArray();
        Criteria criteria = new Criteria(new Column("Floor", "VEHICLE_TYPE"), vehicleType, QueryConstants.EQUAL).and(new Criteria(new Column("Floor", "BUILDING_NAME"), buildingName, QueryConstants.EQUAL));

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
            JSONArray floorArray = getFromDB("Floor", criteria), blockArray, temp;

            for(Object object : floorArray){
                criteria = new Criteria(new Column("Block", "FLOOR_ID"), ((JSONObject) object).get("floor_id"), QueryConstants.EQUAL);
                json = new JSONObject();
                json.put("floor", ((JSONObject) object).get("floor"));
                blockArray = getFromDB("Block", criteria);

                temp = new JSONArray();
                for(Object ob : blockArray){
                    criteria = new Criteria(new Column("Place", "BLOCK_ID"), ((JSONObject) ob).get("block_id"), QueryConstants.EQUAL);
                    jBlock = new JSONObject();

                    jBlock.put("block", ((JSONObject) ob).get("block"));
                    jBlock.put("places", getFromDB("Place", criteria));

                    temp.add(jBlock);
                }
                json.put("blocks", temp);

                jArray.add(json);
            }

            out.print(jArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    JSONArray getFromDB(String tableName, Criteria criteria) throws DataAccessException {
        Row row ;
        DataObject dataObject;
        JSONArray jArray = new JSONArray();

        dataObject = DataAccess.get(tableName, criteria);

        if(tableName.equals("Place")) {
            SortColumn sortId = new SortColumn("Place", "PLACE_ID", true);
            dataObject.sortRows("Place", sortId);
        }

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
