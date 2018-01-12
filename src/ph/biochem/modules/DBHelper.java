package ph.biochem.modules;

import java.sql.*;
public class DBHelper {
    private static String databaseName = "biochemdb.db";
    private static String path = "src/ph/biochem/database/";
    private static ResultSet data;
    private static Connection connection = null;
    private static boolean debugMode = true;
    //private static boolean isNextCalled = false;

    public static void connectToDatabase(){
        try{
            String url = "jdbc:sqlite:" + path + databaseName;
            connection = DriverManager.getConnection(url);
        }

        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
            }
        }
    }

    public static void closeConnection(){
        try{
            if(connection != null){
                connection.close();
            }
        }
        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }

    public static void executeQuery(String strQuery, String[] parameters, StatementType statementType){
        try{
           PreparedStatement statement = connection.prepareStatement(strQuery);

           int paramatersLength = parameters.length;
           for(int index=0; index<paramatersLength; index++){
                statement.setString(index+1, parameters[index]);
           }

           if(statementType == StatementType.SELECT){
               data = statement.executeQuery();
//               isNextCalled = false;
           }

           else{
               statement.executeUpdate();
           }
        }
        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }

    public static void executeQuery(String strQuery){
        try{
            Statement statement = connection.createStatement();
            data = statement.executeQuery(strQuery);
        }

        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }
        }
    }

    public static int getLastInsertedID(){
        try{
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT last_insert_rowid()").getInt("last_insert_rowid()");
        }
        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }

            return -1;
        }
    }

    public static boolean hasNextData(){
        try{
//            isNextCalled = true;
            return data.next();
        }

        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }

            return false;
        }
    }

    public static int getIntData(String colName){
        try{
/*            if(!isNextCalled){
                data.next();
            }*/
            return data.getInt(colName);
        }

        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }

            return -1;
        }
    }

    public static String getStrData(String colName){
        try{
            /*if(!isNextCalled){
                data.next();
            }*/
            return data.getString(colName);
        }

        catch(SQLException e){
            if(debugMode){
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
            }

            return null;
        }
    }

    public static void next(){
        try{
            data.next();
        }
        catch(SQLException e){
            if(debugMode){
                System.out.println("fuk");
            }
        }
    }
}
