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

    //for loading only
    public void setId(int id) { //for init load only
        this.id = id;
    }
    public void setAccessMode(int accessMode) {
        this.accessMode = accessMode;
    }
    public void setUsername(String username) {
        this.username = (null == username) ? "" : username;
    }
    public void setPassword(String password) {
        this.password = (null == password) ? "" : password;
    }
    public void setEMail(String email) {
        this.email = (null == email) ? "" : email;
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

    //for DB settings
    public void setAccessModeDB(DatabaseConnection conn, int accessMode) throws SQLException {
        this.accessMode = accessMode;
        String prepStat = "UPDATE users SET id = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
    }
    public void setUsernameDB(DatabaseConnection conn, String username) throws SQLException {
        this.username = (null == username) ? "" : username;
        String prepStat = "UPDATE users SET name = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setString(1, username);
        preparedStatement.execute();
    }
    public void setPasswordDB(DatabaseConnection conn, String password) throws SQLException {
        this.password = (null == password) ? "" : password;
        String prepStat = "UPDATE users SET password = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
        preparedStatement.setInt(2, this.id);
        preparedStatement.setString(1, password);
        preparedStatement.execute();
    }
    public void setEMailDB(DatabaseConnection conn, String email) throws SQLException {
        this.email = (null == email) ? "" : email;
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


    public int getId() {
        return id;
    }
    public int getAccessMode() {
        return accessMode;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public String getTheme() {
        return theme;
    }
    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Entities.User{" +
                "id=" + id +
                ", accessMode=" + accessMode +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", theme='" + theme + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
