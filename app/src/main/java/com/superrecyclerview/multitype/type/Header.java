package com.superrecyclerview.multitype.type;

/**
 * 那么每个模型就会变成这样:
 */
public class Header implements Visitable {

    public String header;

    public Header(String header) {
        this.header = header;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
