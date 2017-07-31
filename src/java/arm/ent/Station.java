/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.ent;

import java.util.Objects;

/**
 *
 * @author Muzaffar
 */
public class Station {
    private Long id;
    private String codeSt;
    private String nameSt;

    public Station() {
    }

    public Station(Long id, String codeSt, String nameSt) {
        this.id = id;
        this.codeSt = codeSt;
        this.nameSt = nameSt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeSt() {
        return codeSt;
    }

    public void setCodeSt(String codeSt) {
        this.codeSt = codeSt;
    }

    public String getNameSt() {
        return nameSt;
    }

    public void setNameSt(String nameSt) {
        this.nameSt = nameSt;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.codeSt);
        hash = 97 * hash + Objects.hashCode(this.nameSt);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Station other = (Station) obj;
        if (!Objects.equals(this.codeSt, other.codeSt)) {
            return false;
        }
        if (!Objects.equals(this.nameSt, other.nameSt)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
