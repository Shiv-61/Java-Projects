import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.time.LocalDateTime;

class Bank extends Frame implements ActionListener {
    static ArrayList<UserData> userDataList = new ArrayList<>();
    Button log_b, create_b, Pay, Transaction_history;
    TextField User_name, Acc_num, Mob_num, exp, Passwd;

    Label nameLabel = new Label("Enter User ACC to pay: ");
    TextField userAccField = new TextField(20);

    Label amountLabel = new Label("Enter Amount to pay: ");
    TextField amountField = new TextField(20);

    // User data
    String store_name, store_mob_num, store_acc_num, store_pass, store_valid, to_acc, to_amount;

    int balance = 20000;
    int User_id = 0;

    static ArrayList<String> database_pass = new ArrayList<>();
    static ArrayList<String> database_name = new ArrayList<>();
    static ArrayList<String> database_acc_num = new ArrayList<>();
    static ArrayList<String> database_mob_num = new ArrayList<>();
    static ArrayList<String> database_sent_to = new ArrayList<>();
    static ArrayList<String> database_sent_amount = new ArrayList<>();

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
        add(valid, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(exp, gbc);

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
                add(verify, gbc);
            } else if (store_mob_num.length() != 10 || store_acc_num.length() != 12) {
                gbc.gridx = 0;
                gbc.gridy = 7;
                verify.setForeground(Color.red);
                verify.setText("Recheck all values");
                add(verify, gbc);
            } else {
                int flag = 0, i = 0;
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
                        add(verify, gbc);
                        finalShow();
                    } else {
                        gbc.gridx = 0;
                        gbc.gridy = 7;
                        verify.revalidate();
                        verify.setForeground(Color.RED);
                        verify.setText("Incorrect Password");
                        add(verify, gbc);
                    }
                } else {
                    gbc.gridx = 0;
                    gbc.gridy = 7;
                    verify.revalidate();
                    verify.setForeground(Color.red);
                    verify.setText("No such User, Try Creating a new Account");
                    add(verify, gbc);
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
                add(verify, gbc);
            } else if (store_mob_num.length() != 10 || store_acc_num.length() != 12) {
                gbc.gridx = 0;
                gbc.gridy = 7;
                verify.setForeground(Color.red);
                verify.setText("Recheck all values");
                add(verify, gbc);
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
                add(verify, gbc);
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

        Transaction_history = new Button("Transaction History");
        Transaction_history.setBounds(600, 600, 200, 30);
        add(Transaction_history);
        Transaction_history.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                History();
            }
        });
        add(p);
        p.setBounds(0, 0, 500, 250);
    }

    public void History() {
        Dialog th = new Dialog(this, "Transaction History", true);
        th.setSize(700, 700);
        th.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Transaction> transactionList = loadTransactions();

        if (transactionList.isEmpty()) {
            Label temp_msg = new Label("No Transaction History Available");
            gbc.gridx = 0;
            gbc.gridy = 0;
            th.add(temp_msg, gbc);
        } else {
            Label heading = new Label("Transaction History:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            th.add(heading, gbc);

            // Loop through transaction history
            int row = 1;
            for (Transaction transaction : transactionList) {
                Label transactionDetail = new Label(transaction.toString());
                gbc.gridx = 0;
                gbc.gridy = row++;
                th.add(transactionDetail, gbc);
            }
        }

        Button closeButton = new Button("Close");
        gbc.gridx = 0;
        gbc.gridy = 10;
        th.add(closeButton, gbc);
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                th.dispose();
            }
        });

        th.setVisible(true);
    }

    public void saveTransaction(Transaction transaction) {
        try (FileOutputStream fos = new FileOutputStream("Transaction.dat", true);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(transaction);
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("Transaction.dat");
                ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Transaction transaction = (Transaction) ois.readObject();
                    transactionList.add(transaction);
                } catch (EOFException eof) {
                    break; // End of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // e.printStackTrace();
        }
        return transactionList;
    }

    public void Transfer() {
        Dialog d = new Dialog(this, "Pay By ACC", true);
        d.setSize(500, 300);
        d.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        d.add(nameLabel, gbc);
        gbc.gridx = 1;
        d.add(userAccField, gbc);

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

                        // Create and save the transaction for the logged-in user
                        UserData currentUser = findUserByAccountNumber(store_acc_num);
                        if (currentUser != null) {
                            Transaction newTransaction = new Transaction(userAcc, amount, LocalDateTime.now());
                            currentUser.addTransaction(userAcc, amount, LocalDateTime.now());
                            saveTransaction(newTransaction); // Save transaction to file
                            saveUserData(); // Save updated user data
                        }
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
                // System.out.println("Bank Server Closed");
                d.dispose();
            }
        });

        d.setVisible(true);
    }

    // Find a user by account number (helper function)
    public UserData findUserByAccountNumber(String accountNumber) {
        for (UserData user : userDataList) {
            if (user.accountNumber.equals(accountNumber)) {
                return user;
            }
        }
        return null; // Return null if no user found
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
        while (true) {
            System.out.println(
                    "Press 1 - Shut Down Bank Server \n 2- Get info of particular user \n 3 - Show All Users \n 4 - Remove any Particular User \nChoice: ");
            Choice = sc.nextInt();
            switch (Choice) {
                case 1:
                    System.out.println("Please Confirm to Shut down(y/n) : ");
                    ch = sc.next().charAt(0);
                    if (ch == 'y' || ch == 'Y') {
                        System.out.println("Bank Server Closed");
                        System.exit(0);
                    }
                    break;
                case 2:
                    System.out.println("Enter User Id to Search : ");
                    at_user = sc.nextInt();
                    for (int i = 0; i < database_acc_num.size(); i++) {
                        if (i == at_user) {
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
                    int j = 0;
                    for (; j < database_name.size(); j++) {
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
                    for (int i = 0; i < database_acc_num.size(); i++) {
                        if (database_acc_num.subList(database_acc_num.size() - 3, database_acc_num.size())
                                .equals(at_user)) {
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
            }// end of switch
        } // while loop

    }// end of main method
}

class UserData implements Serializable {
    String name, password, mobileNumber, accountNumber;
    ArrayList<Transaction> transactions; // Store transaction history

    UserData(String name, String password, String mobileNumber, String accountNumber) {
        this.name = name;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<>(); // Initialize transaction history
    }

    // Add a transaction to the history
    public void addTransaction(String toAccount, int amount, LocalDateTime timestamp) {
        transactions.add(new Transaction(toAccount, amount, timestamp));
    }

    // For displaying user details
    public String toString() {
        return "Name: " + name + ", Mobile: " + mobileNumber + ", Account: " + accountNumber;
    }
}

class Transaction implements Serializable {
    String toAccount;
    int amount;
    LocalDateTime timestamp;

    Transaction(String toAccount, int amount, LocalDateTime timestamp) {
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "To: " + toAccount + ", Amount: " + amount + ", Timestamp: " + timestamp;
    }
}
