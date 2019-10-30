package Connectivity;

import Entities.Client;
import Entities.User;
import enums.Disability;
import enums.MaritalStatus;
import enums.Retiree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;
import java.util.ArrayList;

public class Server implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final DatabaseConnection connDB;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<Client> clientsData = FXCollections.observableArrayList();

    private Server() {
        System.out.println("Server's running...");
        connDB =
                new DatabaseConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&useSSL=true&useJDBCCompliantTimezoneShift=true" +
                        "&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow", "root", "root");
        try {
            initUsersData();
            initClientsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println(value);
        if (value.equals("init")) {
            try {
                initUsersData();
                initClientsData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tcpConnection.sendString(usersData.size() + " USERS:");
            for (User u : usersData)
                tcpConnection.sendString(u.toString());

            tcpConnection.sendString(clientsData.size() + " CLIENTS:");
            for (Client c : clientsData)
                tcpConnection.sendString(c.toString());
            tcpConnection.sendString("END");
        }
        String[] vals = value.split("#");
        if (vals[0].equals("Client")) {
            for (Client c : clientsData)
                if (c.getId() == Integer.parseInt(vals[2])) {
                    c.set(connDB, vals[1], vals[3]);
                    break;
                }
        }
        if (vals[0].equals("User")) {
            for (User u : usersData)
                if (u.getId() == Integer.parseInt(vals[2])) {
                    try {
                        u.set(connDB, vals[1], vals[3]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
        }
        if (vals[0].equals("addClient")) {
            System.out.println(value.substring(10));
            addClient(value.substring(10));
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (TCPConnection c : connections) c.sendString(value);
    }

    private void initUsersData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            Statement statement2 = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT  * FROM users");
            usersData.clear();

            System.out.println();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setAccessMode(resultSet.getInt("access_mode"));
                user.setUsername(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setEMail(resultSet.getString("email"));
                ResultSet resultSetConfigs = statement2.executeQuery("SELECT * FROM user_configs");
                while (resultSetConfigs.next())
                    if (resultSetConfigs.getInt("userId") == user.getId()) {
                        user.setTheme(resultSetConfigs.getString("theme"));
                        user.setLanguage(resultSetConfigs.getString("language"));
                    }
                usersData.add(user);
                System.out.println(user);
            }
        }
    }

    private void initClientsData() throws SQLException {
        if (connDB.isConnected()) {
            Statement statement = connDB.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
            clientsData.clear();

            System.out.println();
            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt("Id"));
                client.setName(resultSet.getString("Name"));
                client.setSurname(resultSet.getString("Surname"));
                client.setPatronymic(resultSet.getString("Patronymic"));
                client.setBirthDate(resultSet.getDate("Birth_date").toString());
                client.setPassportSeries(resultSet.getString("Passport_series"));
                client.setPassportNumber(resultSet.getString("Passport_number"));
                client.setIssuedBy(resultSet.getString("Issued_by"));
                client.setIssuedDate(resultSet.getDate("Issued_date").toString());
                client.setBirthPlace(resultSet.getString("Birth_place"));
                client.setActualResidenceCity(resultSet.getString("Actual_residence_city"));
                client.setActualResidenceAddress(resultSet.getString("Actual_residence_address"));
                client.setHomeNumber(resultSet.getString("Home_number"));
                client.setMobileNumber(resultSet.getString("Mobile_number"));
                client.setEmail(resultSet.getString("Email"));
                client.setJob(resultSet.getString("Job"));
                client.setPosition(resultSet.getString("Position"));
                client.setRegistrationCity(resultSet.getString("Registration_city"));
                client.setMaritalStatus(MaritalStatus.valueOf(resultSet.getString("Marital_status")));
                client.setCitizenship(resultSet.getString("Citizenship"));
                client.setDisability(Disability.valueOf(resultSet.getString("Disability")));
                client.setRetiree(Retiree.valueOf(resultSet.getString("Is_retiree")));
                client.setMonthlyIncome(resultSet.getString("Monthly_income"));
                client.setIdNumber(resultSet.getString("Id_number"));
                clientsData.add(client);
                System.out.println(client);
            }
        }

    }

    private void addClient(String value) {
        try {
            Client toAdd = new Client(value);
            System.out.println(toAdd);
            String prepStat =
                    "INSERT INTO `test`.`clients` (`Name`, `Surname`, `Patronymic`, `Birth_date`, `Passport_series`, `Passport_number`," +
                            "                              `Issued_by`, `Issued_date`, `Birth_place`, `Actual_residence_city`," +
                            "                              `Actual_residence_address`, `Home_number`, `Mobile_number`, `Email`, `Job`, `Position`," +
                            "                              `Registration_city`, `Disability`, `Marital_status`, `Citizenship`, `Is_retiree`," +
                            "                              `Monthly_income`, `Id_number`)" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, toAdd.getName());
            preparedStatement.setString(2, toAdd.getSurname());
            preparedStatement.setString(3, toAdd.getPatronymic());
            preparedStatement.setDate(4, Date.valueOf(toAdd.getBirthDate()));
            preparedStatement.setString(5, toAdd.getPassportSeries());
            preparedStatement.setString(6, toAdd.getPassportNumber());
            preparedStatement.setString(7, toAdd.getIssuedBy());
            preparedStatement.setDate(8, Date.valueOf(toAdd.getIssuedDate()));
            preparedStatement.setString(9, toAdd.getBirthPlace());
            preparedStatement.setString(10, toAdd.getActualResidenceCity());
            preparedStatement.setString(11, toAdd.getActualResidenceAddress());
            preparedStatement.setString(12, toAdd.getHomeNumber());
            preparedStatement.setString(13, toAdd.getMobileNumber());
            preparedStatement.setString(14, toAdd.getEmail());
            preparedStatement.setString(15, toAdd.getJob());
            preparedStatement.setString(16, toAdd.getPosition());
            preparedStatement.setString(17, toAdd.getRegistrationCity());
            preparedStatement.setString(18, toAdd.getDisability());
            System.out.println(toAdd.getMonthlyIncome());
            preparedStatement.setString(19, toAdd.getMaritalStatus());
            preparedStatement.setString(20, toAdd.getCitizenship());
            preparedStatement.setString(21, toAdd.getRetiree());
            preparedStatement.setString(22, toAdd.getMonthlyIncome());
            preparedStatement.setString(23, toAdd.getIdNumber());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
