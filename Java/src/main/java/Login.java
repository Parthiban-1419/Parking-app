import com.adventnet.ds.query.Column;
import com.adventnet.ds.query.Criteria;
import com.adventnet.ds.query.QueryConstants;
import com.adventnet.persistence.DataAccess;
import com.adventnet.persistence.DataObject;
import com.adventnet.persistence.Row;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/log-in")
public class Login extends HttpServlet {

//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        PrintWriter out = response.getWriter();
//        String loginName = request.getParameter("loginName"), password = request.getParameter("password");
//
//        Criteria criteria = new Criteria(new Column("AaaLogin", "LOGIN_ID"), loginName, QueryConstants.EQUAL);
//        Persistence per;
//        JSONObject json = new JSONObject(), jMessage = new JSONObject();
//
//        try {
//            per = (Persistence) BeanUtil.lookup("Persistence");
//            out.println("name : " + loginName);
//
//            DataObject d = DataAccess.get("AaaLogin", criteria);
//            out.println("contains : " + d.containsTable("AaaLogin") + " emp : " + d.isEmpty());
//            out.println(d);
//            d = per.getForPersonality("AccountDOForSSO", criteria);
//            out.println(d);
//
//            if (d.isEmpty()) {
//                json.put("status", "fail");
//                json.put("message", "No account found on this login name");
//            } else {
//                json.put("status", "success");
//                jMessage.put("loginName", loginName);
//                json.put("message", jMessage);
//            }
//            out.print(d.getRow("AaaRole").get("NAME"));
//        } catch (Exception e) {
//            try {
//                json.put("status", "fail");
//                json.put("message", "Error. Something went wrong");
//            } catch (JSONException ex) {
//                throw new RuntimeException(ex);
//            }
//            out.print(e);
//        }
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        Criteria criteria = new Criteria(new Column("Account", "NAME"),name, QueryConstants.EQUAL);
        JSONObject json = new JSONObject(), jMessage = new JSONObject();

        try {
            DataObject dObject = DataAccess.get("Account", criteria);
            Row row = dObject.getRow("Account");
            if(row.get("PASSWORD").equals(request.getParameter("password"))){
                json.put("status", "success");
                jMessage.put("loginName", name);
                jMessage.put("firstName", row.get("FIRST_NAME"));
                jMessage.put("middleName", row.get("MIDDLE_NAME"));
                jMessage.put("lastName", row.get("LAST_NAME"));
                jMessage.put("role", row.get("ROLE"));
                json.put("message", jMessage);
            }
            else{
                json.put("status", "fail");
                json.put("message", "Wrong password");
            }

            out.print(json);
        }
        catch(NullPointerException e){
            try {
                json.put("status", "fail");
                json.put("message", "No account found on this login name");
                out.print(json);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            try {
                json.put("status", "fail");
                json.put("message", "Error. Something went wrong");
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
