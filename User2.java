package bank;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User2 {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty fname;
    private final SimpleStringProperty lname;
    private final SimpleStringProperty username;
    private final SimpleStringProperty pass;
    private final SimpleStringProperty act_no;
    private final SimpleStringProperty act_type;

    public User2(int id,String fname, String lname, String username, String pass, String act_no, String act_type) {
        this.id = new SimpleIntegerProperty(id);
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.username = new SimpleStringProperty(username);
        this.pass = new SimpleStringProperty(pass);
        this.act_no = new SimpleStringProperty(act_no);
        this.act_type = new SimpleStringProperty(act_type);
    }
//  Getter methods

    public int getId() {
        return id.get();
    }

    public String getFname() {
        return fname.get();
    }

    public String getLname() {
        return lname.get();
    }
    
    public String getUsername() {
        return username.get();
    }

    public String getPass() {
        return pass.get();
    }

    public String getAct_no() {
        return act_no.get();
    }

    public String getAct_type() {
        return act_type.get();
    }
//  Setter methods
    public void setId(int id){
        this.id.set(id);
    }
    
    
    public void setFname(String fname){
        this.fname.set(fname);
    }
    
    public void setLname(String lname){
        this.lname.set(lname);
    }
    
    
    public void setUsername(String username){
        this.username.set(username);
    }
    
    public void setPass(String pass){
        this.pass.set(pass);
    }
    
    public void setAct_no(String act_no){
        this.act_no.set(act_no);
    }
    
    public void setAct_type(String act_type){
        this.act_type.set(act_type);
    }

// Properties
    
    public SimpleIntegerProperty idProperty(){
        return id;
    }
    
    public SimpleStringProperty fnameProperty(){
        return fname;
    }
    
    public SimpleStringProperty lnameProperty(){
        return lname;
    }
    
    public SimpleStringProperty usernameProperty(){
        return username;
    }
    
    public SimpleStringProperty passProperty(){
        return pass;
    }
    
    public SimpleStringProperty act_NOProperty(){
        return act_no;
    }
    
    public SimpleStringProperty act_typeProperty(){
        return act_type;
    }
}
