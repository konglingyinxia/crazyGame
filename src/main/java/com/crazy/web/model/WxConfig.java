package com.crazy.web.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by 田帅 on 2017/10/11/011.
 */
@Entity
@Table(name = "bm_wx_config")
@org.hibernate.annotations.Proxy(lazy = false)
public class WxConfig implements java.io.Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 707069993826500239L;

    private Integer id;

    private String typename;

    private Date time;

    private String typevalue;

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename == null ? null : typename.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTypevalue() {
        return typevalue;
    }

    public void setTypevalue(String typevalue) {
        this.typevalue = typevalue == null ? null : typevalue.trim();
    }
}
