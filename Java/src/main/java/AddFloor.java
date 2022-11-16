import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
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

@WebServlet("/add-floor")
public class AddFloor extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        Row row = new Row("Floor");
        row.set("BUILDING_NAME", request.getParameter("buildingName"));
        row.set("FLOOR", request.getParameter("floor"));
        row.set("VEHICLE_TYPE", request.getParameter("vehicleType"));
        Persistence persistence = null;
        try {
            persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = persistence.constructDataObject();
            dataObject.addRow(row);
            System.out.println(dataObject);

            dataObject = DataAccess.add(dataObject);

            System.out.println(dataObject);

            json.put("status", "success");
            JSONObject floor = dataObject.getRow("Floor").getAsJSON();
            floor.put("rows", new JSONArray());
            json.put("message", floor);
        } catch (Exception e) {
            try {
                json.put("status", "fail");
                json.put("message", e);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
        out.print(json);
    }
}
