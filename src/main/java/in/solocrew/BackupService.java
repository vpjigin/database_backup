package in.solocrew;

import in.solocrew.utils.Utils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Service
public class BackupService {

    public String backup() {

        String TABLE_NAME = "products";
        try{
            String url = "jdbc:mysql://db-common-do-user-12690151-0.b.db.ondigitalocean.com:25060/peprico?autoReconnect=true&useSSL=false"; // table details
            String username = "doadmin"; // MySQL credentials
            String password = "AVNS_Kna815uCbUzS-ellR_x";

            String query = "show create table "+TABLE_NAME; // query to be run

            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver name
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Established successfully");

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query); // Execute query
            rs.next();
            String tableName = rs.getString(1); // Retrieve name from db
            String tableQuery = rs.getString(2); // Retrieve name from db


            int columnCount = getColumnCount(con,TABLE_NAME);



            String q = "select * from "+TABLE_NAME;
            Statement selectStmt = con.createStatement();
            ResultSet r = selectStmt.executeQuery(q);

            String insertQuery = "insert into "+TABLE_NAME+" values";
            while(r.next())
            {
                insertQuery += "(";
                for(int i = 1;i < columnCount+1; i++){
                    String value = r.getString(i);

                    String qValue = value;
                    if(value != null)
                        qValue = Utils.quote(value);

                    insertQuery += qValue;

                    if(i != columnCount) insertQuery += ",";


//                    System.out.println(value);
                }

                insertQuery += ")";

                if(!r.isLast())
                    insertQuery += ",";


//                System.out.println(rs.getString(2));  //Second Column
//                System.out.println(rs.getString(3));  //Third Column
//                System.out.println(rs.getString(4));  //Fourth Column
            }



            System.out.println(tableName); // Print result on console
            System.out.println(tableQuery); // Print result on console
            System.out.println(insertQuery);


            st.close(); // close statement
            con.close(); // close connection
            System.out.println("Connection Closed....");



            File myObj = new File("/Users/jiginvp/IdeaProjects/DatabaseBackup/filename.txt");
            boolean isCreated = myObj.createNewFile();
            System.out.println(isCreated);

            System.out.println(myObj.getAbsolutePath()+"<-------");


            try {
                FileWriter myWriter = new FileWriter(myObj.getAbsolutePath());
                myWriter.write(insertQuery);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            SendMail.sendMailTo();
            SendMail.sendMailAttachment("/Users/jiginvp/IdeaProjects/DatabaseBackup/filename.txt");

            return tableName+":"+tableQuery;
        }catch (Exception e){
            System.out.println(e.toString());

            return e.toString();
        }
    }

    private int getColumnCount(Connection con,String tableName) {
        String Q = "SELECT count(*) FROM information_schema.columns WHERE table_name = '"+tableName+"'";

        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(Q); // Execute query
            rs.next();
            return rs.getInt(1);
        }catch (Exception e){
            return 0;
        }
    }
}
