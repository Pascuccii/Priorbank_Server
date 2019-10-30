package Entities;


import Connectivity.DatabaseConnection;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User implements Serializable {
    public static final long serialVersionUID = 10L;
    private int id;
    private int accessMode;
    private String username;
    private String password;
    private String email;
    private String theme;
    private String language;

    public User() {
        this.id = 0;
        this.accessMode = 0;
        this.username = "";
        this.password = "";
        this.email = "";
        this.theme = "Dark";
        this.language = "English";
    }

    public User(String username, String password) {
        this.id = 0;
        this.accessMode = 0;
        this.username = username;
        this.password = password;
        this.email = "";
        this.theme = "Dark";
        this.language = "English";
    }

    public User(String user) {
        //25 0 anton anton  Dark English
        String[] vals = user.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.accessMode = Integer.parseInt(vals[1]);
        if (!vals[2].equals("null")) this.username = vals[2];
        if (!vals[3].equals("null")) this.password = vals[3];
        if (!vals[4].equals("null")) this.email = vals[4];
        if (!vals[5].equals("null")) this.theme = vals[5];
        if (!vals[6].equals("null")) this.language = vals[6];
    }

    public User(int id, int accessMode, String username, String password, String email) {
        this.id = id;
        this.accessMode = accessMode;
        this.username = username;
        this.password = password;
        this.email = email;
        this.theme = "Dark";
        this.language = "English";
    }

    public User(int id, int accessMode, String username, String password, String email, String theme, String language) {
        this.id = id;
        this.accessMode = accessMode;
        this.username = username;
        this.password = password;
        this.email = email;
        this.theme = theme;
        this.language = language;
    }

    public void setEMail(String email) {
        this.email = (null == email) ? "" : email;
    }

    //for DB settings
    public void setAccessModeDB(DatabaseConnection conn, int accessMode) throws SQLException {
        this.accessMode = accessMode;
        String prepStat = "UPDATE users SET access_mode = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setInt(1, accessMode);
        preparedStatement.execute();
    }

    public void setUsernameDB(DatabaseConnection conn, String username) throws SQLException {
        this.username = (null == username) ? "" : username;
        if(username==null || username.equals(""))
            username = null;

        String prepStat = "UPDATE users SET name = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setString(1, username);
        preparedStatement.execute();
    }

    public void setPasswordDB(DatabaseConnection conn, String password) throws SQLException {
        this.password = (null == password) ? "" : password;
        if(password==null || password.equals(""))
            password = null;
        String prepStat = "UPDATE users SET password = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setString(1, password);
        preparedStatement.execute();
    }

    public void setEMailDB(DatabaseConnection conn, String email) throws SQLException {
        this.email = (null == email) ? "" : email;
        if(email==null || email.equals(""))
            email = null;
        String prepStat = "UPDATE users SET email = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setString(1, email);
        preparedStatement.execute();
    }

    public void setThemeDB(DatabaseConnection conn, String theme) throws SQLException {
        String prepStat = "UPDATE user_configs SET theme = ? WHERE userId = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        if(theme==null || theme.equals(""))
            theme = null;
        theme = theme.trim();
        theme = theme.toLowerCase();
        switch (theme) {
            case "light":
                this.theme = "Light";
                preparedStatement.setString(1, theme);
                break;
            default:
                this.theme = "Dark";
                preparedStatement.setString(1, theme);
                break;
        }
        preparedStatement.execute();
    }

    public void setLanguageDB(DatabaseConnection conn, String language) throws SQLException {
        if(language==null || language.equals(""))
            language = null;
        String prepStat = "UPDATE user_configs SET language = ? WHERE userId = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);

        language = language.trim();
        language = language.toLowerCase();
        switch (language) {
            case "russian":
                this.language = "Russian";
                preparedStatement.setString(1, language);
                break;
            default:
                this.language = "English";
                preparedStatement.setString(1, language);
                break;
        }
        preparedStatement.execute();
    }

    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM users WHERE Id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(DatabaseConnection conn, String field, String value) throws SQLException {
        switch (field) {
            case "setAccessMode":
                setAccessModeDB(conn, Integer.parseInt(value));
                break;
            case "setUsername":
                setUsernameDB(conn, value);
                break;
            case "setPassword":
                setPasswordDB(conn, value);
                break;
            case "setEmail":
                setEMailDB(conn, value);
                break;
            case "setTheme":
                setThemeDB(conn, value);
                break;
            case "setLanguage":
                setLanguageDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }

    public int getId() {
        return id;
    }

    //for loading only
    public void setId(int id) { //for init load only
        this.id = id;
    }

    public int getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(int accessMode) {
        this.accessMode = accessMode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = (null == username) ? "" : username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = (null == password) ? "" : password;
    }

    public String getEmail() {
        return email;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        theme = theme.trim();
        theme = theme.toLowerCase();
        switch (theme) {
            case "light":
                this.theme = "Light";
                break;
            default:
                this.theme = "Dark";
                break;
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        language = language.trim();
        language = language.toLowerCase();
        switch (language) {
            case "russian":
                this.language = "Russian";
                break;
            default:
                this.language = "English";
                break;
        }
    }

    @Override
    public String toString() {
        return id +
                "|" + accessMode +
                "|" + username +
                "|" + password +
                "|" + email +
                "|" + theme +
                "|" + language;
    }
}
