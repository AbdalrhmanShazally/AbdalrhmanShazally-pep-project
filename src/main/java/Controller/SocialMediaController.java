package Controller;

import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class SocialMediaController {

     AccountService accountService;
     MessageService messageService;
 
     /**
      * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
      * suite must receive a Javalin object from this method.
      * @return a Javalin app object which defines the behavior of the Javalin controller.
      */
     public SocialMediaController() {
         this.accountService = new AccountService();
         this.messageService = new MessageService();
     }
 
     public Javalin startAPI() {
         Javalin app = Javalin.create();
      //   app.get("example-endpoint", this::exampleHandler);
         app.post("/register",this::postAccountHandler);
         app.post("/login", this::getUserLoginHandler);
         app.post("/messages", this::postMessagetHandler);
         app.get("/messages", this::getAllMessages);
         app.get("/messages/{message_id}", this::getMsgById);
         app.delete("/messages/{message_id}", this::deleteMsgById);
         app.patch("/messages/{message_id}", this::patchMessagetHandler);
         app.get("/accounts/{account_id}/messages", this::getAllMessagesByUser);
 
         return app;
     }
 
     /**
     * postMessagetHandler: Handle Post Request to add new Message.
     * @param ctx: JavaLin Context.
     * @return Json Object of message added to DB.
     */ 
 
     private void postMessagetHandler(Context ctx) throws JsonProcessingException {
         ObjectMapper mapper = new ObjectMapper();
         Message message = mapper.readValue(ctx.body(),Message.class);
         Message addedMessage = messageService.addMessage(message);
         if(addedMessage != null) {
             ctx.json(mapper.writeValueAsString(addedMessage));
         } else {
             ctx.status(400);
         }
     
     }

     /**
     * postAccountHandler: Handle Post Request to add new Account.
     * @param ctx: JavaLin Context.
     * @return Json Object of account added to DB.
     */ 
     private void postAccountHandler(Context ctx) throws JsonProcessingException {
         ObjectMapper mapper = new ObjectMapper();
         Account account = mapper.readValue(ctx.body(),Account.class);
         Account addedAccount = accountService.addAccount(account);
         if(addedAccount != null) {
             ctx.json(mapper.writeValueAsString(addedAccount));
         } else {
             ctx.status(400);
             }
         }
     
     /**
     * getUserLoginHandler: Handle Post Request to Login user.
     * @param ctx: JavaLin Context.
     * @return Json Object of account.
     */     
     private void getUserLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
         Account account = mapper.readValue(ctx.body(),Account.class);
         Account existAccount = accountService.processUserLogin(account);
         if(existAccount != null) {
             ctx.json(mapper.writeValueAsString(existAccount));
         } else {
             ctx.status(401);
         }
 
 
     }
 
     /**
     * getAllMessages: Handle Get Request to List All Messages.
     * @param ctx: JavaLin Context.
     * @return Json Object of list of Messages.
     */ 
     private void getAllMessages(Context ctx){
         ctx.json(messageService.getAllMessages());
     }
     
     /**
     * getMsgById: Handle Get Request to  Messages by Specific MsgID.
     * @param ctx: JavaLin Context.
     * @return Json Object of Message.
     */ 
     private void getMsgById(Context ctx){
         Message message = messageService.getMessageById(Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id"))));
         if(message != null){
             ctx.json(message);
         }
 
     }
     
    /**
     * deleteMsgById: Handle Delet Request to  Messages by Specific MsgID.
     * @param ctx: JavaLin Context.
     * @return Json Object of Message Deleted.
     */ 
     private void deleteMsgById(Context ctx){
         Message message = messageService.deleteMessageById(Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id"))));
         if(message != null){
             ctx.json(message);
         }
 
     }
 
     /**
     * patchMessagetHandler: Handle Patch Request to Update Messages by Specific MsgID.
     * @param ctx: JavaLin Context.
     * @return Json Object of Message Updated.
     */ 
     private void patchMessagetHandler(Context ctx) throws JsonProcessingException {
         try {
             ObjectMapper mapper = new ObjectMapper();
             Message message = mapper.readValue(ctx.body(), Message.class);
             message.setMessage_id(Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id"))));
             Message deletedMsg = messageService.updateMessageById(message);
             if (deletedMsg != null) {
                 ctx.json(mapper.writeValueAsString(deletedMsg));
             } else {
                 ctx.status(400);
             }
         } catch (Exception e) {
             e.printStackTrace();
             ctx.status(400);
         }
 
     }
 
     /**
     * getAllMessagesByUser: Handle Get Request to get Messages by Specific UserId.
     * @param ctx: JavaLin Context.
     * @return Json Object of List of Messages.
     */ 
     private void getAllMessagesByUser(Context ctx){
         ctx.json(messageService.getAllMessagesByUser(Integer.parseInt(Objects.requireNonNull(ctx.pathParam("account_id")))));
     }

}
