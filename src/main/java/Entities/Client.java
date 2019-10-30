package Entities;


import Connectivity.DatabaseConnection;
import enums.Disability;
import enums.MaritalStatus;
import enums.Retiree;

import java.io.Serializable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class Client implements Serializable {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String birthDate;
    private String passportSeries;
    private String passportNumber;
    private String issuedBy;
    private String issuedDate;
    private String birthPlace;
    private String actualResidenceCity;
    private String actualResidenceAddress;
    private String homeNumber;
    private String mobileNumber;
    private String email;
    private String job;
    private String position;
    private String registrationCity;
    private String maritalStatus;
    private String citizenship;
    private String disability;
    private String retiree;
    private String monthlyIncome;
    private String idNumber;

    public Client() {
    }

    public Client(String client) {
        //0  1    2      3          4          5  6       7                          8          9     10    11                         12      13                  14                  15     16  17    18      19       20 21 22     23
        //93#Глеб#Скачко#Дмитриевич#2000-08-09#MP#3418583#Первомайский РУВД г.Минска#2015-08-09#Минск#Минск#Самоцветная улица, дом 131#2943455#+375 (44) 543-53-90#skachko42@gmail.com#Google#CEO#Минск#Married#Беларусь#No#No#1300.0#4729553F119PB4
        String[] vals = client.split("#");
        this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.name = vals[1];
        if (!vals[2].equals("null")) this.surname = vals[2];
        if (!vals[3].equals("null")) this.patronymic = vals[3];
        if (!vals[4].equals("null")) this.birthDate = vals[4];
        if (!vals[5].equals("null")) this.passportSeries = vals[5];
        if (!vals[6].equals("null")) this.passportNumber = vals[6];
        if (!vals[7].equals("null")) this.issuedBy = vals[7];
        if (!vals[8].equals("null")) this.issuedDate = vals[8];
        if (!vals[9].equals("null")) this.birthPlace = vals[9];
        if (!vals[10].equals("null")) this.actualResidenceCity = vals[10];
        if (!vals[11].equals("null")) this.actualResidenceAddress = vals[11];
        if (!vals[12].equals("null")) this.homeNumber = vals[12];
        if (!vals[13].equals("null")) this.mobileNumber = vals[13];
        if (!vals[14].equals("null")) this.email = vals[14];
        if (!vals[15].equals("null")) this.job = vals[15];
        if (!vals[16].equals("null")) this.position = vals[16];
        if (!vals[17].equals("null")) this.registrationCity = vals[17];
        if (!vals[18].equals("null")) this.maritalStatus = vals[18];
        if (!vals[19].equals("null")) this.citizenship = vals[19];
        if (!vals[20].equals("null")) this.disability = vals[20];
        if (!vals[21].equals("null")) this.retiree = vals[21];
        if (!vals[22].equals("null")) this.monthlyIncome = vals[22];
        if (!vals[23].equals("null")) this.idNumber = vals[23];
    }

    public Client(int id, String name, String surname, String patronymic, String birthDate, String passportSeries, String passportNumber, String issuedBy, String issuedDate, String birthPlace, String actualResidenceCity, String actualResidenceAddress, String homeNumber, String mobileNumber, String email, String job, String position, String registrationCity, String maritalStatus, String citizenship, String disability, String retiree, String monthlyIncome, String idNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthDate = birthDate;
        this.passportSeries = passportSeries;
        this.passportNumber = passportNumber;
        this.issuedBy = issuedBy;
        this.issuedDate = issuedDate;
        this.birthPlace = birthPlace;
        this.actualResidenceCity = actualResidenceCity;
        this.actualResidenceAddress = actualResidenceAddress;
        this.homeNumber = homeNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.job = job;
        this.position = position;
        this.registrationCity = registrationCity;
        this.maritalStatus = maritalStatus;
        this.citizenship = citizenship;
        this.disability = disability;
        this.retiree = retiree;
        this.monthlyIncome = monthlyIncome;
        this.idNumber = idNumber;
    }

    //DB
    public void setIdDB(DatabaseConnection conn, int id) {
        this.id = id;
    }

    public void setNameDB(DatabaseConnection conn, String name) {
        name = name.trim();
        if (name.matches("[а-яА-Я]{2,20}")) {
            this.name = name;
            try {
                String prepStat = "UPDATE clients SET Name = ? WHERE Id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, name);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSurnameDB(DatabaseConnection conn, String surname) {
        surname = surname.trim();
        if (surname.matches("[а-яА-Я]{2,20}")) {
            this.surname = surname;
            try {
                String prepStat = "UPDATE clients SET Surname = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, surname);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPatronymicDB(DatabaseConnection conn, String patronymic) {
        patronymic = patronymic.trim();
        if (patronymic.matches("[а-яА-Я]{2,30}")) {
            this.patronymic = patronymic;
            try {
                String prepStat = "UPDATE clients SET Patronymic = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, patronymic);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBirthDateDB(DatabaseConnection conn, String birthDate) {
        birthDate = birthDate.trim();
        if (birthDate.matches("^\\d{4}[-/](((0)[0-9])|((1)[0-2]))[-/]([0-2][0-9]|(3)[0-1])$")) {
            this.birthDate = birthDate;
            try {
                String prepStat = "UPDATE clients SET Birth_date = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setDate(1, Date.valueOf(this.birthDate));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPassportSeriesDB(DatabaseConnection conn, String passportSeries) {
        this.passportSeries = passportSeries;
        try {
            String prepStat = "UPDATE clients SET Passport_series = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.passportSeries);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPassportNumberDB(DatabaseConnection conn, String passportNumber) {
        passportNumber = passportNumber.trim();
        if (passportNumber.matches("^\\d{7}$")) {
            this.passportNumber = passportNumber;
            try {
                String prepStat = "UPDATE clients SET Passport_number = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, this.passportNumber);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setIssuedByDB(DatabaseConnection conn, String issuedBy) {
        this.issuedBy = issuedBy;
        try {
            String prepStat = "UPDATE clients SET Issued_by = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.issuedBy);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIssuedDateDB(DatabaseConnection conn, String issuedDate) {
        issuedDate = issuedDate.trim();
        if (issuedDate.matches("^\\d{4}[-/](((0)[0-9])|((1)[0-2]))[-/]([0-2][0-9]|(3)[0-1])$")) {
            this.issuedDate = issuedDate;
            try {
                String prepStat = "UPDATE clients SET Issued_date = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setDate(1, Date.valueOf(this.issuedDate));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBirthPlaceDB(DatabaseConnection conn, String birthPlace) {
        boolean exists = true;
        /*try {
            City.valueOf(birthPlace);
        } catch (IllegalArgumentException e) {
            exists = false;
        }*/
        if (exists) {
            this.birthPlace = birthPlace;
            try {
                String prepStat = "UPDATE clients SET Birth_place = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, this.birthPlace);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setActualResidenceCityDB(DatabaseConnection conn, String actualResidenceCity) {
        boolean exists = true;
        /*try {
            City.valueOf(actualResidenceCity);
        } catch (IllegalArgumentException e) {
            exists = false;
        }*/
        if (exists) {
            this.actualResidenceCity = actualResidenceCity;
            try {
                String prepStat = "UPDATE clients SET Actual_residence_city = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, this.actualResidenceCity);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setActualResidenceAddressDB(DatabaseConnection conn, String actualResidenceAddress) {
        this.actualResidenceAddress = actualResidenceAddress;
        try {
            String prepStat = "UPDATE clients SET Actual_residence_address = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.actualResidenceAddress);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setHomeNumberDB(DatabaseConnection conn, String homeNumber) {
        this.homeNumber = homeNumber;
        try {
            String prepStat = "UPDATE clients SET Home_number = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.homeNumber);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMobileNumberDB(DatabaseConnection conn, String mobileNumber) {
        this.mobileNumber = mobileNumber;
        try {
            String prepStat = "UPDATE clients SET Mobile_number = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.mobileNumber);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setEmailDB(DatabaseConnection conn, String email) {
        this.email = email;
        try {
            String prepStat = "UPDATE clients SET Email = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.email);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setJobDB(DatabaseConnection conn, String job) {
        this.job = job;
        try {
            String prepStat = "UPDATE clients SET Job = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.job);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPositionDB(DatabaseConnection conn, String position) {
        this.position = position;
        try {
            String prepStat = "UPDATE clients SET Position = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.position);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRegistrationCityDB(DatabaseConnection conn, String registrationCity) {
        boolean exists = true;
        /*try {
            City.valueOf(registrationCity);
        } catch (IllegalArgumentException e) {
            exists = false;
        }*/
        if (exists) {
            this.registrationCity = registrationCity;
            try {
                String prepStat = "UPDATE clients SET Registration_city = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, this.registrationCity);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMaritalStatusDB(DatabaseConnection conn, MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus.toString();
        try {
            String prepStat = "UPDATE clients SET Marital_status = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            assert maritalStatus != null;
            preparedStatement.setString(1, maritalStatus.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCitizenshipDB(DatabaseConnection conn, String citizenship) {
        boolean exists = true;
        /*try {
            City.valueOf(citizenship);
        } catch (IllegalArgumentException e) {
            exists = false;
        }*/
        if (exists) {
            this.citizenship = citizenship;
            try {
                String prepStat = "UPDATE clients SET Citizenship = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, this.citizenship);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDisabilityDB(DatabaseConnection conn, Disability disability) {
        this.disability = disability.toString();
        try {
            String prepStat = "UPDATE clients SET Disability = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            assert disability != null;
            preparedStatement.setString(1, disability.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRetireeDB(DatabaseConnection conn, Retiree retiree) {
        this.retiree = retiree.toString();
        try {
            String prepStat = "UPDATE clients SET Is_retiree = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, retiree.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMonthlyIncomeDB(DatabaseConnection conn, String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
        try {
            String prepStat = "UPDATE clients SET Monthly_income = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setDouble(1, Double.parseDouble(this.monthlyIncome));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIdNumberDB(DatabaseConnection conn, String idNumber) {
        this.idNumber = idNumber;
        try {
            String prepStat = "UPDATE clients SET Id_number = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, this.idNumber);
            System.out.println(Arrays.toString(prepStat.getBytes()));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM clients WHERE Id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    //Object
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        /*try {
            this.birthDate = birthDate;
        } catch(Exception e) {
            System.out.println("OOOOOOOOOOOOOOOOOOOOO");
            e.printStackTrace();
        }*/
    }

    public String getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getActualResidenceCity() {
        return actualResidenceCity;
    }

    public void setActualResidenceCity(String actualResidenceCity) {
        this.actualResidenceCity = actualResidenceCity;
    }

    public String getActualResidenceAddress() {
        return actualResidenceAddress;
    }

    public void setActualResidenceAddress(String actualResidenceAddress) {
        this.actualResidenceAddress = actualResidenceAddress;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRegistrationCity() {
        return registrationCity;
    }

    public void setRegistrationCity(String registrationCity) {
        this.registrationCity = registrationCity;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus.toString();
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(Disability disability) {
        this.disability = disability.toString();
    }

    public String getRetiree() {
        return retiree;
    }

    public void setRetiree(Retiree retiree) {
        this.retiree = retiree.toString();
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void set(DatabaseConnection conn, String field, String value) {
        switch (field) {
            case "setName":
                setNameDB(conn, value);
                break;
            case "setSurname":
                setSurnameDB(conn, value);
                break;
            case "setPatronymic":
                setPatronymicDB(conn, value);
                break;
            case "setBirthDate":
                setBirthDateDB(conn, value);
                break;
            case "setPassportSeries":
                setPassportSeriesDB(conn, value);
                break;
            case "setPassportNumber":
                setPassportNumberDB(conn, value);
                break;
            case "setIssuedBy":
                setIssuedByDB(conn, value);
                break;
            case "setIssuedDate":
                setIssuedDateDB(conn, value);
                break;
            case "setBirthPlace":
                setBirthPlaceDB(conn, value);
                break;
            case "setActualResidenceCity":
                setActualResidenceCityDB(conn, value);
                break;
            case "setActualResidenceAddress":
                setActualResidenceAddressDB(conn, value);
                break;
            case "setHomeNumber":
                setHomeNumberDB(conn, value);
                break;
            case "setMobileNumber":
                setMobileNumberDB(conn, value);
                break;
            case "setEmail":
                setEmailDB(conn, value);
                break;
            case "setJob":
                setJobDB(conn, value);
                break;
            case "setPosition":
                setPositionDB(conn, value);
                break;
            case "setRegistrationCity":
                setRegistrationCityDB(conn, value);
                break;
            case "setMaritalStatus":
                setMaritalStatusDB(conn, MaritalStatus.valueOf(value));
                break;
            case "setCitizenship":
                setCitizenshipDB(conn, value);
                break;
            case "setDisability":
                setDisabilityDB(conn, Disability.valueOf(value));
                break;
            case "setRetiree":
                setRetireeDB(conn, Retiree.valueOf(value));
                break;
            case "setMonthlyIncome":
                setMonthlyIncomeDB(conn, value);
                break;
            case "setIdNumber":
                setIdNumberDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }

    @Override
    public String toString() {
        return id +
                "#" + name +
                "#" + surname +
                "#" + patronymic +
                "#" + birthDate +
                "#" + passportSeries +
                "#" + passportNumber +
                "#" + issuedBy +
                "#" + issuedDate +
                "#" + birthPlace +
                "#" + actualResidenceCity +
                "#" + actualResidenceAddress +
                "#" + homeNumber +
                "#" + mobileNumber +
                "#" + email +
                "#" + job +
                "#" + position +
                "#" + registrationCity +
                "#" + maritalStatus +
                "#" + citizenship +
                "#" + disability +
                "#" + retiree +
                "#" + monthlyIncome +
                "#" + idNumber;
    }
}
