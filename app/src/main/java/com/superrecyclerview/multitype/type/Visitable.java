package com.superrecyclerview.multitype.type;

/**
 * The Visitor 访问者模式
 * 如果你有很多模型类 (Model class) ,
 * 可能你不会向对每一个再创建对应的视图模型类 (ViewModel class).
 * 那我们再来看看如何只使用数据模型 (model) .
 * 一开始的时候, 当我们把 type() 方法加到每个模型中, 这种方法就太耦合了.
 * 我们应该把方法抽象出来, 比如添加一个接口：
 */
public interface Visitable {
    int type(TypeFactory typeFactory);
}
