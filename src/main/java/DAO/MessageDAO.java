package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO is a class that mediates the transformation of data between the format of objects in Java to rows in a
 * database. The methods here are mostly filled out, you will just need to add a SQL statement.
 *
 * We may assume that the database has already created a table named 'Message'.
 * It contains similar values as the Message class:
 * message_id, which is of type int and is a primary key,
 * posted_by, which is of type int, and is a foreign key associated with the column 'account_id' of 'account',
 * message_text, which is of type varchar(255),
 * time_posted_epoch, which is of type bigint(20).
 * @version 1.0 09-24-2023
 * @author Abdalrhman Alhassan
 */

public class MessageDAO {

    /**
     * insertMessage: Insert a Message to Message table, the message_id is auto generated so no need to provide it.
     * @param message: a message object with no message_id.
     * @return a Message identified by message_id generated from DB.
     */    


    public Message insertMessage(Message message){
        Connection conn = ConnectionUtil.getConnection();
        String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,message.getPosted_by());
            ps.setString(2,message.getMessage_text());
            ps.setLong(3,message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet pkResultSet = ps.getGeneratedKeys();
            if (pkResultSet.next()){
                int generated_account_id = (int)pkResultSet.getLong(1);
                return new Message(generated_account_id,
                                    message.getPosted_by(),
                                    message.getMessage_text(),
                                    message.getTime_posted_epoch()
                                    );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    /**
     *  getAllMessages: retrive all Messages from  Message table.
     * @return a list of Messages or Empty List if no message found.
     */ 

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "select * from message";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return messages;
    }

    /**
     * getMessageById: retrive  Message from  Message table identified by its id.
     * @return a Message object if the query has a return value or null if no data returned from Database.
     */ 
    public Message getMessageById(int msgId){
        Connection conn = ConnectionUtil.getConnection();
        String sql = "select * from message where message_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,msgId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * deleteMessageById: Delete  Message from  Message table identified by its id.
     * @return a Message object of the deleted message.
     */ 
    public Message deleteMessageById(int msgId){
        Message deletedMsg = getMessageById(msgId);
        Connection conn = ConnectionUtil.getConnection();
        String sql = "delete from message where message_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,msgId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deletedMsg;
    }

     /**
     * updateMesageById: Update  Message from  Message table  by its id.
     * @return a Message object of the Updated message.
     */ 
    public Message updateMesageById(Message message){
        Connection conn = ConnectionUtil.getConnection();
        String sql = "Update message set message_text = ? where message_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,message.getMessage_text());
            ps.setInt(2,message.getMessage_id());
            int rowsUpdated = ps.executeUpdate();

            if(rowsUpdated == 1){
                return getMessageById(message.getMessage_id());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     *  getAllMessagesByUser: retrive all Messages from  Message table identified by its user.
     * @return a list of Messages or Empty List if no message found.
     */ 
    public List<Message> getAllMessagesByUser(int postedBy){
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        String sql = "select * from message where posted_by=?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,postedBy);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return messages;
    }


 
}
