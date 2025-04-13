package com.hackathon.backend.locationsservice.Domain.JSONB_POJOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pagination {
    Integer page;
    Integer limit;
    Long totalItems;
    Long totalPages;
}
