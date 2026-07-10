package fr.tioslexe.flexbotticket.save;

public class Save {
    private Integer ticketCount;

    // used by Jackson
    public Save(){

    }

    public Save(Integer ticketCount) {
        this.ticketCount = ticketCount;
    }

    public Integer getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Integer ticketCount) {
        this.ticketCount = ticketCount;
    }

    public void incrementTicketCount() {
        this.ticketCount++;
    }
}
