package entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Programare extends AbstractEntity implements Serializable {
    private String uniqueID = UUID.randomUUID().toString();
    private String data;
    private Integer ora;
    private String scopulProgramarii;
    private String pacientID;


    public Programare(String data, Integer ora, String scopulProgramarii, String pacientID) {
        this.data = data;
        this.ora = ora;
        this.scopulProgramarii = scopulProgramarii;
        this.pacientID = pacientID;
    }

    public  Programare (String data, Integer ora, String scopulProgramarii, String pacientID, String uniqueID) {
        this.uniqueID = uniqueID;
        this.data = data;
        this.ora = ora;
        this.scopulProgramarii = scopulProgramarii;
        this.pacientID = pacientID;
    }


    public String getPacientID() {
        return pacientID;
    }

    public void setPacientID(String pacientID) {
        this.pacientID = pacientID;
    }
    public String getUniqueID() {
        return uniqueID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getOra() {
        return ora;
    }

    public void setOra(Integer ora) {
        this.ora = ora;
    }

    public String getScopulProgramarii() {
        return scopulProgramarii;
    }

    @Override
    public String toString() {
        return uniqueID + " " + data + " " + ora + " " + scopulProgramarii + " " + pacientID;
    }

    public void setScopulProgramarii(String scopulProgramarii) {
        this.scopulProgramarii = scopulProgramarii;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Programare that = (Programare) o;
        return Objects.equals(uniqueID, that.uniqueID) && Objects.equals(data, that.data) && Objects.equals(ora, that.ora) && Objects.equals(scopulProgramarii, that.scopulProgramarii);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueID, data, ora, scopulProgramarii);
    }
}
