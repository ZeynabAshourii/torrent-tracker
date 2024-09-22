package model;

import lombok.Getter;
import lombok.Setter;

public abstract class Bean {


    @Getter
    @Setter
    private long ID;


    public Bean(long ID) {
        this.ID = ID;
    }
}
