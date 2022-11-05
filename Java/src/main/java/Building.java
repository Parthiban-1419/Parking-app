import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataAccessException;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@WebServlet("/get-buildings")
public class Building extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        JSONObject json;
        JSONArray jsonArray = new JSONArray(), vehiclePrice = new JSONArray();
        Iterator i, priceIterator;
        Row row ;
        Criteria criteria = null;
        System.out.println(request.getParameter("role") + " :  " + request.getParameter("name"));
        if(request.getParameter("role").equals("owner")){
            criteria = new Criteria(new Column("Building", "OWNER_NAME"), request.getParameter("name"), QueryConstants.EQUAL);
        }

        try {
            DataObject dataObject = DataAccess.get("Building", criteria);
            System.out.println(dataObject);
            i = dataObject.getRows("Building");
            while(i.hasNext()){
                row = (Row) i.next();
                criteria = new Criteria(new Column("VehiclePrice", "BUILDING_NAME"), row.get("BUILDING_NAME"), QueryConstants.EQUAL);
                json = row.getAsJSON();

                priceIterator = DataAccess.get("VehiclePrice", criteria).getRows("VehiclePrice");
                while(priceIterator.hasNext()){
                    vehiclePrice.add(((Row)priceIterator.next()).getAsJSON());
                }
                json.put("vehiclePrices", vehiclePrice);

                jsonArray.add(json);
            }
            out.print(jsonArray);
        } catch (DataAccessException e) {
            out.print(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
