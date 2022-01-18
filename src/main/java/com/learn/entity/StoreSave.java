package com.learn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @author sunil
 * @project mongo-app
 * @created 2022/01/09 10:56 AM
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreSave {

    @JsonProperty("zipcode")
    String zipcode;

    @JsonProperty("storeNo")
    String storeNo;

    @JsonProperty("distance")
    int distance;
}
