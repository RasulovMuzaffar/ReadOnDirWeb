/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arm.tableutils.sprtemplates.nod;

import java.util.Objects;

/**
 *
 * @author Muzaffar
 */
public class Spr1574Ent {

    private String vsg;
    private String st1;
    private String st2;
    private String st3;
    private String st4;
    private String st5;
    private String st6;
    private String st7;
    private String st8;
    private String st9;

    public Spr1574Ent() {
    }

    public Spr1574Ent(String vsg, String st1, String st2, String st3, String st4, String st5, String st6, String st7, String st8, String st9) {
        this.vsg = vsg;
        this.st1 = st1;
        this.st2 = st2;
        this.st3 = st3;
        this.st4 = st4;
        this.st5 = st5;
        this.st6 = st6;
        this.st7 = st7;
        this.st8 = st8;
        this.st9 = st9;
    }

    public String getVsg() {
        return vsg;
    }

    public void setVsg(String vsg) {
        this.vsg = vsg;
    }

    public String getSt1() {
        return st1;
    }

    public void setSt1(String st1) {
        this.st1 = st1;
    }

    public String getSt2() {
        return st2;
    }

    public void setSt2(String st2) {
        this.st2 = st2;
    }

    public String getSt3() {
        return st3;
    }

    public void setSt3(String st3) {
        this.st3 = st3;
    }

    public String getSt4() {
        return st4;
    }

    public void setSt4(String st4) {
        this.st4 = st4;
    }

    public String getSt5() {
        return st5;
    }

    public void setSt5(String st5) {
        this.st5 = st5;
    }

    public String getSt6() {
        return st6;
    }

    public void setSt6(String st6) {
        this.st6 = st6;
    }

    public String getSt7() {
        return st7;
    }

    public void setSt7(String st7) {
        this.st7 = st7;
    }

    public String getSt8() {
        return st8;
    }

    public void setSt8(String st8) {
        this.st8 = st8;
    }

    public String getSt9() {
        return st9;
    }

    public void setSt9(String st9) {
        this.st9 = st9;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.vsg);
        hash = 97 * hash + Objects.hashCode(this.st1);
        hash = 97 * hash + Objects.hashCode(this.st2);
        hash = 97 * hash + Objects.hashCode(this.st3);
        hash = 97 * hash + Objects.hashCode(this.st4);
        hash = 97 * hash + Objects.hashCode(this.st5);
        hash = 97 * hash + Objects.hashCode(this.st6);
        hash = 97 * hash + Objects.hashCode(this.st7);
        hash = 97 * hash + Objects.hashCode(this.st8);
        hash = 97 * hash + Objects.hashCode(this.st9);
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
        final Spr1574Ent other = (Spr1574Ent) obj;
        if (!Objects.equals(this.vsg, other.vsg)) {
            return false;
        }
        if (!Objects.equals(this.st1, other.st1)) {
            return false;
        }
        if (!Objects.equals(this.st2, other.st2)) {
            return false;
        }
        if (!Objects.equals(this.st3, other.st3)) {
            return false;
        }
        if (!Objects.equals(this.st4, other.st4)) {
            return false;
        }
        if (!Objects.equals(this.st5, other.st5)) {
            return false;
        }
        if (!Objects.equals(this.st6, other.st6)) {
            return false;
        }
        if (!Objects.equals(this.st7, other.st7)) {
            return false;
        }
        if (!Objects.equals(this.st8, other.st8)) {
            return false;
        }
        if (!Objects.equals(this.st9, other.st9)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Spr1574Ent{" + "vsg=" + vsg + ", st1=" + st1 + ", st2=" + st2 + ", st3=" + st3 + ", st4=" + st4 + ", st5=" + st5 + ", st6=" + st6 + ", st7=" + st7 + ", st8=" + st8 + ", st9=" + st9 + '}';
    }
    
}
