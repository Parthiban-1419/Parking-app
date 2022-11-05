import com.adventnet.mfw.bean.BeanUtil;
import com.adventnet.persistence.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/create-account")
public class SignUp extends HttpServlet {
    PrintWriter out;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();
        String loginName = request.getParameter("logInName"), firstName = request.getParameter("firstName"), middleName = request.getParameter("middleName"), lastName = request.getParameter("lastName");
        String password = request.getParameter("password"), role = request.getParameter("role");

        Row row = new Row("Account");
        row.set("NAME", loginName);
        row.set("FIRST_NAME", firstName);
        row.set("MIDDLE_NAME", middleName);
        row.set("LAST_NAME", lastName);
        row.set("PASSWORD", password);
        row.set("ROLE", role);


        try {
            Persistence persistence = (Persistence) BeanUtil.lookup("Persistence");
            DataObject d = persistence.constructDataObject();
            d.addRow(row);
            DataAccess.add(d);
            out.print("Account created successfully...");
        }
        catch(DataAccessException e){
            out.print("Cannot create account. Login name already exists");
        }
        catch(Exception e){
            out.print(e);
        }
    }
}