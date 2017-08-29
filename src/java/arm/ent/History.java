package arm.ent;

import java.util.Objects;

public class History {

    private String sprN;
    private String date;
    private String time;
    private String obj;

    public History() {
    }

    public History(String sprN, String date, String time, String obj) {
        this.sprN = sprN;
        this.date = date;
        this.time = time;
        this.obj = obj;
    }

    public String getSprN() {
        return sprN;
    }

    public void setSprN(String sprN) {
        this.sprN = sprN;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return date.trim() + " " + time.trim() + " | " + obj.trim() + " | Спр:";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.sprN);
        hash = 71 * hash + Objects.hashCode(this.date);
        hash = 71 * hash + Objects.hashCode(this.time);
        hash = 71 * hash + Objects.hashCode(this.obj);
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
        final History other = (History) obj;
        if (!Objects.equals(this.sprN, other.sprN)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.time, other.time)) {
            return false;
        }
        if (!Objects.equals(this.obj, other.obj)) {
            return false;
        }
        return true;
    }

}
