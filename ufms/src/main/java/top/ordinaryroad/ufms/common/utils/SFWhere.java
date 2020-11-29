package top.ordinaryroad.ufms.common.utils;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jpa动态条件生成类
 *
 * @author qq1962247851
 * @since 1.0.0
 */
public class SFWhere {
    /**
     * 条件之间用和(and)链接
     *
     * @param entity 实体对象
     * @param <T>    实体对象的类型
     * @return 返回自身
     */
    public static <T> SFClass<T> and(T entity) {
        return new SFClass<>(entity, "and");
    }

    /**
     * 条件之间用和(or)链接
     *
     * @param entity 实体对象
     * @param <T>    实体对象的类型
     * @return 返回自身
     */
    public static <T> SFClass<T> or(T entity) {
        return new SFClass<>(entity, "or");
    }

    /**
     * 内嵌式条件包装类
     *
     * @param <T> 实体对象的类型
     */
    public static class SFClass<T> {
        private String andOr;
        private Map<String, Specification<T>> sMap = new HashMap<>();

        /**
         * 条件包装类的构造函数
         *
         * @param entity 实体类
         * @param andOr  条件之间的链接字符串
         */
        public SFClass(T entity, String andOr) {
            this.andOr = andOr;
            //反射循环出该实体对象的所有字段
            for (Field declaredField : entity.getClass().getDeclaredFields()) {
                try {
                    //设置是否允许获取字段的值
                    declaredField.setAccessible(true);
                    Object value = declaredField.get(entity);
                    //获取字段的名称
                    String fieldName = declaredField.getName();
                    boolean tmp = false;
                    //过滤字段上的注解
                    for (Annotation annotation : declaredField.getAnnotations()) {
                        if (judgeAnnotation(annotation)) {
                            tmp = true;
                            break;
                        }
                    }
                    //字段注解及值,都允许的情况下才包装条件
                    if (!tmp && value != null) {
                        sMap.put(fieldName, (Specification<T>) (root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get(fieldName), value));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 通过字段上的注解过滤掉字段,加入了这些注解的字段是无法成为条件的
         *
         * @param annotation 注解
         * @return 真假
         */
        private boolean judgeAnnotation(Annotation annotation) {
            return annotation.annotationType().equals(OneToOne.class) ||
                    annotation.annotationType().equals(OneToMany.class) ||
                    annotation.annotationType().equals(ManyToOne.class) ||
                    annotation.annotationType().equals(ManyToOne.class) ||
                    annotation.annotationType().equals(Transient.class);
        }

        /**
         * 编译条件
         *
         * @return 返回编译后的条件
         */
        public Specification<T> build() {
            //构建一个条件实例
            return (Specification<T>) (root, query, criteriaBuilder) -> {
                //建立一个条件容器
                List<Predicate> predicateList = new ArrayList<>();
                //取出条件
                sMap.forEach((String fieldName, Specification<T> sp) -> predicateList.add(sp.toPredicate(root, query, criteriaBuilder)));
                //建立一个数组容器
                Predicate[] predicates = new Predicate[predicateList.size()];
                //链接条件
                if (this.andOr.equals("and")) {
                    return criteriaBuilder.and(predicateList.toArray(predicates));
                } else {
                    return criteriaBuilder.or(predicateList.toArray(predicates));
                }
            };
        }

        /**
         * 生成等于条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值
         * @return 返回自身
         */
        public SFClass<T> equal(boolean condition, String property, Object value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.equal(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        /**
         * 模糊查询
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值,必须是字符串的类型的,全模糊是 "%" + 值 + "%"
         * @return 返回自身
         */
        public SFClass<T> like(boolean condition, String property, String value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.like(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        /**
         * 大于条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值,必须是数值型的
         * @return 返回自身
         */
        public SFClass<T> gt(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.gt(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        public SFClass<T> gt(String property) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> criteriaBuilder.and());
            return this;
        }

        /**
         * 大于等于条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值,必须是数值型的
         * @return 返回自身
         */
        public SFClass<T> ge(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.ge(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        /**
         * 小于条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值,必须是数值型的
         * @return 返回自身
         */
        public SFClass<T> lt(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.lt(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        /**
         * 小于等于条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值,必须是数值型的
         * @return 返回自身
         */
        public SFClass<T> le(boolean condition, String property, Number value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.le(root.get(property), value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        /**
         * 包含条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param value     生成的条件值,必须是集合
         * @return 返回自身
         */
        public <Y> SFClass<T> in(boolean condition, String property, List<Y> value) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    Path<Object> objectPath = root.get(property);
                    return objectPath.in(value);
                }
                return criteriaBuilder.and();
            });
            return this;
        }

        /**
         * 范围条件
         *
         * @param condition 条件
         * @param property  属性名称(字段名称),必须是实体类的属性名称
         * @param start     范围的开始值
         * @param end       范围的结束值
         * @return 返回自身
         */
        public <Y extends Comparable<Y>> SFClass<T> between(boolean condition, String property, Y start, Y end) {
            sMap.put(property, (Specification<T>) (root, query, criteriaBuilder) -> {
                if (condition) {
                    return criteriaBuilder.between(root.get(property), start, end);
                }
                return criteriaBuilder.and();
            });
            return this;
        }
    }
}