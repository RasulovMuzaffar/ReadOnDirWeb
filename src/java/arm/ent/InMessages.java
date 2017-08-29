package arm.ent;

import java.sql.Timestamp;
import java.util.Objects;

public class InMessages {

    private Long id;
    private String header;
    private String body;
    private Timestamp currDate;
    private Long idUser;

    public InMessages() {
    }

    public InMessages(Long id, String header, String body, Timestamp currDate, Long idUser) {
        this.id = id;
        this.header = header;
        this.body = body;
        this.currDate = currDate;
        this.idUser = idUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Timestamp getCurrDate() {
        return currDate;
    }

    public void setCurrDate(Timestamp currDate) {
        this.currDate = currDate;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "InMessages{" + "id=" + id + ", header=" + header + ", body=" + body + ", currDate=" + currDate + ", idUser=" + idUser + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.header);
        hash = 53 * hash + Objects.hashCode(this.body);
        hash = 53 * hash + Objects.hashCode(this.currDate);
        hash = 53 * hash + Objects.hashCode(this.idUser);
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
        final InMessages other = (InMessages) obj;
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.currDate, other.currDate)) {
            return false;
        }
        if (!Objects.equals(this.idUser, other.idUser)) {
            return false;
        }
        return true;
    }

}
