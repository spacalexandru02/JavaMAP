package entity;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Pacient extends AbstractEntity implements Serializable {
    private String uniqueID = UUID.randomUUID().toString();
    private String nume;
    private String prenume;
    private Integer varsta;

    public Pacient(String nume, String prenume, Integer varsta) {
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }

    public Pacient(String nume, String prenume, Integer varsta, String uniqueID) {
        this.uniqueID = uniqueID;
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public Integer getVarsta() {
        return varsta;
    }

    public void setVarsta(Integer varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString() {
        return uniqueID + " " + nume + " " + prenume + " " + varsta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pacient that = (Pacient) o;
        return Objects.equals(uniqueID, that.uniqueID) && Objects.equals(nume, that.nume) && Objects.equals(prenume, that.prenume) && Objects.equals(varsta, that.varsta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueID, nume, prenume, varsta);
    }
}
