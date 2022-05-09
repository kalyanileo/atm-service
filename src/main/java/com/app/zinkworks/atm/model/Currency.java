package com.app.zinkworks.atm.model;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Currency {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, updatable = false, nullable = false)
    public UUID id;
    
    @JsonProperty("Currency")
    @Column(unique = true)
    private int currency;
    
    @JsonProperty("Quantity")
    @Column
    private int quantity;
    
    @JsonProperty("CreateDate")
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;
    
    @JsonProperty("UpdateDate")
    @UpdateTimestamp
    private Timestamp UpdateDate;
	
}



