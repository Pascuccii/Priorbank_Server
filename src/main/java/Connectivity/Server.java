package Connectivity;

import Entities.Client;
import Entities.User;
import enums.Disability;
import enums.MaritalStatus;
import enums.Retiree;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;
import java.time.Instant;
import java.time.Month;
import java.time.ZonedDateTime;

import static java.lang.Thread.sleep;

public class Server extends Application implements TCPConnectionListener {

    private volatile static String log = "";
    private volatile static ObservableList<TCPConnection> connections = FXCollections.observableArrayList();
    private final DatabaseConnection connDB;
    private double xOffset = 0;
    private double yOffset = 0;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<Client> clientsData = FXCollections.observableArrayList();

    @FXML
    private AnchorPane primaryAnchorPane;

    @FXML
    private AnchorPane title;

    @FXML
    private Button hideButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button exitButton;

    @FXML
    private AnchorPane workPane;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextArea textAreaLog = new TextArea();

    @FXML
    private MenuButton serverOnOffMenuButton;
    @FXML
    private MenuItem serverOnMenuItem;
    @FXML
    private MenuItem serverOffMenuItem;

    @FXML
    private Button languageButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button clearButton;

    @FXML
    private TableView<TCPConnection> connectionsTable;
    private Label emptyLabel;

    @FXML
    private TableColumn<TCPConnection, String> ipColumn;
    @FXML
    private TableColumn<TCPConnection, String> portColumn;
    @FXML
    private TableColumn<TCPConnection, String> userColumn;

    @FXML
    private Label serverLabel;
    private String currentLanguage;

