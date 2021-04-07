package me.trent;

import java.sql.*;
import java.util.List;

/**
 * Created by trent on 6/1/2017.
 */
public class DB {

    private String ip;
    private String db;
    private String user;
    private String pass;
    private boolean started = false;

    public DB(String ip, String db, String user, String pass) {
        this.ip = ip;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    public Connection connection = null;

    public boolean start() {
        try {
            //   connection = DriverManager.getConnection("jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pass);

            connection = DriverManager.getConnection("jdbc:mysql://" + ip + "/" + db + "?user=" + user + "&password=" + pass);
            System.out.print("\n\n\n\n CONNECTED TO DATABASE \n\n\n\n");

            started = true;
            return true;

        }catch(SQLException e){
            e.printStackTrace();
            System.out.print("\n\n\n");
        }
        return false;
    }

    public boolean isStarted() {
        return started;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isColumn(String table, String column){
        boolean a = false;
        ResultSet r;
        try {
            DatabaseMetaData m = connection.getMetaData();
            r = m.getColumns(null, null, table, column);
            if (!r.next()){
                a = false;
            }else{
                a = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return a;
    }
    public boolean isTable(String table){
        boolean a = false;
        ResultSet r;
        try {
            DatabaseMetaData m = connection.getMetaData();
            r = m.getTables(null, null, table, null);
            if (!r.next()){
                a = false;
            }else{
                a = true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return a;
    }

    public String[] player_data_columns = {"uuid", ""};


    public String[] tables = {"programs"};

    public void addTable(String tableName, String defaultColumn, int amount){
        PreparedStatement s = preparedStatement("CREATE TABLE "+tableName+" ("+defaultColumn+" VARCHAR("+amount+") NOT NULL)");
        update(s);
    }

    public void addColumn(String tablename, String column, String type){
        PreparedStatement s = preparedStatement("ALTER TABLE "+tablename+" add "+column+" "+type+" NOT NULL");
        update(s);
    }

    public PreparedStatement preparedStatement(String query) {
        PreparedStatement s = null;
        try {
            s = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
    public boolean checkResultSet(String query, String lookfor){
        ResultSet s;
        boolean a = false;
        try{
            s = preparedStatement(query).executeQuery(query);
            while (s.next()) {
                if (s.getString(lookfor) != null) {
                    a = true;
                }else{
                    a = false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return a;
    }
    public String readResultSet(String query){
        String returned = "";
        ResultSet s;
        try{
            s = preparedStatement(query).executeQuery(query);
            while(s.next()){
                returned = s.getString(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
            returned = "";
        }
        return returned;
    }
    public int readResultSetInt(String query){
        int returned = 0;
        ResultSet s;
        try{
            s = preparedStatement(query).executeQuery(query);
            while(s.next()){
                returned = s.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return returned;
    }
    public double readResultSetDouble(String query){
        double returned = 0;
        ResultSet s;
        try{
            s = preparedStatement(query).executeQuery(query);
            while(s.next()){
                returned = s.getDouble(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return returned;
    }

    public void update(PreparedStatement statement) {
        try {
            try {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {}
        } finally {
            try {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {}
        }
    }

    public void removeRow(String table, String column, String where_equals){
        try {
            PreparedStatement s;
            s = preparedStatement("DELETE FROM " + table + " WHERE " + column + " = ?");
            s.setString(1, where_equals);
            s.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void setColumn(String table, String column, String value, String where, String where_equals){
        PreparedStatement s;
        if (where_equals == null && where == null){
            //  s = preparedStatement("UPDATE "+table+" SET "+column+"=?");
            s = preparedStatement("INSERT IGNORE INTO "+table+" ("+column+") VALUES (?) ON DUPLICATE KEY UPDATE "+column+"=?");
        }else{
            s = preparedStatement("UPDATE "+table+" SET "+column+"=? WHERE "+where+"='"+where_equals+"'");
        }
        try {
            s.setString(1, value);
            if (where_equals == null && where == null){
                s.setString(2, value);
            }
            update(s);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void setColumnForList(String table, String column, List<String> value, String where, String where_equals){
        PreparedStatement s;
        if (where_equals == null && where == null){
            //  s = preparedStatement("UPDATE "+table+" SET "+column+"=?");
            s = preparedStatement("INSERT IGNORE INTO "+table+" ("+column+") VALUES (?) ON DUPLICATE KEY UPDATE "+column+"=?");
        }else{
            s = preparedStatement("UPDATE "+table+" SET "+column+"=? WHERE "+where+"='"+where_equals+"'");
        }
        try {
            String goodValue = "";
            for (String strings : value){
                if (goodValue.equalsIgnoreCase("")){
                    //is blank, new place
                    goodValue = strings;
                }else{
                    //is not blank, keep adding them with commas
                    goodValue = goodValue+","+strings;
                }
            }
            s.setString(1, goodValue);
            if (where_equals == null && where == null){
                s.setString(2, goodValue);
            }
            update(s);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void setColumn(String table, String column, int value, String where, String where_equals){
        PreparedStatement s;
        if (where_equals == null && where == null){
            //  s = preparedStatement("UPDATE "+table+" SET "+column+"=?");
            s = preparedStatement("INSERT IGNORE INTO "+table+" ("+column+") VALUES (?) ON DUPLICATE KEY UPDATE "+column+"=?");
        }else{
            s = preparedStatement("UPDATE "+table+" SET "+column+"=? WHERE "+where+"='"+where_equals+"'");
        }
        try {
            s.setInt(1, value);
            if (where_equals == null && where == null){
                s.setInt(2, value);
            }
            update(s);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public String getDb() {
        return db;
    }

    public String getIp() {
        return ip;
    }

    public String getPass() {
        return pass;
    }

    public String getUser() {
        return user;
    }
}