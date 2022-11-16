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

@WebServlet("/add-row")
public class AddRow extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject(), message;
        Row row = new Row("FloorRow");

        row.set("FLOOR_ID", request.getParameter("floorId"));
        row.set("ROW", request.getParameter("row"));

        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject d = persistence.constructDataObject();
            d.addRow(row);
            d = DataAccess.add(d);

            json.put("status", "success");

            message = d.getRow("FloorRow").getAsJSON();
            message.put("blocks", new JSONArray());

            json.put("message", message);

            out.print(json);
        }
        catch(Exception e){
            try {
                json.put("status", "fail");
                json.put("message", e);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
