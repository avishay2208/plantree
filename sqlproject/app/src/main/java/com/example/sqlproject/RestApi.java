package com.example.sqlproject;

public class RestApi {

    static ServerPostCommunication serverPostCommunication = new ServerPostCommunication();

    public static String sqlCommand(String sqlCommand) {
        return  serverPostCommunication.doInBackground("function=sqlCommand", "sqlCommand="+sqlCommand);
    }
}
