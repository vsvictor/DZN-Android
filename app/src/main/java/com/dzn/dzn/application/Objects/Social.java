package com.dzn.dzn.application.Objects;

/**
 * Created by victor on 09.05.16.
 */
public class Social {
    private int ID;
    private String name;
    private String shortName;
    private String login;
    private String password;

    public Social(){
        ID = -1;
        name = "";
        shortName = "";
        login = "";
        password = "";
    }
    public void setID(int ID){this.ID = ID;}
    public int getID(){return this.ID;}
    public void setName(String name){this.name = name;}
    public String getName(){return this.name;}

    public void setShortName(String shortName){this.shortName = shortName;}
    public String getShortName(){return this.shortName;}

    public void setLogin(String login){this.login = login;}
    public String getLogin(){return this.login;}

    public void setPassword(String password){this.password = password;}
    public String getPassword(){return this.password;}

    public boolean isLinked(){
        boolean isnull = (login == null) && (password != null);
        if(isnull) return false;
        boolean isempty = login.isEmpty() && password.isEmpty();
        if(isempty) return false;
        return true;
    }
}
