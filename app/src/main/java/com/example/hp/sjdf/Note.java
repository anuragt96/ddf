package com.example.hp.sjdf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 1/18/2018.
 */

public class Note
{
    private String Name;
    private String Email;
    private String image;
    private boolean isselected;
    private String Id;
    private String EventName;
    private String Senders_Id;
    private String Senders_Name;
    private String Title;
    private String Message;
    private String Image_msg;
    private String VoteName;
    private String token_id;



    public Note(String title) {
        Title = title;
    }

    public Note(String name, String email, boolean isselected, String image,
                String Id, String EventName, String Senders_Id, String Senders_Name, String Title,String Message,
                String Image_msg,String VoteName,String token_id)
    {
        Name = name;
        Email = email;
        this.isselected = isselected;
        this.image = image;
        this.Id= Id;
        this.EventName=EventName;
        this.Senders_Id=Senders_Id;
        this.Senders_Name=Senders_Name;
        this.Title=Title;
        this.Message=Message;
        this.Image_msg=Image_msg;
        this.VoteName=VoteName;
        this.token_id=token_id;

    }

    public String getVoteName() {

        return VoteName;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public void setVoteName(String voteName) {
        VoteName = voteName;
    }

    public String getImage_msg() {
        return Image_msg;
    }

    public void setImage_msg(String image_msg) {
        Image_msg = image_msg;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Note(String senders_Id, String senders_Name) {
        Senders_Id = senders_Id;
        Senders_Name = senders_Name;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isselected()
    {
        return isselected;
    }

    public void setIsselected(boolean isselected)
    {
        this.isselected = isselected;
    }

    public Note()
    {
        //Empty Constructor
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }
    //    public String getEvent() {
//        return Event;
//    }
//
//    public void setEvent(String event) {
//        Event = event;
//    }

    public Note(String name, String email, String Image)
    {
        Name = name;
        Email = email;
        image=Image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSenders_Id() {

        return Senders_Id;
    }

    public void setSenders_Id(String senders_Id) {
        Senders_Id = senders_Id;
    }

    public String getSenders_Name() {
        return Senders_Name;
    }

    public void setSenders_Name(String senders_Name) {
        Senders_Name = senders_Name;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email)
    {
        Email = email;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public Map<String, Object> toMap()
    {

        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", this.Name);
        result.put("Email", this.Email);
        result.put("image", this.image);
        result.put("Id",this.Id);
        result.put("EventName",this.EventName);
        result.put("Senders_Id",this.Senders_Id);
        result.put("Senders_Name",this.Senders_Name);
        result.put("Title",this.Title);
        result.put("Message",this.Message);
        result.put("token_id",this.token_id);

        return result;
    }
}
