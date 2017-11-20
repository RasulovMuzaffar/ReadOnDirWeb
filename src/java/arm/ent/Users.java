package arm.ent;

import java.util.Objects;


public class Users {

    Long id;
    String fName;
    String lName;
    int idRole;
    int idOrg;
    String autoNo;
    String login;
    String password;
    String autoOtv;
    
    String typeOgr;
    
    String org;

    public Users() {
    }

    public Users(Long id, String fName, String lName, int idRole, int idOrg, String autoNo, String login, String password, String autoOtv, String typeOgr, String org) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.idRole = idRole;
        this.idOrg = idOrg;
        this.autoNo = autoNo;
        this.login = login;
        this.password = password;
        this.autoOtv = autoOtv;
        this.typeOgr = typeOgr;
        this.org = org;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public int getIdOrg() {
        return idOrg;
    }

    public void setIdOrg(int idOrg) {
        this.idOrg = idOrg;
    }

    public String getAutoNo() {
        return autoNo;
    }

    public void setAutoNo(String autoNo) {
        this.autoNo = autoNo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAutoOtv() {
        return autoOtv;
    }

    public void setAutoOtv(String autoOtv) {
        this.autoOtv = autoOtv;
    }

    public String getTypeOgr() {
        return typeOgr;
    }

    public void setTypeOgr(String typeOgr) {
        this.typeOgr = typeOgr;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.fName);
        hash = 31 * hash + Objects.hashCode(this.lName);
        hash = 31 * hash + this.idRole;
        hash = 31 * hash + this.idOrg;
        hash = 31 * hash + Objects.hashCode(this.autoNo);
        hash = 31 * hash + Objects.hashCode(this.login);
        hash = 31 * hash + Objects.hashCode(this.password);
        hash = 31 * hash + Objects.hashCode(this.autoOtv);
        hash = 31 * hash + Objects.hashCode(this.typeOgr);
        hash = 31 * hash + Objects.hashCode(this.org);
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
        final Users other = (Users) obj;
        if (this.idRole != other.idRole) {
            return false;
        }
        if (this.idOrg != other.idOrg) {
            return false;
        }
        if (!Objects.equals(this.fName, other.fName)) {
            return false;
        }
        if (!Objects.equals(this.lName, other.lName)) {
            return false;
        }
        if (!Objects.equals(this.autoNo, other.autoNo)) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.autoOtv, other.autoOtv)) {
            return false;
        }
        if (!Objects.equals(this.typeOgr, other.typeOgr)) {
            return false;
        }
        if (!Objects.equals(this.org, other.org)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
