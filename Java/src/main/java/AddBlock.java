import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add-block")
public class AddBlock extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        Row row = new Row("FloorRow");

//        row.set("FLOOR_ID", request.getParameter("floorId"));
//        row.set("ROW", request.getParameter("row"));

        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject d = persistence.constructDataObject();
//            d.addRow(row);
//            d = DataAccess.add(d);

            row = new Row("Block");
            row.set("ROW_ID", request.getParameter("rowId"));
            row.set("BLOCK", request.getParameter("block"));

//            d = persistence.constructDataObject();
            d.addRow(row);
            d = DataAccess.add(d);

            JSONObject message = d.getRow("Block").getAsJSON();
            message.put("places", new JSONArray());
//            message.put("row", Integer.parseInt(request.getParameter("row")));
//            message.put("rowId", d.getRow("Block").get("ROW_ID"));

//            JSONObject block = new JSONObject();
//
//            block.put("places", new JSONArray());
//            block.put("block", d.getRow("Block").get("BLOCK"));
//            block.put("block_id", d.getRow("Block").get("BLOCK_ID"));
//
//            JSONArray blocks = new JSONArray();
//            blocks.add(block);
//
//            message.put("blocks", blocks);

            json.put("status", "success");
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
            out.print(json);
        }
    }
}
