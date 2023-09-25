package Service;


import DAO.AccountDAO;
import Model.Account;

/**
 * A MessageService is a class that contains the application's business logic, including validation rules,
 *  calculations, and any other operations that are specific to the application's requirements
 * @version 1.0 09-24-2023
 * @author Abdalrhman Alhassan
 */

public class AccountService {
    
     /**
     * accountDAO object to call Database Operations class will be used with Message validation Requirement.
     */
    public AccountDAO accountDAO;


    /**
     * Constructor: to intialize accountDAO objects.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor: to intialize accountDAO objects recived as prameter.
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * addAccount: call accountDAO.insertAccount to insert Receord to DB.
     * @param account: an account object with no account_id.
     * @return an account identified by account_id generated from DB.
     */  
    public Account addAccount(Account account){

        //check the account requirement before add new account:

        if (checkAccount(account)){
            Account addedAccount = accountDAO.insertAccount(account);
            return addedAccount;
        }
        return null;
    }

    /**
     * processUserLogin: call accountDAO.geAccountByUserNamePassword.
     * @param account: an account object with no account_id.
     * @return an account identified by account_id retrieved from DB.
     */ 
    public Account processUserLogin(Account account){
        Account existAccount = accountDAO.geAccountByUserNamePassword(account.getUsername(),account.getPassword());
        if(existAccount != null){
            return existAccount;
        }
        return null;
    }

     /**
     * checkAccount: Check Account Requirement before Add it to DB.
     * @param account: an account object with no account_id.
     * @return true if all requirements met or false if at least one of requirements faild.
     */ 
    public boolean checkAccount(Account account){
        if(accountDAO.geAccountByUserName(account.getUsername()) != null || account.getUsername().isEmpty() || account.getPassword().length() < 4 ) {
            return false;
        }
        return true;
    }

}