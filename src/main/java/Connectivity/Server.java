package Connectivity;

import Entities.Client;
import Entities.User;
import enums.Disability;
import enums.MaritalStatus;
import enums.Retiree;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Server extends Application implements TCPConnectionListener {

    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final DatabaseConnection connDB;
    private double xOffset = 0;
    private double yOffset = 0;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<Client> clientsData = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private AnchorPane leftAnchorPane;

    @FXML
    private FlowPane menuAdmin;

    @FXML
    private Label currentUserLabelAdmin;

    @FXML
    private Button menuAdminButton1;

    @FXML
    private Button menuAdminButton2;

    @FXML
    private Button menuAdminButton3;

    @FXML
    private Button menuAdminButton4;

    @FXML
    private Button logoutButtonAdmin;

    @FXML
    private FlowPane menuUser;

    @FXML
    private Label currentUserLabelUser;

    @FXML
    private Button menuUserButton1;

    @FXML
    private Button menuUserButton2;

    @FXML
    private Button menuUserButton3;

    @FXML
    private Button menuUserButton4;

    @FXML
    private Button logoutButtonUser;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private AnchorPane menuPane1;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<?> usersTable;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> accessModeColumn;

    @FXML
    private TableColumn<?, ?> usernameColumn;

    @FXML
    private TableColumn<?, ?> passwordColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private MenuButton criteriaButton;

    @FXML
    private MenuItem criteriaMenuItem_Id;

    @FXML
    private MenuItem criteriaMenuItem_Access;

    @FXML
    private MenuItem criteriaMenuItem_Username;

    @FXML
    private MenuItem criteriaMenuItem_Password;

    @FXML
    private MenuItem criteriaMenuItem_Email;

    @FXML
    private Button searchButton;

    @FXML
    private Button resetSearchButton;

    @FXML
    private ImageView fixImage;

    @FXML
    private AnchorPane createUser_AnchorPane;

    @FXML
    private TextField createUser_AnchorPane_Username;

    @FXML
    private TextField createUser_AnchorPane_Password;

    @FXML
    private TextField createUser_AnchorPane_Email;

    @FXML
    private Button createUserButton;

    @FXML
    private MenuButton createUser_AnchorPane_AccessMode_MenuButton;

    @FXML
    private MenuItem createUser_AccessMenuItem_User;

    @FXML
    private MenuItem createUser_AccessMenuItem_Admin;

    @FXML
    private AnchorPane changeUser_AnchorPane;

    @FXML
    private TextField changeUser_AnchorPane_Id;

    @FXML
    private TextField changeUser_AnchorPane_Username;

    @FXML
    private TextField changeUser_AnchorPane_Password;

    @FXML
    private TextField changeUser_AnchorPane_Email;

    @FXML
    private Button changeUser_AnchorPane_IdSubmitButton;

    @FXML
    private Button changeUserButton;

    @FXML
    private MenuButton changeUser_AnchorPane_AccessMode_MenuButton;

    @FXML
    private MenuItem changeUser_AccessMenuItem_User;

    @FXML
    private MenuItem changeUser_AccessMenuItem_Admin;

    @FXML
    private AnchorPane deleteUser_AnchorPane;

    @FXML
    private TextField deleteUserTextField;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Label deleteUserLabel;

    @FXML
    private AnchorPane menuPane2;

    @FXML
    private ScrollPane clientManagementScrollPane;

    @FXML
    private AnchorPane clientManagementAnchorPane;

    @FXML
    private AnchorPane createClient_AnchorPane;

    @FXML
    private AnchorPane createClient_AnchorPane_NameJobResidencePane;

    @FXML
    private Label addClientNameLabel;

    @FXML
    private Label addClientSurnameLabel;

    @FXML
    private Label addClientPatronymicLabel;

    @FXML
    private Label addClientJobLabel;

    @FXML
    private Label addClientPositionLabel;

    @FXML
    private Label addClientRegistrationCityLabel;

    @FXML
    private Label addClientCityLabel;

    @FXML
    private Label addClientAddressLabel;

    @FXML
    private TextField addClientRegistrationCityTextField;

    @FXML
    private TextField addClientNameTextField;

    @FXML
    private TextField addClientPositionTextField;

    @FXML
    private TextField addClientPatronymicTextField;

    @FXML
    private TextField addClientJobTextField;

    @FXML
    private TextField addClientSurnameTextField;

    @FXML
    private TextField addClientCityTextField;

    @FXML
    private TextField addClientAddressTextField;

    @FXML
    private Label addClientNameDescription;

    @FXML
    private Label addClientSurnameDescription;

    @FXML
    private Label addClientPatronymicDescription;

    @FXML
    private Label addClientJobDescription;

    @FXML
    private Label addClientPositionDescription;

    @FXML
    private Label addClientRegistrationCityDescription;

    @FXML
    private Label addClientCityDescription;

    @FXML
    private Label addClientAddressDescription;

    @FXML
    private AnchorPane createClient_AnchorPane_PassportDataPane;

    @FXML
    private Label addClientBirthDateLabel;

    @FXML
    private Label addClientBirthPlaceLabel;

    @FXML
    private Label addClientPassportSeriesLabel;

    @FXML
    private Label addClientPassportNumberLabel;

    @FXML
    private Label addClientIssuedByLabel;

    @FXML
    private Label addClientIssuedDateLabel;

    @FXML
    private Label addClientCitizenshipLabel;

    @FXML
    private Label addClientIDNumberLabel;

    @FXML
    private DatePicker addClientIssuedDatePicker;

    @FXML
    private DatePicker addClientBirthDatePicker;

    @FXML
    private TextField addClientPassportSeriesTextField;

    @FXML
    private TextField addClientPassportNumberTextField;

    @FXML
    private TextField addClientBirthPlaceTextField;

    @FXML
    private TextField addClientCitizenshipTextField;

    @FXML
    private TextField addClientIssuedByTextField;

    @FXML
    private TextField addClientIDNumberTextField;

    @FXML
    private Label addClientPassportSeriesDescription;

    @FXML
    private Label addClientPassportNumberDescription;

    @FXML
    private Label addClientIssuedByDescription;

    @FXML
    private Label addClientIssuedDateDescription;

    @FXML
    private Label addClientBirthPlaceDescription;

    @FXML
    private Label addClientBirthDateDescription;

    @FXML
    private Label addClientCitizenshipDescription;

    @FXML
    private Label addClientIDNumberDescription;

    @FXML
    private AnchorPane createClient_AnchorPane_ContactsOtherPane;

    @FXML
    private MenuButton addClientMaritalStatusMenuButton;

    @FXML
    private MenuItem addClientMaritalStatusMenuItem_Single;

    @FXML
    private MenuItem addClientMaritalStatusMenuItem_Married;

    @FXML
    private MenuItem addClientMaritalStatusMenuItem_Divorced;

    @FXML
    private MenuButton addClientDisabilityMenuButton;

    @FXML
    private MenuItem addClientDisabilityMenuItem_FirstGroup;

    @FXML
    private MenuItem addClientDisabilityMenuItem_SecondGroup;

    @FXML
    private MenuItem addClientDisabilityMenuItem_ThirdGroup;

    @FXML
    private MenuItem addClientDisabilityMenuItem_No;

    @FXML
    private MenuButton addClientRetireeMenuButton;

    @FXML
    private MenuItem addClientRetireeMenuItem_Yes;

    @FXML
    private MenuItem addClientRetireeMenuItem_No;

    @FXML
    private Label addClientMaritalStatusLabel;

    @FXML
    private Label addClientDisabilityLabel;

    @FXML
    private Label addClientRetireeLabel;

    @FXML
    private Label addClientHomePhoneLabel;

    @FXML
    private Label addClientMonthlyIncomeLabel;

    @FXML
    private Label addClientMobilePhoneLabel;

    @FXML
    private Label addClientEmailLabel;

    @FXML
    private TextField addClientMobilePhoneTextField;

    @FXML
    private TextField addClientMonthlyIncomeTextField;

    @FXML
    private TextField addClientEmailTextField;

    @FXML
    private TextField addClientHomePhoneTextField;

    @FXML
    private Label addClientMaritalStatusDescription;

    @FXML
    private Label addClientDisabilityDescription;

    @FXML
    private Label addClientRetireeDescription;

    @FXML
    private Label addClientHomePhoneDescription;

    @FXML
    private Label addClientMonthlyIncomeDescription;

    @FXML
    private Label addClientMobilePhoneDescription;

    @FXML
    private Label addClientEmailDescription;

    @FXML
    private Label addClientLabel;

    @FXML
    private Button addClientButton;

    @FXML
    private TableView<?> clientsTable;

    @FXML
    private TableColumn<?, ?> idClientColumn;

    @FXML
    private TableColumn<?, ?> surnameColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> patronymicColumn;

    @FXML
    private TableColumn<?, ?> birthDateColumn;

    @FXML
    private TableColumn<?, ?> passportSeriesColumn;

    @FXML
    private TableColumn<?, ?> passportNumberColumn;

    @FXML
    private TableColumn<?, ?> issuedByColumn;

    @FXML
    private TableColumn<?, ?> issuedDateColumn;

    @FXML
    private TableColumn<?, ?> birthPlaceColumn;

    @FXML
    private TableColumn<?, ?> actualResidenceCityColumn;

    @FXML
    private TableColumn<?, ?> actualResidenceAddressColumn;

    @FXML
    private TableColumn<?, ?> homeNumberColumn;

    @FXML
    private TableColumn<?, ?> mobileNumberColumn;

    @FXML
    private TableColumn<?, ?> emailClientColumn;

    @FXML
    private TableColumn<?, ?> jobColumn;

    @FXML
    private TableColumn<?, ?> positionColumn;

    @FXML
    private TableColumn<?, ?> registrationCityColumn;

    @FXML
    private TableColumn<?, ?> citizenshipColumn;

    @FXML
    private TableColumn<?, ?> monthlyIncomeColumn;

    @FXML
    private TableColumn<?, ?> idNumberColumn;

    @FXML
    private TextField searchFieldClient;

    @FXML
    private Button searchButtonClient;

    @FXML
    private Button resetSearchButtonClient;

    @FXML
    private MenuButton criteriaButtonClient;

    @FXML
    private Menu criteriaClientMenuFIO;

    @FXML
    private MenuItem criteriaClientName;

    @FXML
    private MenuItem criteriaClientSurname;

    @FXML
    private MenuItem criteriaClientPatronymic;

    @FXML
    private MenuItem criteriaClientFIO;

    @FXML
    private Menu criteriaClientMenuPassport;

    @FXML
    private MenuItem criteriaClientPassportSeries;

    @FXML
    private MenuItem criteriaClientPassportNumber;

    @FXML
    private MenuItem criteriaClientIssuedBy;

    @FXML
    private MenuItem criteriaClientIssuedDate;

    @FXML
    private MenuItem criteriaClientBirthDate;

    @FXML
    private MenuItem criteriaClientBirthPlace;

    @FXML
    private MenuItem criteriaClientIDNumber;

    @FXML
    private MenuItem criteriaClientCitizenship;

    @FXML
    private Menu criteriaClientMenuResidence;

    @FXML
    private MenuItem criteriaClientActCity;

    @FXML
    private MenuItem criteriaClientActAddress;

    @FXML
    private MenuItem criteriaClientRegCity;

    @FXML
    private Menu criteriaClientMenuJob;

    @FXML
    private MenuItem criteriaClientJob;

    @FXML
    private MenuItem criteriaClientPosition;

    @FXML
    private Menu criteriaClientMenuContacts;

    @FXML
    private MenuItem criteriaClientEmail;

    @FXML
    private MenuItem criteriaClientHomePhone;

    @FXML
    private MenuItem criteriaClientMobilePhone;

    @FXML
    private Menu criteriaClientMenuOther;

    @FXML
    private MenuItem criteriaClientDisability;

    @FXML
    private MenuItem criteriaClientRetiree;

    @FXML
    private MenuItem criteriaClientMonthlyIncome;

    @FXML
    private MenuItem criteriaClientMaritalStatus;

    @FXML
    private MenuItem criteriaClientID;

    @FXML
    private ImageView fixImage2;

    @FXML
    private AnchorPane menuPane3;

    @FXML
    private AnchorPane menuPane31;

    @FXML
    private MenuButton languageButton;

    @FXML
    private MenuItem languageItem_Russian;

    @FXML
    private MenuItem languageItem_English;

    @FXML
    private Label languageLabel;

    @FXML
    private Label themeLabel;

    @FXML
    private MenuButton themeButton;

    @FXML
    private MenuItem themeItem_Dark;

    @FXML
    private MenuItem themeItem_Light;

    @FXML
    private Label customizationLabel;

    @FXML
    private AnchorPane accountSettingsPane;

    @FXML
    private Label accountSettingsLabel;

    @FXML
    private TextField accountSettingsUsernameTextField;

    @FXML
    private PasswordField accountSettingsPasswordTextField;

    @FXML
    private TextField accountSettingsEmailTextField;

    @FXML
    private Label accountSettingsUsernameLabel;

    @FXML
    private Label accountSettingsPasswordLabel;

    @FXML
    private Label accountSettingsEmailLabel;

    @FXML
    private Button accountSettingsSaveButton;

    @FXML
    private Label settingsWarningLabel;

    @FXML
    private AnchorPane databaseSettingsPane;

    @FXML
    private Label databaseSettingsLabel;

    @FXML
    private TextField databaseSettingsURLTextField;

    @FXML
    private Label databaseSettingsURLLabel;

    @FXML
    private Label databaseSettingsUsernameLabel;

    @FXML
    private Label databaseSettingsPasswordLabel;

    @FXML
    private Button databaseSettingsConnectButton;

    @FXML
    private TextField databaseSettingsUsernameTextField;

    @FXML
    private PasswordField databaseSettingsPasswordTextField;

    @FXML
    private Label databaseSettingsConnectionStatusLabel;

    @FXML
    private ProgressIndicator databaseSettingsConnectionProgressIndicator;

    @FXML
    private Button connectionIndicator;

    @FXML
    private Label menuPane1_DBLabel;

    @FXML
    private Button serverConnectButton;

    @FXML
    private AnchorPane menuPane4;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private AnchorPane loginElementsPane;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginUsernameLabel;

    @FXML
    private Label loginPasswordLabel;

    @FXML
    private Button signUpButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label loginWarning;

    @FXML
    private AnchorPane mainPane;

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
        String[] vals = value.split("\\|");
        switch (vals[0]) {
            case "Client":
                for (Client c : clientsData)
                    if (c.getId() == Integer.parseInt(vals[2])) {
                        c.set(connDB, vals[1], vals[3]);
                        break;
                    }
                break;
            case "User":
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
                System.out.println(value.substring(8));
                addUser(value.substring(8));
                break;
            case "addClient":
                System.out.println(value.substring(10));
                addClient(value.substring(10));
                break;
            case "changeAccountData":
                System.out.println(value.substring(18));
                changeAccountData(vals[1], vals[2], vals[3], vals[4]);
                break;
            case "deleteAllUsers":
                deleteAllUsers();
                break;
            default:
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
        new Thread(()-> {
            System.out.println("sss");
            new Server().listen();
        }).start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @FXML
    void initialize() {
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
    //TODO: make server jfx app
}
