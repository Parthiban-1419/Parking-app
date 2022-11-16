import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add-building")
public class AddBuilding extends HttpServlet {
    PrintWriter out;
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();

        Row row = new Row("Building");
        row.set("BUILDING_NAME", request.getParameter("buildingName"));
        row.set("OWNER_NAME", request.getParameter("name"));
        row.set("STREET_ADDRESS", request.getParameter("streetAddress"));
        row.set("AREA", request.getParameter("area"));
        row.set("PINCODE", request.getParameter("pincode"));
        row.set("PHONE_NUMBER", request.getParameter("phoneNumber"));
        row.set("DESCRIPTION", request.getParameter("description"));

        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = persistence.constructDataObject();
            dataObject.addRow(row);
            DataAccess.add(dataObject);

            addVehiclePrices(request.getParameter("buildingName"), request.getParameter("vehiclePrices"));

            out.print("Building added successfully");
        }
        catch(Exception e){
            out.print(e);
        }
    }

    public void addVehiclePrices(String buildingName, String vehicles) throws Exception {
        Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
        DataObject dataObject ;
        Row row ;

        System.out.println(vehicles);

        JSONParser parser = new JSONParser();
        JSONArray jArray ;
        JSONObject vehicle ;

        jArray = (JSONArray) parser.parse(vehicles);

        System.out.println(jArray);

        for(Object object: jArray){
            dataObject = persistence.constructDataObject();
            row = new Row("VehiclePrice");
            row.set("BUILDING_NAME", buildingName);
            vehicle = (JSONObject) object;
            row.set("VEHICLE_TYPE", vehicle.get("vehicle_type"));
            row.set("PRICE_PER_HOUR", vehicle.get("price_per_hour"));

            dataObject.addRow(row);
            System.out.println(row);
            System.out.println(dataObject);
            DataAccess.add(dataObject);
        }
    }
}
