package cn.kio.cloud.boot.aspect;

import cn.kio.cloud.boot.common.base.BaseQuery;
import cn.kio.cloud.boot.config.CloudPageAdviceProperties;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * 通用分页切面
 *
 * @author kio
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties({CloudPageAdviceProperties.class})
public class PageAdvice {

    private final CloudPageAdviceProperties cloudPageAdviceProperties;

    @Pointcut("@annotation(cn.kio.cloud.boot.common.annotation.Page)")
    public void page() {
    }

    @Before("page()")
    public void beforePage(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return;
        }
        Object entity = joinPoint.getArgs()[0];
        Class<?> superclass = entity.getClass().getSuperclass();
        if (!superclass.equals(BaseQuery.class)) {
            superclass = superclass.getSuperclass();
            if (!superclass.equals(BaseQuery.class)) {
                return;
            }
        }
        BaseQuery query = new BaseQuery();
        BeanUtils.copyProperties(entity, query);
        if (query.getPage() != null) {
            PageHelper.startPage(query.getPage(), query.getSize());
            buildPageSort(query);
            return;
        }

        if (query.getOffset() != null) {
            PageHelper.offsetPage(query.getOffset(), query.getLimit());
            buildPageSort(query);
        }

    }

    private void buildPageSort(BaseQuery query) {
        if (StringUtils.isBlank(query.getSort())) {
            PageHelper.orderBy(cloudPageAdviceProperties.getDefaultSort() + " desc");
            return;
        }
        String[] sorts = query.getSort().split(",");
        String collect = Arrays.stream(sorts).map(sort -> {
            if (sort.startsWith("-")) {
                return StrUtil.toUnderlineCase(sort.substring(1)) + " desc";
            } else {
                return StrUtil.toUnderlineCase(sort) + " asc";
            }
        }).collect(Collectors.joining(", "));

        PageHelper.orderBy(collect);
    }
}
