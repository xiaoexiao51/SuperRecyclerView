/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.superrecyclerview.multitype.type;

import android.support.annotation.NonNull;

import com.superrecyclerview.bean.ProductBean;

import java.util.List;

/**
 * 那么每个模型就会变成这样:
 */
public class HotList implements Visitable {

    public List<ProductBean> products;

    public HotList(@NonNull List<ProductBean> products) {
        this.products = products;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
