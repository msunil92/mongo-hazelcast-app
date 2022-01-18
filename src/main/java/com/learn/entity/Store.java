package com.learn.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/06 6:52 PM
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonProperty(value = "storeNo")
    String storeNo;

    @BsonProperty(value = "distance")
    int distance;
}
