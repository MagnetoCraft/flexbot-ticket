package fr.tioslexe.flexbotticket.save;

public class Save {
    // Config
    private String categoryId;
    private String roleId;

    // Save
    private Integer ticketCount;

    // used by Jackson
    public Save(){

    }

    public Save(String categoryId, Integer ticketCount) {
        this.categoryId = categoryId;
        this.ticketCount = ticketCount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Integer getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(Integer ticketCount) {
        this.ticketCount = ticketCount;
    }

    // synchronized to avoid multiple channels having the same ticket number
    public synchronized Integer incrementTicketCount() {
        this.ticketCount++;
        return this.ticketCount;
    }
}
