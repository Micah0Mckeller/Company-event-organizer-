import java.time.LocalDate; 
public class Event{ 
    
    String eventDescription; 
    String eventDate; 
    String eventLocation; 
    String eventName; 
    LocalDate eventTimeSubmitted = LocalDate.now(); 
    
    public void setDescription(String info){ 
        this.eventDescription = info; 
    } 
    public String getDescription(){ 
        return this.eventDescription; 
    } 
    public  void setDate(String date){ 
        this.eventDate = date; 
    } 
    public  String getDate(){ 
        return this.eventDate; 
    } 
    public  void setLocation(String location){ 
        this.eventLocation = location; 
    } 
    public String getLocation(){ 
        return this.eventLocation; 
    } 
    public  void setName(String name){ 
        this.eventName = name; 
    } 
    public String getName(){ 
        return this.eventName; 
    } 
    public LocalDate getTimeSubmitted(){ 
        return this.eventTimeSubmitted; 
    }
}


