import com.adventnet.ds.query.*;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet("/change-place")
public class Place extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        if(request.getParameter("action").equals("restrictPlace")){
            restrictPlace(Integer.parseInt(request.getParameter("placeId")));
        }
        else if(request.getParameter("action").equals("freePlace")){
            try {
                System.out.println(request.getParameter("placeId"));
                freePlace(request.getParameter("placeId"));
            } catch (DataAccessException e) {
                System.out.println(e);
            }
        }
        out.print("success");
    }

    public void restrictPlace(int placeId){
        UpdateQuery updateQuery = new UpdateQueryImpl("Place");
        Criteria criteria = new Criteria(new Column("Place", "PLACE_ID"), placeId, QueryConstants.EQUAL);
        updateQuery.setCriteria(criteria);
        updateQuery.setUpdateColumn("STATUS", "Restricted");


        try {
            DataAccess.update(updateQuery);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void freePlace(String placeId) throws DataAccessException {
        System.out.println("placeId : " + placeId);
        UpdateQuery updateQuery = new UpdateQueryImpl("Place");
        Criteria criteria = new Criteria(new Column("Place", "PLACE_ID"), placeId, QueryConstants.EQUAL);
        updateQuery.setCriteria(criteria);
        updateQuery.setUpdateColumn("VEHICLE_NUMBER", null);
        updateQuery.setUpdateColumn("STATUS", "Available");
        updateQuery.setUpdateColumn("ENTRY_TIME", 0);
        updateQuery.setUpdateColumn("NAME", null);

        DataAccess.update(updateQuery);
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
}
