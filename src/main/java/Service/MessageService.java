package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import Model.Account;

/**
 * A MessageService is a class that contains the application's business logic, including validation rules,
 *  calculations, and any other operations that are specific to the application's requirements
 * @version 1.0 09-24-2023
 * @author Abdalrhman Alhassan
 */

public class MessageService {

    /**
     * messageDAO object to call Database Operations class.
     */
    public MessageDAO messageDAO;

     /**
     * accountDAO object to call Database Operations class will be used with Message validation Requirement.
     */
    public AccountDAO accountDAO;


    /**
     * Constructor: to intialize DAO objects.
     */
    public MessageService(){
        this.messageDAO = new MessageDAO();
        this.accountDAO = new AccountDAO();
    }

     /**
     * Constructor: to intialize messageDAO objects.
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    /**
     * addMessage: call messageDAO.InsertMessage to insert Receord to DB.
     * @param message: a message object with no message_id.
     * @return a Message identified by message_id generated from DB.
     */  

    public Message addMessage(Message message){
        //check the message before add new message to Apply Requirements:
        if(checkMessage(message)){
         Message messageAdded = messageDAO.insertMessage(message);
         return messageAdded;
        }

        return null;
    }

    /**
     * checkMessage: check the new Added Message to see if all requirements applies.
     * @param message: a message object with no message_id.
     * @return true if All requirments Apply or False if its not.
     */  
    public boolean checkMessage(Message message){
        //check if the posted_by field is actually belong to an existing account:
        Account account = accountDAO.geAccountById(message.getPosted_by());
        // if the account(posted_by) is not exist or the message is blank
        // or the message length greater than 255 return false else return true
       if(account == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 254) {
           return false;
       }

        return true;
    }

     /**
     * getAllMessages: call messageDAO.getAllMessage to retrieve all messages Receord from DB.
     * @return a List of Messages if there existing recoerds or empty list if no recoerd found.
     */ 
    public List<Message> getAllMessages(){
        List<Message> messages = messageDAO.getAllMessages();
        return messages;
    }

     /**
     * getMessageById: call messageDAO.getMessageByID to retrieve message from DB where id.
     * @param message_id:  int message_id.
     * @return message Object if recoerd found under message table or null if no record found.
     */ 
    public Message getMessageById(int message_id){
        Message retrievedMsg = messageDAO.getMessageById(message_id);
        return retrievedMsg;
    }

    /**
     * getMessageById: call messageDAO.deleteMessageById to Delete message from DB where id.
     * @param message_id:  int message_id.
     * @return message Object if recoerd deleted under message table or null if no record found.
     */ 
    public Message deleteMessageById(int message_id){
        Message deletedMSG = messageDAO.deleteMessageById(message_id);
        return deletedMSG;
    }

     /**
     * getMessageById: call messageDAO.updateMesageById to update message from DB where id.
     * @param message:  message Object.
     * @return message Object if recoerd Updated under message table or null if no record found.
     */ 
    public Message updateMessageById(Message message){
        //Check the message Before Update it
        if(checkUpdatedMessage(message)) {
            Message updatedMsg = messageDAO.updateMesageById(message);
            return updatedMsg;
        }
        return null;
    }

    /**
     * checkUpdatedMessage: Apply Requirements on message beofre update it.
     * @param message:  message Object.
     * @return ture if all conditions met or false if one condition is not met.
     */ 
    public boolean checkUpdatedMessage(Message message){
        Message message1 = messageDAO.getMessageById(message.getMessage_id());
        if(message1 == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 254) {
            return false;
        }

        return true;
    }

    /**
     * getAllMessagesByUser: retrieve List of messages for specific user.
     * @param postedBy:  Int posted_by the Account Id who posted this message.
     * @return List of Messages if there is messages found or empty list.
     */
    public List<Message> getAllMessagesByUser(int postedBy){
        List<Message> messages = messageDAO.getAllMessagesByUser(postedBy);
        return messages;
    }


}