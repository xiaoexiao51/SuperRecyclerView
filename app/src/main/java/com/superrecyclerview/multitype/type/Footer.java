package com.superrecyclerview.multitype.type;

/**
 * 那么每个模型就会变成这样:
 */
public class Footer implements Visitable {

    public String id;
    public String footer;

    public Footer(String id, String footer) {
        this.id = id;
        this.footer = footer;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
