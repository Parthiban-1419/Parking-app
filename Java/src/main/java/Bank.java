import com.adventnet.ds.query.*;
import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/pay-bill")
public class Bank extends HttpServlet {
    PrintWriter out;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();

        Row row = new Row("Transaction");
        Criteria criteria = new Criteria(new Column("Transaction","ACCOUNT_NUMBER"), request.getParameter("accountNumber"), QueryConstants.EQUAL);

        row.set("NAME", request.getParameter("name"));
        row.set("ACCOUNT_NUMBER", request.getParameter("accountNumber"));
        row.set("IFSC", request.getParameter("ifsc"));

        try {
            if(!DataAccess.get("Transaction", criteria).getRows("Transaction").hasNext()) {
                Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
                DataObject d = persistence.constructDataObject();
                d.addRow(row);
                DataAccess.add(d);
            }
            updatePlace(request.getParameter("placeId"));

            out.print("Paid successfully");
        }
        catch(Exception e){
            System.out.println(e);
            out.print("Cannot make payment. Please try again later");
        }
    }

    public void updatePlace(String placeId) throws DataAccessException {
        UpdateQuery updateQuery = new UpdateQueryImpl("Place");
        Criteria criteria = new Criteria(new Column("Place", "PLACE_ID"), placeId, QueryConstants.EQUAL);
        updateQuery.setCriteria(criteria);
        updateQuery.setUpdateColumn("VEHICLE_NUMBER", null);
        updateQuery.setUpdateColumn("STATUS", "Available");
        updateQuery.setUpdateColumn("ENTRY_TIME", 0);
        updateQuery.setUpdateColumn("NAME", null);

        DataAccess.update(updateQuery);
    }
}
