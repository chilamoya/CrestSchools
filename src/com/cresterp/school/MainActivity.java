package com.cresterp.school;


import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.innate.cresterp.school.services.SchoolService;
import java.io.IOException;

public class MainActivity {

    private Form current;
    private SchoolService service = new SchoolService();
    java.util.List<StudentLogin> student;
    
    private Resources theme;
    
   final private StudentLogin studLogin = new StudentLogin();

    public void init(Object context) {
        try {
            theme = Resources.openLayered("/theme");
            UIManager.getInstance().setThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
        } catch(IOException e){
            e.printStackTrace();
        }
        // Pro users - uncomment this code to get crash reports sent to you automatically
        /*Display.getInstance().addEdtErrorHandler(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                Log.p("Exception in AppName version " + Display.getInstance().getProperty("AppVersion", "Unknown"));
                Log.p("OS " + Display.getInstance().getPlatformName());
                Log.p("Error " + evt.getSource());
                Log.p("Current Form " + Display.getInstance().getCurrent().getName());
                Log.e((Throwable)evt.getSource());
                Log.sendLog();
            }
        });*/
    }
    
    
    
    public void start() {
        if(current != null){
            current.show();
            return;
        }
        //show the login method of the app
        showLoginForm();
    }
    
    public void showLoginForm(){
        Form f = new Form("Login");
        
        Container padding = new Container();
        Style s = new Style();
        s.setPadding(0, 15, 5, 5);
        s.setPaddingUnit(new byte[]{
            Style.UNIT_TYPE_DIPS, 
            Style.UNIT_TYPE_DIPS,
            Style.UNIT_TYPE_DIPS,
            Style.UNIT_TYPE_DIPS
        });
        
        
        padding.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        padding.addComponent(new Label("Username"));
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        passwordField.setConstraint(TextField.PASSWORD);
        padding.addComponent(usernameField);
        padding.addComponent(new Label("Password"));
        padding.addComponent(passwordField);
        
        Button loginButton = new Button("Login");
        loginButton.addActionListener((e)->{
            try {
               // student.login(usernameField.getText(), passwordField.getText());
                showStudentInfo();
                
            } catch (Exception ex) {
                Dialog.show("Login Failed", ex.getMessage(), "OK", "Cancel");
                return;
            }
        });
        
        padding.addComponent(loginButton);
        f.setLayout(new BorderLayout());
        f.addComponent(BorderLayout.CENTER, padding);
        f.show();
    }
    
    public void showStudentInfo(){
        Form form1 = new Form("Welcome");
        form1.setLayout(new BorderLayout());
        
        
        Tabs t = new Tabs();
        t.addTab("Homework", showHomework());
        t.addTab("Payment", showPayments());
        t.addTab("Results", showResults());
        
        
        
        Button homeWork = new Button("H/W");
        homeWork.addActionListener((e)->{
            
        });
        
        Button payments = new Button("Payments");
        payments.addActionListener((e)->{
            
        });
        
        Button results = new Button("Notifications");
        results.addActionListener((e)->{
            
        });
        
        //cn1.addComponent(homeWork);
        //cn1.addComponent(payments);
        //cn1.addComponent(results);
        
        
        form1.addComponent(BorderLayout.NORTH, t);
        form1.show();
    }
    
    private Container showHomework(){
        Container homework = new Container(new BorderLayout());
        Form f = new Form("Homework");
        homework.addComponent(BorderLayout.NORTH, f);
        return homework;
    }
    
    private Container showPayments(){
        Container payments = new Container(new BorderLayout());
        Form f = new Form("Payments");
        payments.addComponent(BorderLayout.NORTH, f);
        return payments;
    }
     
     private Container showResults(){
        Container results = new Container(new BorderLayout());
        Form f = new Form("Notifications");
        results.addComponent(BorderLayout.NORTH, f);
        return results;
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }
    
    public void destroy() {
    }

}
