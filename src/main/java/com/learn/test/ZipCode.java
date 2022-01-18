package com.learn.test;

import com.learn.entity.Store;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ZipCode {

    @BsonProperty(value = "zipcode")
    private String zipcode;
    private List<Store> stores;

}
