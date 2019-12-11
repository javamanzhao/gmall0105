package com.zzhao.gmall.bean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;


@Table(name = "test")
public class TestBean implements Serializable {

    @Id
    private String id;

    @Column(name = "num")
    private BigDecimal num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id='" + id + '\'' +
                ", num=" + num +
                '}';
    }
}
