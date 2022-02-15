package cn.edu.bit.ruixin.community.vo;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 分页查询结果
 */
@Getter
@NoArgsConstructor
public class PageVo<T> {
    private long totalElements;

    private long totalPages;

    private boolean hasPrevious;

    private boolean hasNext;

    private List<T> items;

    public static <S> PageVo<S> convertToVo(Page<S> page){
        PageVo<S> vo = new PageVo<S>();
        vo.hasNext = page.hasNext();
        vo.hasPrevious = page.hasPrevious();
        vo.totalElements = page.getTotalElements();
        vo.totalPages = page.getTotalPages();
        vo.items = page.getContent();
        return vo;
    }

    public <S> PageVo<S> foreach(Function<T,S> function){
        PageVo<S> vo = new PageVo<S>();
        vo.hasNext = this.hasNext;
        vo.hasPrevious = this.hasPrevious;
        vo.totalElements = this.totalElements;
        vo.totalPages = this.totalPages;
        vo.items = this.items.stream()
            .map(function)
            .collect(Collectors.toList());
        return vo;
    }
}
