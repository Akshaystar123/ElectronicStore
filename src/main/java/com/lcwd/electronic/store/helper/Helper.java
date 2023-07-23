package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {

        List<U> entitis = page.getContent();
        List<V> dtoList = entitis.stream().map(entity -> new ModelMapper().map(entity, type)).collect(Collectors.toList());
        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements((int) page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }

}
