package com.github.bhjj.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Data
@Builder
public class UserInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer status;

}