    public Server() {
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
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    void initialize() {
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        textAreaLog.getStyleClass().add("textAreaLog");
        mainPane.getStyleClass().add("mainPane");
        title.getStyleClass().add("title");
        workPane.getStyleClass().add("workPane");
        primaryAnchorPane.getStylesheets().add("CSS/DarkTheme.css");
        primaryAnchorPane.setVisible(true);
        hideButton.getStyleClass().add("hideButton");
        minimizeButton.getStyleClass().add("minimizeButton");
        exitButton.getStyleClass().add("exitButton");
        hideButton.setOnAction(actionEvent -> {
            Stage stage2 = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage2.setIconified(true);
        });
        minimizeButton.setOnAction(actionEvent -> minimize());
        exitButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            System.exit(0);
        });
        languageButton.setOnAction(event -> translate());
        emptyLabel = new Label("No connections");
        serverOnMenuItem.setOnAction(event -> serverOnOffMenuButton.setText(serverOnMenuItem.getText()));
        serverOffMenuItem.setOnAction(event -> serverOnOffMenuButton.setText(serverOffMenuItem.getText()));
        connectionsTable.setPlaceholder(emptyLabel);
        connectionsTable.setItems(connections);
        new Thread(() -> {
            while (true) {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateLog();
            }
        }).start();
        currentLanguage = "English";
        translate();
    }

    public void translate() {
        if (currentLanguage.equals("English")) {
            currentLanguage = "Russian";
            Platform.runLater(() -> {
                saveButton.setText("Сохранить");
                clearButton.setText("Отчистить");
                serverLabel.setText("Сервер:");
                userColumn.setText("Пользователь");
                portColumn.setText("Порт");
                emptyLabel.setText("Нет подключений");
                serverOnMenuItem.setText("ВКЛ");
                serverOffMenuItem.setText("ВЫКЛ");
                if (serverOnOffMenuButton.getText().equals("ON"))
                    serverOnOffMenuButton.setText("ВКЛ");
                else
                    serverOnOffMenuButton.setText("ВЫКЛ");
            });

        } else {
            currentLanguage = "English";
            Platform.runLater(() -> {
                saveButton.setText("Save log");
                clearButton.setText("Clear log");
                serverLabel.setText("Server:");
                userColumn.setText("Current user");
                portColumn.setText("Port");
                emptyLabel.setText("No connections");
                serverOnMenuItem.setText("ON");
                serverOffMenuItem.setText("OFF");
                if (serverOnOffMenuButton.getText().equals("ВКЛ"))
                    serverOnOffMenuButton.setText("ON");
                else
                    serverOnOffMenuButton.setText("OFF");
            });
        }
        connectionsTable.refresh();
    }

    public void listen() {
        System.out.println("listening");
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            while (true) {
                try {
                    connections.add(new TCPConnection(this, serverSocket.accept()));
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        Platform.runLater(() -> {
            ZonedDateTime zdt = ZonedDateTime.now();
            String year = String.valueOf(zdt.getYear());
            String month = String.valueOf(zdt.getMonthValue());
            String day = String.valueOf(zdt.getDayOfMonth());
            String hour = String.valueOf(zdt.getHour());
            String minute = String.valueOf(zdt.getMinute());
            String second = String.valueOf(zdt.getSecond());

            log += year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second + " " + tcpConnection + " CONNECTED\n";
            System.out.println(log);
        });
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        ZonedDateTime zdt = ZonedDateTime.now();
        String year = String.valueOf(zdt.getYear());
        String month = String.valueOf(zdt.getMonthValue());
        String day = String.valueOf(zdt.getDayOfMonth());
        String hour = String.valueOf(zdt.getHour());
        String minute = String.valueOf(zdt.getMinute());
        String second = String.valueOf(zdt.getSecond());

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
        } else {
            String[] vals = value.split("\\|");
            switch (vals[0]) {
                case "Client":

                    log(tcpConnection, value);
                    for (Client c : clientsData)
                        if (c.getId() == Integer.parseInt(vals[2])) {
                            c.set(connDB, vals[1], vals[3]);
                            break;
                        }
                    break;
                case "User":

                    log(tcpConnection, value);
                    for (User u : usersData)
                        if (u.getId() == Integer.parseInt(vals[2])) {
                            try {
                                u.set(connDB, vals[1], vals[3]);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    break;
                case "addUser":
                    log(tcpConnection, value);
                    System.out.println(value.substring(8));
                    addUser(value.substring(8));
                    break;
                case "addClient":
                    log(tcpConnection, value);
                    System.out.println(value.substring(10));
                    addClient(value.substring(10));
                    break;
                case "changeAccountData":
                    log(tcpConnection, value);
                    System.out.println(value.substring(18));
                    changeAccountData(vals[1], vals[2], vals[3], vals[4]);
                    break;
                case "deleteAllUsers":
                    log(tcpConnection, value);
                    deleteAllUsers();
                    break;
                case "setCurrentUser":
                    System.out.println(value.substring(15));
                    if (vals[1].equals("null"))
                        log += year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second + " " + tcpConnection + " \'" + tcpConnection.getUsername() + "\' logged out\n";
                    else
                        log += year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second + " " + tcpConnection + " \'" + vals[1] + "\' logged in\n";
                    tcpConnection.setUsername(vals[1]);


                    break;
                default:
                    log(tcpConnection, value);
            }
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        ZonedDateTime zdt = ZonedDateTime.now();
        String year = String.valueOf(zdt.getYear());
        String month = String.valueOf(zdt.getMonthValue());
        String day = String.valueOf(zdt.getDayOfMonth());
        String hour = String.valueOf(zdt.getHour());
        String minute = String.valueOf(zdt.getMinute());
        String second = String.valueOf(zdt.getSecond());
        Platform.runLater(() -> {
            log += year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second + " " + tcpConnection + " DISCONNECTED\n";
            System.out.println(log);
        });
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
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

    private void addUser(String value) {
        User u = new User(value);
        try {
            String prepStat =
                    "INSERT INTO `test`.`users` (`name`, `password`, `email`,`access_mode`) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, u.getUsername());
            preparedStatement.setString(2, u.getPassword());
            preparedStatement.setString(3, u.getEmail());
            preparedStatement.setInt(4, u.getAccessMode());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllUsers() {
        try {
            String prepStat = "DELETE FROM `test`.`users` WHERE (`id` > -1)";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeAccountData(String id, String username, String password, String email) {
        try {
            if(email.equals("null"))
                email = null;
            String prepStat = "UPDATE `test`.`users` SET `name` = ?, `password` = ?, `email` = ? WHERE (`id` = ?);";
            PreparedStatement preparedStatement = connDB.getConnection().prepareStatement(prepStat);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, Integer.parseInt(id));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting...");
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainWindow.fxml"));
        primaryStage.setTitle("Main");
        Scene scene = new Scene(root, 800, 500, Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.getIcons().add(new Image("assets/server-icon.png"));
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            primaryStage.setX(mouseEvent.getScreenX() - xOffset);
            primaryStage.setY(mouseEvent.getScreenY() - yOffset);
        });
        primaryStage.setMaximized(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        new Thread(() -> {
            new Server().listen();
        }).start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public void updateLog() {
        connectionsTable.refresh();
        if (!textAreaLog.getText().equals(log)) {
            textAreaLog.setText(log);
            textAreaLog.setScrollTop(Double.MAX_VALUE);
        }
    }

    public void log(TCPConnection tcp, String value) {
        ZonedDateTime zdt = ZonedDateTime.now();
        String year = String.valueOf(zdt.getYear());
        String month = String.valueOf(zdt.getMonthValue());
        String day = String.valueOf(zdt.getDayOfMonth());
        String hour = String.valueOf(zdt.getHour());
        String minute = String.valueOf(zdt.getMinute());
        String second = String.valueOf(zdt.getSecond());

        log += year + "/" + month + "/" + day + "-" + hour + ":" + minute + ":" + second + " " + tcp + " " + tcp.getUsername() + ": " + value + "\n";
    }

    private void minimize() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        if (stage.isMaximized()) {
            stage.setMaximized(false);
            minimizeButton.setStyle("-fx-background-image: url(assets/expand-white.png)");

        } else {
            stage.setMaximized(true);
            minimizeButton.setStyle("-fx-background-image: url(assets/minimize-white.png)");
        }
    }
    //TODO: currentUser
}
