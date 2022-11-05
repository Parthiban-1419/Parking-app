import com.adventnet.db.api.RelationalAPI;
import com.adventnet.ds.query.*;
import com.adventnet.persistence.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/vehicle-detail")
public class Amount extends HttpServlet {
    PrintWriter out;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();
        JSONArray jsonArray = new JSONArray();
        Row row;
        Criteria nameCriteria = new Criteria(new Column("Place", "NAME"), request.getParameter("name"), QueryConstants.EQUAL),
                statusCriteria = new Criteria(new Column("Place", "STATUS"), "Unavailable", QueryConstants.EQUAL);

        try {
//            SelectQuery selectQuery = new SelectQueryImpl(new Table("Place"));
//
//            PersonalizationUtil p;
//            Persistence per = (Persistence) BeanUtil.lookup("Persistence");
//            System.out.println(per.getForPersonality("BuildingDetail", nameCriteria));
//            RelationalAPI relApi = RelationalAPI.getInstance();
//            Connection con 	= relApi.getConnection();
////            DataSet ds 	= relApi.executeQuery(sq, con);


//            DataObject dataObject = DataAccess.get("Place", nameCriteria.and(statusCriteria));
//            Iterator it = dataObject.getRows("Place");
//            while (it.hasNext()) {
//                row = (Row) it.next();
//                jsonArray.add(row.getAsJSON());
//            }
//            out.print(jsonArray);
            out.print(demo(nameCriteria.and(statusCriteria)));
        } catch (Exception e) {
            out.print(e);
        }
    }

    public JSONArray demo(Criteria criteria) throws SQLException, QueryConstructionException {
        JSONArray jsonArray = new JSONArray();
        JSONObject json;

        Table table = new Table("Floor");

        SelectQuery sQuery = new SelectQueryImpl(table);

        ArrayList<Column> colList = new ArrayList<Column>();

        //Adding columns of table 'Place' to list
        colList.add(Column.getColumn("Place", "PLACE_ID"));
        colList.add(Column.getColumn("Place", "PLACE"));
        colList.add(Column.getColumn("Place", "VEHICLE_NUMBER"));
        colList.add(Column.getColumn("Place", "ENTRY_TIME"));

        //Adding columns of table 'Block' to list
        colList.add(Column.getColumn("Block", "BLOCK"));
        colList.add(Column.getColumn("Block", "BLOCK_ID"));

        //Adding columns of table 'Floor' to list
        colList.add(Column.getColumn("Floor", "FLOOR"));
        colList.add(Column.getColumn("Floor", "FLOOR_ID"));
        colList.add(Column.getColumn("Floor", "VEHICLE_TYPE"));

        sQuery.addSelectColumns(colList); //Adding columns to Select Query
        sQuery.setCriteria(criteria);      //Setting Criteria to Select Query

        //Adding Joins to Select Query
        sQuery.addJoin(new Join("Floor", "Block", new String[]{"FLOOR_ID"}, new String[]{"FLOOR_ID"}, Join.INNER_JOIN));
        sQuery.addJoin(new Join("Block", "Place", new String[]{"BLOCK_ID"}, new String[]{"BLOCK_ID"}, Join.INNER_JOIN));

        RelationalAPI rAPI = RelationalAPI.getInstance();
        Connection con = rAPI.getConnection();
        DataSet ds = rAPI.executeQuery(sQuery, con);

        while (ds.next()){
            json = new JSONObject();

            json.put("floor", ds.getValue("floor"));
            json.put("block", ds.getValue("block"));
            json.put("placeId", ds.getValue("place_id"));
            json.put("place", ds.getValue("place"));
            json.put("type", ds.getValue("vehicle_type"));
            json.put("vehicleNumber", ds.getValue("vehicle_number"));
            json.put("entryTime", ds.getValue("entry_time"));

            jsonArray.add(json);
        }

        return jsonArray;
    }

}
