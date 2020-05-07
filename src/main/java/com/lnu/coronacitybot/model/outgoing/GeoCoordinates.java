package com.lnu.coronacitybot.model.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class GeoCoordinates {
    private Double longitude;
    private Double latitude;
}
