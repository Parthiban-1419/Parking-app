import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add-place")
public class AddPlace extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        Row row = new Row("Place");
        row.set("BLOCK_ID", request.getParameter("blockId"));
        row.set("PLACE", request.getParameter("place"));

        try{
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject dataObject = persistence.constructDataObject();
            dataObject.addRow(row);

            dataObject = DataAccess.add(dataObject);

            json.put("status", "success");
            json.put("message" , dataObject.getRow("Place").getAsJSON());
        }
        catch(Exception e){
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
