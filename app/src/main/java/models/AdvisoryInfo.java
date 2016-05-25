package models;

public class AdvisoryInfo {

    private String advisoryClass;
    private String advisorySubclass;
    private String advisoryText;

    AdvisoryInfo(String advisoryClass, String advisorySubclass, String advisoryText) {
        this.advisoryClass = advisoryClass;
        this.advisorySubclass = advisorySubclass;
        this.advisoryText = advisoryText;
    }


    public String getAdvisoryClass() {
        return advisoryClass;
    }

    public void setAdvisoryClass(String advisoryClass) {
        this.advisoryClass = advisoryClass;
    }

    public String getAdvisorySubclass() {
        return advisorySubclass;
    }

    public void setAdvisorySubclass(String advisorySubclass) {
        this.advisorySubclass = advisorySubclass;
    }

    public String getAdvisoryText() {
        return advisoryText;
    }

    public void setAdvisoryText(String advisoryText) {
        this.advisoryText = advisoryText;
    }

}
