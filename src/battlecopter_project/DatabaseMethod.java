/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlecopter_project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


class DatabaseMethod {
    private final Connection con;
    private Statement state;
    private ResultSet resultSet;
    private PreparedStatement prstate;
    public DatabaseMethod()
    {
        con = ConnectDatabase.ConnectDB();
    }
    private boolean excuteDB(String sql) /* dùng hàm này để thực hiện những chức năng như delete or insert
            trả về true nếu thực hiện được else false*/
    {
        try {
            state = con.createStatement();
            state.execute(sql);
            return true;
        } catch (SQLException ex) {
//            Logger.getLogger(DatabaseMethod.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    private ArrayList selectDb(String sql,String select[]) throws SQLException 
            /* Dùng hàm này để thực hiện chức năng select của database, sql là câu lệnh select
            select[] là mảng tên các trường cần select
            kết quả select được cho vào array theo thứ tự select[]*/
    {
        ArrayList array = new ArrayList();
        prstate = con.prepareStatement(sql);
        resultSet = prstate.executeQuery();
        while(resultSet.next())
        {
            for(int i = 0 ;i < select.length;i++)
            {
                if(resultSet.getString(select[i])==null)
                    array.add("");
                else
                    array.add(resultSet.getString(select[i]));
            }
        }
        return array;
    }
    
    public boolean SignUp(String UserName,String Password, String Email)
    {
        String sql ="insert into User(UserName,Password,Email,win,lost) values(\""+UserName+"\","
                + "\""+Password+"\",\""+Email+"\",0,0)";
        return excuteDB(sql);
    }
    
    public boolean Login(String UserName, String Password)
    {
        try {
            String sql = "select * from User where UserName =\""+UserName+"\" and Password = \""+Password+"\"";
            String cot[] = {"UserName","Password"};
            ArrayList<String> ar = selectDb(sql, cot);
            if(ar.size()==2)
                if(ar.get(0).compareTo(UserName)==0)
                    if(ar.get(1).compareTo(Password)==0)
                        return true;
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }
}
