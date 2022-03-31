/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PojoClasses;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author developer
 */
public class ConfigProperties {

    @JsonProperty("Charset")
    private String Charset;
    @JsonProperty("deadlockpriority")
    private String deadlockpriority;

    public String getDeadlockpriority() {
        return deadlockpriority;
    }

    public void setDeadlockpriority(String deadlockpriority) {
        this.deadlockpriority = deadlockpriority;
    }
    @JsonProperty("TimeoutForProcessingInMinutes")
    private String TimeoutForProcessingInMinutes;
    @JsonProperty("TimeRelayInSeconds")
    private String TimeRelayInSeconds;
    @JsonProperty("CleansingLogic")
    private String CleansingLogic;
    @JsonProperty("IPAddress")
    private String IPAddress;
    @JsonProperty("UserName")
    private String UserName;
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("DBName")
    private String DBName;
    @JsonProperty("Input_Table")
    private String Input_Table;
    @JsonProperty("Output_Table")
    private String Output_Table;
    @JsonProperty("NER_NLP_Module")
    private String NER_NLP_Module;
    @JsonProperty("DiacriticsClean")
    private String DiacriticsClean;

    public String getDiacriticsClean() {
        return DiacriticsClean;
    }

    public void setDiacriticsClean(String DiacriticsClean) {
        this.DiacriticsClean = DiacriticsClean;
    }

    public String getNER_NLP_Module() {
        return NER_NLP_Module;
    }

    public void setNER_NLP_Module(String NER_NLP_Module) {
        this.NER_NLP_Module = NER_NLP_Module;
    }
    @JsonProperty("Status")
    private int Status;
    @JsonProperty("Start")
    private int Start;

    public String getCharset() {
        return Charset;
    }

    public void setCharset(String Charset) {
        this.Charset = Charset;
    }

    public String getTimeoutForProcessingInMinutes() {
        return TimeoutForProcessingInMinutes;
    }

    public void setTimeoutForProcessingInMinutes(String TimeoutForProcessingInMinutes) {
        this.TimeoutForProcessingInMinutes = TimeoutForProcessingInMinutes;
    }

    public String getTimeRelayInSeconds() {
        return TimeRelayInSeconds;
    }

    public void setTimeRelayInSeconds(String TimeRelayInSeconds) {
        this.TimeRelayInSeconds = TimeRelayInSeconds;
    }

    public String getCleansingLogic() {
        return CleansingLogic;
    }

    public void setCleansingLogic(String CleansingLogic) {
        this.CleansingLogic = CleansingLogic;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getDBName() {
        return DBName;
    }

    public void setDBName(String DBName) {
        this.DBName = DBName;
    }

    public String getInput_Table() {
        return Input_Table;
    }

    public void setInput_Table(String Input_Table) {
        this.Input_Table = Input_Table;
    }

    public String getOutput_Table() {
        return Output_Table;
    }

    public void setOutput_Table(String Output_Table) {
        this.Output_Table = Output_Table;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getStart() {
        return Start;
    }

    public void setStart(int Start) {
        this.Start = Start;
    }

    public int getEnd() {
        return End;
    }

    public void setEnd(int End) {
        this.End = End;
    }
    @JsonProperty("End")
    private int End;
}
