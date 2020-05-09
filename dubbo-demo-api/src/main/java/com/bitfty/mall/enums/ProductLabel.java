package com.bitfty.mall.enums;

import com.bitfty.util.SecurityLevel;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhangmi
 */

public enum ProductLabel {


    HOT(1, "热销",2),
    LIMIT(2, "限量",3),
    RECOMMEND(3, "推荐",4),
    ;

    ProductLabel(int label, String labelValue,int weight) {
        this.label = label;
        this.labelValue = labelValue;
        this.weight = weight;
    }

    @Getter
    private int label;
    @Getter
    private int weight;
    @Getter
    private String labelValue;


    /**
     * 拼接标签字符串
     * @param list
     * @return
     */
    public static String getLabels(List<String> list){
        if (list == null || list.isEmpty()){
            return "";
        }
        List<ProductLabel> result = new ArrayList<>();
        for (ProductLabel label : values()) {
            Optional<String> op = list.stream().filter(e -> label.getLabel() == Integer.parseInt(e)).findAny();
            if(op.isPresent()){
                result.add(label);
            }
        }
        if(result.isEmpty()){
            return "";
        }
        return result.stream().map(e->e.getLabel()+"").collect(Collectors.joining("|"));
    }


    /**
     * 计算标签权重："1,2"
     * @param labels
     * @return
     */
    public static int getLabelsWeight(String labels){
        if(StringUtils.isBlank(labels)){
            return 0;
        }
        int weight2 = 0;
        List<String> list = Arrays.asList(labels.split(","));
        for (ProductLabel label : values()) {
            weight2 += list.stream().filter(e -> e.equals(String.valueOf(label.getLabel()))).mapToInt(e -> label.getWeight()).sum();
        }
        return weight2;
    }
}
