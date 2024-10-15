import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Bank extends Frame implements ActionListener {
    static ArrayList<UserData> userDataList = new ArrayList<>();
    Button log_b, create_b, Pay;
    TextField User_name, Acc_num, Mob_num, exp, Passwd;

    // User data
    String store_name, store_mob_num, store_acc_num, store_pass, store_valid;

    int balance = 2000;
    int User_id=0;

    static ArrayList<String> database_pass = new ArrayList<>();
    static ArrayList<String> database_name = new ArrayList<>();
    static ArrayList<String> database_acc_num = new ArrayList<>();
    static ArrayList<String> database_mob_num = new ArrayList<>();

    GridBagConstraints gbc = new GridBagConstraints();

    Label name, num, acc, valid, pass;    
    
    Label verify = new Label("");

    public Bank() {
        loadUserData(); // Load existing users from file

        for (UserData user : userDataList) {
            database_name.add(user.name);
            database_pass.add(user.password);
            database_mob_num.add(user.mobileNumber);
            database_acc_num.add(user.accountNumber);
        }

        setLayout(new GridBagLayout());

        User_name = new TextField(20);
        Mob_num = new TextField(20);
        Acc_num = new TextField(20);
        exp = new TextField(20);
        Passwd = new TextField(20);
        Passwd.setEchoChar('*');

        User_name.setEditable(true);
        Acc_num.setEditable(true);
        Mob_num.setEditable(true);
        exp.setEditable(true);
        Passwd.setEditable(true);

        name = new Label("Enter name : ");
        num = new Label("Mobile Number : ");
        pass = new Label("Password : ");
        acc = new Label("Account Number : ");
        valid = new Label("validity(MM/YY) : ");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(name, gbc);

        gbc.gridx = 1;
        add(User_name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(num, gbc);

        gbc.gridx = 1;
        add(Mob_num, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(acc, gbc);

        gbc.gridx = 1;
        add(Acc_num, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(pass, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(Passwd, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(valid,gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(exp,gbc);

        log_b = new Button("Login");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(log_b, gbc);

        create_b = new Button("Create ACC");
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(create_b, gbc);

        log_b.addActionListener(this);
        create_b.addActionListener(this);

        setTitle("Bank");
        setSize(500, 500);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saveUserData();
                System.out.println("Bank Server Closed");
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent a) {
        if (a.getSource() == log_b) {
           
            verify.revalidate();

            store_name = User_name.getText();
            store_mob_num = Mob_num.getText();
            store_acc_num = Acc_num.getText();
            store_pass = Passwd.getText();

            if (store_name.isEmpty() || store_mob_num.isEmpty() || store_acc_num.isEmpty() || store_pass.isEmpty()) {
                  gbc.gridx = 0;
                  gbc.gridy = 7;
                  verify.setForeground(Color.red);
                  verify.setText("Enter all values");
                  add(verify,gbc);
            } else if (store_mob_num.length() != 10 || store_acc_num.length() != 12) {
                        gbc.gridx = 0;
                        gbc.gridy = 7;
                        verify.setForeground(Color.red);
                        verify.setText("Recheck all values");
                        add(verify,gbc);
            } else {
                int flag = 0,i=0;
                int pass_index = -1;
                for (UserData user : userDataList) {
                  if (user.name.equals(store_name) && user.password.equals(store_pass)) {
                        flag++;
                        pass_index = i;
                  }
                  i++;
                 }
                if (flag > 0) {

                    if (database_pass.get(pass_index).equals(store_pass)) {
                        gbc.gridx = 0;
                        gbc.gridy = 7;
                        verify.revalidate();
                        verify.setForeground(Color.green);
                        verify.setText("Password Matched");
                        add(verify,gbc);
                        finalShow();
                    } else {
                        gbc.gridx = 0;
                        gbc.gridy = 7;
                        verify.revalidate();
                        verify.setForeground(Color.RED);
                        verify.setText("Incorrect Password");
                        add(verify,gbc);
                    }
                } else {
                    gbc.gridx = 0;
                    gbc.gridy = 7;
                    verify.revalidate();
                    verify.setForeground(Color.red);
                    verify.setText("No such User, Try Creating a new Account");
                    add(verify,gbc);
                }
            }
        } else if (a.getSource() == create_b) {

            verify.revalidate(); 

            store_name = User_name.getText();
            store_mob_num = Mob_num.getText();
            store_acc_num = Acc_num.getText();
            store_pass = Passwd.getText();
            store_valid = exp.getText();

            if (store_name.isEmpty() || store_mob_num.isEmpty() || store_acc_num.isEmpty() || store_pass.isEmpty()) {
                        gbc.gridx = 0;
                        gbc.gridy = 7;
                        verify.setForeground(Color.RED);
                        verify.setText("Enter all values");
                        add(verify,gbc);
            } else if (store_mob_num.length() != 10 || store_acc_num.length() != 12) {
                  gbc.gridx = 0;
                  gbc.gridy = 7;
                  verify.setForeground(Color.red);
                  verify.setText("Recheck all values");
                  add(verify,gbc);
            } else {
                
                User_id++;
                userDataList.add(new UserData(store_name, store_pass, store_mob_num, store_acc_num));
                saveUserData(); 

                database_name.add(store_name);
                database_pass.add(store_pass);
                database_mob_num.add(store_mob_num);
                database_acc_num.add(store_acc_num);

                gbc.gridx = 1;
                gbc.gridy = 7;
                verify.setText("Account Created");
                verify.setForeground(Color.GREEN);
                add(verify,gbc);
                finalShow();
            }
        }
    }
     void finalShow() {
        // Show logged-in user's details and allow transfers
        removeAll();
        revalidate();
        repaint();
        setLayout(null);

        Panel p = new Panel(new GridBagLayout());

        name = new Label(store_name);
        num = new Label(store_mob_num);
        pass = new Label(store_pass);
        acc = new Label(store_acc_num);
        valid = new Label(store_valid);

        Font f = new Font("Arial", Font.PLAIN, 20);

        name.setFont(f);
        num.setFont(f);
        pass.setFont(f);
        acc.setFont(f);
        valid.setFont(f);

        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(name, gbc);

        gbc.gridy = 1;
        p.add(num, gbc);

        gbc.gridy = 2;
        p.add(pass, gbc);

        gbc.gridy = 3;
        p.add(acc, gbc);

        gbc.gridy = 4;
        p.add(valid, gbc);

        Pay = new Button("Pay by ACC");
        Pay.setBounds(400, 600, 100, 30);
        add(Pay);
        Pay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Transfer();
            }
        });

        add(p);
        p.setBounds(0, 0, 500, 250);
    }

    public void Transfer() {
        Dialog d = new Dialog(this, "Pay By ACC", true);
        d.setSize(500, 300);
        d.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Label nameLabel = new Label("Enter User ACC to pay: ");
        TextField userAccField = new TextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        d.add(nameLabel, gbc);
        gbc.gridx = 1;
        d.add(userAccField, gbc);

        Label amountLabel = new Label("Enter Amount to pay: ");
        TextField amountField = new TextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        d.add(amountLabel, gbc);
        gbc.gridx = 1;
        d.add(amountField, gbc);

        Button payButton = new Button("Pay");
        gbc.gridx = 0;
        gbc.gridy = 2;
        d.add(payButton, gbc);

        Label messageLabel = new Label();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        d.add(messageLabel, gbc);

        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userAcc = userAccField.getText();
                String amountStr = amountField.getText();
                int amount;
                try {
                    amount = Integer.parseInt(amountStr);
                    if (amount > balance) {
                        messageLabel.setForeground(Color.red);
                        messageLabel.setText("Insufficient balance");
                    } else {
                        balance -= amount;
                        messageLabel.setForeground(Color.green);
                        messageLabel.setText("Payment successful! Remaining balance: " + balance);
                    }
                } catch (NumberFormatException ex) {
                    messageLabel.setForeground(Color.red);
                    messageLabel.setText("Invalid amount");
                }
            }
        });

        d.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                saveUserData();
                System.out.println("Bank Server Closed");
                d.dispose();
            }
        });

        d.setVisible(true);
    }

    public static void saveUserData() {
        try (FileOutputStream fos = new FileOutputStream("users.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userDataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadUserData() {
        try (FileInputStream fis = new FileInputStream("users.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            userDataList = (ArrayList<UserData>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            userDataList = new ArrayList<>();
        }
    }

      public static void main(String[] args) {
            System.out.println("Bank Server Open");
            new Bank();
            char ch;
            Scanner sc = new Scanner(System.in);
            int at_user;
            int Choice;
            while(true){
                  System.out.println("Press 1 - Shut Down Bank Server \n 2- Get info of particular user \n 3 - Show All Users \n 4 - Remove any Particular User \nChoice: ");
                  Choice = sc.nextInt();
                  switch (Choice) {
                        case 1:
                        System.out.println("Please Confirm to Shut down(y/n) : ");
                        ch = sc.next().charAt(0);
                        if(ch=='y' || ch=='Y') {
                              System.out.println("Bank Server Closed");
                              System.exit(0);
                        }
                        break;
                        case 2:
                        System.out.println("Enter User Id to Search : ");
                        at_user = sc.nextInt();
                        for(int i=0;i<database_acc_num.size();i++){
                        if(i==at_user){
                              System.out.println("User Name : " + database_name.get(i));
                              System.out.println("Password : " + database_pass.get(i));
                              System.out.println("Mobile Number : " + database_mob_num.get(i));
                              System.out.println("Account Number : " + database_acc_num.get(i));
                              System.out.println("\n");
                              break;
                        }
                  } 
                  break;
                  case 3:
                  int j=0;
                  for(;j<database_name.size();j++){
                        System.out.println("User Name : " + database_name.get(j));
                        System.out.println("Password : " + database_pass.get(j));
                        System.out.println("Mobile Number : " + database_mob_num.get(j));
                        System.out.println("Account Number : " + database_acc_num.get(j));
                        System.out.println("\n");
                  } 
                  break;
                  case 4:
                  System.out.println("Enter User id to remove : ");
                  at_user = sc.nextInt();
                  for(int i=0;i<database_acc_num.size();i++){
                        if(database_acc_num.subList(database_acc_num.size()-3,database_acc_num.size()).equals(at_user)){
                              System.out.println("User Name : " + database_name.get(i));
                              System.out.println("Password : " + database_pass.get(i));
                              System.out.println("Mobile Number : " + database_mob_num.get(i));
                              System.out.println("Account Number : " + database_acc_num.get(i));
                              System.out.println("\n"); 
                              database_name.remove(i);
                              database_pass.remove(i);
                              database_acc_num.remove(i);
                              database_mob_num.remove(i);
                              break;
                        }
                  }
                  break; 
                  default:
                  System.out.println("Invalid Choice");
                  break;
            }//end of switch
      }//while loop
            
      }//end of main method
}

//Conversion of the instance into Bytes through serialization
class UserData implements Serializable{
      String name, password, mobileNumber, accountNumber;

      UserData(String name, String password, String mobileNumber, String accountNumber) {
          this.name = name;
          this.password = password;
          this.mobileNumber = mobileNumber;
          this.accountNumber = accountNumber;
      }
  
      // For displaying user details
      public String toString() {
          return "Name: " + name + ", Password: " + password + ", Mobile: " + mobileNumber + ", Account: " + accountNumber;
      }
}  