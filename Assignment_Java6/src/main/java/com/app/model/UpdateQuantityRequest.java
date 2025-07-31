package com.app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateQuantityRequest {

    private Long productId;

    private String size;

    private int quantity;

    private boolean increase;

}
