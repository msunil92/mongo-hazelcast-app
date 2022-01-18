package com.learn.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/09 10:54 AM
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZipcodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonProperty(value = "destZipCode")
    String zipCode;

    @BsonProperty(value = "Stores")
    List<Store> storeList;
}
