package bank;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.function.Predicate;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Bank extends Application {

    Label welcome, create_Act;
    TextField useOrAct;
    PasswordField pass;
    HBox hBox = new HBox();
    Button loginBtn;
    public Stage window;
    Login login = new Login();
    SignUp signUp = new SignUp();

    Connection conn = DbConnector.establishConnection();
    ResultSet rs = null;
    PreparedStatement pst = null;

    String Gblname, Gbl_lastname, Gblusername, Gblemail, Gbltel, Gbl_location;
    Double Gblamount;
    int GblId;

    @Override
    public void start(Stage stage) {
        window = stage;
        window.setTitle("Bank Application");
        window.setScene(login.login());
        window.setResizable(false);
        window.show();
    }

    class Login {

        Label welcome, create_Act, feedback;
        TextField useOrAct;
        PasswordField pass;
        HBox hBox = new HBox(40);
        BorderPane borderPane = new BorderPane();
        Button loginBtn;

        private void confirmPasswordandUsername() {
            ViewActInfo viewActInfo = new ViewActInfo();
            UserDetails userDetails = new UserDetails();
            try {
                conn = DbConnector.establishConnection();
                String st = "SELECT * FROM user WHERE username = ? AND pass = ?";
                pst = conn.prepareStatement(st);
                pst.setString(1, useOrAct.getText());
                pst.setString(2, pass.getText());

                rs = pst.executeQuery();

                if (rs.next()) {
                    Gblname = rs.getString("fname");
                    Gbl_lastname = rs.getString("lname");
                    Gblusername = rs.getString("username");
                    Gblamount = rs.getDouble("amount");
                    Gblemail = rs.getString("email");
                    Gbltel = rs.getString("tel");
                    Gbl_location = rs.getString("location");

                    GblId = rs.getInt("id");
                    try {
//                            Thread.sleep(1000);
                    } finally {
                        feedback.setText("Login was successful!");
                        feedback.setStyle("-fx-text-fill:green");
                    }
//                        Thread.sleep(1000);
                    if ("super".equals(rs.getString("act_type"))) {
                        window.setScene(viewActInfo.viewActInfo());
                    } else {
                        window.setScene(userDetails.UserDetails());
                    }

//                    else if(useOrAct.getText().equals(rs.getString("username"+"_n"))){
//                        window.setScene(userDetails.UserDetails());
//                    }
                } else {
                    feedback.setText("Login failed!");
                    feedback.setStyle("-fx-text-fill:red");
                    useOrAct.clear();
                    pass.clear();
                }
            } catch (Exception x) {
            }
        }

        public Scene login() {
            SignUp signUp = new SignUp();
            Bank obj = new Bank();

            welcome = new Label("Bank Application");
            welcome.setId("welcomeLogin");
            hBox.setPadding(new Insets(10, 10, 10, 10));

            Image v = new Image(getClass().getResource("visa.png").toExternalForm());
            ImageView visaImage = new ImageView(v);
            visaImage.setFitWidth(60);

            hBox.getChildren().addAll(welcome, visaImage);
            hBox.setId("loginTopContainer");
            borderPane.setTop(hBox);

            GridPane pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setHgap(10);
            pane.setVgap(10);
            pane.setId("paneLogin");

            useOrAct = new TextField();
            useOrAct.setId("useOrAct");
            useOrAct.setPromptText("Username");
            useOrAct.setOnKeyPressed(e -> {
                if (feedback != null) {
                    feedback.setText(null);
                }
            });
            useOrAct.setPrefColumnCount(15);
            GridPane.setConstraints(useOrAct, 0, 1);

            pass = new PasswordField();
            pass.setId("pass");
            pass.setPromptText("Password");
            pass.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    confirmPasswordandUsername();
                }
            });
            GridPane.setConstraints(pass, 0, 2);

            feedback = new Label();

            loginBtn = new Button("Login");
            GridPane.setConstraints(loginBtn, 0, 3);
            loginBtn.setId("loginBtn");
            loginBtn.setOnAction((ActionEvent e) -> {
                confirmPasswordandUsername();
            });

            create_Act = new Label("Don't have an A/C?");
            create_Act.setOnMouseClicked(e -> {
                window.setScene(signUp.signUp());
            });
            GridPane.setConstraints(create_Act, 0, 4);

            GridPane.setConstraints(feedback, 0, 5);

            pane.getChildren().addAll(useOrAct, pass, loginBtn, create_Act, feedback);

            borderPane.setCenter(pane);

            Scene signIn = new Scene(borderPane, 350, 300);
            signIn.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            return signIn;
        }

    }

    private class UserDetails {

        Label wlm, lgOt, profName;
        HBox hBoxTop = new HBox(40);

        Label fname, lname, username, amount, email, tel, location, amtVl;

        Button withdraw, deposit, update;
        TextField opr;

        public Scene UserDetails() {
            Login login = new Login();
            BorderPane pane = new BorderPane();
            Scene userDetails = new Scene(pane, 400, 400);

//            top side
            wlm = new Label("PERSONAL DETAILS");
//            wlm.setStyle("-fx-font-size:16px");
            profName = new Label("Welcome " + Gblname);

            lgOt = new Label("Logout");
            lgOt.setStyle("-fx-text-fill:#f50057");
            lgOt.setOnMouseClicked(e -> {
                window.setScene(login.login());
            });

            hBoxTop.setPadding(new Insets(10, 10, 10, 10));
            hBoxTop.getChildren().addAll(wlm, profName, lgOt);
            hBoxTop.setId("hBoxTop");
            pane.setTop(hBoxTop);
//            end top side

//            center
            GridPane gPane = new GridPane();
            gPane.setId("gPane");
            gPane.setPadding(new Insets(10, 10, 10, 10));
            gPane.setVgap(20);
            gPane.setHgap(15);
            fname = new Label();
            fname.setText("FirstName : " + Gblname);
            fname.setMaxWidth(300);
            GridPane.setConstraints(fname, 0, 0);

            lname = new Label();
            lname.setText("Lastname : " + Gbl_lastname);
            lname.setMaxWidth(300);
            GridPane.setConstraints(lname, 0, 1);

            username = new Label();
            username.setText("Username : " + Gblusername);
            username.setMaxWidth(300);
            GridPane.setConstraints(username, 0, 2);

            email = new Label();
            email.setText("Email : " + Gblemail);
            email.setMaxWidth(300);
            GridPane.setConstraints(email, 0, 3);

            tel = new Label();
            tel.setText("Tel : " + Gbltel);
            tel.setMaxWidth(300);
            GridPane.setConstraints(tel, 0, 4);

            location = new Label();
            location.setText("Location : " + Gbl_location);
            location.setMaxWidth(300);
            GridPane.setConstraints(location, 0, 5);

            amount = new Label("Balance: " + Gblamount);
            GridPane.setConstraints(amount, 0, 6);

            opr = new TextField();
            opr.setPromptText("Only values allowed");
            opr.setFocusTraversable(false);
            GridPane.setConstraints(opr, 0, 7);

            HBox hBoxCenter = new HBox(15);
            withdraw = new Button("Withdraw");
            withdraw.setOnAction(e -> {
                try {

                    String qry = "UPDATE user"
                            + " SET amount = ? WHERE  id = ' " + GblId + " ' ";
                    pst = conn.prepareStatement(qry);
                    if (Double.parseDouble(opr.getText()) > Gblamount) {
                        System.out.println("Insufficient funds");
                    } else {
                        Gblamount -= Double.parseDouble(opr.getText());
                        pst.setString(1, Gblamount.toString());
                        System.out.println("Withdraw complete");
                    }

                    opr.clear();
                    pst.executeUpdate();
                    pst.close();

                } catch (Exception x) {
                    x.printStackTrace();
                }

            });

            deposit = new Button("Deposit");
            deposit.setOnAction(e -> {
                try {

                    String qry = "UPDATE user"
                            + " SET amount = ? WHERE  id = ' " + GblId + " ' ";
                    pst = conn.prepareStatement(qry);
                    String y = opr.getText();
                    if (Double.parseDouble(opr.getText()) > 4000000) {
                        System.out.println("Sorry you cann't deposit more than 4,000,000");
                    } else {
                        Gblamount += Double.parseDouble(opr.getText());
                        pst.setString(1, Gblamount.toString());
                        System.out.println("Deposit complete");
                    }

                    opr.clear();
                    pst.executeUpdate();
                    pst.close();

                } catch (Exception x) {
                    x.printStackTrace();
                }

            });

            update = new Button("Update");
            update.setOnAction(e -> {
//                GridPane.setConstraints(amtVl, 1, 6);
            });

            hBoxCenter.getChildren().addAll(withdraw, deposit, update);
            GridPane.setConstraints(hBoxCenter, 0, 8);

            gPane.getChildren().addAll(fname, lname, username, email, tel, location, amount, opr, hBoxCenter);
            pane.setCenter(gPane);
//            end center

//            userDetails.getStylesheets().add(getClass().getResource("UserDetails.css").toExternalForm());
            userDetails.getStylesheets().add("UserDetails.css");

            return userDetails;

        }

    }

    public class SignUp {

        Stage myStage;
        Label welcome;
        TextField fname, lname, username, act_no, act_type;
        PasswordField pass;
        HBox hBox = new HBox();
        HBox hBox_btns = new HBox(20);
        Button bk2login, save;

        ViewActInfo viewActInfo = new ViewActInfo();

        private void clearFields() {
            fname.clear();
            lname.clear();
            username.clear();
            pass.clear();
            act_no.clear();
            act_type.clear();
        }

        public Scene signUp() {
            Login login = new Login();
            Bank bank = new Bank();

            BorderPane borderPane = new BorderPane();

            GridPane pane = new GridPane();
            pane.setPadding(new Insets(10, 10, 10, 10));
            pane.setHgap(10);
            pane.setVgap(10);
            pane.setId("signUpMainContainer");

            welcome = new Label("Create Account");
            welcome.setId("welcomeSignUp");
            hBox.getChildren().add(welcome);
            hBox.setPadding(new Insets(10, 10, 10, 10));
            hBox.setId("signUpTopContainer");
            borderPane.setTop(hBox);

            fname = new TextField();
            fname.setPromptText("Firstname");
            fname.setId("fname");
            GridPane.setConstraints(fname, 0, 0);

            lname = new TextField();
            lname.setPromptText("Lastname");
            lname.setId("lname");
            GridPane.setConstraints(lname, 0, 1);

            username = new TextField();
            username.setPromptText("Username");
            username.setId("username");
            GridPane.setConstraints(username, 0, 2);

            pass = new PasswordField();
            pass.setPromptText("Password");
            pass.setId("pass");
            GridPane.setConstraints(pass, 0, 3);

            act_no = new TextField();
            act_no.setPromptText("A/c no");
            act_no.setId("act_no");
            GridPane.setConstraints(act_no, 0, 4);

            act_type = new TextField();
            act_type.setPromptText("A/c type");
            act_type.setId("act_type");
            GridPane.setConstraints(act_type, 0, 5);

            save = new Button("Save");
            save.setId("save");
            save.setPrefWidth(100);
            save.setOnAction(e -> {
                try {
                    String qry = "INSERT INTO user (fname, lname, username, pass, act_no, act_type)"
                            + "VALUES (?,?,?,?,?,?)";
                    pst = conn.prepareStatement(qry);
                    pst.setString(1, fname.getText());
                    pst.setString(2, lname.getText());
                    pst.setString(3, username.getText());
                    pst.setString(4, pass.getText());
                    pst.setString(5, act_no.getText());
                    pst.setString(6, act_type.getText());

                    pst.execute();
                    pst.close();
                } catch (Exception x) {
                    x.printStackTrace();
                }
                clearFields();
                System.out.println("Save complete");
            });

            bk2login = new Button("Back");
            bk2login.setId("bk2login");
            bk2login.setPrefWidth(100);
            bk2login.setOnAction(e -> {
                window.setScene(login.login());
            });

            hBox_btns.getChildren().addAll(save, bk2login);
            GridPane.setConstraints(hBox_btns, 0, 6);

            pane.getChildren().addAll(fname, lname, username, pass, act_no, act_type, hBox_btns);

            borderPane.setCenter(pane);

            Scene signUp = new Scene(borderPane, 350, 400);
            signUp.getStylesheets().add(getClass().getResource("signUp.css").toExternalForm());
            return signUp;
        }
    }

    class ViewActInfo {

        FileChooser fileChooser = new FileChooser();
        File file;
        Desktop desktop = Desktop.getDesktop();
        ImageView imageView;
        Image image;

        TextField fname, lname, username, act_no, act_type, id, email, tel, location;
        PasswordField pass;
        FileInputStream fis;

        Login login = new Login();

        public Scene viewActInfo() {
            BorderPane pane = new BorderPane();
            pane.setId("viewActInfoContent");

            HBox hBox = new HBox(20);
            Label welcome = new Label("Welcome to this page");

            pane.setTop(hBox);

//            leftside
            VBox leftSide = new VBox(10);
            leftSide.setPadding(new Insets(10, 10, 10, 10));
            leftSide.setId("leftSide");
            Label label1 = new Label("Create new User");
            label1.setId("label1");
            label1.setOnMouseClicked(e -> {
                clearInsertFields();
            });
            fname = new TextField();
            fname.setPromptText("Firstname");
            fname.setMaxWidth(300);

            lname = new TextField();
            lname.setPromptText("Lastname");
            lname.setMaxWidth(300);

            username = new TextField();
            username.setPromptText("Username");
            username.setMaxWidth(300);

            pass = new PasswordField();
            pass.setPromptText("Password");
            pass.setMaxWidth(300);

            email = new TextField();
            email.setPromptText("Email");
            email.setMaxWidth(300);

            tel = new TextField();
            tel.setPromptText("Telephone");
            tel.setMaxWidth(300);

            location = new TextField();
            location.setPromptText("Location");
            location.setMaxWidth(300);

            act_no = new TextField();
            act_no.setPromptText("A/C number");
            act_no.setMaxWidth(300);

            act_type = new TextField();
            act_type.setPromptText("A/C type");
            act_type.setMaxWidth(300);

            Button save = new Button("Save");
            save.setPrefWidth(100);
            save.setPrefHeight(40);
            save.setOnAction(e -> {
                validateInsertFields();
                clearInsertFields();
            });

            Label profName = new Label("Admin : " + Gblname);
            profName.setId("profName");

            HBox hBoxBottom = new HBox(10);
            hBoxBottom.getChildren().addAll(profName);

            Label logOut = new Label("Logout");
            logOut.setId("logOut");
            logOut.setOnMousePressed(e -> {
                window.setScene(login.login());
            });

            leftSide.getChildren().addAll(label1, fname, lname, username, pass, email, tel, location, act_no, act_type, save, hBoxBottom, logOut);
            pane.setLeft(leftSide);
//              end leftside

            Button browse = new Button("Add Profile Picture");
            browse.setOnAction(e -> {
//                myFileChooser();
//                selectImageOnly();
                file = fileChooser.showOpenDialog(window);
                if (file != null) {
                    Image profPic = new Image(file.toURI().toString());
                    ImageView profPicView = new ImageView(profPic);

                    profPicView.setFitWidth(150);
                    profPicView.setFitHeight(200);
                    profPicView.setPreserveRatio(true);

                    pane.setCenter(profPicView);
                    BorderPane.setAlignment(profPicView, Pos.CENTER);
                }

            });

//            rightside
            VBox rightSide = new VBox(10);

            TableView<User2> table = new TableView<>();
            final ObservableList<User2> data = FXCollections.observableArrayList();
            table.setItems(data);
            table.setEditable(true);

            Label label = new Label("Accounts");

            TableColumn col1 = new TableColumn("ID");
            col1.setMinWidth(50);
            col1.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn col2 = new TableColumn("Firstname");
            col2.setMinWidth(50);
            col2.setCellValueFactory(new PropertyValueFactory<>("fname"));

            TableColumn col3 = new TableColumn("Lastname");
            col3.setMinWidth(50);
            col3.setCellValueFactory(new PropertyValueFactory<>("lname"));

            TableColumn col4 = new TableColumn("Username");
            col4.setMinWidth(50);
            col4.setCellValueFactory(new PropertyValueFactory<>("username"));

            TableColumn col5 = new TableColumn("Password");
            col5.setMinWidth(50);
            col5.setCellValueFactory(new PropertyValueFactory<>("pass"));

            TableColumn col6 = new TableColumn("A/C No.");
            col6.setMinWidth(50);
            col6.setCellValueFactory(new PropertyValueFactory<>("act_no"));

            TableColumn col7 = new TableColumn("A/C Type");
            col7.setMinWidth(50);
            col7.setCellValueFactory(new PropertyValueFactory<>("act_type"));

            table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);

            BorderPane.setMargin(table, new Insets(10, 10, 10, 10));

            table.getSelectionModel().selectedItemProperty().addListener((obs, oldv, nvl) -> {
                User2 user = table.getSelectionModel().getSelectedItem();
                if (table.getSelectionModel().getSelectedItem() != null) {
                    System.out.println(nvl.getFname() + " " + nvl.getLname());
                    try {
                        String qry = "SELECT * FROM user WHERE id = ?";
                        pst = conn.prepareStatement(qry);
                        pst.setInt(1, user.getId());
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            fname.setText(rs.getString("fname"));
                            lname.setText(rs.getString("lname"));
                            username.setText(rs.getString("username"));
                            pass.setText(rs.getString("pass"));
                            email.setText(rs.getString("email"));
                            tel.setText(rs.getString("tel"));
                            location.setText(rs.getString("location"));
                            act_no.setText(rs.getString("act_no"));
                            act_type.setText(rs.getString("act_type"));

                            InputStream is = rs.getBinaryStream("image");
                            OutputStream os = new FileOutputStream(new File("photo.jpg"));
                            byte[] content = new byte[1024];
                            int size = 0;
                            while ((size = is.read(content)) != -1) {
                                os.write(content, 0, size);
                            }
                            os.close();
                            is.close();

                            Circle crop = new Circle(50,50,50);
                            image = new Image("file:photo.jpg");
                            ImageView profPicView = new ImageView(image);

                            profPicView.setFitWidth(150);
                            profPicView.setFitHeight(200);
                            profPicView.setPreserveRatio(true);

                            profPicView.setClip(crop);
                            pane.setCenter(profPicView);
                            BorderPane.setAlignment(profPicView, Pos.CENTER);
                            pst.close();
                            rs.close();
                        }
                    } catch (Exception e) {
                    }
                }

            });

            Button load = new Button("Load Accounts");
            load.setOnAction(e -> {
                data.clear();
                String str = "SELECT * FROM user";
                try {
                    pst = conn.prepareStatement(str);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        data.add(new User2(
                                rs.getInt("id"),
                                rs.getString("fname"),
                                rs.getString("lname"),
                                rs.getString("username"),
                                rs.getString("pass"),
                                rs.getString("act_no"),
                                rs.getString("act_type")
                        ));
                        table.setItems(data);
                    }
                    pst.close();
                    rs.close();
                } catch (Exception x) {

                }

            });

            HBox hbox2 = new HBox(10);
            hbox2.setPadding(new Insets(10, 50, 10, 0));

            TextField search = new TextField();
            search.setPrefWidth(400);
            search.setPromptText("Search The Table");

            FilteredList<User2> filteredData = new FilteredList<>(data, e -> true);
            search.setOnKeyPressed(e -> {
                search.textProperty().addListener((obsVl, ov, nv) -> {
                    filteredData.setPredicate((Predicate<? super User2>) user -> {
                        if ((nv == null) || (nv.isEmpty())) {
                            return true;
                        }
                        String lowerCaseFilter = nv.toLowerCase();

                        if (user.getFname().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (user.getLname().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if(user.getUsername().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        } else if(user.getAct_no().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        } else if(user.getAct_type().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                        return false;
                    });
                });

                SortedList<User2> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(table.comparatorProperty());
                table.setItems(sortedData);
            });

            Button updateTb = new Button("Update User");
            updateTb.setOnAction(e -> {
                try {

                    User2 user = (User2) table.getSelectionModel().getSelectedItem();
                    String qry = "UPDATE user"
                            + " SET fname = ?, lname = ?, username = ?, pass = ?, email = ?, tel = ?, location = ?, act_no = ?, act_type = ?, image = ?"
                            + "WHERE  id = ? ";
                    pst = conn.prepareStatement(qry);
                    pst.setInt(11, user.getId());

                    pst.setString(1, fname.getText());
                    pst.setString(2, lname.getText());
                    pst.setString(3, username.getText());
                    pst.setString(4, pass.getText());
                    pst.setString(5, email.getText());
                    pst.setString(6, tel.getText());
                    pst.setString(7, location.getText());
                    pst.setString(8, act_no.getText());
                    pst.setString(9, act_type.getText());

                    fis = new FileInputStream(file);
                    pst.setBinaryStream(10, (InputStream) fis, (int) file.length());
                    System.out.println("Update effected");

                    pst.executeUpdate();
                    pst.close();
                    clearInsertFields();

                } catch (Exception x) {
                    x.printStackTrace();
                }
                data.clear();
            });

            Button deleteUser = new Button("Delete User");
            deleteUser.setOnAction(e -> {
                try {
                    User2 user = (User2) table.getSelectionModel().getSelectedItem();
                    String deletQry = "DELETE FROM user WHERE id = ?";
                    pst = conn.prepareStatement(deletQry);
                    pst.setInt(1, user.getId());
                    pst.executeUpdate();
                    System.out.println("Delete complete");
                    pst.close();
                } catch (Exception x) {
                    x.printStackTrace();
                }
                clearInsertFields();
                data.clear();
            });

            hbox2.getChildren().addAll(load, browse, search, updateTb, deleteUser);
            BorderPane.setMargin(hbox2, new Insets(10, 10, 10, 10));

            rightSide.getChildren().addAll(table, hbox2);
            pane.setRight(rightSide);
//          end rightside

            Scene viewActInfo = new Scene(pane, 1200, 500);
            viewActInfo.getStylesheets().add(getClass().getResource("viewActInfo.css").toExternalForm());

            return viewActInfo;
        }

        public void saveIntoDb() {

            try {
                String qry = "INSERT INTO user (fname, lname, username, pass, email, tel, location, act_no, act_type)"
                        + "VALUES (?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(qry);
                pst.setString(1, fname.getText());
                pst.setString(2, lname.getText());
                pst.setString(3, username.getText());
                pst.setString(4, pass.getText());
                pst.setString(5, email.getText());
                pst.setString(6, tel.getText());
                pst.setString(7, location.getText());
                pst.setString(8, act_no.getText());
                pst.setString(9, act_type.getText());

//                saving image
//                fis = new FileInputStream(file);
//                pst.setBinaryStream(7, (InputStream) fis, (int) file.length());
                System.out.println("Insert complete");
                pst.execute();
                pst.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        private void clearInsertFields() {
            fname.clear();
            lname.clear();
            username.clear();
            pass.clear();
            email.clear();
            tel.clear();
            location.clear();
            act_no.clear();
            act_type.clear();
        }

        private void validateInsertFields() {
            if ((fname.getText().isEmpty()) | (lname.getText().isEmpty()) | (pass.getText().isEmpty()) | (act_no.getText().isEmpty()) | (act_type.getText().isEmpty())) {
                System.out.println("Some fields were left un attended to");

            } else {
                saveIntoDb();
            }
        }

        public ObservableList<AcUser> getActUser() {
            ObservableList<AcUser> actUser = FXCollections.observableArrayList();
            actUser.add(new AcUser("Ssemuluwa", "Maishanu"));
            return actUser;
        }

    }

    public static void main(String[] args) {
        launch(args);

    }

}
