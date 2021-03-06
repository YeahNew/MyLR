package org.haohhxx.util.matrics.feature;

import org.haohhxx.util.matrics.VectorLine;

/**
 * @author zhenyuan_hao@163.com
 */
public abstract class AbstractFeatureLine<Integer,Double> implements VectorLine,FeatureLine{


    /**
     * 按位相减
     *
     * @param x2 x2
     * @return
     */
    @Override
    public abstract AbstractFeatureLine sub(VectorLine x2);

}
