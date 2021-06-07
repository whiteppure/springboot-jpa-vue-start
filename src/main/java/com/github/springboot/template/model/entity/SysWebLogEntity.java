package com.github.springboot.template.model.entity;

import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@ToString
@Entity
@Table(name = "SYS_WEB_LOG")
public class SysWebLogEntity {

    @Id
    @Column(name = "ID", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SYS_WEB_LOG_SEQ")
    @SequenceGenerator(name = "SYS_WEB_LOG_SEQ", sequenceName = "SYS_WEB_LOG_id", allocationSize = 1)
    private long id;

    private String broswer;
    private String logcontent;
    private String loglevel;
    private String ip;
    private String operatetime;
    private String operatetype;
    private String loginname;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "BROSWER", nullable = true, length = 50)
    public String getBroswer() {
        return broswer;
    }

    public void setBroswer(String broswer) {
        this.broswer = broswer;
    }

    @Basic
    @Column(name = "LOGCONTENT", nullable = true, length = 500)
    public String getLogcontent() {
        return logcontent;
    }

    public void setLogcontent(String logcontent) {
        this.logcontent = logcontent;
    }

    @Basic
    @Column(name = "LOGLEVEL", nullable = true, length = 50)
    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    @Basic
    @Column(name = "IP", nullable = true, length = 200)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "OPERATETIME", nullable = true, length = 50)
    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    @Basic
    @Column(name = "OPERATETYPE", nullable = true, length = 50)
    public String getOperatetype() {
        return operatetype;
    }

    public void setOperatetype(String operatetype) {
        this.operatetype = operatetype;
    }

    @Basic
    @Column(name = "LOGINNAME", nullable = true, length = 50)
    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysWebLogEntity that = (SysWebLogEntity) o;
        return id == that.id &&
                Objects.equals(broswer, that.broswer) &&
                Objects.equals(logcontent, that.logcontent) &&
                Objects.equals(loglevel, that.loglevel) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(operatetime, that.operatetime) &&
                Objects.equals(operatetype, that.operatetype) &&
                Objects.equals(loginname, that.loginname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, broswer, logcontent, loglevel, ip, operatetime, operatetype, loginname);
    }
}
