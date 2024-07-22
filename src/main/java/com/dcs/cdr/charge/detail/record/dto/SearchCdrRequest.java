package com.dcs.cdr.charge.detail.record.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class SearchCdrRequest {
    private String vehicleId;
    private String sessionId;
    private String sortField = SortableFields.START_TIME.getFieldName();
    private String sortOrder = Sort.Direction.ASC.name();
    private int pageNumber = 0;
    private int pageSize = 1;

    public void setSortOrder(String sortOrder) {
        this.sortOrder = Sort.Direction.fromString(sortOrder).name();
    }

    public void setSortField(String sortField) {
        this.sortField = SortableFields.mapToFieldName(sortField).getFieldName();
    }
}
