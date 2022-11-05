import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Persistence;
import com.adventnet.persistence.Row;

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
            out.print("Building added successfully");
        }
        catch(Exception e){
            out.print(e);
        }
    }
}
